package com.example.PortalSale.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PortalSale.models.Evento;
import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.services.EventoService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" }, allowCredentials = "true")
@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public List<Evento> listarEventos() {
        return eventoService.listarEventos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarEventoPorId(@PathVariable long id) {
        Optional<Evento> evento = eventoService.buscarEventoPorId(id);
        return evento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Evento criarEvento(@RequestBody Evento evento) {
        return eventoService.salvarEvento(evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEvento(@PathVariable Long id) {
        eventoService.excluirEvento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Evento> buscarEventos(@RequestParam String nome) {
        return eventoService.buscarPorNome(nome);
    }

    // ✅ Endpoint para ADMIN (se quiser validar role futuramente)
    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuario.getRole())) {
            return "redirect:/home"; // redireciona se não for admin
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("eventos", eventoService.listarEventos());
        return "admin"; // renderiza o arquivo admin.html
    }

    // ✅ Endpoint para buscar evento + inscritos (corrigido)
    @GetMapping("/{id}/inscritos")
    public ResponseEntity<Map<String, Object>> getEventoComInscritos(@PathVariable Long id) {
        Optional<Evento> eventoOpt = eventoService.buscarEventoPorId(id);
        if (eventoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Evento evento = eventoOpt.get();
        List<Usuario> inscritos = evento.getInscritos(); // precisa existir getInscritos() no model Evento

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", evento.getId());
        resposta.put("nome", evento.getNome());
        resposta.put("dataHora", evento.getDataHora());
        resposta.put("descricao", evento.getDescricao());
        resposta.put("inscritos", inscritos);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/{id}/inscrever")
    public ResponseEntity<?> inscreverUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Evento> eventoOpt = eventoService.buscarEventoPorId(id);
        if (eventoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Evento evento = eventoOpt.get();

        boolean jaInscrito = evento.getInscritos().stream()
                .anyMatch(u -> u.getId() == usuario.getId());

        if (jaInscrito) {
            return ResponseEntity.badRequest().body("Usuário já está inscrito neste evento.");
        }

        evento.getInscritos().add(usuario);
        eventoService.salvarEvento(evento);

        return ResponseEntity.ok("Inscrição realizada com sucesso no evento: " + evento.getNome());
    }
}
