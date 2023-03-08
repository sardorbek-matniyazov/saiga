package saiga.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import saiga.model.Address;
import saiga.model.enums.AddressType;
import saiga.repository.AddressRepository;
import saiga.service.impl.AddressServiceImpl;

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Mar 2023
 **/
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock private AddressRepository addressRepository;
    private AddressService service;

    @BeforeEach
    void setUp() {
        service = new AddressServiceImpl(addressRepository);
    }

    @Test
    void canGetAddressByTypeSTATIC() {
        // given
        final List<Address> addressByType = service.getAddressByType(AddressType.STATIC);

        // when
        verify(addressRepository).findAllByAddressType(AddressType.STATIC);

        // then
    }
}