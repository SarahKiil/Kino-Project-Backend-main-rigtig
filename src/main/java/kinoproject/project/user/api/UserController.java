package kinoproject.project.user.api;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import kinoproject.project.user.dto.UserRequest;
import kinoproject.project.user.dto.UserResponse;
import kinoproject.project.user.service.UserService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/users")
class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //security --> Admin
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    List<UserResponse> getUsers() {
        return userService.getUsers();
    }


    //Admin
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{username}")
    UserResponse getUserById(@PathVariable String username) throws Exception {
        return userService.findById(username);
    }

    //Security --> Anonymous
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse addUser(@RequestBody UserRequest body) {
        return userService.addUser(body);
    }

    //Security --> Admin
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{username}")
    void editUser(@RequestBody UserRequest body, @PathVariable String username) {
        userService.editUser(body, username);
    }

    // Security --> ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{username}")
    void deleteUserByUsername(@PathVariable String username) {
        userService.deleteById(username);
    }
}