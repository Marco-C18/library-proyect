package com.library.library_proyect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.library.library_proyect.model.Libros;
import com.library.library_proyect.services.CatalogoService;

@Controller
public class DetalleController {

    @Autowired
    private CatalogoService catalogoService;
    
    @GetMapping("/detalleLibro/{id}")
    public String mostrarDetalleLibro(@PathVariable Long id,Model model){
        Libros detalleLibro = catalogoService.obtenerLibroPorId(id);
        if (detalleLibro != null) {
            // Se pasa el ID del libro actual al método del servicio
            List<Libros> sugeridos = catalogoService.obtenerLibrosSugeridos(id);
            
            model.addAttribute("detalleLibro", detalleLibro);
            model.addAttribute("sugeridos", sugeridos);
            return "detalle";
        } else {
            return "redirect:/catalogo"; 
        }
    }
}
