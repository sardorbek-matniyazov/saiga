package saiga.utils.statics;

import org.springframework.security.core.context.SecurityContextHolder;
import saiga.model.User;
import saiga.utils.exceptions.TypesInError;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static saiga.utils.statics.Constants._ONLY_DIGITS_REGEX;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public class GlobalMethodsToHelp {
    public static Timestamp parseDdMMYyyyStringToDate(String s) {
        final Timestamp parse;
        try {
            parse = new Timestamp(new SimpleDateFormat("dd-MM-yyyy").parse(s).getTime());
        } catch (ParseException e) {
            throw new TypesInError("non parseable date format");
        }
        return parse;
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static BigDecimal parseStringMoneyToBigDecimalValue(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            throw new TypesInError("Amount type is non parseable");
        }
    }
}
