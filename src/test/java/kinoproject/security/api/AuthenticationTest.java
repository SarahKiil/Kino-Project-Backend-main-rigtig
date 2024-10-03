package kinoproject.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import kinoproject.security.TestUtils;
import kinoproject.security.dto.LoginRequest;
import kinoproject.security.repository.UserWithRolesRepository;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  //Ensures that we use the in-memory database
@Transactional
public class AuthenticationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;


  private final ObjectMapper objectMapper = new ObjectMapper();
  public boolean isDataInitialized = false;



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
    if(!isDataInitialized) {
      isDataInitialized = true;
      TestUtils.setupTestUsers(userWithRolesRepository);
    }
  }

  @Test
  void login() throws Exception {
    LoginRequest loginRequest = new LoginRequest("u1", "secret");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("u1"))
            .andExpect(jsonPath("$.roles", hasSize(2)))
            .andExpect(jsonPath("$.roles", containsInAnyOrder("USER","ADMIN")))
            .andExpect(result -> {
              //Not a bulletproof test, but acceptable. First part should always be the same. A token must always contain two dots.
              String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
              assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));
              assertTrue(token.chars().filter(ch -> ch == '.').count() == 2);
            });
  }

  @Test
  void loginWithWrongPassword() throws Exception {
    LoginRequest loginRequest = new LoginRequest("u1", "wrong");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());

  }
  @Test
  void loginWithWrongUsername() throws Exception {
    LoginRequest loginRequest = new LoginRequest("u111111", "wrong");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
  }
}