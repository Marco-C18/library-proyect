package com.library.library_proyect.controller;

import com.library.library_proyect.model.TipoUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {
    
    /**
     * Ruta general de dashboard - redirige según el rol
     */
    // @GetMapping("/dashboard")
    // public String dashboard(HttpSession session, RedirectAttributes redirectAttributes) {
    //     String tipoUsuario = (String) session.getAttribute("usuarioTipo");
        
    //     if (tipoUsuario == null) {
    //         redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión primero");
    //         return "redirect:/login";
    //     }
        
    //     // Redirigir según el tipo de usuario
    //     if (tipoUsuario.equals(TipoUsuario.ESTUDIANTE.name())) {
    //         return "redirect:/estudiante/dashboard/inicio";
    //     } else if (tipoUsuario.equals(TipoUsuario.BIBLIOTECARIO.name())) {
    //         return "redirect:/bibliotecario/dashboard/inicio";
    //     }
        
    //     return "redirect:/home";
    // }
    
    /**
     * Dashboard de Estudiante
     */
    // @GetMapping("/estudiante/dashboard/inicio")
    // public String dashboardEstudiante(HttpSession session, RedirectAttributes redirectAttributes) {
    //     String tipoUsuario = (String) session.getAttribute("usuarioTipo");
        
    //     // Verificar que esté logueado
    //     if (tipoUsuario == null) {
    //         redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión primero");
    //         return "redirect:/login";
    //     }
        
    //     // Verificar que sea estudiante
    //     if (!tipoUsuario.equals(TipoUsuario.ESTUDIANTE.name())) {
    //         redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
    //         return "redirect:/dashboard";
    //     }
        
    //     return "dashboards/dash_estudiante";
    // }
    
    /**
     * Dashboard de Bibliotecario
     */
    // @GetMapping("/bibliotecario/dashboard/inicio")
    // public String dashboardBibliotecario(HttpSession session, RedirectAttributes redirectAttributes) {
    //     String tipoUsuario = (String) session.getAttribute("usuarioTipo");
        
    //     // Verificar que esté logueado
    //     if (tipoUsuario == null) {
    //         redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión primero");
    //         return "redirect:/login";
    //     }
        
    //     // Verificar que sea bibliotecario
    //     if (!tipoUsuario.equals(TipoUsuario.BIBLIOTECARIO.name())) {
    //         redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
    //         return "redirect:/dashboard";
    //     }

    //     System.out.println("📘 Entrando a DashboardController /inicio");
        
    //     return "dashboards/dash_bibliotecario";
    // }
}