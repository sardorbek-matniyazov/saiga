package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static saiga.utils.constants.ModelConstants._DESC_LENGTH;
import static saiga.utils.constants.ModelConstants._NUMBER_LENGTH;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "car")
public class Car extends BaseCreatable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    private CarModel model;

    @Column(name = "car_number", length = _NUMBER_LENGTH)
    private String number;

    @Column(name = "car_color", length = _DESC_LENGTH)
    private String color;
}
