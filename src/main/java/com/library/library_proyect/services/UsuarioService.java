package com.library.library_proyect.services;

import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Registrar un nuevo usuario
     */
    public Usuario registrarUsuario(Usuario usuario) throws Exception {
        // Validar que el correo no exista
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new Exception("El correo ya está registrado");
        }
        
        // Validar que el teléfono no exista
        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new Exception("El teléfono ya está registrado");
        }
        
        // TODO: En producción, hashear la contraseña con BCrypt
        // String hashedPassword = new BCryptPasswordEncoder().encode(usuario.getContrasena());
        // usuario.setContrasena(hashedPassword);
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Autenticar usuario (Login)
     */
    public Usuario autenticarUsuario(String correo, String contrasena) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        
        if (usuarioOpt.isEmpty()) {
            throw new Exception("Correo o contraseña incorrectos");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // TODO: En producción, comparar contraseñas hasheadas
        // if (!new BCryptPasswordEncoder().matches(contrasena, usuario.getContrasena())) {
        //     throw new Exception("Correo o contraseña incorrectos");
        // }
        
        // Por ahora comparación simple (NO SEGURO para producción)
        if (!usuario.getContrasena().equals(contrasena)) {
            throw new Exception("Correo o contraseña incorrectos");
        }
        
        return usuario;
    }
    
    /**
     * Buscar usuario por ID
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    /**
     * Buscar usuario por correo
     */
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}