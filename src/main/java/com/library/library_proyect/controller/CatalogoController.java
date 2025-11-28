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
        
        List<String> categoriasActivas = (categoriasSeleccionadas != null) 
                                        ? categoriasSeleccionadas 
                                        : Collections.emptyList(); 
        
        if (!categoriasActivas.isEmpty()) {
            
            List<CategoriaLibro> categoriasEnum = categoriasActivas.stream()
                .map(String::toUpperCase)
                .map(CategoriaLibro::valueOf)
                .collect(Collectors.toList());

            libros = catalogoService.obtenerLibrosPorCategorias(categoriasEnum);
            
        } else {
            libros = catalogoService.obtenerLibros();
        }

        model.addAttribute("libros", libros);
        
        model.addAttribute("categorias", CategoriaLibro.values()); 
        
        model.addAttribute("categoriasActivas", categoriasActivas);
        
        model.addAttribute("activePage", "catalogo");

        return "catalogo";
    }
}
