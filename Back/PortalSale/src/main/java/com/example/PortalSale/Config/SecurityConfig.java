package com.example.PortalSale.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ❌ Desativa CSRF para testes com front-end
            .cors(cors -> cors.configure(http)) // ✅ Usa a config global de CORS
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 Libera todos os endpoints
            );

        return http.build();
    }
}
