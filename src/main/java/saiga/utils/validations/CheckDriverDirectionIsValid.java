package saiga.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DriverOrderDirectionHandler.class)
public @interface CheckDriverDirectionIsValid {
    String message() default "validation.driver_order";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
