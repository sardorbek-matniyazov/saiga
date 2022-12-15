package saiga.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saiga.payload.dto.OrderDto;
import saiga.service.OrderService;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public record OrderController(OrderService service) {

    @PostMapping("/create")
    public HttpEntity<?> createOrder(@RequestBody @Valid OrderDto dto) {
        return service.createOrder(dto).handleResponse();
    }
}
