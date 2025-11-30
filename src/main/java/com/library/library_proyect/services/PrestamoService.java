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

    public Prestamo solicitarPrestamo(Usuario usuario, Libros libro, LocalDate fechaDevolucion) {
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.PENDIENTE);
        return prestamoRepository.save(prestamo);
    }

    public void aprobarPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.APROBADO);
        prestamo.setFechaAprobacion(LocalDate.now());
        prestamoRepository.save(prestamo);
    }

    public void rechazarPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.RECHAZADO);
        prestamoRepository.save(prestamo);
    }

    public void devolverLibro(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElseThrow();
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamoRepository.save(prestamo);
    }

    public List<Prestamo> obtenerSolicitudesPendientes(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstado(usuario, EstadoPrestamo.PENDIENTE);
    }

    public List<Prestamo> obtenerConfirmaciones(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstadoIn(
                usuario,
                Arrays.asList(EstadoPrestamo.APROBADO, EstadoPrestamo.RECHAZADO));
    }

    public List<Prestamo> obtenerPrestamosActivos(Usuario usuario) {
        return prestamoRepository.findByUsuarioAndEstado(usuario, EstadoPrestamo.APROBADO);
    }

    public List<Prestamo> obtenerHistorial(Usuario usuario) {
        return prestamoRepository.findByUsuarioOrderByFechaSolicitudDesc(usuario);
    }

    public List<Prestamo> obtenerPendientesAprobacion() {
        return prestamoRepository.findByEstado(EstadoPrestamo.PENDIENTE);
    }

    public void cancelarSolicitud(Long idPrestamo) {
        prestamoRepository.deleteById(idPrestamo);
    }

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
                limiteVencimiento);
    }

    public boolean libroEstaPrestado(Long idLibro) {
        return prestamoRepository.existsByLibroIdLibroAndEstado(idLibro, EstadoPrestamo.APROBADO);
    }

    public List<Prestamo> obtenerRecienAprobados(Usuario usuario) {
        LocalDate fechaLimite = LocalDate.now().minusDays(2);

        return prestamoRepository.findRecienAprobados(
                usuario,
                EstadoPrestamo.APROBADO,
                fechaLimite);
    }

    public long countByEstado(String estadoTexto) {
        EstadoPrestamo estado = EstadoPrestamo.valueOf(estadoTexto);
        return prestamoRepository.countByEstado(estado);
    }

    public List<Prestamo> obtenerUltimosPrestamos(int cantidad) {
        return prestamoRepository.findTop5ByOrderByFechaSolicitudDesc();
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        LocalDate hoy = LocalDate.now();
        return prestamoRepository.prestamosVencidos(
                EstadoPrestamo.APROBADO,
                hoy);
    }

    public List<Prestamo> obtenerPrestamosPorVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);

        return prestamoRepository.prestamosPorVencer(
                EstadoPrestamo.APROBADO,
                hoy,
                limite);
    }

    public List<Prestamo> obtenerPrestamosVenceEn(LocalDate fecha) {
        return prestamoRepository.prestamosPorVencer(
                EstadoPrestamo.APROBADO,
                fecha,
                fecha);
    }

    public long totalPrestamos() {
        return prestamoRepository.count();
    }

    public long prestamosPorEstado(EstadoPrestamo estado) {
        return prestamoRepository.countByEstado(estado);
    }

    public List<Prestamo> ultimosPrestamos() {
        return prestamoRepository.findTop5ByOrderByFechaSolicitudDesc();
    }

    public List<Prestamo> prestamosVencidos() {
        LocalDate hoy = LocalDate.now();
        return prestamoRepository.prestamosVencidos(EstadoPrestamo.APROBADO, hoy);
    }

    public List<Prestamo> prestamosPorVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);

        return prestamoRepository.prestamosPorVencer(
                EstadoPrestamo.APROBADO,
                hoy,
                limite);
    }

    public List<Prestamo> obtenerPendientes() {
        return prestamoRepository.findByEstado(EstadoPrestamo.PENDIENTE);
    }

}