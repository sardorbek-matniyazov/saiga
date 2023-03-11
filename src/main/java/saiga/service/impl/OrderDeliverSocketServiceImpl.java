package saiga.service.impl;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import saiga.model.enums.OrderType;
import saiga.payload.MyResponse;
import saiga.payload.dto.OrderDTO;
import saiga.service.OrderDeliverService;

import static saiga.payload.MyResponse._OK;
import static saiga.payload.MyResponse._UPDATED;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
@Service
public record OrderDeliverSocketServiceImpl(
        SimpMessageSendingOperations messagingTemplate
) implements OrderDeliverService {

    @Override
    public void sendOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        final MyResponse res = _OK()
                .setMessage("New order")
                .addData("order", orderDTO);

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
    public void sendReceivedOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        final MyResponse myResponse = _UPDATED()
                .setMessage("Order received")
                .addData("order", orderDTO);
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

    @Override
    public void sendCanceledOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        final MyResponse myResponse = _UPDATED()
                .setMessage("Order received")
                .addData("order", orderDTO);
        switch (orderType) {
            case FROM_DRIVER -> messagingTemplate.convertAndSend(
                    "/topic/cancel-order-from-driver",
                    myResponse
            );
            case FROM_USER -> messagingTemplate.convertAndSend(
                    "/topic/cancel-order-from-user",
                    myResponse
            );
        }
    }

    @Override
    public void sendEndOrderToClient(OrderDTO order) {
        final MyResponse myResponse = _UPDATED()
                .setMessage("Order received")
                .addData("order", order);
        messagingTemplate.convertAndSend(
                "/topic/end-order",
                myResponse
        );
    }
}
