package saiga.payload.dto;

import saiga.model.enums.Status;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String phoneNumber,
        String lang,
        String role,
        Status status
) {
}
