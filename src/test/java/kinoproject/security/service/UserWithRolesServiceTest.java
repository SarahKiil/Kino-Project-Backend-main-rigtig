package kinoproject.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kinoproject.security.TestUtils;
import kinoproject.security.dto.UserWithRolesRequest;
import kinoproject.security.dto.UserWithRolesResponse;
import kinoproject.security.entity.Role;
import kinoproject.security.entity.UserWithRoles;
import kinoproject.security.repository.UserWithRolesRepository;

import static org.junit.jupiter.api.Assertions.*;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@DataJpaTest
class UserWithRolesServiceTest {

  UserWithRolesService userWithRolesService;

  @Autowired
  UserWithRolesRepository userWithRolesRepository;

  private boolean dataInitialized = false;

  @BeforeEach
  void setUp() {
    userWithRolesService = new UserWithRolesService(userWithRolesRepository);
    if(!dataInitialized) {
      userWithRolesRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository);
      dataInitialized = true;
    }
  }


  @Test
  void getUserWithRoles() {
    UserWithRolesResponse user = userWithRolesService.getUserWithRoles("u1");
    assertEquals(2, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("USER"));
    assertTrue(user.getRoleNames().contains("ADMIN"));
  }

  @Test
  void addRole() {
    UserWithRolesResponse user = userWithRolesService.addRole("u4", Role.USER);
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("USER"));
  }

  @Test
  void removeRole() {
    UserWithRolesResponse user = userWithRolesService.removeRole("u1", Role.USER);
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("ADMIN"));
    user = userWithRolesService.removeRole("u1", Role.ADMIN);
    assertEquals(0, user.getRoleNames().size());
  }

  @Test
  void editUserWithRoles() {
    String originalPassword = userWithRolesRepository.findById("u1").get().getPassword();
    UserWithRolesRequest user1 = new UserWithRolesRequest("u1New", "new_Password", "newMail@a.dk");
    UserWithRolesResponse user = userWithRolesService.editUserWithRoles("u1",user1);
    assertEquals("u1", user.getUserName());  //IMPORTANT: The username should not be changed
    assertEquals("newMail@a.dk", user.getEmail());
    UserWithRoles editedUser = userWithRolesRepository.findById("u1").get();
    assertNotEquals(originalPassword, editedUser.getPassword());
  }

  @Test
  void addUserWithRolesWithNoRole() {
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user, null);
    assertEquals(0, newUser.getRoleNames().size());
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
    //Verify that the password is hashed
    assertEquals(60,userWithRolesRepository.findById("u5").get().getPassword().length());
  }
  @Test
  void addUserWithRolesWithRole() {
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user, Role.USER);
    assertEquals(1, newUser.getRoleNames().size());
    assertTrue(newUser.getRoleNames().contains("USER"));
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
  }

}