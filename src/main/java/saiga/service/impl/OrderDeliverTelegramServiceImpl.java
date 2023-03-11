package saiga.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import saiga.client.TelegramFeignClient;
import saiga.config.telegram.TelegramProperties;
import saiga.model.enums.OrderType;
import saiga.payload.dto.OrderDTO;
import saiga.payload.telegram.TgSendMessage;
import saiga.service.OrderDeliverService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
@Service
public record OrderDeliverTelegramServiceImpl (
        TelegramProperties telegramProperties,
        TelegramFeignClient telegramFeignClient
) implements OrderDeliverService {
    @Override
    public void sendOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        String text = switch (orderType) {
            case FROM_DRIVER -> """
                    <b> Need a user to order </b>
                    
                    
                    <b>Order from driver</b>
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>Driver: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    orderDTO.direction().getAddressFrom().getTitle(),
                    orderDTO.direction().getAddressTo() == null ? "No address" : orderDTO.direction().getAddressTo().getTitle(),
                    orderDTO.money(),
                    orderDTO.timeWhen(),
                    orderDTO.comment(),
                    orderDTO.fromUser().firstName(),
                    orderDTO.fromUser().lastName());
            case FROM_USER -> """
                    <b>Need a taxi </b>
                    
                    
                    <b>Order from user</b>
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>User: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    orderDTO.direction().getAddressFrom().getTitle(),
                    orderDTO.direction().getAddressTo() == null ? "No address" : orderDTO.direction().getAddressTo().getTitle(),
                    orderDTO.money(),
                    orderDTO.timeWhen(),
                    orderDTO.comment(),
                    orderDTO.fromUser().firstName(),
                    orderDTO.fromUser().lastName());
        };
        TgSendMessage tgSendMessage = new TgSendMessage(
                telegramProperties.getGroupId(),
                text,
                ParseMode.HTML,
                null
        );
        telegramFeignClient.sendMessageToTelegram(telegramProperties.getBotUrl(), tgSendMessage);
    }

    @Override
    public void sendReceivedOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        String text = switch (orderType) {
            case FROM_DRIVER -> """
                    <b>Order from driver</b>
                    
                    
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>Driver: </b> <i>%s</i> <i>%s</i>
                    <b>Received by: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    orderDTO.direction().getAddressFrom().getTitle(),
                    orderDTO.direction().getAddressTo().getTitle(),
                    orderDTO.money(),
                    orderDTO.timeWhen(),
                    orderDTO.comment(),
                    orderDTO.fromUser().firstName(),
                    orderDTO.fromUser().lastName(),
                    orderDTO.toUser().firstName(),
                    orderDTO.toUser().lastName());
            case FROM_USER -> """
                    <b>Order from user</b>
                    
                    
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>User: </b> <i>%s</i> <i>%s</i>
                    <b>Received by: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    orderDTO.direction().getAddressFrom().getTitle(),
                    orderDTO.direction().getAddressTo() == null ? "No address" : orderDTO.direction().getAddressTo().getTitle(),
                    orderDTO.money(),
                    orderDTO.timeWhen(),
                    orderDTO.comment(),
                    orderDTO.fromUser().firstName(),
                    orderDTO.fromUser().lastName(),
                    orderDTO.toUser().firstName(),
                    orderDTO.toUser().lastName());
        };
        TgSendMessage tgSendMessage = new TgSendMessage(
                telegramProperties.getGroupId(),
                text,
                ParseMode.HTML,
                null
        );

        telegramFeignClient.sendMessageToTelegram(telegramProperties.getBotUrl(), tgSendMessage);
    }

    @Override
    public void sendCanceledOrderToClient(OrderDTO orderDTO, OrderType orderType) {
        String text = switch (orderType) {
            case FROM_DRIVER -> """
                    <b>Order Canceled</b>
                    
                    
                    
                    <b>id: </b> <i>%s</i>
                    """.formatted(
                    orderDTO.id());
            case FROM_USER -> """
                    <b>Order Canceled</b>
                    
                    
                    
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>User: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    orderDTO.direction().getAddressFrom().getTitle(),
                    orderDTO.direction().getAddressTo() == null ? "No address" : orderDTO.direction().getAddressTo().getTitle(),
                    orderDTO.money(),
                    orderDTO.timeWhen(),
                    orderDTO.comment(),
                    orderDTO.fromUser().firstName(),
                    orderDTO.fromUser().lastName());
        };
    }

    @Override
    public void sendEndOrderToClient(OrderDTO order) {
        String text = """
                <b>Order Ended</b>
                
                
                
                <b>Direction:</b> <i>%s</i> to <i>%s</i>
                <b>Amount of money:</b> <i>%s</i>
                <b>Time when:</b> <i>%s</i>
                <b>Comment:</b> <i>%s</i>
                <b>Driver: </b> <i>%s</i> <i>%s</i>
                """.formatted(
                order.direction().getAddressFrom().getTitle(),
                order.direction().getAddressTo().getTitle(),
                order.money(),
                order.timeWhen(),
                order.comment(),
                order.fromUser().firstName(),
                order.fromUser().lastName());
        TgSendMessage tgSendMessage = new TgSendMessage(
                telegramProperties.getGroupId(),
                text,
                ParseMode.HTML,
                null
        );
        telegramFeignClient.sendMessageToTelegram(telegramProperties.getBotUrl(), tgSendMessage);
    }
}
