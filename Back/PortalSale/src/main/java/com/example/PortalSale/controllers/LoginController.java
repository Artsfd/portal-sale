package com.example.PortalSale.controllers;

import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.UsuarioRepository;
import com.example.PortalSale.security.ApplicationUserDetails;
import com.example.PortalSale.security.JwtTokenUtil;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getRa(), usuario.getSenha())
            );

            ApplicationUserDetails userDetails = (ApplicationUserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "id", userDetails.getId(),
                    "nome", userDetails.getUsuario().getNome(),
                    "ra", userDetails.getUsername(),
                    "role", userDetails.getAuthorities().stream().findFirst().map(Object::toString).orElse("USER")
            ));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("mensagem", "Credenciais inválidas"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(401).body(Map.of("mensagem", "Token inválido ou expirado"));
            }

            if (authentication.getPrincipal() instanceof ApplicationUserDetails) {
                ApplicationUserDetails userDetails = (ApplicationUserDetails) authentication.getPrincipal();
                return ResponseEntity.ok(Map.of(
                        "valido", true,
                        "id", userDetails.getId(),
                        "nome", userDetails.getUsuario().getNome(),
                        "ra", userDetails.getUsername(),
                        "role", userDetails.getAuthorities().stream().findFirst().map(Object::toString).orElse("USER")
                ));
            }

            return ResponseEntity.status(401).body(Map.of("mensagem", "Token inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("mensagem", "Erro ao validar token: " + e.getMessage()));
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastroUsuario(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> existenteEmail = ur.findByEmail(usuario.getEmail());
            if (existenteEmail.isPresent()) {
                return ResponseEntity
                        .status(409)
                        .body(Map.of("mensagem", "Já existe um usuário cadastrado com este e-mail."));
            }

            Optional<Usuario> existenteRa = ur.findByRa(usuario.getRa());
            if (existenteRa.isPresent()) {
                return ResponseEntity
                        .status(409)
                        .body(Map.of("mensagem", "Já existe um usuário cadastrado com este RA."));
            }

            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            ur.save(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("mensagem", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }
}