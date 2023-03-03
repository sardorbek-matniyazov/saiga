package saiga.utils.validations;

import saiga.payload.request.DirectionRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static saiga.utils.statics.Constants._TITLE_LENGTH;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public class DriverOrderDirectionHandler implements ConstraintValidator<CheckDriverDirectionIsValid, DirectionRequest>  {
    @Override
    public boolean isValid(DirectionRequest directionRequest, ConstraintValidatorContext constraintValidatorContext) {
        return directionRequest != null
                && (directionRequest.addressFrom() != null && (directionRequest.addressFrom().title() == null || directionRequest.addressFrom().title().length() <= _TITLE_LENGTH))
                && (directionRequest.addressTo() != null && (directionRequest.addressTo().title() == null || directionRequest.addressTo().title().length() <= _TITLE_LENGTH));
    }
}
