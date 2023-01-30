package saiga.payload.mapper;

import org.springframework.stereotype.Service;
import saiga.model.Order;
import saiga.payload.dto.OrderDTO;

import java.util.function.Function;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Service
public record OrderDTOMapper(
        UserDTOMapper userDTOMapper
) implements Function<Order, OrderDTO> {
    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
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
