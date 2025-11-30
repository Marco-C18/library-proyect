package com.library.library_proyect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.library.library_proyect.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);

    void deleteById(Long id);
}
