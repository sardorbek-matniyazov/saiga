package saiga.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 05 Feb 2023
 **/
public record TopUpBalanceRequest (
        @NotBlank(message = "Money amount is required!")
        String amount,
        @NotNull(message = "User ID is required!")
        Long userID
) {
}
