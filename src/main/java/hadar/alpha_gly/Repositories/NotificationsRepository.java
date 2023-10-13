package hadar.alpha_gly.Repositories;

import hadar.alpha_gly.Entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Long> {
}
