package saiga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import saiga.security.AdminAuthenticationProvider;
import saiga.security.MyFilter;

import java.util.logging.Logger;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    private final Logger logger = Logger.getLogger(SecurityConf.class.getName());

    private final MyFilter myFilter;

    @Autowired
    public SecurityConf(MyFilter myFilter) {
        this.myFilter = myFilter;
    }

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable().cors().disable()
                .requestMatcher(request -> request.getRequestURI().startsWith("/api"))
                .authorizeRequests(
                        authorityConfig -> {
                            authorityConfig.antMatchers("/api/auth/sign-in", "/api/auth/sign-up").permitAll();
                            authorityConfig.antMatchers("/ws/**", "/").permitAll();

                            // driver privileges
                            authorityConfig.antMatchers(
                                    "/api/driver/**", "api/orders/driver-order",
                                    "api/orders/receive/*", "api/orders/history").hasRole("DRIVER");

                            // user privileges
                            authorityConfig.antMatchers("api/orders/user-order").hasRole("USER");

                            // admin privileges
                            authorityConfig.antMatchers("/api/admin/**").hasRole("ADMIN");

                            authorityConfig.anyRequest().authenticated();
                        }
                ).addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().build();
    }

    @Bean
    public SecurityFilterChain securityFilterChainForAdmin(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(
                        authorityConfig -> {
                            authorityConfig.antMatchers("/ws/**", "/").permitAll();
                            authorityConfig.antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").hasRole("ADMIN");
                            authorityConfig.antMatchers(
                                    "/socket.io.js.map",
                                    "jquery-3.3.1.min.js",
                                    "moment-2.24.0.min.js").permitAll();

                            authorityConfig.anyRequest().authenticated();
                        }
                )
                .formLogin(withDefaults())
                .authenticationProvider(new AdminAuthenticationProvider())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessListener() {
        return event -> logger.info(
                String.format("User { %s }, [ %s ] logged in",
                        event.getAuthentication().getName(),
                        event.getAuthentication().getAuthorities()
                )
        );
    }
}
