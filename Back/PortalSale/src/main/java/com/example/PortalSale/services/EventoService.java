package com.example.PortalSale.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PortalSale.models.Evento;
import com.example.PortalSale.models.Usuario;
import com.example.PortalSale.repository.EventoRepository;
import com.example.PortalSale.repository.UsuarioRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

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

    /**
     * 🔹 Retorna a lista de usuários inscritos em um evento
     */
    public List<Usuario> buscarInscritosPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findByIdWithInscritos(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        return evento.getInscritos();
    }

    /**
     * 🔹 Inscreve o usuário no evento, validando duplicidade
     */
    @Transactional
    public void inscreverUsuario(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findByIdWithInscritos(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean jaInscrito = evento.getInscritos().stream()
                .anyMatch(u -> u.getId().equals(usuario.getId()));

        if (jaInscrito) {
            throw new RuntimeException("Usuário já inscrito neste evento");
        }

        // ✅ Adiciona o usuário ao evento
        evento.getInscritos().add(usuario);
        eventoRepository.saveAndFlush(evento);
    }

    /**
     * 🔹 Busca evento com todos os inscritos carregados
     */
    public Evento buscarEventoComInscritos(Long id) {
        return eventoRepository.findByIdWithInscritos(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    // EventoService.java
    @Transactional
    public Evento atualizarEvento(Long id, Evento eventoAtualizado) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        evento.setNome(eventoAtualizado.getNome());
        evento.setDescricao(eventoAtualizado.getDescricao());
        evento.setDataHora(eventoAtualizado.getDataHora());
        evento.setLocal(eventoAtualizado.getLocal());
        evento.setTipoEvento(eventoAtualizado.getTipoEvento());
        evento.setPalestrante(eventoAtualizado.getPalestrante());
        evento.setVagas(eventoAtualizado.getVagas());

        return eventoRepository.save(evento);
    }

    /**
     * 🔹 Remove a inscrição de um usuário em um evento
     */
    @Transactional
    public void removerInscricao(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findByIdWithInscritos(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        boolean removido = evento.getInscritos().removeIf(u -> u.getId().equals(usuarioId));

        if (!removido) {
            throw new RuntimeException("Usuário não estava inscrito neste evento");
        }

        eventoRepository.save(evento);
    }

}
