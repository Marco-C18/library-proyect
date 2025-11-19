package com.library.library_proyect.services;

import com.library.library_proyect.model.EstadoPrestamo;
import com.library.library_proyect.model.Libros;
import com.library.library_proyect.model.Prestamo;
import com.library.library_proyect.model.Usuario;
import com.library.library_proyect.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    // Crear solicitud de préstamo
    public Prestamo solicitarPrestamo(Usuario usuario, Libros libro, LocalDate fechaDevolucion) {
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.PENDIENTE);
        return prestamoRepository.save(prestamo);
    }

    // Aprobar préstamo
    public void aprobarPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.APROBADO);
        prestamo.setFechaAprobacion(LocalDate.now());
        prestamoRepository.save(prestamo);
    }

    // Rechazar préstamo
    public void rechazarPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.RECHAZADO);
        prestamoRepository.save(prestamo);
    }

    // Devolver libro
    public void devolverLibro(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamoRepository.save(prestamo);
    }

    // Obtener solicitudes pendientes (estudiante)
    public List<Prestamo> obtenerSolicitudesPendientes(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstado(usuario, EstadoPrestamo.PENDIENTE);
    }

    // Obtener confirmaciones (estudiante)
    public List<Prestamo> obtenerConfirmaciones(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstadoIn(
                usuario,
                Arrays.asList(EstadoPrestamo.APROBADO, EstadoPrestamo.RECHAZADO));
    }

    // Obtener préstamos activos (estudiante)
    public List<Prestamo> obtenerPrestamosActivos(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstado(usuario, EstadoPrestamo.APROBADO);
    }

    // Obtener historial completo (estudiante)
    public List<Prestamo> obtenerHistorial(Usuario usuario) {
        return prestamoRepository.findByUsuarioOrderByFechaSolicitudDesc(usuario);
    }

    // Obtener préstamos pendientes para aprobar (bibliotecario)
    public List<Prestamo> obtenerPendientesAprobacion() {
        return prestamoRepository.findByEstado(EstadoPrestamo.PENDIENTE);
    }

    // Cancelar solicitud (estudiante)
    public void cancelarSolicitud(Long idPrestamo) {
        prestamoRepository.deleteById(idPrestamo);
    }

    // Verificar si el usuario ya tiene una solicitud activa de este libro
    public boolean tienePrestamoActivo(Usuario usuario, Libros libro) {
        List<EstadoPrestamo> estadosActivos = Arrays.asList(
                EstadoPrestamo.PENDIENTE,
                EstadoPrestamo.APROBADO);
        return prestamoRepository.existsByUsuarioAndLibroAndEstadoIn(usuario, libro, estadosActivos);
    }

    public long contarProximosAVencer(Usuario usuario) {
        LocalDate hoy = LocalDate.now();
        LocalDate limiteVencimiento = hoy.plusDays(3);

        return prestamoRepository.countProximosAVencer(
                usuario,
                EstadoPrestamo.APROBADO,
                hoy,
                limiteVencimiento);
    }

    public List<Prestamo> obtenerProximosAVencer(Usuario usuario) {
    LocalDate hoy = LocalDate.now();
    LocalDate limiteVencimiento = hoy.plusDays(3);
    
    return prestamoRepository.findProximosAVencer(
        usuario, 
        EstadoPrestamo.APROBADO, 
        hoy, 
        limiteVencimiento
    );
    }

    public List<Prestamo> obtenerRecienAprobados(Usuario usuario) {
    LocalDate fechaLimite = LocalDate.now().minusDays(2);
    
    return prestamoRepository.findRecienAprobados(
        usuario, 
        EstadoPrestamo.APROBADO, 
        fechaLimite
    );
}
}