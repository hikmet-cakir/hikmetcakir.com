package com.hikmetcakir.coreapi.config;

import com.hikmetcakir.coreapi.auth.JwtFilter;
import com.hikmetcakir.coreapi.auth.JwtUtil;
import com.hikmetcakir.coreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Value("${frontend.reader.origin}")
    private String frontendReaderOrigin;

    @Value("${frontend.editor.origin}")
    private String frontendEditorOrigin;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, userRepository);

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/article*", "/article/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/article/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/article/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/article/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/category/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/category/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/category/**").authenticated()
                    .anyRequest().denyAll()
            )
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                frontendReaderOrigin,
                frontendEditorOrigin,
                "https://www.hikmetcakir.com",
                "https://hikmetcakir.com"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
