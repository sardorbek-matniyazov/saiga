package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.request.DriverOrderRequest;
import saiga.service.OrderService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@RestController
@RequestMapping(value = "orders")
public record OrderController (
        OrderService service
) {
//
//    @PostMapping(value = "user-order", name = "Users order")
//    public HttpEntity<?> usersOrder() {
//    }

    @PostMapping(value = "driver-order", name = "Drivers order")
    public HttpEntity<?> driversOrder(@RequestBody DriverOrderRequest driverOrderRequest) {
        return service.driversOrder(driverOrderRequest).handleResponse();
    }
}
