package com.example.PortalSale.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.PortalSale.models.Evento;
import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.EventoRepository;
import com.example.PortalSale.repository.UsuarioRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository; // necessário para buscar usuário

    public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> buscarEventoPorId(long id) {
        return eventoRepository.findById(id);
    }

    public Evento salvarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void excluirEvento(long id) {
        eventoRepository.deleteById(id);
    }

    public List<Evento> buscarPorNome(String nome) {
        return eventoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Usuario> buscarInscritosPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        return evento.getInscritos();
    }

    public void inscreverUsuario(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findByIdWithInscritos(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica duplicidade usando ID
        boolean jaInscrito = evento.getInscritos().stream()
                .anyMatch(u -> u.getId() == usuario.getId());

        if (jaInscrito) {
            throw new RuntimeException("Usuário já inscrito neste evento");
        }

        // Verifica vagas
        if (evento.getInscritos().size() >= evento.getVagas()) {
            throw new RuntimeException("Não há vagas disponíveis para este evento");
        }

        // Adiciona usuário à lista de inscritos
        evento.getInscritos().add(usuario);

        // Salva no banco (inscrição persistida)
        eventoRepository.save(evento);
    }

    public Evento buscarEventoComInscritos(Long id) {
        return eventoRepository.findByIdWithInscritos(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

}
