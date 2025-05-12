package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.controller.PrestamoController;
import com.biblioteca.SistemaBibliotecaSpring.exception.GlobalExceptionHandler;
import com.biblioteca.SistemaBibliotecaSpring.exception.PrestamoNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {PrestamoController.class, GlobalExceptionHandler.class})
public class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void obtenerTodos_deberiaRetornarListaDePrestamos() throws Exception {
        // Arrange
        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("El Quijote");
        libro1.setAutor("Miguel de Cervantes");

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");

        Prestamo prestamo1 = new Prestamo();
        prestamo1.setId(1L);
        prestamo1.setLibro(libro1);
        prestamo1.setUsuario(usuario1);
        prestamo1.setFechaPrestamo(LocalDate.of(2025, 5, 1));
        prestamo1.setFechaDevolucion(LocalDate.of(2025, 5, 15));

        Libro libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("María López");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setId(2L);
        prestamo2.setLibro(libro2);
        prestamo2.setUsuario(usuario2);
        prestamo2.setFechaPrestamo(LocalDate.of(2025, 5, 5));
        prestamo2.setFechaDevolucion(LocalDate.of(2025, 5, 19));

        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);

        when(prestamoService.obtenerTodos()).thenReturn(prestamos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].libro.id", is(1)))
                .andExpect(jsonPath("$[0].libro.titulo", is("El Quijote")))
                .andExpect(jsonPath("$[0].usuario.id", is(1)))
                .andExpect(jsonPath("$[0].usuario.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[0].fechaPrestamo", is("2025-05-01")))
                .andExpect(jsonPath("$[0].fechaDevolucion", is("2025-05-15")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].libro.id", is(2)))
                .andExpect(jsonPath("$[1].libro.titulo", is("Cien años de soledad")))
                .andExpect(jsonPath("$[1].usuario.id", is(2)))
                .andExpect(jsonPath("$[1].usuario.nombre", is("María López")))
                .andExpect(jsonPath("$[1].fechaPrestamo", is("2025-05-05")))
                .andExpect(jsonPath("$[1].fechaDevolucion", is("2025-05-19")));

        verify(prestamoService, times(1)).obtenerTodos();
    }

    @Test
    public void buscarPorUsuario_deberiaRetornarPrestamosPorUsuario() throws Exception {
        // Arrange
        Long usuarioId = 1L;

        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("El Quijote");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNombre("Juan Pérez");

        Prestamo prestamo1 = new Prestamo();
        prestamo1.setId(1L);
        prestamo1.setLibro(libro1);
        prestamo1.setUsuario(usuario);
        prestamo1.setFechaPrestamo(LocalDate.of(2025, 5, 1));
        prestamo1.setFechaDevolucion(LocalDate.of(2025, 5, 15));

        Libro libro2 = new Libro();
        libro2.setId(3L);
        libro2.setTitulo("1984");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setId(3L);
        prestamo2.setLibro(libro2);
        prestamo2.setUsuario(usuario);
        prestamo2.setFechaPrestamo(LocalDate.of(2025, 5, 10));
        prestamo2.setFechaDevolucion(LocalDate.of(2025, 5, 24));

        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);

        when(prestamoService.buscarPorUsuarioId(usuarioId)).thenReturn(prestamos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(1)))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].usuario.id", is(1)));

        verify(prestamoService, times(1)).buscarPorUsuarioId(usuarioId);
    }

    @Test
    public void buscarPorLibro_deberiaRetornarPrestamosPorLibro() throws Exception {
        // Arrange
        Long libroId = 2L;

        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setTitulo("Cien años de soledad");

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");

        Prestamo prestamo1 = new Prestamo();
        prestamo1.setId(2L);
        prestamo1.setLibro(libro);
        prestamo1.setUsuario(usuario1);
        prestamo1.setFechaPrestamo(LocalDate.of(2025, 4, 15));
        prestamo1.setFechaDevolucion(LocalDate.of(2025, 4, 29));

        Usuario usuario2 = new Usuario();
        usuario2.setId(3L);
        usuario2.setNombre("Pedro Gómez");

        Prestamo prestamo2 = new Prestamo();
        prestamo2.setId(4L);
        prestamo2.setLibro(libro);
        prestamo2.setUsuario(usuario2);
        prestamo2.setFechaPrestamo(LocalDate.of(2025, 5, 1));
        prestamo2.setFechaDevolucion(LocalDate.of(2025, 5, 15));

        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);

        when(prestamoService.buscarPorLibroId(libroId)).thenReturn(prestamos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/libro/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].libro.id", is(2)))
                .andExpect(jsonPath("$[1].id", is(4)))
                .andExpect(jsonPath("$[1].libro.id", is(2)));

        verify(prestamoService, times(1)).buscarPorLibroId(libroId);
    }

    @Test
    public void crear_conDatosValidos_deberiaCrearPrestamo() throws Exception {
        // Arrange
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("El Quijote");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");

        Prestamo prestamoRequest = new Prestamo();
        prestamoRequest.setLibro(libro);
        prestamoRequest.setUsuario(usuario);
        prestamoRequest.setFechaPrestamo(LocalDate.of(2025, 5, 12));
        prestamoRequest.setFechaDevolucion(LocalDate.of(2025, 5, 26));

        Prestamo prestamoResponse = new Prestamo();
        prestamoResponse.setId(5L);
        prestamoResponse.setLibro(libro);
        prestamoResponse.setUsuario(usuario);
        prestamoResponse.setFechaPrestamo(LocalDate.of(2025, 5, 12));
        prestamoResponse.setFechaDevolucion(LocalDate.of(2025, 5, 26));

        when(prestamoService.guardar(any(Prestamo.class))).thenReturn(prestamoResponse);

        // Act & Assert
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.libro.id", is(1)))
                .andExpect(jsonPath("$.usuario.id", is(1)))
                .andExpect(jsonPath("$.fechaPrestamo", is("2025-05-12")))
                .andExpect(jsonPath("$.fechaDevolucion", is("2025-05-26")));

        verify(prestamoService, times(1)).guardar(any(Prestamo.class));
    }

    @Test
    public void actualizar_conDatosValidos_deberiaActualizarPrestamo() throws Exception {
        // Arrange
        Long id = 1L;

        Libro libro = new Libro();
        libro.setId(2L);
        libro.setTitulo("Cien años de soledad");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");

        Prestamo prestamoRequest = new Prestamo();
        prestamoRequest.setLibro(libro);
        prestamoRequest.setUsuario(usuario);
        prestamoRequest.setFechaPrestamo(LocalDate.of(2025, 5, 12));
        prestamoRequest.setFechaDevolucion(LocalDate.of(2025, 5, 30)); // Fecha actualizada

        Prestamo prestamoResponse = new Prestamo();
        prestamoResponse.setId(id);
        prestamoResponse.setLibro(libro);
        prestamoResponse.setUsuario(usuario);
        prestamoResponse.setFechaPrestamo(LocalDate.of(2025, 5, 12));
        prestamoResponse.setFechaDevolucion(LocalDate.of(2025, 5, 30));

        when(prestamoService.actualizar(eq(id), any(Prestamo.class))).thenReturn(prestamoResponse);

        // Act & Assert
        mockMvc.perform(put("/api/prestamos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.libro.id", is(2)))
                .andExpect(jsonPath("$.usuario.id", is(1)))
                .andExpect(jsonPath("$.fechaPrestamo", is("2025-05-12")))
                .andExpect(jsonPath("$.fechaDevolucion", is("2025-05-30")));

        verify(prestamoService, times(1)).actualizar(eq(id), any(Prestamo.class));
    }

    @Test
    public void actualizar_conIdNoExistente_deberiaRetornarError() throws Exception {
        // Arrange
        Long id = 99L;

        Libro libro = new Libro();
        libro.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Prestamo prestamoRequest = new Prestamo();
        prestamoRequest.setLibro(libro);
        prestamoRequest.setUsuario(usuario);
        prestamoRequest.setFechaPrestamo(LocalDate.of(2025, 5, 12));
        prestamoRequest.setFechaDevolucion(LocalDate.of(2025, 5, 26));

        when(prestamoService.actualizar(eq(id), any(Prestamo.class)))
                .thenThrow(new PrestamoNoEncontradoException(id));

        // Act & Assert
        mockMvc.perform(put("/api/prestamos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Prestamo no encontrado con id: " + id)));

        verify(prestamoService, times(1)).actualizar(eq(id), any(Prestamo.class));
    }

    @Test
    public void eliminar_conIdExistente_deberiaEliminarPrestamo() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(prestamoService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isOk());

        verify(prestamoService, times(1)).eliminar(id);
    }

    @Test
    public void eliminar_conIdNoExistente_deberiaRetornarError() throws Exception {
        // Arrange
        Long id = 99L;
        doThrow(new PrestamoNoEncontradoException(id)).when(prestamoService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/prestamos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Prestamo no encontrado con id: " + id)));

        verify(prestamoService, times(1)).eliminar(id);
    }
}