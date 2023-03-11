package saiga.utils.statics;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import saiga.model.User;
import saiga.utils.exceptions.TypesInError;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Component
public class GlobalMethodsToHelp {
    public Timestamp parseDdMMYyyyStringToDate(String s) {
        final Timestamp parse;
        try {
            // todo: hour pattern should be added
            parse = new Timestamp(new SimpleDateFormat("dd-MM-yyyy").parse(s).getTime());
        } catch (ParseException e) {
            throw new TypesInError("non parseable date format");
        }
        return parse;
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void isValidDecimalValue(String s) {
        if (s.matches("^[1-9]\\d*(\\.\\d+)?$")) {
            return;
        } else {
            throw new TypesInError("Amount type is not valid");
        }
    }
}
