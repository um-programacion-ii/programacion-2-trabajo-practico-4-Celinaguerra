package com.biblioteca.SistemaBibliotecaSpring.repository;

import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import java.util.Optional;
import java.util.List;

public interface PrestamoRepository {
    Prestamo save(Prestamo prestamo);
    Optional<Prestamo> findById(Long id);
    List<Prestamo> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Prestamo> findByUsuarioId(Long usuarioId);
    List<Prestamo> findByLibroId(Long libroId);
}