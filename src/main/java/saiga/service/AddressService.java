package saiga.service;

import saiga.model.Address;
import saiga.model.enums.AddressType;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 02 Mar 2023
 **/
public interface AddressService {
    List<Address> getAddressByType(AddressType type);
}
