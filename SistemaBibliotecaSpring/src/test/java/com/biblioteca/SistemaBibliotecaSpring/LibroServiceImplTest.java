package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.exception.LibroNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.repository.LibroRepository;
import com.biblioteca.SistemaBibliotecaSpring.service.LibroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
  Tests unitarios para LibroServiceImpl
  Casos cubiertos:
   - Guardado de libro
   - Búsqueda por ISBN existente y no existente
   - Búsqueda por ID existente y no existente
   - Actualización válida e inválida
   - Eliminación de libro
   - Listado de todos los libros
 */

@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl libroService;

    private Libro libro;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1L);
        libro.setIsbn("12345");
        libro.setTitulo("Dracula");
        libro.setAutor("Bram Stoker");
    }

    @Test
    void guardarLibro_devolverLibro() {
        // Arrange
        when(libroRepository.save(libro)).thenReturn(libro);

        // Act
        Libro resultado = libroService.guardar(libro);

        // Assert
        assertEquals(libro, resultado);
        verify(libroRepository).save(libro);
    }

    @Test
    void buscarPorIsbnExistente_DevolverLibro() {
        // Arrange
        when(libroRepository.findByIsbn("12345")).thenReturn(Optional.of(libro));

        // Act
        Libro resultado = libroService.buscarPorIsbn("12345");

        // Assert
        assertEquals(libro, resultado);
        verify(libroRepository).findByIsbn("12345");
    }

    @Test
    void buscarPorIsbnInexistente_LanzarExcepcion() {
        // Arrange
        when(libroRepository.findByIsbn("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> libroService.buscarPorIsbn("999"));
        verify(libroRepository).findByIsbn("999");
    }

    @Test
    void buscarPorIdExistente_devolverLibro() {
        // Arrange
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        // Act
        Libro resultado = libroService.buscarPorId(1L);

        // Assert
        assertEquals(libro, resultado);
        verify(libroRepository).findById(1L);
    }

    @Test
    void buscarPorIdInexistente_lanzarExcepcion() {
        // Arrange
        when(libroRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> libroService.buscarPorId(999L));
        verify(libroRepository).findById(999L);
    }

    @Test
    void actualizarExistente_actualizarYDevolverLibro() {
        // Arrange
        Libro actualizado = new Libro();
        actualizado.setTitulo("Mujercitas");

        when(libroRepository.existsById(1L)).thenReturn(true);
        when(libroRepository.save(any(Libro.class))).thenReturn(actualizado);

        // Act
        Libro resultado = libroService.actualizar(1L, actualizado);

        // Assert
        assertEquals("Mujercitas", resultado.getTitulo());
        verify(libroRepository).existsById(1L);
        verify(libroRepository).save(actualizado);
    }

    @Test
    void actualizaInexistente_lanzarExcepcion() {
        // Arrange
        when(libroRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> libroService.actualizar(999L, libro));
        verify(libroRepository).existsById(999L);
    }

    @Test
    void eliminar_llamarAEliminarEnRepositorio() {
        // Arrange
        libroService.eliminar(1L);

        // Act & Assert
        verify(libroRepository).deleteById(1L);
    }

    @Test
    void obtenerTodos_retornarListaDeLibros() {
        // Arrange
        List<Libro> lista = List.of(libro);
        when(libroRepository.findAll()).thenReturn(lista);

        // Act
        List<Libro> resultado = libroService.obtenerTodos();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(libro, resultado.get(0));
        verify(libroRepository).findAll();
    }
}
