package saiga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saiga.model.ConfirmationCode;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Apr 2023
 **/
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, String> {
}
