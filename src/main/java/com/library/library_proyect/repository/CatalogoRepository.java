package com.library.library_proyect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.library_proyect.model.Categoria;
import com.library.library_proyect.model.Libros;

@Repository
public interface CatalogoRepository extends JpaRepository<Libros, Long> {
    
    // ✅ Buscar libros por categoría (Entidad)
    List<Libros> findByCategoria(Categoria categoria);
    
    // Para sugerencias
    List<Libros> findTop4ByIdLibroNotOrderByIdLibroDesc(Long idLibro);
    
    // Para filtros múltiples
    List<Libros> findByCategoriaIn(List<Categoria> categorias);
    
    // Contar libros por categoría
    long countByCategoria(Categoria categoria);
}