package saiga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import saiga.security.MyFilter;

@Configuration
@EnableWebSecurity
public class SecurityConf {
    private final MyFilter myFilter;

    @Autowired
    public SecurityConf(MyFilter myFilter) {
        this.myFilter = myFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable().cors().disable()
                .authorizeRequests(
                        authorityConfig -> {
                            authorityConfig.antMatchers("/api/auth/**").permitAll();
                            authorityConfig.antMatchers(
                                    "/api/auth/sign-in",
                                    "/api/auth/sign-up",
                                    "/ws/**", "/").permitAll();
                            authorityConfig.antMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html").permitAll();
                            authorityConfig.antMatchers(
                                    "/socket.io.js.map",
                                    "jquery-3.3.1.min.js",
                                    "moment-2.24.0.min.js").permitAll();

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
