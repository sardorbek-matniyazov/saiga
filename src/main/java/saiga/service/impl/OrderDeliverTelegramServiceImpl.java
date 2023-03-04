package saiga.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import saiga.client.TelegramFeignClient;
import saiga.config.telegram.TelegramProperties;
import saiga.model.Order;
import saiga.model.enums.OrderType;
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
    public void sendOrderToClient(Order order, OrderType orderType) {
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
                    order.getDirection().getAddressFrom().getTitle(),
                    order.getDirection().getAddressTo().getTitle(),
                    order.getMoney(),
                    order.getTimeWhen(),
                    order.getComment(),
                    order.getCabinetFrom().getUser().getFirstName(),
                    order.getCabinetFrom().getUser().getLastName());
            case FROM_USER -> """
                    <b>Need a taxi </b>
                    <b>Order from user</b>
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>User: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    order.getDirection().getAddressFrom().getTitle(),
                    order.getDirection().getAddressTo() == null ? "No address" : order.getDirection().getAddressTo().getTitle(),
                    order.getMoney(),
                    order.getTimeWhen(),
                    order.getComment(),
                    order.getCabinetFrom().getUser().getFirstName(),
                    order.getCabinetFrom().getUser().getLastName());
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
    public void sendReceivedOrderToClient(Order order, OrderType orderType) {
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
                    order.getDirection().getAddressFrom().getTitle(),
                    order.getDirection().getAddressTo().getTitle(),
                    order.getMoney(),
                    order.getTimeWhen(),
                    order.getComment(),
                    order.getCabinetFrom().getUser().getFirstName(),
                    order.getCabinetFrom().getUser().getLastName(),
                    order.getCabinetTo().getUser().getFirstName(),
                    order.getCabinetTo().getUser().getLastName());
            case FROM_USER -> """
                    <b>Order from user</b>
                    <b>Direction:</b> <i>%s</i> to <i>%s</i>
                    <b>Amount of money:</b> <i>%s</i>
                    <b>Time when:</b> <i>%s</i>
                    <b>Comment:</b> <i>%s</i>
                    <b>User: </b> <i>%s</i> <i>%s</i>
                    <b>Received by: </b> <i>%s</i> <i>%s</i>
                    """.formatted(
                    order.getDirection().getAddressFrom().getTitle(),
                    order.getDirection().getAddressTo() == null ? "No address" : order.getDirection().getAddressTo().getTitle(),
                    order.getMoney(),
                    order.getTimeWhen(),
                    order.getComment(),
                    order.getCabinetFrom().getUser().getFirstName(),
                    order.getCabinetFrom().getUser().getLastName(),
                    order.getCabinetTo().getUser().getFirstName(),
                    order.getCabinetTo().getUser().getLastName());
        };
        TgSendMessage tgSendMessage = new TgSendMessage(
                telegramProperties.getGroupId(),
                text,
                ParseMode.HTML,
                null
        );

        telegramFeignClient.sendMessageToTelegram(telegramProperties.getBotUrl(), tgSendMessage);
    }
}
