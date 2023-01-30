package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saiga.model.enums.OrderType;
import saiga.model.enums.RoleEnum;
import saiga.payload.request.DirectionRequest;
import saiga.utils.exceptions.TypesInError;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static saiga.utils.statics.GlobalMethodsToHelp.parseDdMMYyyyStringToDate;
import static saiga.utils.statics.ModelConstants._COMMENT_LENGTH;
import static saiga.utils.statics.ModelConstants._ENUM_LENGTH;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Jan 2023
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order extends BaseCreatable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    private Cabinet cabinetFrom;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    private Cabinet cabinetTo;

    @Column(name = "order_type", length = _ENUM_LENGTH)
    @Enumerated(value = EnumType.STRING)
    private OrderType type;

    @OneToOne(
            targetEntity = Direction.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private Direction direction;

    @Column(name = "comment", length = _COMMENT_LENGTH)
    private String comment;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "time_when")
    private Timestamp timeWhen;

    public Order(
            Cabinet cabinetFrom,
            DirectionRequest direction,
            String amountOfMoney,
            String timeWhen,
            String comment
    ) {
        this(
                direction,
                amountOfMoney,
                cabinetFrom,
                comment
        );
        this.timeWhen = parseDdMMYyyyStringToDate(timeWhen);
    }

    public Order(
            Cabinet currentUsersCabinet,
            DirectionRequest direction,
            String amountOfMoney,
            String comment
    ) {
        this(
                direction,
                amountOfMoney,
                currentUsersCabinet,
                comment
        );
        this.timeWhen = Timestamp.valueOf(LocalDateTime.now());
    }

    private Order(
            DirectionRequest direction,
            String amountOfMoney,
            Cabinet cabinetFromR,
            String comment

    ) {
        this.cabinetFrom = cabinetFromR;

        this.direction = new Direction(
                new Address(
                        direction.addressFrom().title(),
                        direction.addressFrom().lat(),
                        direction.addressFrom().lon()
                ),
                direction.addressTo() == null
                        ? null
                        : new Address(
                        direction.addressTo().title(),
                        direction.addressTo().lat(),
                        direction.addressTo().lon()
                )
        );

        this.type = cabinetFromR.getUser().getRole().getRole() == RoleEnum.DRIVER ? OrderType.FROM_DRIVER : OrderType.FROM_USER;

        try {
            this.money = new BigDecimal(amountOfMoney);
        } catch (Exception e) {
            throw new TypesInError("Date type is non parseable");
        }

        this.comment = comment;
    }
}
