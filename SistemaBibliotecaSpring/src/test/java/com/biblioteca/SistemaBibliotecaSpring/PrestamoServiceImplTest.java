package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.exception.PrestamoNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.repository.PrestamoRepository;
import com.biblioteca.SistemaBibliotecaSpring.service.PrestamoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/*
  Tests unitarios para PrestamoServiceImpl
  Casos cubiertos:
   - Guardado de préstamo
   - Búsqueda por ID (existente y no existente)
   - Actualización válida e inválida
   - Eliminación
   - Listado de todos los préstamos
 */

@ExtendWith(MockitoExtension.class)
class PrestamoServiceImplTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    private Prestamo prestamo;

    @BeforeEach
    void setUp() {
        Libro libro = new Libro();
        libro.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        prestamo = new Prestamo();
        prestamo.setId(1L);
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
    }

    @Test
    void guardar_guardarPrestamo() {
        // Arrange
        when(prestamoRepository.save(prestamo)).thenReturn(prestamo);

        // Act
        Prestamo resultado = prestamoService.guardar(prestamo);

        // Assert
        assertEquals(prestamo, resultado);
        verify(prestamoRepository).save(prestamo);
    }

    @Test
    void obtenerTodos_retornarLista() {
        // Arrange
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        // Act
        List<Prestamo> resultado = prestamoService.obtenerTodos();

        // Assert
        assertEquals(1, resultado.size());
        verify(prestamoRepository).findAll();
    }

    @Test
    void eliminar_llamarDeleteById() {
        // Arrange
        prestamoService.eliminar(1L);

        // Act & Assert
        verify(prestamoRepository).deleteById(1L);
    }

    @Test
    void actualizarExistente_actualizarPrestamo() {
        // Arrange
        when(prestamoRepository.existsById(1L)).thenReturn(true);
        when(prestamoRepository.save(prestamo)).thenReturn(prestamo);

        // Act
        Prestamo resultado = prestamoService.actualizar(1L, prestamo);

        // Assert
        assertEquals(prestamo, resultado);
        verify(prestamoRepository).existsById(1L);
        verify(prestamoRepository).save(prestamo);
    }

    @Test
    void actualizarInexistente_lanzarExcepcion() {
        // Arrange
        when(prestamoRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(PrestamoNoEncontradoException.class, () -> prestamoService.actualizar(99L, prestamo));
        verify(prestamoRepository).existsById(99L);
    }

    @Test
    void buscarPorUsuarioId_filtrarPrestamosPorUsuario() {
        // Arrange
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        // Act
        List<Prestamo> resultado = prestamoService.buscarPorUsuarioId(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        verify(prestamoRepository).findAll();
    }

    @Test
    void buscarPorLibroId_filtrarPrestamosPorLibro() {
        // Arrange
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        // Act
        List<Prestamo> resultado = prestamoService.buscarPorLibroId(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getLibro().getId());
        verify(prestamoRepository).findAll();
    }
}
