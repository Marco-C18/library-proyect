package com.library.library_proyect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.library_proyect.model.Libros;

@Repository
public interface CatalogoRepository extends JpaRepository<Libros, Long>{
    
    // Método que busca los 5 libros más recientes (por ID) excluyendo uno específico
    List<Libros> findTop4ByIdLibroNotOrderByIdLibroDesc(Long idLibro);
}
