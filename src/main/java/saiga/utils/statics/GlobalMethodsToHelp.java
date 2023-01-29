package saiga.utils.statics;

import saiga.utils.exceptions.TypesInError;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
}
