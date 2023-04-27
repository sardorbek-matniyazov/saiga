package saiga.service;

import saiga.model.Address;
import saiga.payload.MyResponse;
import saiga.payload.dto.CabinetDTO;
import saiga.payload.request.AddressRequest;
import saiga.payload.request.TopUpBalanceRequest;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Feb 2023
 **/
public interface AdminService {
    List<CabinetDTO> getAllCabinets();
    MyResponse createStaticAddress(AddressRequest addressRequest);

    List<Address> getAllStaticAddresses();

    MyResponse topUpBalance(TopUpBalanceRequest topUpBalanceRequest);

    MyResponse backupDb();
}
