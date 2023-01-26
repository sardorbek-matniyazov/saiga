package saiga.payload.dto;

import saiga.model.enums.Status;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String lang,
        String role,
        Status status
) {
}
