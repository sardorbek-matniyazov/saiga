package saiga.payload.request;

import saiga.model.enums.Lang;

import javax.validation.constraints.Size;

import static saiga.utils.statics.Constants._NAME_LENGTH;
import static saiga.utils.statics.Constants._NUMBER_LENGTH;

public record UpdateUserRequest(
        @Size(min = 2, max = _NAME_LENGTH)
        String firstName,
        @Size(min = 2, max = _NAME_LENGTH)
        String lastName,
        Lang lang,
        @Size(min = 2, max = _NUMBER_LENGTH)
        String phoneNumber
) {
}
