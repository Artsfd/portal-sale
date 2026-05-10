package com.example.PortalSale.repository;

import com.example.PortalSale.models.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByRa(String ra);

    @Query(value = "SELECT * FROM usuario WHERE ra = :ra AND senha = :senha", nativeQuery = true)
    Usuario login(@Param("ra") String ra, @Param("senha") String senha);

}
