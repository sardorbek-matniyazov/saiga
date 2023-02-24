package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.OrderEndRequest;
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

    @GetMapping(value = "non-received/driver")
    public HttpEntity<?> nonReceivedOrdersDriver() {
        return MyResponse._OK().addData("data", service.nonReceivedOrders(OrderType.FROM_DRIVER)).handleResponse();
    }

    @GetMapping(value = "non-received/user")
    public HttpEntity<?> nonReceivedOrdersUser() {
        return MyResponse._OK().addData("data", service.nonReceivedOrders(OrderType.FROM_USER)).handleResponse();
    }

    @PostMapping(value = "receive/{orderId}")
    public HttpEntity<?> receiveOrderById(@PathVariable Long orderId) {
        return service.receiveOrderById(orderId).handleResponse();
    }

    @GetMapping(value = "history")
    public HttpEntity<?> currentDriversHistoryOfOrder() {
        return MyResponse._OK().addData("data", service.currentDriversHistoryOfOrder()).handleResponse();
    }

    @PutMapping(value = "end-order")
    public HttpEntity<?> endOrderById(@RequestBody @Valid OrderEndRequest endRequest) {
        return service.endOrderById(endRequest).handleResponse();
    }

    @PutMapping(value = "cancel-order/{id}")
    public HttpEntity<?> cancelOwnOrder(@PathVariable Long id) {
        return service.cancelOwnOrderByOrderId(id).handleResponse();
    }
}
