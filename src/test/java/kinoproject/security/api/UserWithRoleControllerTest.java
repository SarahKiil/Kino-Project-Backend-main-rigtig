package kinoproject.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import kinoproject.security.TestUtils;
import kinoproject.security.dto.LoginRequest;
import kinoproject.security.dto.LoginResponse;
import kinoproject.security.dto.UserWithRolesRequest;
import kinoproject.security.repository.UserWithRolesRepository;
import kinoproject.security.service.UserWithRolesService;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Import(PasswordEncoderConfig.class)
@Transactional
class UserWithRoleControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  @Autowired
  UserWithRolesService userWithRolesService;
  @Autowired
  PasswordEncoder passwordEncoder;

  String adminToken;
  String userToken;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private boolean dataInitialized = false;

  @BeforeAll
  public static void iniSetUp(){
    System.setProperty(TestUtils.h2UrlName, TestUtils.h2UrlValue);
    System.setProperty(TestUtils.h2UsernameName, TestUtils.h2UsernameValue);
    System.setProperty(TestUtils.h2PassName, TestUtils.h2PassValue);
    System.setProperty(TestUtils.tokenSecretName, TestUtils.tokenSecretValue);
    System.setProperty(TestUtils.omdbKeyName, TestUtils.omdbKeyValue);
  }

  @AfterAll
  public static void tearDown(){
    System.clearProperty(TestUtils.h2UrlName);
    System.clearProperty(TestUtils.h2UsernameName);
    System.clearProperty(TestUtils.h2PassName);
    System.clearProperty(TestUtils.tokenSecretName);
    System.clearProperty(TestUtils.omdbKeyName);
  }

  @BeforeEach
  void setUp() throws Exception {
    userWithRolesService = new UserWithRolesService(userWithRolesRepository);
    if (!dataInitialized) {
      userWithRolesRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository);
      userToken = loginAndGetToken("u2", "secret");
      adminToken = loginAndGetToken("u3", "secret");
      dataInitialized = true;
    }
  }

  String loginAndGetToken(String user, String pw) throws Exception {
    LoginRequest loginRequest = new LoginRequest(user, pw);
    MvcResult response = mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    LoginResponse loginResponse = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class);
    return loginResponse.getToken();
  }

  @Test
  void addUsersWithRolesNoRoles() throws Exception {
    UserWithRolesRequest newUserReq = new UserWithRolesRequest("u100", "secret", "u100@a.dk");
    UserWithRoleController.DEFAULT_ROLE_TO_ASSIGN = null;
    mockMvc.perform(post("/api/user-with-role")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newUserReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.roleNames").isEmpty());
  }

  @Test
  void addUsersWithRoles() throws Exception {
    UserWithRolesRequest newUserReq = new UserWithRolesRequest("u100", "secret", "u100@a.dk");
    //UserWithRoleController.DEFAULT_ROLE_TO_ASSIGN = null;
    mockMvc.perform(post("/api/user-with-role")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newUserReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("USER")));
  }

  @Test
  void addRoleFAilsWhenNotAuthenticatedWithRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u4/admin")
                    .accept("application/json"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void addRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u4/admin")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u4"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("ADMIN")));
  }

  @Test
  void addRoleFailsWithWrongRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u2/admin")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                    .accept("application/json"))
            .andExpect(status().isForbidden());
  }

  @Test
  void removeRoleFailsWhenNotAuthenticatedWithRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u2/user")
                    .accept("application/json"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void removeRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u1/user")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u1"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("ADMIN")));
  }
}