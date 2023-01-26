package saiga.payload.request;

import saiga.model.enums.RoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record SignUpRequest(
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "Phone number is required")
        String firstName,
        @NotBlank(message = "Phone number is required")
        String lastName,
        @NotNull(message = "Role is required")
        RoleEnum role
) {
}
