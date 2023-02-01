package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.UserOrderRequest;
import saiga.service.OrderService;

import javax.validation.Valid;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@RestController
@RequestMapping(value = "api/orders")
public record OrderController (
        OrderService service
) {

    @PostMapping(value = "user-order", name = "Users order")
    public HttpEntity<?> usersOrder(@RequestBody @Valid UserOrderRequest userOrderRequest) {
        return service.usersOrder(userOrderRequest).handleResponse();
    }

    @PostMapping(value = "driver-order", name = "Drivers order")
    public HttpEntity<?> driversOrder(@RequestBody @Valid DriverOrderRequest driverOrderRequest) {
        return service.driversOrder(driverOrderRequest).handleResponse();
    }
}
