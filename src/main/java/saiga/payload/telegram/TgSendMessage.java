package saiga.payload.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public record TgSendMessage (
    @JsonProperty(value = "chat_id")
    String chatId,
    String text,
    @JsonProperty(value = "parse_mode")
    String parseMode,
    @JsonProperty(value = "reply_to_message_id")
    Integer replyToMessageId
) {}