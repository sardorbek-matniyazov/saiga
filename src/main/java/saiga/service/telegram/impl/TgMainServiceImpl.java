package saiga.service.telegram.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import saiga.config.telegram.TelegramProperties;
import saiga.service.telegram.TgMainService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
@Service
public record TgMainServiceImpl (
        TelegramProperties telegramProperties
) implements TgMainService {
    @Override
    public void handleMessage(Message message) {
        System.out.println(message);
        System.out.println(telegramProperties);
    }

    @Override
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        System.out.println(callbackQuery);
        System.out.println(telegramProperties);
    }
}
