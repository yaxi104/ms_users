package com.hexagonal.ms_user.infrastructure.configuration;

import com.hexagonal.ms_user.infrastructure.security.CustomAccessDenied;
import com.hexagonal.ms_user.infrastructure.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.hexagonal.ms_user.domain.utils.Constanst.ROLE_ADMIN;
import static com.hexagonal.ms_user.domain.utils.Constanst.ROLE_PROPIETARIO;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDenied accessDeniedHandler;

    public SecurityConfig(@Lazy JwtFilter jwtFilter, CustomAccessDenied accessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/swagger-ui/index.html"
                        ).permitAll()
                        .requestMatchers("/api/v1/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/owner").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/user**")
                        .hasAnyRole(ROLE_ADMIN, "PROPIETARIO", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/v1/role/foodcourt").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/role/foodcourt**")
                        .hasAnyRole(ROLE_ADMIN, "PROPIETARIO", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/v1/employee").hasRole(ROLE_PROPIETARIO)
                        .requestMatchers("/api/v1/customer").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}