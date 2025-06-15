package com.example.PortalSale.controllers;

import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // para permitir chamadas do frontend
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioLogado = ur.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioLogado != null) {
            return ResponseEntity.ok(usuarioLogado);
        } else {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos.");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastroUsuario(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> existente = ur.findByEmail(usuario.getEmail());
            if (existente.isPresent()) {
                return ResponseEntity.status(409).body("Usuário já cadastrado com esse e-mail.");
            }

            ur.save(usuario);
            return ResponseEntity.ok("Usuário cadastrado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
}
