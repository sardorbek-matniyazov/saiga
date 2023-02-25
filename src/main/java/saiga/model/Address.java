package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saiga.model.enums.AddressType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static saiga.utils.statics.Constants._ENUM_LENGTH;
import static saiga.utils.statics.Constants._TITLE_LENGTH;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", length = _ENUM_LENGTH)
    private AddressType addressType = AddressType.DYNAMIC;

    @Column(name = "address_district", length = _TITLE_LENGTH)
    private String district;

    public Address(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Address(String title, double latitude, double longitude, AddressType addressType) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
