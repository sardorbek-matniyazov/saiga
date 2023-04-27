package saiga.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 17 Feb 2023
 **/
public record OrderEndRequest(
        @NotNull(message = "validation.required")
        Long orderId,
        @NotNull(message = "validation.required")
        String orderMoney,
        @NotNull(message = "validation.required")
        Double OrderLengthOfWay,
        @NotBlank(message = "validation.required")
        String startedTimeOfWay,
        @NotBlank(message = "validation.required")
        String finishedTimeOfWay
) {
}
