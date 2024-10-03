package kinoproject.security;

import kinoproject.security.entity.Role;
import kinoproject.security.entity.UserWithRoles;
import kinoproject.security.repository.UserWithRolesRepository;

public class TestUtils {

    public static String h2UrlName = "JDBC_DATABASE_URL";
    public static String h2UrlValue = "jdbc:h2:mem:test";
    public static String h2UsernameName = "JDBC_DATABASE_USERNAME";
    public static String h2UsernameValue = "JDBC_DATABASE_USERNAME";
    public static String h2PassName = "JDBC_DATABASE_DRIVER";
    public static String h2PassValue = "test";
    public static String tokenSecretName = "TOKEN_SECRET";
    public static String tokenSecretValue = "s/4KMb61LOrMYYAn4rfaQYSgr+le5SMrsMzKw8G6bXx=";
    public static String omdbKeyName = "OMDB_KEY";
    public static String omdbKeyValue = "6dfe795d";


    public static void setupTestUsers(UserWithRolesRepository userWithRolesRepository) {
        userWithRolesRepository.deleteAll();
        String passwordUsedByAll = "secret";
        UserWithRoles user1 = new UserWithRoles("u1", passwordUsedByAll, "u1@a.dk");
        UserWithRoles user2 = new UserWithRoles("u2", passwordUsedByAll, "u2@a.dk");
        UserWithRoles user3 = new UserWithRoles("u3", passwordUsedByAll, "u3@a.dk");
        UserWithRoles userNoRoles = new UserWithRoles("u4", passwordUsedByAll, "u4@a.dk");
        user1.addRole(Role.USER);
        user1.addRole(Role.ADMIN);
        user2.addRole(Role.USER);
        user3.addRole(Role.ADMIN);
        userWithRolesRepository.save(user1);
        userWithRolesRepository.save(user2);
        userWithRolesRepository.save(user3);
        userWithRolesRepository.save(userNoRoles);
    }
}
