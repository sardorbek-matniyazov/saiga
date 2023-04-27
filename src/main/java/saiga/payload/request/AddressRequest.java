package saiga.payload.request;

import javax.validation.constraints.Size;

import static saiga.utils.statics.Constants._TITLE_LENGTH;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public record AddressRequest(
        double lat,
        double lon,
        @Size(min = 5, max = _TITLE_LENGTH, message = "validation.length")
        String title,
        @Size(min = 5, max = _TITLE_LENGTH, message = "validation.length")
        String district
) {
}
