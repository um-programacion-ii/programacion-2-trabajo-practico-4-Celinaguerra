package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.model.EstadoLibro;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.repository.LibroRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
  Tests unitarios para LibroRepositoryImpl
  Casos cubiertos:
   - Guardado de libro
   - Búsqueda por ISBN existente y no existente
   - Búsqueda por ID existente y no existente
   - Verificar existencia de libro por ID
   - Eliminación de libro
   - Listado de todos los libros
 */

class LibroRepositoryImplTest {

    private LibroRepositoryImpl libroRepository;
    private Libro libro;

    @BeforeEach
    void setUp() {
        libroRepository = new LibroRepositoryImpl();

        libro = new Libro();
        libro.setIsbn("12345");
        libro.setTitulo("Dracula");
        libro.setAutor("Bram Stoker");
        libro.setEstado(EstadoLibro.DISPONIBLE);
    }

    @Test
    void guardarNuevoLibro_asignarIdYGuardar() {
        Libro guardado = libroRepository.save(libro);

        assertNotNull(guardado.getId());
        assertEquals("Dracula", guardado.getTitulo());
    }

    @Test
    void encontrarPorIdExistente_retornarLibro() {
        Libro guardado = libroRepository.save(libro);

        Optional<Libro> resultado = libroRepository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals(guardado.getId(), resultado.get().getId());
    }

    @Test
    void encontrarPorIdInexistente_retornarVacio() {
        Optional<Libro> resultado = libroRepository.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void encontrarPorIsbnExistente_retornarLibro() {
        libroRepository.save(libro);

        Optional<Libro> resultado = libroRepository.findByIsbn("12345");

        assertTrue(resultado.isPresent());
        assertEquals("Dracula", resultado.get().getTitulo());
    }

    @Test
    void EncontrarPorIsbnInexistente_retornarVacio() {
        Optional<Libro> resultado = libroRepository.findByIsbn("11111");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void encontrarTodos_retornarTodosLosLibros() {
        libroRepository.save(libro);
        Libro otro = new Libro();
        otro.setIsbn("23456");
        otro.setTitulo("1984");
        otro.setAutor("George Orwell");
        otro.setEstado(EstadoLibro.DISPONIBLE);
        libroRepository.save(otro);

        List<Libro> libros = libroRepository.findAll();

        assertEquals(2, libros.size());
    }

    @Test
    void eliminarPorIdExistente_eliminarLibro() {
        Libro guardado = libroRepository.save(libro);

        libroRepository.deleteById(guardado.getId());

        assertTrue(libroRepository.findById(guardado.getId()).isEmpty());
    }

    @Test
    void eliminarPorIdInexistente_noDeberiaRomper() {
        assertDoesNotThrow(() -> libroRepository.deleteById(999L));
    }

    @Test
    void idExistente_retornarTrue() {
        Libro guardado = libroRepository.save(libro);

        assertTrue(libroRepository.existsById(guardado.getId()));
    }

    @Test
    void idInexistente_retornarFalse() {
        assertFalse(libroRepository.existsById(12345L));
    }
}
