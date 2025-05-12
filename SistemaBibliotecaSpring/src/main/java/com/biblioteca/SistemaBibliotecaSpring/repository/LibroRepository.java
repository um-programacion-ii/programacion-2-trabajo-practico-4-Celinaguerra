package com.biblioteca.SistemaBibliotecaSpring.repository;

import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import java.util.Optional;
import java.util.List;

public interface LibroRepository {
    Libro save(Libro libro);
    Optional<Libro> findById(Long id);
    Optional<Libro> findByIsbn(String isbn);
    List<Libro> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}