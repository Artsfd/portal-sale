//Controla a API, definindo as rotas HTTP e manipulando as requisições
package com.example.PortalSale.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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
import com.example.PortalSale.services.EventoService;

@CrossOrigin(origins = "*") //Permite a requisição de qualquer origem
@RestController //Indica que a classe é um controlador REST, lidando com as requisições e retornando em JSON
@RequestMapping("/eventos") //Defifindo o prefixo das URLs
public class EventoController {
    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping //Retorna uma lista de todos os eventos
    public List<Evento> listarEventos() {
        return eventoService.listarEventos();
    }

    @GetMapping("/{id}") //Busca o evento pelo ID
    public ResponseEntity<Evento> buscarEventoPorId(@PathVariable long id) {
        Optional<Evento> evento = eventoService.buscarEventoPorId(id); //O Optional evita erros caso o evento não exista
        return evento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping //Criação do evento
    public Evento criarEvento(@RequestBody Evento evento) {
        return eventoService.salvarEvento(evento);
    }

    @DeleteMapping("/{id}") //Deleta o evento pelo ID
    public ResponseEntity<Void> excluirEvento(@PathVariable Long id){
        eventoService.excluirEvento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Evento> buscarEventos(@RequestParam String nome){
        return eventoService.buscarPorNome(nome);
    }

}