package saiga.service;

import saiga.payload.MyResponse;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Feb 2023
 **/
public interface AdminService {
    MyResponse getAllCabinets();
    MyResponse createStaticAddress(AddressRequest addressRequest);

    MyResponse getAllStaticAddresses();

    MyResponse topUpBalance(TopUpBalanceRequest topUpBalanceRequest);
}
