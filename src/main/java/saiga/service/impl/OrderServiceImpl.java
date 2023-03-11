package saiga.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.Order;
import saiga.model.User;
import saiga.model.enums.OrderStatus;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.dto.OrderDTO;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.OrderEndRequest;
import saiga.payload.request.UserOrderRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.OrderService;
import saiga.service.OrderDeliverService;
import saiga.utils.exceptions.BadRequestException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.statics.GlobalMethodsToHelp;
import saiga.utils.statics.MessageResourceHelperFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static saiga.payload.MyResponse._CREATED;
import static saiga.payload.MyResponse._UPDATED;
import static saiga.utils.statics.Constants._ORDER_TAX;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record OrderServiceImpl(
        OrderRepository repository,
        CabinetRepository cabinetRepository,
        OrderDTOMapper orderDTOMapper,
        OrderDeliverService orderDeliverSocketServiceImpl,
        OrderDeliverService orderDeliverTelegramServiceImpl,
        MessageResourceHelperFunction messageResourceHelper,
        GlobalMethodsToHelp globalMethodsToHelp
) implements OrderService {
    @Override
    public MyResponse driversOrder(DriverOrderRequest driverOrderRequest) {
        // get cabinet of current user
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // parse date format to timestamp
        final Timestamp timestamp = globalMethodsToHelp.parseDdMMYyyyStringToDate(driverOrderRequest.timeWhen());

        // check if amount value is valid decimal
        globalMethodsToHelp.isValidDecimalValue(driverOrderRequest.amountOfMoney());

        // saving order
        final Order savedOrder = saveOrderToDatabase(
                new Order(
                        currentUsersCabinet,
                        driverOrderRequest.direction(),
                        new BigDecimal(driverOrderRequest.amountOfMoney()),
                        timestamp,
                        driverOrderRequest.comment()
                )
        );

        // emitting to socket client
        emitNewOrderToSocket(savedOrder);

        // sending order to telegram
        emitNewOrderToTelegram(savedOrder);

        return _CREATED()
                .setMessage(
                        messageResourceHelper.apply("order.created_success"))
                .addData("data", orderDTOMapper.apply(savedOrder));
    }

    @Override
    public MyResponse usersOrder(UserOrderRequest userOrderRequest) {
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // check amount value is valid
        globalMethodsToHelp.isValidDecimalValue(userOrderRequest.amountOfMoney());

        // users order are saving
        final Order savedOrder = saveOrderToDatabase(
                new Order(
                        currentUsersCabinet,
                        userOrderRequest.direction(),
                        new BigDecimal(userOrderRequest.amountOfMoney()),
                        userOrderRequest.comment()
                )
        );

        // emitting to socket client
        emitNewOrderToSocket(savedOrder);

        // sending order to telegram
        emitNewOrderToTelegram(savedOrder);

        return _CREATED()
                .setMessage(
                        messageResourceHelper.apply("order.created_success"))
                .addData("data", orderDTOMapper.apply(savedOrder));
    }

    @Override
    public List<OrderDTO> nonReceivedOrders(OrderType type) {
        return repository.findAllByCabinetToIsNullAndType(Sort.by(Sort.Direction.DESC, "id"), type)
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public MyResponse receiveOrderById(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus().equals(OrderStatus.RECEIVED))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.already_received"));

        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        if (currentUsersCabinet.equals(order.getCabinetFrom()))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.cant_receive_own"));

        // change balance of user
        changeUsersBalanceByTax(currentUsersCabinet, '-');

        // reset status of order
        order.setStatus(OrderStatus.RECEIVED);

        order.setCabinetTo(currentUsersCabinet);

        order = saveOrderToDatabase(order);

        // emit received order to socket client
        emitReceivedOrderToSocket(order);

        // sending order to telegram
        emitReceivedOrderToTelegram(order);

        return _CREATED()
                .setMessage(
                        messageResourceHelper.apply("order.created_success"))
                .addData("data", orderDTOMapper.apply(order));
    }

    @Override
    public List<OrderDTO> currentDriversHistoryOfOrder() {
        return repository.findAllByCabinetTo_Id(getCurrentUsersCabinet().getId())
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public MyResponse endOrderById(OrderEndRequest orderEndRequest) {
        final Order order = getOrderById(orderEndRequest.orderId());

        // check if order is received
        if (order.getStatus().equals(OrderStatus.ACTIVE))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.not_received")
            );

        // check if order is ended
        if (order.getStatus().equals(OrderStatus.ORDERED))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.already_ended"));

        // check if order amount is valid
        globalMethodsToHelp.isValidDecimalValue(orderEndRequest.orderMoney());

        // money of clients screen
        order.setMoney(new BigDecimal(orderEndRequest.orderMoney()));

        // set length of the taken way
        order.setLengthOfWay(orderEndRequest.OrderLengthOfWay());

        // update status of order
        order.setStatus(OrderStatus.ORDERED);

        saveOrderToDatabase(order);

        return _UPDATED().setMessage(
                messageResourceHelper.apply("order.end_success"));
    }

    @Override
    public MyResponse cancelOwnReceivedOrderByOrderId(Long id) {
        Order order = getOrderById(id);
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // check if order is not received
        if (order.getStatus().equals(OrderStatus.ACTIVE))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.not_received")
            );

        // check if order is ended
        if (order.getStatus().equals(OrderStatus.ORDERED))
            throw new BadRequestException("Order is already ended!");

        // check if order its own
        if (!currentUsersCabinet.getId().equals(order.getCabinetTo().getId()))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.cant_cancel_somebody's_order"));

        // reset status of canceled order
        order.setStatus(OrderStatus.ACTIVE);

        // change users balance
        changeUsersBalanceByTax(currentUsersCabinet, '+');

        // change order toUser to null
        order.setCabinetTo(null);

        // save changed order to database
        saveOrderToDatabase(order);

        // emit canceled order to socket
        emitNewOrderToSocket(order);

        // emit canceled order to telegram
        emitNewOrderToTelegram(order);

        return _UPDATED().setMessage(
                messageResourceHelper.apply("order.cancel_success"));
    }

    @Override
    public MyResponse cancelOwnNonReceivedOrderByOrderId(Long id) {
        final Order order = getOrderById(id);

        // check if order status is received
        if (order.getStatus().equals(OrderStatus.RECEIVED))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.received_so_cant_cancel"));

        // check if order status is ordered
        if (order.getStatus().equals(OrderStatus.ORDERED))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.ended_so_cant_cancel"));

        // check if order its own
        if (!getCurrentUsersCabinet().getId().equals(order.getCabinetFrom().getId()))
            throw new BadRequestException(
                    messageResourceHelper.apply("order.cant_cancel_somebody's_order")
            );

        // deleting canceled order, maybe later we use status instead of delete it
        repository.deleteById(id);

        // emitting deleted order to socket
        emitDeletedOrderToSocket(order);

        // emitting deleted order to Telegram
        emitDeletedOrderToTelegram(order);
        return _UPDATED()
                .setMessage(
                        messageResourceHelper.apply("order.deleted_success"))
                .addData("data", orderDTOMapper.apply(order));
    }

    private Cabinet getCurrentUsersCabinet() {
        // get current authenticated user
        final User currentUser = globalMethodsToHelp.getCurrentUser();

        return cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException(
                        messageResourceHelper.apply("order.access_denied"))
        );
    }

    private Order getOrderById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(
                                messageResourceHelper.apply("order.not_found_with_id"),
                                id
                        )
                )
        );
    }

    private Order saveOrderToDatabase(Order order) {
        return repository.save(order);
    }

    private void changeUsersBalanceByTax(Cabinet cabinet, char expressionMethod) {
        switch (expressionMethod) {
            case '-' -> {
                if (cabinet.getBalance().compareTo(_ORDER_TAX) < 0)
                    throw new BadRequestException(
                            messageResourceHelper.apply("order.haven't_enough_tax_amount"));

                // subtracting tax amount
                cabinet.setBalance(cabinet.getBalance().subtract(_ORDER_TAX));
            }
            case '+' ->
                // change current users balance
                cabinet.setBalance(cabinet.getBalance().subtract(_ORDER_TAX));
        }
    }

    private void emitNewOrderToSocket(Order order) {
        orderDeliverSocketServiceImpl.sendOrderToClient(orderDTOMapper.apply(order), order.getType());
    }

    private void emitReceivedOrderToSocket(Order order) {
        orderDeliverSocketServiceImpl.sendReceivedOrderToClient(orderDTOMapper.apply(order), order.getType());
    }

    private void emitNewOrderToTelegram(Order order) {
        orderDeliverTelegramServiceImpl.sendOrderToClient(orderDTOMapper.apply(order), order.getType());
    }

    private void emitReceivedOrderToTelegram(Order order) {
        orderDeliverTelegramServiceImpl.sendReceivedOrderToClient(orderDTOMapper.apply(order), order.getType());
    }

    private void emitDeletedOrderToSocket(Order order) {
        orderDeliverSocketServiceImpl.sendCanceledOrderToClient(orderDTOMapper.apply(order), order.getType());
    }

    private void emitDeletedOrderToTelegram(Order order) {
        orderDeliverTelegramServiceImpl.sendCanceledOrderToClient(orderDTOMapper.apply(order), order.getType());
    }
}
