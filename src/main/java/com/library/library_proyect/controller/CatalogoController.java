package com.library.library_proyect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.library_proyect.model.Libros;
import com.library.library_proyect.services.CatalogoService;

@Controller
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    
    @GetMapping("/catalogo")
    public String catalogo(Model model){
        List<Libros> libros = catalogoService.obtenerLibros();
        model.addAttribute("libros", libros);
        return "catalogo";
    }
}
