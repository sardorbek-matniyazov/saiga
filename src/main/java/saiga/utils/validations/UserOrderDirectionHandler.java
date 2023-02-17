package saiga.utils.validations;

import saiga.payload.request.DirectionRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 30 Jan 2023
 **/
public class UserOrderDirectionHandler implements ConstraintValidator<CheckUserDirectionIsValid, DirectionRequest> {
    @Override
    public boolean isValid(DirectionRequest directionRequest, ConstraintValidatorContext constraintValidatorContext) {
        return directionRequest != null
                && directionRequest.addressFrom() != null
                && ((directionRequest.addressFrom().lat() != 0.0 && directionRequest.addressFrom().lon() != 0.0) || directionRequest.addressFrom().title() != null);
    }
}
