package saiga.service.impl;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import saiga.model.Order;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.mapper.OrderDTOMapper;
import saiga.service.OrderSocketService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
@Service
public record OrderSocketServiceImpl(
        SimpMessageSendingOperations messagingTemplate,
        OrderDTOMapper orderDTOMapper
) implements OrderSocketService {

    @Override
    public void sendOrderToClient(Order order, OrderType orderType) {
        final MyResponse res = MyResponse._OK
                .setMessage("New order")
                .addData("order", orderDTOMapper.apply(order));

        switch (orderType) {
            case FROM_DRIVER -> messagingTemplate.convertAndSend(
                    "/topic/new-order-from-driver",
                    res
            );
            case FROM_USER -> messagingTemplate.convertAndSend(
                    "/topic/new-order-from-user",
                    res
            );
        }
    }

    @Override
    public void sendReceivedOrderToClient(Order order, OrderType orderType) {
        final MyResponse myResponse = MyResponse._UPDATED
                .setMessage("Order received")
                .addData("order", orderDTOMapper.apply(order));
        switch (orderType) {
            case FROM_DRIVER -> messagingTemplate.convertAndSend(
                    "/topic/received-order-from-driver",
                    myResponse
            );
            case FROM_USER -> messagingTemplate.convertAndSend(
                    "/topic/received-order-from-user",
                    myResponse
            );
        }
    }
}
