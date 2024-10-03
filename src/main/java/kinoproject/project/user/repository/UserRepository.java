package kinoproject.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kinoproject.project.user.entity.User;


public interface UserRepository extends JpaRepository<User, String> {

}
