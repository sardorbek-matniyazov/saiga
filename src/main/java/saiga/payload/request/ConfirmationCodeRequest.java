package saiga.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 27 Apr 2023
 **/
public record ConfirmationCodeRequest(
        @NotBlank(message = "validation.required")
        String phoneNumber,
        @NotBlank(message = "Code is required")
        String code
){}
