package saiga.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import saiga.payload.telegram.TgResult;
import saiga.payload.telegram.TgSendMessage;
import saiga.utils.statics.Constants;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
@FeignClient(
        url = Constants._TELEGRAM_BASE_URL,
        name = "TELEGRAM-SERVICE"
) public interface TelegramFeignClient {
    @PostMapping(value = "{path}/sendMessage")
    TgResult sendMessageToTelegram(@PathVariable String path, TgSendMessage sendMessage);

    @PostMapping(value = "{path}/sendDocument")
    TgResult sendFileToTelegram(@PathVariable String botUrl, SendDocument fileName);
}
