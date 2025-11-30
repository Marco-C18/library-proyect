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

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        // Si ya está logueado, redirigir al home
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = usuarioService.autenticarUsuario(correo.trim().toLowerCase(), contrasena);

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

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {

        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada correctamente");
        return "redirect:/login";
    }

    @GetMapping("/")
    public String index(HttpSession session) {

        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}