package saiga.utils.validations;

import saiga.payload.request.DirectionRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public class DriverOrderDirectionHandler implements ConstraintValidator<CheckDriverDirectionIsValid, DirectionRequest>  {
    @Override
    public boolean isValid(DirectionRequest directionRequest, ConstraintValidatorContext constraintValidatorContext) {
        return directionRequest != null
                && (directionRequest.addressFrom() != null && directionRequest.addressTo() != null);
    }
}
