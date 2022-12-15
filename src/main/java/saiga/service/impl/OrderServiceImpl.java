package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.payload.dao.MyResponse;
import saiga.payload.dto.OrderDto;
import saiga.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public MyResponse createOrder(OrderDto dto) {

        return null;
    }
}
