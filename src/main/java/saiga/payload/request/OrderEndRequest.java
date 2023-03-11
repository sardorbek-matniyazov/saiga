package saiga.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 17 Feb 2023
 **/
public record OrderEndRequest(
        @NotNull(message = "Order id required")
        Long orderId,
        @NotNull(message = "Order money is required")
        String orderMoney,
        @NotNull(message = "Order Length of Way is required")
        Double OrderLengthOfWay,
        @NotBlank(message = "Order started time of way is required")
        String startedTimeOfWay,
        @NotBlank(message = "Order finished time of way is required")
        String finishedTimeOfWay
) {
}
