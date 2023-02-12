package saiga.payload.request;

import saiga.payload.request.enums.SocketEvent;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
public record SocketEventRequest(
        String token,
        SocketEvent event
) {
}
