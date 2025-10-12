package com.library.library_proyect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estudiante/dashboard")
public class DashEstudianteController {

    @GetMapping("/{seccion}")
    public String mostrarDashboard(@PathVariable String seccion, Model model) {
        if (!esSeccionValida(seccion)) {
            return "redirect:/estudiante/dashboard/inicio";
        }

        model.addAttribute("seccion", seccion);
        model.addAttribute("activePage", "dashboardEstudiante");
        return "dashboards/dash_estudiante";
    }

    @GetMapping
    public String dashboardDefault() {
        return "redirect:/estudiante/dashboard/inicio";
    }

    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
                || seccion.equals("prestamos-activos")
                || seccion.equals("solicitud-prestamo")
                || seccion.equals("confirmacion-prestamo")
                || seccion.equals("historial-prestamo");
    }
}