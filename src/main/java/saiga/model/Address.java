package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static saiga.utils.constants.ModelConstants._TITLE_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "balance")
public class Address {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_title", length = _TITLE_LENGTH)
    private String title;

    @Column(name = "address_latitude")
    private Double latitude;
    @Column(name = "address_longitude")
    private Double longitude;
}
