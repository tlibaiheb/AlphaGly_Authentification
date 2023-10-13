package hadar.alpha_gly.Repositories;

import hadar.alpha_gly.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmailEquals(String email);
    List<User> findByFirstNameOrLastNameContainingIgnoreCase(String firstName, String lastName);
    Optional<User> findByEmail(String username);
    Boolean existsByEmail(String email);

    List<User> findByEnabledTrue();
}
