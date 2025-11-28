package com.library.library_proyect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.Libros;
import java.util.Collections;
import java.util.stream.Collectors;
import com.library.library_proyect.services.CatalogoService;

@Controller
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/catalogo")
    public String catalogo(
        Model model,
        @RequestParam(required = false, name = "cat") List<String> categoriasSeleccionadas 
    ) {
        List<Libros> libros;
        
        // 1. INICIALIZACIÓN CLAVE: Aseguramos que categoriasActivas sea siempre una lista (no null)
        List<String> categoriasActivas = (categoriasSeleccionadas != null) 
                                        ? categoriasSeleccionadas 
                                        : Collections.emptyList(); // Si es null, usa una lista vacía
        
        // 2. Verifica si se seleccionó alguna categoría (la lista ahora puede estar vacía, pero no null)
        if (!categoriasActivas.isEmpty()) {
            
            List<CategoriaLibro> categoriasEnum = categoriasActivas.stream()
                .map(String::toUpperCase)
                .map(CategoriaLibro::valueOf)
                .collect(Collectors.toList());

            libros = catalogoService.obtenerLibrosPorCategorias(categoriasEnum);
            
        } else {
            // 3. Si no hay parámetros (lista vacía), muestra todos los libros
            libros = catalogoService.obtenerLibros();
        }

        // 4. Lógica existente:
        model.addAttribute("libros", libros);
        
        // Cargar todos los enums para la vista
        model.addAttribute("categorias", CategoriaLibro.values()); 
        
        // 5. Pasamos la lista activa (que ahora nunca es null)
        model.addAttribute("categoriasActivas", categoriasActivas);
        
        model.addAttribute("activePage", "catalogo");

        return "catalogo";
    }
}
