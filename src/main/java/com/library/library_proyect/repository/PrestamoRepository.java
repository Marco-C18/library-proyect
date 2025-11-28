package com.library.library_proyect.repository;

import com.library.library_proyect.model.EstadoPrestamo;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.model.Prestamo;
import com.library.library_proyect.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // estudiante
    List<Prestamo> findByUsuarioAndEstado(Usuario usuario, EstadoPrestamo estado);

    List<Prestamo> findByUsuarioAndEstadoIn(Usuario usuario, List<EstadoPrestamo> estados);

    List<Prestamo> findByUsuarioOrderByFechaSolicitudDesc(Usuario usuario);

    // bibliotecario
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    boolean existsByUsuarioAndLibroAndEstadoIn(Usuario usuario, Libros libro, List<EstadoPrestamo> estados);

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario = :usuario " +
            "AND p.estado = :estado " +
            "AND p.fechaDevolucion BETWEEN :hoy AND :limiteVencimiento")
    long countProximosAVencer(
            @Param("usuario") Usuario usuario,
            @Param("estado") EstadoPrestamo estado,
            @Param("hoy") LocalDate hoy,
            @Param("limiteVencimiento") LocalDate limiteVencimiento);

    @Query("SELECT p FROM Prestamo p WHERE p.usuario = :usuario " +
            "AND p.estado = :estado " +
            "AND p.fechaDevolucion BETWEEN :hoy AND :limiteVencimiento " +
            "ORDER BY p.fechaDevolucion ASC")
    List<Prestamo> findProximosAVencer(
            @Param("usuario") Usuario usuario,
            @Param("estado") EstadoPrestamo estado,
            @Param("hoy") LocalDate hoy,
            @Param("limiteVencimiento") LocalDate limiteVencimiento);

    @Query("SELECT p FROM Prestamo p WHERE p.usuario = :usuario " +
            "AND p.estado = :estado " +
            "AND p.fechaAprobacion >= :fechaLimite " +
            "ORDER BY p.fechaAprobacion DESC")
    List<Prestamo> findRecienAprobados(
            @Param("usuario") Usuario usuario,
            @Param("estado") EstadoPrestamo estado,
            @Param("fechaLimite") LocalDate fechaLimite);
}