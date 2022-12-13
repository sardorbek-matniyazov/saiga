package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
@EntityListeners(value = AuditingEntityListener.class)
public class Order extends BaseCreatable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.EAGER,
            targetEntity = Direction.class
    )
    @JoinColumn(
            name = "if_order_id",
            referencedColumnName = "id"
    )
    private Direction direction;

    @Column(name = "order_price")
    private Double price;

    @ManyToOne(
            cascade = {CascadeType.MERGE},
            fetch = FetchType.EAGER,
            targetEntity = Cabinet.class
    )
    private Cabinet driver;

    @CreatedBy
    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE},
            fetch = FetchType.EAGER,
            targetEntity = User.class
    )
    private User orderedUser;
}
