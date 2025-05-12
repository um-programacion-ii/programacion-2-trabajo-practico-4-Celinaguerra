package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.model.EstadoLibro;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.repository.PrestamoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoRepositoryImplTest {

    private PrestamoRepositoryImpl prestamoRepository;
    private Usuario usuario;
    private Libro libro;

    @BeforeEach
    void setUp() {
        prestamoRepository = new PrestamoRepositoryImpl();
        usuario = new Usuario(1L, "Celina Guerra", "celi@gmail.com", "ACTIVO");
        libro = new Libro(1L, "12345", "Dracula", "Bram Stoker", EstadoLibro.valueOf("DISPONIBLE"));
    }

    @Test
    void guardarPrestamo_asignaIdYLoGuarda() {
        Prestamo prestamo = new Prestamo(null, libro, usuario, LocalDate.now(), null);
        Prestamo guardado = prestamoRepository.save(prestamo);

        assertNotNull(guardado.getId());
        assertEquals(prestamo, guardado);
    }

    @Test
    void buscarPorId_existente_devuelvePrestamo() {
        Prestamo prestamo = prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));
        Optional<Prestamo> encontrado = prestamoRepository.findById(prestamo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(prestamo, encontrado.get());
    }

    @Test
    void buscarPorId_inexistente_devuelveVacio() {
        Optional<Prestamo> encontrado = prestamoRepository.findById(999L);
        assertTrue(encontrado.isEmpty());
    }

    @Test
    void eliminarPrestamo_porId_loQuitaDelRepositorio() {
        Prestamo prestamo = prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));
        prestamoRepository.deleteById(prestamo.getId());

        assertFalse(prestamoRepository.existsById(prestamo.getId()));
    }

    @Test
    void existsById_devuelveTrueSiExiste() {
        Prestamo prestamo = prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));

        assertTrue(prestamoRepository.existsById(prestamo.getId()));
    }

    @Test
    void obtenerTodos_devuelveListaDePrestamos() {
        prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));
        prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));

        List<Prestamo> prestamos = prestamoRepository.findAll();

        assertEquals(2, prestamos.size());
    }

    @Test
    void buscarPorUsuarioId_filtraCorrectamente() {
        prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));

        List<Prestamo> resultado = prestamoRepository.findByUsuarioId(usuario.getId());

        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.get(0).getUsuario().getId());
    }

    @Test
    void buscarPorLibroId_filtraCorrectamente() {
        prestamoRepository.save(new Prestamo(null, libro, usuario, LocalDate.now(), null));

        List<Prestamo> resultado = prestamoRepository.findByLibroId(libro.getId());

        assertEquals(1, resultado.size());
        assertEquals(libro.getId(), resultado.get(0).getLibro().getId());
    }
}
