package saiga.payload.request;

import saiga.model.enums.RoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static saiga.utils.statics.Constants.*;

public record SignUpRequest(
        @Size(min = 2, max = _NUMBER_LENGTH)
        @NotBlank(message = "validation.required")
        String phoneNumber,
        @Size(min = 2, max = _NAME_LENGTH)
        @NotBlank(message = "validation.required")
        String firstName,
        @Size(min = 2, max = _NAME_LENGTH)
        @NotBlank(message = "validation.required")
        String lastName,
        @NotNull(message = "validation.required")
        RoleEnum role
) {
}
