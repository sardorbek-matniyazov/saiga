package saiga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import saiga.model.enums.Lang;
import saiga.model.enums.Status;
import saiga.payload.request.UpdateUserRequest;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

import static saiga.model.enums.Status.ACTIVE;
import static saiga.utils.statics.Constants._ENUM_LENGTH;
import static saiga.utils.statics.Constants._NAME_LENGTH;
import static saiga.utils.statics.Constants._NUMBER_LENGTH;
import static saiga.utils.statics.Constants._TOKEN_LENGTH;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 26 Jan 2023
 **/
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

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Role role;

    @Column(name = "user_status", length = _ENUM_LENGTH)
    @Enumerated(EnumType.STRING)
    private Status status = ACTIVE;

    public User(String firstName, String lastName, String phoneNumber, Role role, String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.currentToken = token;
    }

    // custom setters
    public User setToken(String token) {
        this.currentToken = token;
        return this;
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

    public void updateWithDto(UpdateUserRequest dto) {
        this.firstName = dto.firstName();
        this.lastName = dto.lastName();
        this.lang = dto.lang();
        this.phoneNumber = dto.phoneNumber();
    }
}
