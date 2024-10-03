package kinoproject.project.user.dto;

import lombok.*;
import kinoproject.project.user.entity.User;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    String username;
    String email;
    String password;

    public static User getUserEntity(UserRequest m) {
        return new User(m.username, m.getPassword(), m.getEmail());
    }

    // User to UserRequest conversion
    public UserRequest(User m) {
        this.username   = m.getUsername();
        this.password   = m.getPassword();
        this.email      = m.getEmail();
    }
}
