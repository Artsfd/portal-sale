package com.example.PortalSale.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.PortalSale.models.Evento;
import com.example.PortalSale.services.EventoService;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
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

    /** 🔹 Retorna evento + lista de inscritos */
    @GetMapping("/{id}/inscritos")
    public ResponseEntity<Map<String, Object>> getEventoComInscritos(@PathVariable Long id) {
        Evento evento = eventoService.buscarEventoComInscritos(id);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", evento.getId());
        resposta.put("nome", evento.getNome());
        resposta.put("descricao", evento.getDescricao());
        resposta.put("dataHora", evento.getDataHora());
        resposta.put("local", evento.getLocal());
        resposta.put("tipoEvento", evento.getTipoEvento());
        resposta.put("palestrante", evento.getPalestrante());
        resposta.put("inscritos", evento.getInscritos());

        return ResponseEntity.ok(resposta);
    }

    /** 🔹 Inscreve um usuário no evento */
    @PostMapping("/{eventoId}/inscrever/{usuarioId}")
    public ResponseEntity<Map<String, Object>> inscreverUsuario(
            @PathVariable Long eventoId,
            @PathVariable Long usuarioId) {
        try {
            eventoService.inscreverUsuario(eventoId, usuarioId);
            Evento eventoAtualizado = eventoService.buscarEventoComInscritos(eventoId);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("id", eventoAtualizado.getId());
            resposta.put("nome", eventoAtualizado.getNome());
            resposta.put("descricao", eventoAtualizado.getDescricao());
            resposta.put("dataHora", eventoAtualizado.getDataHora());
            resposta.put("local", eventoAtualizado.getLocal());
            resposta.put("tipoEvento", eventoAtualizado.getTipoEvento());
            resposta.put("palestrante", eventoAtualizado.getPalestrante());
            resposta.put("inscritos", eventoAtualizado.getInscritos());

            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
