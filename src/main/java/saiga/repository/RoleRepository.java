package saiga.repository;

import org.springframework.data.repository.CrudRepository;
import saiga.model.Role;
import saiga.model.enums.RoleEnum;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(RoleEnum user);
}
