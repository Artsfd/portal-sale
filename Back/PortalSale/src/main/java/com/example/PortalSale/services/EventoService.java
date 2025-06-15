package com.example.PortalSale.services;

import com.example.PortalSale.models.Evento;
import com.example.PortalSale.repository.EventoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service //Indica que esse arquivo contém as regras de negócio
public class EventoService {
    private final EventoRepository eventoRepository; //O "final" indica que a variável não pode ser alterada após sua inicialização

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll(); //Busca todos os registros do banco
    }

    public Optional<Evento> buscarEventoPorId(long id) {
        return eventoRepository.findById(id); //Busca por um ID passado pelo parâmetro
    }

    public Evento salvarEvento (Evento evento) {
        return eventoRepository.save(evento); //Salva o registro no banco
    }

    public void excluirEvento(long id) {
        eventoRepository.deleteById(id); //Deleta o ID
    }

    public List<Evento> buscarPorNome(String nome) {
        return eventoRepository.findByNomeContainingIgnoreCase(nome);
    }
}
