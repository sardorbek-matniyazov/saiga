package saiga.payload.dto;

import java.math.BigDecimal;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Feb 2023
 **/
public record BalanceInOutDTO(
        BigDecimal balance,
        BigDecimal benefit,
        BigDecimal balanceOut
) {
}
