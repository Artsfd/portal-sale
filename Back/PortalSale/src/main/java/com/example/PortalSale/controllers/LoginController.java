package com.example.PortalSale.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
    origins = {"http://localhost:5500", "http://127.0.0.1:5500"},
    allowCredentials = "true"
)
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario, HttpSession session) {
        Usuario usuarioLogado = ur.login(usuario.getRa(), usuario.getSenha());

        if (usuarioLogado != null) {
            // ✅ Guarda o usuário logado na sessão
            session.setAttribute("usuarioLogado", usuarioLogado);

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
