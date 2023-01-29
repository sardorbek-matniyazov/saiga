package saiga.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import saiga.model.Cabinet;
import saiga.model.Order;
import saiga.model.User;
import saiga.payload.MyResponse;
import saiga.payload.mapper.DriverOrderDTOMapper;
import saiga.payload.request.DriverOrderRequest;
import saiga.repository.CabinetRepository;
import saiga.repository.OrderRepository;
import saiga.service.OrderService;
import saiga.utils.exceptions.NotFoundException;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record OrderServiceImpl (
        OrderRepository repository,
        CabinetRepository cabinetRepository,
        DriverOrderDTOMapper driverOrderDTOMapper
) implements OrderService {
    @Override
    public MyResponse driversOrder(DriverOrderRequest driverOrderRequest) {
        // get current authenticated user
        final User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final Cabinet cabinet = cabinetRepository.findByUserId(currentUser.getId()).orElseThrow(
                () -> new NotFoundException("You haven't an access to create order !")
        );

        // saving order
        final Order savedOrder = repository.save(
                new Order(
                        cabinet,
                        driverOrderRequest.direction(),
                        driverOrderRequest.amountOfMoney(),
                        driverOrderRequest.timeWhen(),
                        driverOrderRequest.comment()
                )
        );

        return MyResponse._CREATED
                .setMessage("Order created successfully")
                .addData("data", driverOrderDTOMapper.apply(savedOrder));
    }
}
