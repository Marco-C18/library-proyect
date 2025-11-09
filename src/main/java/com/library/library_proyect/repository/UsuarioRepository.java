package com.library.library_proyect.repository;

import com.library.library_proyect.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);
    
    // Verificar si existe un correo
    boolean existsByCorreo(String correo);
    
    // Verificar si existe un teléfono
    boolean existsByTelefono(String telefono);
}