package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import saiga.model.enums.RoleEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static saiga.utils.constants.ModelConstants._ENUM_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", length = _ENUM_LENGTH)
    private RoleEnum role;

    public Role(RoleEnum role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}
