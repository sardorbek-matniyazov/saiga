package saiga.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.function.Predicate;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 27 Apr 2023
 **/
public record ConfirmationCodeRequest(
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "Code is required")
        String code
){}
