package saiga.payload.request;

import saiga.utils.validations.CheckUserDirectionIsValid;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public record UserOrderRequest(
        @CheckUserDirectionIsValid
        DirectionRequest direction,
        String comment,
        String amountOfMoney
) {
}
