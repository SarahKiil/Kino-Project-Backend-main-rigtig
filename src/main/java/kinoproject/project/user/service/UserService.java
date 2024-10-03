package kinoproject.project.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kinoproject.project.user.dto.UserRequest;
import kinoproject.project.user.dto.UserResponse;
import kinoproject.project.user.entity.User;
import kinoproject.project.user.repository.UserRepository;
import kinoproject.security.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user))
                .collect(Collectors.toList());
    }


    public User getUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this username does not exist"));
    }

    public UserResponse findById(String username) {
        return new UserResponse(getUserByUsername(username));
    }

    public UserResponse addUser(UserRequest body) {
        User newUser = UserRequest.getUserEntity(body);

        if (userRepository.existsById(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already exists");
        }
        newUser.addRole(Role.USER);
        newUser = userRepository.save(newUser);
        return new UserResponse(newUser);
    }

    public void editUser(UserRequest body, String username) {
        User user = getUserByUsername(username);
        if (!body.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change username");
        }
        user.setUsername(body.getUsername());
        user.setPassword(body.getPassword());
        user.setEmail(body.getEmail());
        userRepository.save(user);
    }

    public void deleteById(String username) {
        userRepository.deleteById(username);
    }
}
