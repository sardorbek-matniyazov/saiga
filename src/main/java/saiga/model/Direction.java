package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 29 Jan 2023
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "direction")
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            targetEntity = Address.class,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    private Address addressFrom;

    @ManyToOne(
            targetEntity = Address.class,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    private Address addressTo;

    @Column(name = "is_favourite")
    private Boolean isFavourite = false;

    public Direction(Address addressFrom, Address addressTo) {
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
    }
}
