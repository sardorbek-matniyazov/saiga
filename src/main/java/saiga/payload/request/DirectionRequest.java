package saiga.payload.request;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public record DirectionRequest(
        AddressRequest addressFrom,
        AddressRequest addressTo
) {
}
