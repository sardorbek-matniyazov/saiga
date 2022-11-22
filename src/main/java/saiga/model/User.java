package saiga.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import saiga.model.enums.Lang;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_first_name", length = 50)
    private String firstName;

    @Column(name = "user_last_name", length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_lang", length = 10)
    private Lang lang = Lang.KAA;

    @Column(name = "user_phone_number", unique = true, length = 50)
    private String phoneNumber;

    @ManyToOne(optional = false)
    private Role role;

    public User(String phoneNumber, Role byRole) {
        this.role = byRole;
        this.phoneNumber = phoneNumber;
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
        return true;
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
}
