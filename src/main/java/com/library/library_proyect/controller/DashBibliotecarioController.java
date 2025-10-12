package com.library.library_proyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bibliotecario/dashboard")
public class DashBibliotecarioController {
  
    @GetMapping("/{seccion}")
    public String mostrarDashboard(@PathVariable String seccion, Model model) {
        if (!esSeccionValida(seccion)) {
            return "redirect:/bibliotecario/dashboard/inicio";
        }
        
        model.addAttribute("seccion", seccion);
        return "dashboards/dash_bibliotecario";
    }
    
    @GetMapping
    public String dashboardDefault() {
        return "redirect:/bibliotecario/dashboard/inicio";
    }
    
    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
            || seccion.equals("libros")
            || seccion.equals("categorias")
            || seccion.equals("reportes")
            || seccion.equals("aprobaciones");
    }
}
