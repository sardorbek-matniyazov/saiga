package saiga.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import saiga.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByPhoneNumber(String username);

    @Query(
            value = "SELECT * FROM users u " +
                    "where UPPER(u.user_first_name) like UPPER(CONCAT('%', ?1, '%')) " +
                    "or UPPER(u.user_last_name) like UPPER(CONCAT('%', ?1, '%')) " +
                    "or UPPER(u.user_phone_number) like UPPER(CONCAT('%', ?1, '%')) ",
            nativeQuery = true
    )
    List<User> findAllByFirstNameLike(String query);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdIsNot(String phone, Long id);
}
