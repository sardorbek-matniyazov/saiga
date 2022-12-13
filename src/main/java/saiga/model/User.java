package saiga.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import saiga.model.enums.Lang;
import saiga.model.enums.Status;
import saiga.payload.dto.UpdateUserDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static saiga.model.enums.Status.ACTIVE;
import static saiga.utils.constants.ModelConstants._ENUM_LENGTH;
import static saiga.utils.constants.ModelConstants._NAME_LENGTH;
import static saiga.utils.constants.ModelConstants._NUMBER_LENGTH;
import static saiga.utils.constants.ModelConstants._TOKEN_LENGTH;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User
        extends BaseCreatable
        implements UserDetails
{
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_first_name", length = _NAME_LENGTH)
    private String firstName;

    @Column(name = "user_last_name", length = _NAME_LENGTH)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_lang", length = _ENUM_LENGTH)
    private Lang lang = Lang.KAA;

    @Column(name = "user_phone_number", unique = true, length = _NUMBER_LENGTH, nullable = false)
    private String phoneNumber;

    @Column(name = "user_current_token", unique = true, length = _TOKEN_LENGTH)
    private String currentToken;

    @ManyToOne(optional = false)
    private Role role;

    @Column(name = "user_status", length = _ENUM_LENGTH)
    private Status status = ACTIVE;

    // custom setters
    public User setToken(String token) {
        this.currentToken = token;
        return this;
    }

    public User(String phoneNumber, Role byRole, String currentToken) {
        this.role = byRole;
        this.phoneNumber = phoneNumber;
        this.currentToken = currentToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return "password - is - kaa";
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(ACTIVE);
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> mp = new HashMap<>();
        mp.put("id", id);
        mp.put("firstName", firstName);
        mp.put("lastName", lastName);
        mp.put("lang", lang);
        mp.put("phoneNumber", phoneNumber);
        mp.put("role", role);
        return mp;
    }

    public void updateWithDto(UpdateUserDto dto) {
        this.firstName = dto.getFirstname();
        this.lastName = dto.getLastname();
        this.lang = dto.getLang();
        this.phoneNumber = dto.getPhoneNumber();
    }
}
