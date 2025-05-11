package com.biblioteca.SistemaBibliotecaSpring.exception;

public class PrestamoNoEncontradoException extends RuntimeException {

    public PrestamoNoEncontradoException(Long id) {
        super("Prestamo no encontrado con id: " + id);
    }
}