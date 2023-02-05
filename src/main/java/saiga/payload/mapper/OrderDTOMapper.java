package saiga.payload.mapper;

import org.springframework.stereotype.Service;
import saiga.model.Order;

import java.util.function.Function;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record OrderDTOMapper(
        UserDTOMapper userDTOMapper
) implements Function<Order, saiga.payload.dto.OrderDTO> {
    @Override
    public saiga.payload.dto.OrderDTO apply(Order order) {
        return new saiga.payload.dto.OrderDTO(
                order.getId(),
                userDTOMapper.apply(order.getCabinetFrom().getUser()),
                order.getCabinetTo() != null ? userDTOMapper.apply(order.getCabinetTo().getUser()) : null,
                order.getDirection(),
                order.getComment(),
                order.getMoney(),
                order.getTimeWhen()
        );
    }
}
