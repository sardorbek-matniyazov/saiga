package saiga.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saiga.model.enums.Lang;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String firstname;
    private String lastname;
    private Lang lang;
    private String phoneNumber;
}
