package com.library.library_proyect.controller;

import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Mostrar formulario de login
     */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        // Si ya está logueado, redirigir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        return "login";
    }
    
    /**
     * Procesar login
     */
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Autenticar usuario
            Usuario usuario = usuarioService.autenticarUsuario(correo.trim().toLowerCase(), contrasena);
            
            // Guardar información en la sesión
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNombre", usuario.getNombreCompleto());
            session.setAttribute("usuarioTipo", usuario.getTipoUsuario().name());
            
            redirectAttributes.addFlashAttribute("success", "¡Bienvenido " + usuario.getNombre() + "!");
            return "redirect:/home";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }
    
    /**
     * Cerrar sesión
     */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada correctamente");
        return "redirect:/login";
    }
    
    /**
     * Redirigir raíz al login
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        // Si está logueado, ir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        // Si no está logueado, ir al login
        return "redirect:/login";
    }
}