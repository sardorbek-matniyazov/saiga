package saiga.service;

import saiga.model.Order;
import saiga.model.enums.OrderType;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
public interface OrderDeliverService {
    void sendOrderToClient(Order order, OrderType orderType);
    void sendReceivedOrderToClient(Order order, OrderType orderType);
}
