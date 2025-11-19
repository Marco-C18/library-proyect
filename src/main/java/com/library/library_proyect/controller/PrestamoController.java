package com.library.library_proyect.controller;

import com.library.library_proyect.model.Libros;
import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.services.CatalogoService;
import com.library.library_proyect.services.PrestamoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private CatalogoService catalogoService;

    // Solicitar préstamo
    @PostMapping("/solicitar")
    public String solicitarPrestamo(
            @RequestParam Long idLibro,
            @RequestParam String fechaDevolucion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            if (usuario == null) {
                return "redirect:/login";
            }

            Libros libro = catalogoService.obtenerLibroPorId(idLibro);
            if (libro == null) {
                redirectAttributes.addFlashAttribute("error", "Libro no encontrado.");
                return "redirect:/catalogo";
            }

            // ✨ VALIDACIÓN: Verificar si ya tiene una solicitud activa
            if (prestamoService.tienePrestamoActivo(usuario, libro)) {
                redirectAttributes.addFlashAttribute("error",
                        "Ya tienes una solicitud pendiente o un préstamo activo de este libro.");
                return "redirect:/detalleLibro/" + idLibro;
            }

            LocalDate fecha = LocalDate.parse(fechaDevolucion);

            prestamoService.solicitarPrestamo(usuario, libro, fecha);

            redirectAttributes.addFlashAttribute("success",
                    "¡Solicitud enviada! Espera a que un bibliotecario la acepte.");

            return "redirect:/detalleLibro/" + idLibro;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Hubo un error al enviar tu solicitud. Intenta de nuevo.");
            return "redirect:/detalleLibro/" + idLibro;
        }
    }

    // Cancelar solicitud
    @PostMapping("/cancelar/{idPrestamo}")
    public String cancelarSolicitud(@PathVariable Long idPrestamo) {
        prestamoService.cancelarSolicitud(idPrestamo);
        return "redirect:/estudiante/dashboard/solicitud-prestamo?cancelled=true";
    }

    // Cancelar confirmación
    @PostMapping("/cancelar-confirmacion/{idPrestamo}")
    public String cancelarConfirmacion(@PathVariable Long idPrestamo) {
        prestamoService.cancelarSolicitud(idPrestamo);
        return "redirect:/estudiante/dashboard/confirmacion-prestamo?cancelled=true";
    }
}