package saiga.payload.request;

import saiga.model.enums.Lang;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        Lang lang,
        String phoneNumber
) {
}
