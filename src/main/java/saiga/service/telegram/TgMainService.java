package saiga.service.telegram;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
public interface TgMainService {
    void handleMessage(Message message);

    void handleCallbackQuery(CallbackQuery callbackQuery);
}
