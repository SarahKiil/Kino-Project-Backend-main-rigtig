package kinoproject.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import kinoproject.security.error.CustomOAuth2AccessDeniedHandler;
import kinoproject.security.error.CustomOAuth2AuthenticationEntryPoint;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

@Configuration
public class SecurityConfig {

  //Remove default value below BEFORE deployment
  @Value("${app.secret-key}")
  private String tokenSecret;

  @Autowired
  CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
    MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
    http
            .cors(Customizer.withDefaults()) //Will use the CorsConfigurationSource bean declared in CorsConfig.java
            .csrf(csrf -> csrf.disable())  //We can disable csrf, since we are using token based authentication, not cookie based
            .httpBasic(Customizer.withDefaults())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer((oauth2ResourceServer) ->
                    oauth2ResourceServer
                            .jwt((jwt) -> jwt.decoder(jwtDecoder())
                                    .jwtAuthenticationConverter(authenticationConverter())
                            )
                            .authenticationEntryPoint(new CustomOAuth2AuthenticationEntryPoint())
                            .accessDeniedHandler(new CustomOAuth2AccessDeniedHandler()));
    //ROFLMFAO
    http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/api/auth/login")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/api/user-with-role")).permitAll() //Clients can create a user for themself
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"/api/users")).permitAll() //Admins can create users

            //This is for demo purposes only, and should be removed for a real system
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/demo/anonymous")).permitAll()

            //Allow index.html and everything else on root level. So make sure to put ALL your endpoints under /api
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"/*")).permitAll()

            .requestMatchers(mvcMatcherBuilder.pattern("/error")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"/api/films/{imdbId}")).hasAuthority("ADMIN")

            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"/api/films/{imdbId}")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"api/films")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "api/theaters")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "api/showings")).permitAll()

            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/theaters")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings/{id}")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings/includeSeats")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings/{id}/includeSeats")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings/{id}/includeMovieDetails")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/showings/findAllByFilmId/{filmId}")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "api/signUp")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "api/users")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "api/reservations")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/reservations/{username}")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "api/reservations/id/{id}")).permitAll()

            //This is for demo purposes only, and should be removed for a real system
            //.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/test/user-only")).hasAuthority("USER")
            //.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/test/admin-only")).hasAuthority("ADMIN")

            //Use this to completely disable security (Will not work if endpoints has been marked with @PreAuthorize)
            //.requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll());
            .anyRequest().authenticated());

    return http.build();
  }
//LOL
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationConverter authenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  public SecretKey secretKey() {
    return new SecretKeySpec(tokenSecret.getBytes(), "HmacSHA256");
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(secretKey()).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(
            new ImmutableSecret<SecurityContext>(secretKey())
    );
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
          throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
