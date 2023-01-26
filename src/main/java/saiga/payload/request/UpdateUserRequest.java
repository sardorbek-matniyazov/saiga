package saiga.payload.request;

import saiga.model.enums.Lang;

public record UpdateUserRequest(
        String firstname,
        String lastname,
        Lang lang,
        String phoneNumber
) {
}
