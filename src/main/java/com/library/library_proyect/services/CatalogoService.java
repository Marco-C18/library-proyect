package com.library.library_proyect.services;

import java.util.List;

import com.library.library_proyect.repository.CategoriaRepository;
import com.library.library_proyect.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.model.Libros;

@Service
public class CatalogoService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;

    public Libros guardarLibro(Libros libro) {
        return catalogoRepository.save(libro);
    }

    public List<Libros> obtenerLibros() {
        return catalogoRepository.findAll();
    }

    public Libros obtenerLibroPorId(Long id) {
        return catalogoRepository.findById(id).orElse(null);
    }

    public List<Libros> obtenerLibrosSugeridos(Long idLibroActual) {
        return catalogoRepository.findTop4ByIdLibroNotOrderByIdLibroDesc(idLibroActual);
    }

    public void eliminarLibro(Long id) {
        catalogoRepository.deleteById(id);
    }

    public List<Libros> obtenerLibrosPorCategoria(Categoria categoria) {
        return catalogoRepository.findByCategoria(categoria);
    }

    public List<Libros> obtenerLibrosPorCategorias(List<Categoria> categorias) {
        return catalogoRepository.findByCategoriaIn(categorias);
    }

    public List<Categoria> obtenerTodasCategorias() {
        return categoriaRepository.findAll();
    }
}