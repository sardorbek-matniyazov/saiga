package saiga.payload.request;

import saiga.utils.validations.CheckDriverDirectionIsValid;

import javax.validation.constraints.NotBlank;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public record DriverOrderRequest(
        @CheckDriverDirectionIsValid
        DirectionRequest direction,
        String timeWhen,
        String comment,
        @NotBlank(message = "validation.required")
        String amountOfMoney
) {
}
