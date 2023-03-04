package saiga.controller.telegram;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import saiga.service.telegram.TgMainService;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
@RestController
@RequestMapping("telegram")
public record TgMainController(
        TgMainService tgService
) {

    @PostMapping
    public void useBot(@RequestBody Update update) {
        if (update.hasMessage()) {
            tgService.handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            tgService.handleCallbackQuery(update.getCallbackQuery());
        }
    }
}
