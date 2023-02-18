package saiga.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
import saiga.service.OrderSocketService;
import saiga.utils.exceptions.BadRequestException;
import saiga.utils.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static saiga.payload.MyResponse.*;
import static saiga.utils.statics.Constants._PERCENT_ORDER_TAX;


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
        OrderSocketService orderSocketService
) implements OrderService {
    @Override
    public MyResponse driversOrder(DriverOrderRequest driverOrderRequest) {
        // get cabinet of current user
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // saving order
        final Order savedOrder = saveOrderToDatabase(
                new Order(
                        currentUsersCabinet,
                        driverOrderRequest.direction(),
                        driverOrderRequest.amountOfMoney(),
                        driverOrderRequest.timeWhen(),
                        driverOrderRequest.comment()
                )
        );

        // emitting to socket client
        emitNewOrderToSocket(savedOrder);

        return _CREATED
                .setMessage("Order created successfully")
                .addData("data", orderDTOMapper.apply(savedOrder));
    }

    @Override
    public MyResponse usersOrder(UserOrderRequest userOrderRequest) {
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // users order are saving
        final Order savedOrder = saveOrderToDatabase(
                new Order(
                        currentUsersCabinet,
                        userOrderRequest.direction(),
                        userOrderRequest.amountOfMoney(),
                        userOrderRequest.comment()
                )
        );

        // emitting to socket client
        emitNewOrderToSocket(savedOrder);

        return _CREATED
                .setMessage("Order created successfully")
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

        if (order.getCabinetTo() != null)
            throw new BadRequestException("Order already received!");

        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        if (currentUsersCabinet.equals(order.getCabinetFrom()))
            throw new BadRequestException("You can't receive your own order!");

        // change balance of user
        changeUsersBalanceByTax(currentUsersCabinet, '-');

        // reset status of order
        order.setStatus(OrderStatus.RECEIVED);

        order.setCabinetTo(currentUsersCabinet);

        order = saveOrderToDatabase(order);

        // emit received order to socket client
        emitReceiverOrderToSocket(order);

        return _CREATED
                .setMessage("Order received successfully.")
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

        // money of clients screen
        order.setMoney(BigDecimal.valueOf(orderEndRequest.orderMoney()));

        // set length of the taken way
        order.setLengthOfWay(orderEndRequest.OrderLengthOfWay());

        // update status of order
        order.setStatus(OrderStatus.ORDERED);

        saveOrderToDatabase(order);

        return _UPDATED.setMessage("Order Successfully ended");
    }

    @Override
    public MyResponse cancelOwnOrderByOrderId(Long id) {
        Order order = getOrderById(id);
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        if (currentUsersCabinet.getId().equals(order.getCabinetTo().getId()))
            throw new BadRequestException("You only can cancel your own received order");

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

        return _UPDATED.setMessage("Order Canceled successfully");
    }

    private Cabinet getCurrentUsersCabinet() {
        // get current authenticated user
        final User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException("You haven't an access to create order !")
        );
    }

    private Order getOrderById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Order not found with id " + id)
        );
    }

    private Order saveOrderToDatabase(Order order) {
        return repository.save(order);
    }

    private void changeUsersBalanceByTax(Cabinet cabinet, char expressionMethod) {
        switch (expressionMethod) {
            case '-' -> {
                if (cabinet.getBalance().compareTo(_PERCENT_ORDER_TAX) < 0)
                    throw new BadRequestException("You don't have enough tax amount for receive this order, please top up your balance.");

                // subtracting tax amount
                cabinet.setBalance(cabinet.getBalance().subtract(_PERCENT_ORDER_TAX));
            }
            case '+' ->
                // change current users balance
                    cabinet.setBalance(cabinet.getBalance().subtract(_PERCENT_ORDER_TAX));
        }
    }

    private void emitNewOrderToSocket(Order order) {
        orderSocketService.sendOrderToClient(order, order.getType());
    }

    private void emitReceiverOrderToSocket(Order order) {
        orderSocketService.sendReceivedOrderToClient(order, order.getType());
    }
}
