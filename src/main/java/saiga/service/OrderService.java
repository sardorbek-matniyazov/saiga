package saiga.service;

import saiga.payload.MyResponse;
import saiga.payload.request.DriverOrderRequest;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface OrderService {
    MyResponse driversOrder(DriverOrderRequest driverOrderRequest);
}
