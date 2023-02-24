package saiga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saiga.model.Address;
import saiga.model.enums.AddressType;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Feb 2023
 **/
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByTitleAndAddressType(String title, AddressType aStatic);

    List<Address> findAllByAddressType(AddressType aStatic);
}
