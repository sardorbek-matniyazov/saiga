package saiga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saiga.model.Order;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface OrderRepository extends JpaRepository<Order, Long> {
}
