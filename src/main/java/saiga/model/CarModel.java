package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saiga.model.enums.CarType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static saiga.utils.constants.ModelConstants._ENUM_LENGTH;
import static saiga.utils.constants.ModelConstants._NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "car_model")
public class CarModel {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "car_model_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_model_type", length = _ENUM_LENGTH, nullable = false)
    private CarType type;

    @Column(name = "car_model_name", length = _NAME_LENGTH, nullable = false)
    private String name;
}
