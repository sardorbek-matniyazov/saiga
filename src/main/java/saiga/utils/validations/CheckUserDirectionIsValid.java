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
 * @created : 30 Jan 2023
 **/
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserOrderDirectionHandler.class)
public @interface CheckUserDirectionIsValid {
    String message() default "validation.user_order";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
