package com.library.library_proyect.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.library_proyect.model.Libros;
import com.library.library_proyect.repository.CatalogoRepository;

@Service
public class CatalogoService {
    
    @Autowired
    CatalogoRepository catalogoRepository;

    public List<Libros> obtenerLibros(){
        return catalogoRepository.findAll();
    }

    public Libros obtenerLibroPorId(Long id){
        Optional<Libros> libro = catalogoRepository.findById(id);
        return libro.orElse(null);
    }

    // Nuevo método que obtiene 5 libros sugeridos, excluyendo el que se pasa por ID
    public List<Libros> obtenerLibrosSugeridos(Long idLibroActual) {
        return catalogoRepository.findTop4ByIdLibroNotOrderByIdLibroDesc(idLibroActual);
    }
}
