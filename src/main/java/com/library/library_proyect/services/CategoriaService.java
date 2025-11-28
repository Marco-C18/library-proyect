package com.library.library_proyect.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.repository.CatalogoRepository;
import com.library.library_proyect.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CatalogoRepository catalogoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    CategoriaService(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

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

    public boolean eliminarCategoria(Long id) {
    Categoria categoria = categoriaRepository.findById(id).orElse(null);
    if (categoria == null) return false;

    // Desasociar libros antes de eliminar
    List<Libros> libros = catalogoRepository.findByCategorias(categoria);
    for (Libros l : libros) {
        l.setCategoria(null);
        catalogoRepository.save(l);
    }

    categoriaRepository.delete(categoria);
    return true;
}

}
