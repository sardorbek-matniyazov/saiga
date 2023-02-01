package saiga.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.Order;
import saiga.model.User;
import saiga.payload.MyResponse;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.UserOrderRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.OrderService;
import saiga.socket.SocketModule;
import saiga.utils.exceptions.NotFoundException;

import java.util.List;


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
        @Lazy SocketModule socketModule
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

        return MyResponse._CREATED
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

        socketModule.sendMessageToUserByEmit(savedOrder);

        return MyResponse._CREATED
                .setMessage("Order created successfully")
                .addData("data", orderDTOMapper.apply(savedOrder));
    }

    @Override
    public List<Order> getAllNotReceivedOrders() {
        return repository.findAllByCabinetToIsNull(Sort.by(Sort.Direction.DESC, "id"));
    }

    private Cabinet getCurrentUsersCabinet() {
        // get current authenticated user
        final User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException("You haven't an access to create order !")
        );
    }

}
