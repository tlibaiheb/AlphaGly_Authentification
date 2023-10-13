package hadar.alpha_gly.Repositories;

import hadar.alpha_gly.Entities.Role;
import hadar.alpha_gly.Entities.RolesTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RolesTypes name);
}
