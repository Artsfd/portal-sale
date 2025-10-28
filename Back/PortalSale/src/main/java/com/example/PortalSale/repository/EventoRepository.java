package com.example.PortalSale.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.PortalSale.models.Evento;

//Faz as operações com o banco. A extenção garante automático a métodos de manipulação de dados
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT e FROM Evento e LEFT JOIN FETCH e.inscritos WHERE e.id = :id")
    Optional<Evento> findByIdWithInscritos(@Param("id") Long id);

}
