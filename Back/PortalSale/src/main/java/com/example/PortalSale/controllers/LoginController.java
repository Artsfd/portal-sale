package com.example.PortalSale.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    // 🔹 LOGIN
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
                "role", usuarioLogado.getRole(),
                "mensagem", "Login realizado com sucesso."
            ));
        } else {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("mensagem", "Credenciais inválidas"));
        }
    }

    // 🔹 LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("mensagem", "Logout realizado com sucesso."));
    }

    // 🔹 CADASTRO DE USUÁRIO
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

            // 🔹 Se não for admin, define o role padrão como USER
            if (usuario.getRole() == null || usuario.getRole().isBlank()) {
                usuario.setRole("USER");
            }

            ur.save(usuario);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("mensagem", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }

    // 🔹 Verifica se usuário logado é admin
    @GetMapping("/verificar-admin")
    public ResponseEntity<?> verificarAdmin(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body(Map.of("mensagem", "Usuário não autenticado."));
        }

        boolean isAdmin = "ADMIN".equalsIgnoreCase(usuarioLogado.getRole());
        return ResponseEntity.ok(Map.of(
            "autenticado", true,
            "isAdmin", isAdmin,
            "usuario", Map.of(
                "id", usuarioLogado.getId(),
                "nome", usuarioLogado.getNome(),
                "ra", usuarioLogado.getRa(),
                "role", usuarioLogado.getRole()
            )
        ));
    }
}
