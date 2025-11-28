package com.library.library_proyect.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.EstadoPrestamo;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.model.Prestamo;
import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.services.CatalogoService;
import com.library.library_proyect.services.CategoriaService;
import com.library.library_proyect.services.PrestamoService;

@Controller
@RequestMapping("/bibliotecario/dashboard")
public class DashBibliotecarioController {

    @Autowired
    private CatalogoService catalogoService;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private CategoriaService categoriaService;

    @ModelAttribute("libro")
    public Libros setupLibroForm() {
        return new Libros();
    }

    public static class CategoriaForm {
        private String nombre;
        public CategoriaForm() {}
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    /* ======================
     *  DASHBOARD GENERAL
     * ====================== */
    @GetMapping("/{seccion}")
    public String mostrarDashboard(@PathVariable String seccion,
                                   @RequestParam(required = false) String filtro,
                                   Model model,
                                   HttpServletRequest request) {

        if (!esSeccionValida(seccion)) {
            return "redirect:/bibliotecario/dashboard/inicio";
        }

        model.addAttribute("seccion", seccion);

        // INICIO
        if (seccion.equals("inicio")) {
            model.addAttribute("totalPrestamos", prestamoService.totalPrestamos());
            model.addAttribute("pendientes", prestamoService.prestamosPorEstado(EstadoPrestamo.PENDIENTE));
            model.addAttribute("aprobados", prestamoService.prestamosPorEstado(EstadoPrestamo.APROBADO));
            model.addAttribute("rechazados", prestamoService.prestamosPorEstado(EstadoPrestamo.RECHAZADO));
            model.addAttribute("ultimosPrestamos", prestamoService.ultimosPrestamos());
            model.addAttribute("vencidos", prestamoService.prestamosVencidos());
            model.addAttribute("porVencer", prestamoService.prestamosPorVencer(5));
        }

        // LIBROS
        if (seccion.equals("libros")) {
        model.addAttribute("libros", catalogoService.obtenerLibros());
        model.addAttribute("prestamoService", prestamoService);

        List<Categoria> categoriasDb = categoriaService.obtenerTodas();
        model.addAttribute("categoriasDb", categoriasDb);
        }

        // REPORTES
        if (seccion.equals("reportes")) {
            if (filtro == null || filtro.isBlank()) {
                model.addAttribute("vencidos", prestamoService.prestamosVencidos());
                model.addAttribute("porVencer", prestamoService.prestamosPorVencer(7));
                model.addAttribute("activeReport", "todos");
            } else if ("vencidos".equalsIgnoreCase(filtro)) {
                model.addAttribute("vencidos", prestamoService.prestamosVencidos());
                model.addAttribute("porVencer", List.of());
                model.addAttribute("activeReport", "vencidos");
            } else if ("porvencer".equalsIgnoreCase(filtro) || "por_vencer".equalsIgnoreCase(filtro)) {
                model.addAttribute("vencidos", List.of());
                model.addAttribute("porVencer", prestamoService.prestamosPorVencer(7));
                model.addAttribute("activeReport", "porvencer");
            } else {
                model.addAttribute("vencidos", prestamoService.prestamosVencidos());
                model.addAttribute("porVencer", prestamoService.prestamosPorVencer(7));
                model.addAttribute("activeReport", "todos");
            }
        }

        // APROBACIONES
        if (seccion.equals("aprobaciones")) {
            model.addAttribute("prestamos", prestamoService.obtenerPendientes());
        }

        // Fragmento AJAX
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "dashboards/dash_bibliotecario :: main-content";
        }

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

