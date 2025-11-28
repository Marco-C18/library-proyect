package com.library.library_proyect.services;

import java.util.List;
import java.util.Optional;

import com.library.library_proyect.repository.CategoriaRepository;
import com.library.library_proyect.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.Libros;

@Service
public class CatalogoService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    public CatalogoService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // ======================
    // LIBROS
    // ======================

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

    public List<Libros> obtenerLibrosPorCategoria(CategoriaLibro categoria) {
        return catalogoRepository.findByCategoria(categoria);
    }

    public List<Libros> obtenerLibrosPorCategorias(List<CategoriaLibro> categorias) {
        return catalogoRepository.findByCategoriaIn(categorias);
    }
    public void reasignarCategoria(CategoriaLibro categoriaActual, CategoriaLibro nuevaCategoria) {
        List<Libros> libros = catalogoRepository.findByCategoria(categoriaActual);
        for (Libros l : libros) {
            l.setCategoria(nuevaCategoria);
            catalogoRepository.save(l);
        }
    }

    public void eliminarCategoria(CategoriaLibro categoria) {
        List<Libros> libros = catalogoRepository.findByCategoria(categoria);
        if (!libros.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar esta categoría porque tiene libros asociados.");
        }
    }

public List<Libros> obtenerLibrosPorCategoria(Categoria categoria) {
    return catalogoRepository.findByCategorias(categoria);
}
public List<Categoria> obtenerTodas() {
    return categoriaRepository.findAll(); 
}


}
