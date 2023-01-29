package saiga.payload.mapper;

import org.springframework.stereotype.Service;
import saiga.model.Order;
import saiga.payload.dto.DriverOrderDTO;

import java.util.function.Function;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record DriverOrderDTOMapper(
        UserDTOMapper userDTOMapper
) implements Function<Order, DriverOrderDTO> {
    @Override
    public DriverOrderDTO apply(Order order) {
        return new DriverOrderDTO(
                order.getId(),
                userDTOMapper.apply(order.getCabinetFrom().getUser()),
                order.getDirection(),
                order.getComment(),
                order.getMoney(),
                order.getTimeWhen()
        );
    }
}
