package saiga.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import saiga.model.Order;
import saiga.model.enums.OrderStatus;
import saiga.model.enums.OrderType;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCabinetToIsNullAndType(Sort sort, OrderType type);

    List<Order> findAllByCabinetTo_Id(Long id);

    Double countByCabinetToIdAndStatus(Long cabinetTo_id, OrderStatus status);

    @Query(
            value = """
                    select sum(money) from orders where cabinet_to_cabinet_id = 2 and order_status = 'ORDERED'
                    """,
            nativeQuery = true
    )
    BigDecimal sumOfBenefitByCabinetToIdAndStatus(Long id, OrderStatus ordered);
}
