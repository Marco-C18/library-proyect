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

    public Usuario registrarUsuario(Usuario usuario) throws Exception {

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new Exception("El correo ya está registrado");
        }

        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new Exception("El teléfono ya está registrado");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String correo, String contrasena) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Correo o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new Exception("Correo o contraseña incorrectos");
        }

        return usuario;
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}