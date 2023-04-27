package saiga.service.telegram.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import saiga.client.TelegramFeignClient;
import saiga.config.telegram.TelegramProperties;
import saiga.payload.telegram.TgResult;
import saiga.payload.telegram.TgSendMessage;
import saiga.service.telegram.TgMainService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 04 Mar 2023
 **/
@Service
public record TgMainServiceImpl (
        TelegramProperties telegramProperties,
        TelegramFeignClient telegramFeignClient
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

    @Override
    public void sendErrorMessage(String message) {
        TgSendMessage tgSendMessage = new TgSendMessage(
                telegramProperties.getGroupId(),
                message,
                ParseMode.HTML,
                null
        );
        telegramFeignClient.sendMessageToTelegram(telegramProperties.getBotUrl(), tgSendMessage);
    }

    @Override
    public void sendFile(String fileName) {
        SendDocument sendDocument = null;
        try {
            sendDocument = new SendDocument(
                    telegramProperties.getGroupId(),
                    new InputFile(new FileInputStream(new File(fileName)), fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        final TgResult tgResult = telegramFeignClient.sendFileToTelegram(telegramProperties.getBotUrl(), sendDocument);
        sendErrorMessage(tgResult.toString());
    }
}
