package saiga.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import saiga.model.Order;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCabinetToIsNull(Sort sort);
}
