package saiga.utils.exceptions;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 17 Feb 2023
 **/
public class BadRequestException extends RuntimeException {
    public BadRequestException(String s) {
        super(s);
    }
}
