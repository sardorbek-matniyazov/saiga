package saiga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saiga.model.enums.RoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotNull(message = "Role is required")
    private RoleEnum role;
}
