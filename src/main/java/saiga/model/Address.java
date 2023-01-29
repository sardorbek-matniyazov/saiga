package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static saiga.utils.statics.ModelConstants._TITLE_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    // with description
    @Column(name = "address_title", length = _TITLE_LENGTH)
    private String title;

    // with map
    @Column(name = "address_latitude")
    private Double latitude;
    @Column(name = "address_longitude")
    private Double longitude;

    public Address(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
