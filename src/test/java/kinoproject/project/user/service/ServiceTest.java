package kinoproject.project.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kinoproject.project.user.dto.UserRequest;
import kinoproject.project.user.dto.UserResponse;
import kinoproject.project.user.entity.User;
import kinoproject.project.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testGetUsers() {
        User user1 = new User("user1", "password1", "user1@gmail.com");
        User user2 = new User("user2", "password2", "user2@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        List<UserResponse> userResponses = userService.getUsers();
        assertEquals(2, userResponses.size());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User("testUser", "testPassword", "test@gmail.com");
        userRepository.save(user);

        User newUser = userService.getUserByUsername("testUser");
        assertNotNull(newUser);
        assertEquals("testUser", newUser.getUsername());
    }

    @Test
    public void testAddUser() {
        User user = new User("newUser", "newPassword", "newuser@gmail.com");
        UserResponse userResponse = userService.addUser(new UserRequest(user));

        assertNotNull(userResponse);
        assertEquals("newUser", userResponse.getUsername());

        User newUser = userRepository.findById("newUser").orElse(null);
        assertNotNull(newUser);
        assertEquals("newUser", newUser.getUsername());
    }

    @Test
    public void testEditUser() {
        User user = new User("editUser", "password", "edituser@gmail.com");
        UserRequest newUserRequest = new UserRequest(user);
        userService.addUser(newUserRequest);

        User editUser = new User("editUser", "editPassword", "newedituser@gmail");
        UserRequest updatedUserRequest = new UserRequest(editUser);
        userService.editUser(updatedUserRequest, "editUser");

        User updatedUser = userRepository.findById("editUser").orElse(null);
        assertNotNull(updatedUser);
        assertEquals("newedituser@gmail", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("deleteUser", "password", "deleteuser@gmail.com");
        UserRequest userRequest = new UserRequest(user);
        userService.addUser(userRequest);

        userService.deleteById("deleteUser");

        User deletedUser = userRepository.findById("deleteUser").orElse(null);
        assertNull(deletedUser);
    }

}
