package saiga.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.Order;
import saiga.model.User;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.dto.OrderDTO;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.UserOrderRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.OrderService;
import saiga.service.OrderSocketService;
import saiga.utils.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static saiga.payload.MyResponse._BAD_REQUEST;
import static saiga.payload.MyResponse._CREATED;
import static saiga.utils.statics.Constants._PERCENT_ORDER_TAX;


/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record OrderServiceImpl (
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
        final Order savedOrder = repository.save(
                new Order(
                        currentUsersCabinet,
                        driverOrderRequest.direction(),
                        driverOrderRequest.amountOfMoney(),
                        driverOrderRequest.timeWhen(),
                        driverOrderRequest.comment()
                )
        );

        // emitting to socket client
        orderSocketService.sendOrderToClient(savedOrder, OrderType.FROM_USER);

        return _CREATED
                .setMessage("Order created successfully")
                .addData("data", orderDTOMapper.apply(savedOrder));
    }

    @Override
    public MyResponse usersOrder(UserOrderRequest userOrderRequest) {
        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        // users order are saving
        final Order savedOrder = repository.save(
                new Order(
                        currentUsersCabinet,
                        userOrderRequest.direction(),
                        userOrderRequest.amountOfMoney(),
                        userOrderRequest.comment()
                )
        );

        // emitting to socket client
        orderSocketService.sendOrderToClient(savedOrder, OrderType.FROM_USER);

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
        final Order order = repository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Order with id " + orderId + " not found")
        );

        if (order.getCabinetTo() != null)
            return badRequestHandler("Order already received!");

        final Cabinet currentUsersCabinet = getCurrentUsersCabinet();

        if (currentUsersCabinet.equals(order.getCabinetFrom()))
            return badRequestHandler("You can't receive your own order!");

        if (currentUsersCabinet.getBalance().compareTo(_PERCENT_ORDER_TAX) <= 0)
            return badRequestHandler("You don't have enough tax amount for receive this order, please top up your balance.");

        // subtracting tax amount
        currentUsersCabinet.setBalance(currentUsersCabinet.getBalance().subtract(_PERCENT_ORDER_TAX));

        order.setCabinetTo(currentUsersCabinet);

        // emitting to socket client
        switch (order.getType()) {
            case FROM_USER -> orderSocketService.sendOrderToClient(order, OrderType.FROM_USER);
            case FROM_DRIVER -> orderSocketService.sendOrderToClient(order, OrderType.FROM_DRIVER);
        }

        return _CREATED
                .setMessage("Order received successfully.")
                .addData("data", orderDTOMapper.apply(repository.save(order)));
    }

    @Override
    public List<OrderDTO> currentDriversHistoryOfOrder() {
        return repository.findAllByCabinetTo_Id(getCurrentUsersCabinet().getId())
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    private Cabinet getCurrentUsersCabinet() {
        // get current authenticated user
        final User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException("You haven't an access to create order !")
        );
    }

    private MyResponse badRequestHandler(String message) {
        return _BAD_REQUEST.setMessage(message);
    }
}