    /* ======================
     *  LIBROS
     * ====================== */
    @PostMapping("/libros/guardar")
    public String guardarNuevoLibro(@ModelAttribute("libro") Libros libro) {
        try {
            MultipartFile file = libro.getFile();
            if (file != null && !file.isEmpty()) {
                String uploadsDir = new File("src/main/resources/static/img/libros/").getAbsolutePath() + "/";
                File uploadsFolder = new File(uploadsDir);
                if (!uploadsFolder.exists()) uploadsFolder.mkdirs();
                String uniqueFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadsDir + uniqueFilename));
                libro.setImagen(uniqueFilename);
            }
            catalogoService.guardarLibro(libro);
            return "redirect:/bibliotecario/dashboard/libros?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/bibliotecario/dashboard/libros?error=true";
        }
    }

    @GetMapping("/libros/editar/{id}")
    public String editarLibroForm(@PathVariable Long id, Model model) {
        Libros libro = catalogoService.obtenerLibroPorId(id);
        if (libro == null) return "redirect:/bibliotecario/dashboard/libros?notfound=true";
        model.addAttribute("libro", libro);
            List<Categoria> categoriasDb = categoriaService.obtenerTodas();
            model.addAttribute("categoriasDb", categoriasDb);

            model.addAttribute("seccion", "libros");
            return "dashboards/forms/editar_libro";
        }

    @PostMapping("/libros/editar/{id}")
    public String editarLibroGuardar(@PathVariable Long id, @ModelAttribute("libro") Libros datosNuevos) {
        Libros libro = catalogoService.obtenerLibroPorId(id);
        if (libro == null) return "redirect:/bibliotecario/dashboard/libros?notfound=true";

        MultipartFile file = datosNuevos.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                String uploadsDir = new File("src/main/resources/static/img/libros/").getAbsolutePath() + "/";
                File folder = new File(uploadsDir);
                if (!folder.exists()) folder.mkdirs();
                String nombreNuevo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadsDir + nombreNuevo));
                libro.setImagen(nombreNuevo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        libro.setTitulo(datosNuevos.getTitulo());
        libro.setAutor(datosNuevos.getAutor());
        libro.setDescripcion(datosNuevos.getDescripcion());
        libro.setCategoria(datosNuevos.getCategoria());
        libro.setAnio(datosNuevos.getAnio());
        catalogoService.guardarLibro(libro);

        return "redirect:/bibliotecario/dashboard/libros?edit_success=true";
    }

    @GetMapping("/libros/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        catalogoService.eliminarLibro(id);
        return "redirect:/bibliotecario/dashboard/libros?delete_success=true";
    }

    @GetMapping("/libros/json/{id}")
    @ResponseBody
    public Map<String, Object> obtenerLibroJson(@PathVariable Long id) {
        Libros libro = catalogoService.obtenerLibroPorId(id);
        if (libro == null) return Map.of("ok", false, "message", "no encontrado");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ok", true);
        map.put("idLibro", libro.getIdLibro());
        map.put("titulo", libro.getTitulo());
        map.put("autor", libro.getAutor());
        map.put("anio", libro.getAnio());
        map.put("categoria", libro.getCategoria() != null ? libro.getCategoria().name() : "");
        map.put("categoriaDisplay", libro.getCategoria() != null ? libro.getCategoria().getDisplayName() : "");
        map.put("paginas", libro.getPaginas());
        map.put("descripcion", libro.getDescripcion());
        map.put("imagen", libro.getImagen());
        return map;
    }

    /* ======================
     *  CATEGORÍAS 
     * ====================== */
  @GetMapping("/categorias")
