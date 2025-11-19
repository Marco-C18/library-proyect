package com.library.library_proyect.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.repository.CatalogoRepository;

@Service
public class CatalogoService {

    @Autowired
    CatalogoRepository catalogoRepository;

    //Método para guardar
    public Libros guardarLibro(Libros libro) {
        return catalogoRepository.save(libro);
    }

    public List<Libros> obtenerLibros() {
        return catalogoRepository.findAll();
    }

    public Libros obtenerLibroPorId(Long id) {
        Optional<Libros> libro = catalogoRepository.findById(id);
        return libro.orElse(null);
    }

    public List<Libros> obtenerLibrosSugeridos(Long idLibroActual) {
        return catalogoRepository.findTop4ByIdLibroNotOrderByIdLibroDesc(idLibroActual);
    }

    
    //Nuevo método para obtener libros filtrados por categoría.    
    public List<Libros> obtenerLibrosPorCategorias(List<CategoriaLibro> categorias) {
        // Asegúrate de que este método exista en CatalogoRepository.java
        return catalogoRepository.findByCategoriaIn(categorias); 
    }
}
