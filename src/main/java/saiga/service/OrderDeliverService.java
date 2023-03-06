package saiga.service;

import saiga.model.enums.OrderType;
import saiga.payload.dto.OrderDTO;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
public interface OrderDeliverService {
    void sendOrderToClient(OrderDTO order, OrderType type);
    void sendReceivedOrderToClient(OrderDTO order, OrderType type);

    void sendCanceledOrderToClient(OrderDTO order, OrderType type);
}
