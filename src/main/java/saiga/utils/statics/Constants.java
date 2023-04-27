package saiga.utils.statics;

import java.math.BigDecimal;

public interface Constants {
    int _TITLE_LENGTH = 70;
    int _NAME_LENGTH = 50;
    int _ENUM_LENGTH = 20;
    int _NUMBER_LENGTH = 20;
    int _DESC_LENGTH = 20;
    int _COMMENT_LENGTH = 500;
    int _TOKEN_LENGTH = 200;
    BigDecimal _ORDER_TAX = BigDecimal.valueOf(500D);
    BigDecimal _STARTING_BALANCE = BigDecimal.valueOf(15_000D);
    String _ONLY_DIGITS_REGEX = "[0-9]+";
    Integer _CONFIRMATION_CODE_LIMIT = 3;

    String _TELEGRAM_BASE_URL = "https://api.telegram.org/";
}
