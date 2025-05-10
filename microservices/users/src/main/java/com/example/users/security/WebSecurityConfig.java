package com.example.users.security;

import com.example.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserService userService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/signup",
                                "/api/auth/signin",
                                "/api/auth/logout"
                        ).permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/users/**",
                                "/type/**",
                                "/statistics/**",
                                "/orders/**",
                                "/newsletter/**",
                                "/equipment/**"
                        ).hasRole("admin")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/users/**",
                                "/type/**",
                                "/statistics/**",
                                "/orders/**",
                                "/newsletter/**",
                                "/equipment/**"
                        ).hasRole("admin")

                        // Organizer endpoints
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/type/**",
                                "/orders/**",
                                "/equipment/**"
                        ).hasAnyRole("admin", "organizer")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/type/**",
                                "/orders/**",
                                "/equipment/**"
                        ).hasAnyRole("admin", "organizer")

                        // User endpoints
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/users/**",
                                "/orders/**"
                        ).hasAnyRole("admin", "user")

                        // Role-based access for general endpoints
                        .requestMatchers("/users/**").hasAnyRole("admin", "organizer", "user")
                        .requestMatchers("/type/**").hasAnyRole("admin", "organizer", "user")
                        .requestMatchers("/statistics/**").hasAnyRole("admin", "organizer", "user")
                        .requestMatchers("/orders/**").hasAnyRole("admin", "organizer", "user")
                        .requestMatchers("/newsletter/**").hasAnyRole("admin", "organizer", "user")
                        .requestMatchers("/equipment/**").hasAnyRole("admin", "organizer", "user")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
