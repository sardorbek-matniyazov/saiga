package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saiga.model.enums.Status;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.HashSet;
import java.util.Set;

import static saiga.model.enums.Status.ACTIVE;
import static saiga.utils.constants.ModelConstants._ENUM_LENGTH;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "driver_cabinet")
public class Cabinet extends BaseCreatable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "cabinet_id")
    private Long id;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            targetEntity = User.class
    )
    private User user;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            targetEntity = Address.class
    )
    private Address region;

    @Column(name = "cabinet_balance")
    private Double amount = 0D;

    @Column(name = "cabinet_status", length = _ENUM_LENGTH)
    private Status status = ACTIVE;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            targetEntity = Direction.class
    )
    @JoinColumn(
            name = "cabinet_id",
            referencedColumnName = "cabinet_id"
    )
    private Set<Direction> directions = new HashSet<>();

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            targetEntity = Car.class
    )
    private Car car;
}
