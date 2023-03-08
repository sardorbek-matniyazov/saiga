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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Jan 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cabinet")
public class Cabinet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cabinet_id")
    private Long id;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            optional = false
    )
    private User user;

    @Column(name = "cabinet_balance")
    private BigDecimal balance = BigDecimal.valueOf(100_000L);

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<Direction> directions = new ArrayList<>();

    public Cabinet(User user) {
        this.user = user;
    }

    public Cabinet(Long id, User user, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }
}
