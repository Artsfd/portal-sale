package com.example.PortalSale.repository;

import com.example.PortalSale.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Faz as operações com o banco. A extenção garante automático a métodos de manipulação de dados
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByNomeContainingIgnoreCase(String nome);

}
