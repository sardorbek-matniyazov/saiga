package saiga.repository;

import org.springframework.data.repository.CrudRepository;
import saiga.model.Role;
import saiga.model.enums.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum user);
}
