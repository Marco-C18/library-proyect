package com.library.library_proyect.controller;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.services.CatalogoService;
import com.library.library_proyect.services.CategoriaService;

@Controller
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/catalogo")
    public String catalogo(
        Model model,
        @RequestParam(required = false, name = "cat") List<Long> categoriasSeleccionadas 
    ) {
        List<Libros> libros;
        
        List<Long> categoriasActivas = (categoriasSeleccionadas != null) 
                                        ? categoriasSeleccionadas 
                                        : Collections.emptyList(); 
        
        if (!categoriasActivas.isEmpty()) {
            // Convertir IDs a objetos Categoria
            List<Categoria> categoriasEntidad = categoriasActivas.stream()
                .map(id -> categoriaService.obtenerPorId(id).orElse(null))
                .filter(cat -> cat != null)
                .collect(Collectors.toList());

            libros = catalogoService.obtenerLibrosPorCategorias(categoriasEntidad);
            
        } else {
            libros = catalogoService.obtenerLibros();
        }

        model.addAttribute("libros", libros);
        
        // ✅ Cargar todas las categorías desde la BD
        model.addAttribute("categorias", categoriaService.obtenerTodas()); 
        
        model.addAttribute("categoriasActivas", categoriasActivas);
        
        model.addAttribute("activePage", "catalogo");

        return "catalogo";
    }
}