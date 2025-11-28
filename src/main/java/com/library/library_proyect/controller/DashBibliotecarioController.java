package com.library.library_proyect.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.model.Prestamo;
import com.library.library_proyect.services.CatalogoService;
import com.library.library_proyect.services.PrestamoService;

@Controller
@RequestMapping("/bibliotecario/dashboard")
public class DashBibliotecarioController {

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private PrestamoService prestamoService;

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

        // Cargar préstamos pendientes si está en la sección de aprobaciones
        if (seccion.equals("aprobaciones")) {

            List<Prestamo> prestamos = prestamoService.obtenerPendientesAprobacion();

            System.out
                    .println("📋 Préstamos pendientes encontrados: " + (prestamos != null ? prestamos.size() : "NULL"));

            if (prestamos != null && !prestamos.isEmpty()) {
                for (Prestamo p : prestamos) {
                    System.out.println("  - ID: " + p.getIdPrestamo() +
                            " | Libro: " + p.getLibro().getTitulo() +
                            " | Usuario: " + p.getUsuario().getNombre() +
                            " | Estado: " + p.getEstado());
                }
            } else {
                System.out.println("❌ No hay préstamos pendientes");
            }

            model.addAttribute("prestamos", prestamos);
            System.out.println("========================================");
        }

        System.out.println("📗 Entrando a DashBibliotecarioController /{seccion}");

        return "dashboards/dash_bibliotecario";
    }

    @GetMapping
    public String dashboardDefault() {
        return "redirect:/bibliotecario/dashboard/inicio";
    }

    @GetMapping("/categorias-disponibles")
    @ResponseBody
    public Map<String, String> getCategoriasDisponibles() {
        Map<String, String> categorias = new LinkedHashMap<>();
        for (CategoriaLibro cat : CategoriaLibro.values()) {
            categorias.put(cat.name(), cat.getDisplayName());
        }
        return categorias;
    }

    @PostMapping("/libros/guardar")
    public String guardarNuevoLibro(@ModelAttribute("libro") Libros libro) {
        try {
            MultipartFile file = libro.getFile();
            if (file != null && !file.isEmpty()) {
                String uploadsDir = new File("src/main/resources/static/img/libros/").getAbsolutePath() + "/";
                File uploadsFolder = new File(uploadsDir);
                if (!uploadsFolder.exists()) {
                    uploadsFolder.mkdirs();
                }

                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
                String filePath = uploadsDir + uniqueFilename;

                file.transferTo(new File(filePath));
                libro.setImagen(uniqueFilename);
            }

            catalogoService.guardarLibro(libro);
            return "redirect:/bibliotecario/dashboard/libros?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/bibliotecario/dashboard/libros?error=true";
        }
    }

    // Aprobar préstamo
    @PostMapping("/aprobar/{idPrestamo}")
    public String aprobarPrestamo(@PathVariable Long idPrestamo) {
        prestamoService.aprobarPrestamo(idPrestamo);
        return "redirect:/bibliotecario/dashboard/aprobaciones?approved=true";
    }

    // Rechazar préstamo
    @PostMapping("/rechazar/{idPrestamo}")
    public String rechazarPrestamo(@PathVariable Long idPrestamo) {
        prestamoService.rechazarPrestamo(idPrestamo);
        return "redirect:/bibliotecario/dashboard/aprobaciones?rejected=true";
    }

    private boolean esSeccionValida(String seccion) {
        return seccion.equals("inicio")
                || seccion.equals("libros")
                || seccion.equals("categorias")
                || seccion.equals("reportes")
                || seccion.equals("aprobaciones");
    }
}