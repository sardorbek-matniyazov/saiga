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
    BigDecimal _ORDER_TAX = BigDecimal.valueOf(1000D);
    String _ONLY_DIGITS_REGEX = "[0-9]+";
}
