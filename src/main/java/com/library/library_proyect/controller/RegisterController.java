package com.library.library_proyect.controller;

import com.library.library_proyect.model.TipoUsuario;
import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Mostrar formulario de registro
     */
    @GetMapping("/registro")
    public String mostrarRegistro(HttpSession session, Model model) {
        // Si ya está logueado, redirigir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "register";
    }
    
    /**
     * Procesar registro de usuario
     */
    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("confirmarContrasena") String confirmarContrasena,
            @RequestParam("tipoUsuario") String tipoUsuario,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        
        try {
            // Validación: contraseñas coinciden
            if (!contrasena.equals(confirmarContrasena)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/registro";
            }
            
            // Validación: contraseña mínima
            if (contrasena.length() < 8) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/registro";
            }
            
            // Validación: tipo de usuario válido
            TipoUsuario tipo;
            try {
                tipo = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", "Tipo de usuario inválido");
                return "redirect:/registro";
            }
            
            // Crear usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setApellido(apellido.trim());
            nuevoUsuario.setCorreo(correo.trim().toLowerCase());
            nuevoUsuario.setTelefono(telefono.trim());
            nuevoUsuario.setContrasena(contrasena);
            nuevoUsuario.setTipoUsuario(tipo);
            
            // Registrar usuario
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);
            
            // Iniciar sesión automáticamente
            session.setAttribute("usuarioLogueado", usuarioRegistrado);
            session.setAttribute("usuarioId", usuarioRegistrado.getId());
            session.setAttribute("usuarioNombre", usuarioRegistrado.getNombreCompleto());
            session.setAttribute("usuarioTipo", usuarioRegistrado.getTipoUsuario().name());
            
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. ¡Bienvenido!");
            return "redirect:/home";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
}