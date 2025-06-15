package com.example.PortalSale.repository;

import com.example.PortalSale.models.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Usuario findById(long id);

    @Query(value = "SELECT * FROM usuario WHERE email = :email AND senha = :senha", nativeQuery = true)
    Usuario login(@Param("email") String email, @Param("senha") String senha);
}
