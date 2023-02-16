package saiga.service;

import org.springframework.http.HttpEntity;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.dto.OrderDTO;
import saiga.payload.request.DriverOrderRequest;
import saiga.payload.request.OrderEndRequest;
import saiga.payload.request.UserOrderRequest;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface OrderService {
    MyResponse driversOrder(DriverOrderRequest driverOrderRequest);

    MyResponse usersOrder(UserOrderRequest userOrderRequest);

    List<OrderDTO> nonReceivedOrders(OrderType orderType);

    MyResponse receiveOrderById(Long orderId);

    List<OrderDTO> currentDriversHistoryOfOrder();

    MyResponse endOrderById(OrderEndRequest orderEndRequest);

    MyResponse cancelOwnOrderByOrderId(Long id);
}
