package com.example.PortalSale.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt para codificar as senhas. É o padrão e muito seguro.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF, pois não usamos sessões/cookies
            .csrf(csrf -> csrf.disable())
            // Define a política de sessão como STATELESS (sem estado), essencial para APIs REST com JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Libera os endpoints de autenticação para qualquer um
                .requestMatchers("/auth/**").permitAll()
                // Permite que qualquer um veja os eventos
                .requestMatchers(HttpMethod.GET, "/eventos/**").permitAll()
                // Apenas usuários com a role 'ADMIN' podem criar, deletar ou editar eventos
                .requestMatchers(HttpMethod.POST, "/eventos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/eventos/**").hasRole("ADMIN")
                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            );
        
        // AQUI VAI ENTRAR O FILTRO JWT (veremos nos próximos passos)

        return http.build();
    }
}