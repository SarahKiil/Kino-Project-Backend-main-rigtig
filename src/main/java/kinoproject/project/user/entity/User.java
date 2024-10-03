package kinoproject.project.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kinoproject.security.entity.UserWithRoles;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User extends UserWithRoles {

// Constructors
    public User(String username, String password, String email) {
        super(username, password, email); // Call the constructor of the superclass
    }

}
 