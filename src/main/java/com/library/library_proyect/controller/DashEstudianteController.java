package com.library.library_proyect.controller;

import com.library.library_proyect.model.Prestamo;
import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.services.PrestamoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/estudiante/dashboard")
public class DashEstudianteController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/{seccion}")
    public String mostrarDashboard(@PathVariable String seccion, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!esSeccionValida(seccion)) {
            return "redirect:/estudiante/dashboard/inicio";
        }

        model.addAttribute("seccion", seccion);
        model.addAttribute("activePage", "dashboardEstudiante");

        switch (seccion) {
            case "inicio":
                cargarDatosInicio(model, usuario);
                break;
            case "solicitud-prestamo":
                cargarSolicitudes(model, usuario);
                break;
            case "confirmacion-prestamo":
                cargarConfirmaciones(model, usuario);
                break;
            case "prestamos-activos":
                cargarPrestamosActivos(model, usuario);
                break;
            case "historial-prestamo":
                cargarHistorial(model, usuario);
                break;
        }

        return "dashboards/dash_estudiante";
    }

    @GetMapping
    public String dashboardDefault() {
        return "redirect:/estudiante/dashboard/inicio";
    }

    private void cargarDatosInicio(Model model, Usuario usuario) {
        int prestamosActivos = prestamoService.obtenerPrestamosActivos(usuario).size();
        int solicitudesPendientes = prestamoService.obtenerSolicitudesPendientes(usuario).size();
        long proximosAVencer = prestamoService.contarProximosAVencer(usuario);

        // Notificaciones
        List<Prestamo> notificacionesProximoVencer = prestamoService.obtenerProximosAVencer(usuario);
        List<Prestamo> notificacionesAprobadas = prestamoService.obtenerRecienAprobados(usuario);

        model.addAttribute("cantidadPrestamosActivos", prestamosActivos);
        model.addAttribute("cantidadSolicitudesPendientes", solicitudesPendientes);
        model.addAttribute("cantidadProximosAVencer", proximosAVencer);
        model.addAttribute("notificacionesProximoVencer", notificacionesProximoVencer);
        model.addAttribute("notificacionesAprobadas", notificacionesAprobadas);

    }

    private void cargarSolicitudes(Model model, Usuario usuario) {
        List<Prestamo> solicitudes = prestamoService.obtenerSolicitudesPendientes(usuario);
        model.addAttribute("solicitudes", solicitudes);
    }

    private void cargarConfirmaciones(Model model, Usuario usuario) {
        List<Prestamo> confirmaciones = prestamoService.obtenerConfirmaciones(usuario);
        model.addAttribute("confirmaciones", confirmaciones);
    }

    private void cargarPrestamosActivos(Model model, Usuario usuario) {
        List<Prestamo> prestamosActivos = prestamoService.obtenerPrestamosActivos(usuario);
        model.addAttribute("prestamosActivos", prestamosActivos);
    }

    private void cargarHistorial(Model model, Usuario usuario) {
        List<Prestamo> historial = prestamoService.obtenerHistorial(usuario);
        model.addAttribute("historial", historial);
    }

    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
                || seccion.equals("prestamos-activos")
                || seccion.equals("solicitud-prestamo")
                || seccion.equals("confirmacion-prestamo")
                || seccion.equals("historial-prestamo");
    }
}