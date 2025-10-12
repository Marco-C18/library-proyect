package com.library.library_proyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bibliotecario/dashboard")
public class DashBibliotecarioController {
    
    /**
     * Maneja todas las secciones del dashboard del bibliotecario
     * @param seccion - nombre de la sección a mostrar
     * @param model - modelo de Spring para pasar datos a la vista
     * @return vista del dashboard
     */
    @GetMapping("/{seccion}")
    public String mostrarDashboard(@PathVariable String seccion, Model model) {
        // Validar que la sección sea válida
        if (!esSeccionValida(seccion)) {
            return "redirect:/bibliotecario/dashboard/inicio";
        }
        
        model.addAttribute("seccion", seccion);
        return "dashboards/dash_bibliotecario";
    }
    
    /**
     * Redirige al dashboard por defecto (inicio)
     * @return redirección a la sección inicio
     */
    @GetMapping
    public String dashboardDefault() {
        return "redirect:/bibliotecario/dashboard/inicio";
    }
    
    /**
     * Valida si la sección solicitada existe
     * @param seccion - nombre de la sección
     * @return true si es válida, false en caso contrario
     */
    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
            || seccion.equals("libros")
            || seccion.equals("categorias")
            || seccion.equals("reportes")
            || seccion.equals("aprobaciones");
    }
}
