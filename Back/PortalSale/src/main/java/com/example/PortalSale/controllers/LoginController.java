package com.example.PortalSale.controllers;

import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // para permitir chamadas do frontend
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioLogado = ur.login(usuario.getRa(), usuario.getSenha());

        if (usuarioLogado != null) {
            return ResponseEntity.ok(Map.of(
                "id", usuarioLogado.getId(),
                "nome", usuarioLogado.getNome(),
                "ra", usuarioLogado.getRa(),
                "role", usuarioLogado.getRole()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("mensagem", "Credenciais inválidas"));
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

            ur.save(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("mensagem", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }
}
