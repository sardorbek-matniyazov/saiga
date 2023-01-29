package saiga.payload.dto;

import saiga.model.Direction;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public record DriverOrderDTO(
        long id,
        UserDTO from,
        Direction direction,
        String comment,
        BigDecimal money,
        Timestamp timeWhen
) {
}
