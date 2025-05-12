package com.biblioteca.SistemaBibliotecaSpring.service;

import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;

import java.util.List;

public interface PrestamoService {
    Prestamo guardar(Prestamo prestamo);
    List<Prestamo> obtenerTodos();
    void eliminar(Long id);
    Prestamo actualizar(Long id, Prestamo prestamo);
    List<Prestamo> buscarPorUsuarioId(Long usuarioId);
    List<Prestamo> buscarPorLibroId(Long libroId);
}
