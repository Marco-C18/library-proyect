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

    @GetMapping("/registro")
    public String mostrarRegistro(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/home";
        }

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "register";
    }

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
            if (!contrasena.equals(confirmarContrasena)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/registro";
            }

            if (contrasena.length() < 8) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/registro";
            }

            TipoUsuario tipo;
            try {
                tipo = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", "Tipo de usuario inválido");
                return "redirect:/registro";
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setApellido(apellido.trim());
            nuevoUsuario.setCorreo(correo.trim().toLowerCase());
            nuevoUsuario.setTelefono(telefono.trim());
            nuevoUsuario.setContrasena(contrasena);
            nuevoUsuario.setTipoUsuario(tipo);

            Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);

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