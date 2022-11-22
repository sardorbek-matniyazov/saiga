package saiga.repository;

import org.springframework.data.repository.CrudRepository;
import saiga.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByPhoneNumber(String username);

    List<User> findAllByFirstName(String name);
}
