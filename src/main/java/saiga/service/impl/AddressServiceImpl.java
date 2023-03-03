package saiga.service.impl;

import org.springframework.stereotype.Service;
import saiga.model.Address;
import saiga.model.enums.AddressType;
import saiga.service.AddressService;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 02 Mar 2023
 **/
@Service
public class AddressServiceImpl implements AddressService {
    @Override
    public List<Address> getAddressByType(AddressType type) {
        return null;
    }
}
