package com.library.library_proyect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.library_proyect.model.CategoriaLibro;
import com.library.library_proyect.model.Libros;

@Repository
public interface CatalogoRepository extends JpaRepository<Libros, Long> {

    List<Libros> findTop4ByIdLibroNotOrderByIdLibroDesc(Long idLibro);

    // NUEVO MÉTODO para filtrar por categoría
    List<Libros> findByCategoriaIn(List<CategoriaLibro> categorias);
}
