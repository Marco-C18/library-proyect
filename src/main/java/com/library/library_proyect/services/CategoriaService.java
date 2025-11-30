package com.library.library_proyect.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.repository.CatalogoRepository;
import com.library.library_proyect.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private CatalogoRepository catalogoRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    // ✅ Verificar si tiene libros asociados
    public boolean tieneLibrosAsociados(Categoria categoria) {
        return catalogoRepository.countByCategoria(categoria) > 0;
    }

    // ✅ Contar libros por categoría
    public long contarLibrosPorCategoria(Categoria categoria) {
        return catalogoRepository.countByCategoria(categoria);
    }
}