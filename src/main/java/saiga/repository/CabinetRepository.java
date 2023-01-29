package saiga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saiga.model.Cabinet;

import java.util.Optional;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
    Optional<Cabinet> findByUserId(Long id);
}
