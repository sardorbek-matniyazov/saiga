package saiga.payload.dto;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String phoneNumber,
        String lang,
        String role
) {
}