public String mostrarCategorias(Model model, HttpServletRequest request) {

    for (CategoriaLibro catEnum : CategoriaLibro.values()) {
        if (!categoriaService.existePorNombre(catEnum.getDisplayName())) {
            Categoria nueva = new Categoria();
            nueva.setNombre(catEnum.getDisplayName());
            categoriaService.guardar(nueva);
        }
    }

    List<Categoria> categoriasDb = categoriaService.obtenerTodas();
    model.addAttribute("categoriasDb", categoriasDb);

    model.addAttribute("categoria", new Categoria());
    model.addAttribute("seccion", "categorias");

    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
        return "dashboards/dash_bibliotecario :: main-content";
    }

    return "dashboards/dash_bibliotecario";
}

    @PostMapping("/categorias/guardar-db")
    public String guardarCategoriaDb(@ModelAttribute("categoria") Categoria categoria,
                                     RedirectAttributes ra) {
        if (categoriaService.existePorNombre(categoria.getNombre())) {
            ra.addFlashAttribute("errorCategoria", "La categoría ya existe.");
        } else {
            categoriaService.guardar(categoria);
            ra.addFlashAttribute("successCategoria", "Categoría creada correctamente.");
        }
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    @PostMapping("/categorias/editar-db/{id}")
    public String editarCategoriaDb(@PathVariable Long id,
                                    @ModelAttribute("categoria") Categoria datosNuevos,
                                    RedirectAttributes ra) {
        Categoria cat = categoriaService.obtenerPorId(id).orElse(null);
        if (cat != null) {
            cat.setNombre(datosNuevos.getNombre());
            categoriaService.guardar(cat);
            ra.addFlashAttribute("successCategoria", "Categoría actualizada correctamente.");
        } else {
            ra.addFlashAttribute("errorCategoria", "Categoría no encontrada.");
        }
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    @PostMapping("/categorias/eliminar-db/{id}")
public String eliminarCategoriaDb(@PathVariable Long id, RedirectAttributes ra) {
    Categoria cat = categoriaService.obtenerPorId(id).orElse(null);
    if (cat == null) {
        ra.addFlashAttribute("errorCategoria", "Categoría no encontrada.");
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    try {
        CategoriaLibro catEnum = CategoriaLibro.valueOf(cat.getNombre());

        List<Libros> libros = catalogoService.obtenerLibrosPorCategoria(catEnum);

        if (!libros.isEmpty()) {
            ra.addFlashAttribute("errorCategoria", "No se puede eliminar: la categoría tiene libros asociados.");
        } else {
            ra.addFlashAttribute("errorCategoria", "No se puede eliminar: la categoría es parte del enum y no puede borrarse.");
        }

    } catch (IllegalArgumentException e) {
        if (catalogoService.obtenerLibrosPorCategoria(cat).isEmpty()) {
            categoriaService.eliminar(id);
            ra.addFlashAttribute("successCategoria", "Categoría eliminada correctamente.");
        } else {
            ra.addFlashAttribute("errorCategoria", "No se puede eliminar: la categoría tiene libros asociados.");
        }
    }

    return "redirect:/bibliotecario/dashboard/categorias";
}


    @GetMapping("/categorias-disponibles-db")
    @ResponseBody
    public Map<Long, String> getCategoriasDisponiblesDb() {
        Map<Long, String> categorias = new LinkedHashMap<>();
        List<Categoria> lista = categoriaService.obtenerTodas();
        for (Categoria cat : lista) {
            categorias.put(cat.getId(), cat.getNombre());
        }
        return categorias;
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

    @PostMapping("/categorias/reasignar")
    public String reasignarCategoria(@RequestParam("from") String categoriaFrom,
                                     @RequestParam("to") String categoriaTo,
                                     RedirectAttributes ra) {
        try {
            CategoriaLibro from = CategoriaLibro.valueOf(categoriaFrom);
            CategoriaLibro to = CategoriaLibro.valueOf(categoriaTo);

            if (from == to) {
                ra.addFlashAttribute("categoriaMsg", "Seleccione categorías diferentes para reasignar.");
                return "redirect:/bibliotecario/dashboard/categorias";
            }

            catalogoService.reasignarCategoria(from, to);
            ra.addFlashAttribute("categoriaMsg", "Libros reasignados correctamente.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("categoriaMsg", "Categoría inválida.");
        } catch (Exception ex) {
            ra.addFlashAttribute("categoriaMsg", "Error al reasignar: " + ex.getMessage());
        }
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    @PostMapping("/categorias/eliminar")
    public String eliminarCategoriaEndpoint(@RequestParam("categoria") String categoria,
                                            RedirectAttributes ra) {
        try {
            CategoriaLibro cat = CategoriaLibro.valueOf(categoria);
            List<Libros> libros = catalogoService.obtenerLibrosPorCategoria(cat);
            if (!libros.isEmpty()) {
                ra.addFlashAttribute("categoriaMsg", "No se puede eliminar: la categoría tiene libros asociados.");
            } else {
                ra.addFlashAttribute("categoriaMsg", "No hay libros en la categoría. La categoría es parte del enum y no puede eliminarse.");
            }
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("categoriaMsg", "Categoría inválida.");
        }
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") CategoriaForm categoria,
                                   RedirectAttributes ra) {
        ra.addFlashAttribute("errorCategoria", "Las categorías no se pueden crear porque están definidas como ENUM.");
        return "redirect:/bibliotecario/dashboard/categorias";
    }

    /* ======================
     *  APROBACIONES
     * ====================== */
    @PostMapping("/aprobar/{idPrestamo}")
    public String aprobarPrestamo(@PathVariable Long idPrestamo) {
        prestamoService.aprobarPrestamo(idPrestamo);
        return "redirect:/bibliotecario/dashboard/aprobaciones?approved=true";
    }

    @PostMapping("/rechazar/{idPrestamo}")
    public String rechazarPrestamo(@PathVariable Long idPrestamo) {
        prestamoService.rechazarPrestamo(idPrestamo);
        return "redirect:/bibliotecario/dashboard/aprobaciones?rejected=true";
    }
}
