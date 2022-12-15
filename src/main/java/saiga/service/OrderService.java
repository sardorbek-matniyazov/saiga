package saiga.service;

import saiga.payload.dao.MyResponse;
import saiga.payload.dto.OrderDto;

public interface OrderService {
    MyResponse createOrder(OrderDto dto);
}
