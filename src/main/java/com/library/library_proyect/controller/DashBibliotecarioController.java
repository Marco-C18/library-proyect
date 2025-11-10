package com.library.library_proyect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.library_proyect.model.Libros;
import com.library.library_proyect.services.CatalogoService;

@Controller
@RequestMapping("/bibliotecario/dashboard")
public class DashBibliotecarioController {

    @Autowired
    private CatalogoService catalogoService;

    // SOLUCIÓN ROBUSTA: Garantiza que 'libro' siempre esté en el Model
    @ModelAttribute("libro")
    public Libros setupLibroForm() {
        return new Libros();
    }

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

    // --- El método GET /libros/agregar y su lógica se eliminó ---

    @PostMapping("/libros/guardar")
    public String guardarNuevoLibro(@ModelAttribute("libro") Libros libro) {
        try {
            catalogoService.guardarLibro(libro);
            return "redirect:/bibliotecario/dashboard/libros?success=true";
        } catch (Exception e) {
            System.err.println("Error al guardar el libro: " + e.getMessage());
            return "redirect:/bibliotecario/dashboard/libros?error=true";
        }
    }

    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
            || seccion.equals("libros")
            || seccion.equals("categorias")
            || seccion.equals("reportes")
            || seccion.equals("aprobaciones");
    }
}
