package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.controller.LibroController;
import com.biblioteca.SistemaBibliotecaSpring.exception.GlobalExceptionHandler;
import com.biblioteca.SistemaBibliotecaSpring.exception.LibroNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(controllers = {LibroController.class, GlobalExceptionHandler.class})
    public class LibroControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private LibroService libroService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void obtenerTodos_deberiaRetornarListaDeLibros() throws Exception {
            // Arrange
            Libro libro1 = new Libro();
            libro1.setId(1L);
            libro1.setTitulo("El Quijote");
            libro1.setAutor("Miguel de Cervantes");

            Libro libro2 = new Libro();
            libro2.setId(2L);
            libro2.setTitulo("Cien años de soledad");
            libro2.setAutor("Gabriel García Márquez");

            List<Libro> libros = Arrays.asList(libro1, libro2);

            when(libroService.obtenerTodos()).thenReturn(libros);

            // Act & Assert
            mockMvc.perform(get("/api/libros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].titulo", is("El Quijote")))
                    .andExpect(jsonPath("$[0].autor", is("Miguel de Cervantes")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].titulo", is("Cien años de soledad")))
                    .andExpect(jsonPath("$[1].autor", is("Gabriel García Márquez")));

            verify(libroService, times(1)).obtenerTodos();
        }

        @Test
        public void obtenerPorId_conIdExistente_deberiaRetornarLibro() throws Exception {
            // Arrange
            Libro libro = new Libro();
            libro.setId(1L);
            libro.setTitulo("El Quijote");
            libro.setAutor("Miguel de Cervantes");

            when(libroService.buscarPorId(1L)).thenReturn(libro);

            // Act & Assert
            mockMvc.perform(get("/api/libros/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.titulo", is("El Quijote")))
                    .andExpect(jsonPath("$.autor", is("Miguel de Cervantes")));

            verify(libroService, times(1)).buscarPorId(1L);
        }

        @Test
        public void obtenerPorId_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
            // Arrange
            Long id = 99L;
            when(libroService.buscarPorId(id)).thenThrow(new LibroNoEncontradoException(id));

            // Act & Assert
            mockMvc.perform(get("/api/libros/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Libro no encontrado con ID: " + id)));

            verify(libroService, times(1)).buscarPorId(id);
        }

        @Test
        public void crear_conDatosValidos_deberiaCrearLibro() throws Exception {
            // Arrange
            Libro libroRequest = new Libro();
            libroRequest.setTitulo("Nuevo Libro");
            libroRequest.setAutor("Nuevo Autor");

            Libro libroResponse = new Libro();
            libroResponse.setId(3L);
            libroResponse.setTitulo("Nuevo Libro");
            libroResponse.setAutor("Nuevo Autor");

            when(libroService.guardar(any(Libro.class))).thenReturn(libroResponse);

            // Act & Assert
            mockMvc.perform(post("/api/libros")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libroRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(3)))
                    .andExpect(jsonPath("$.titulo", is("Nuevo Libro")))
                    .andExpect(jsonPath("$.autor", is("Nuevo Autor")));

            verify(libroService, times(1)).guardar(any(Libro.class));
        }

        @Test
        public void actualizar_conDatosValidos_deberiaActualizarLibro() throws Exception {
            // Arrange
            Long id = 1L;
            Libro libroRequest = new Libro();
            libroRequest.setTitulo("Título Actualizado");
            libroRequest.setAutor("Autor Actualizado");

            Libro libroResponse = new Libro();
            libroResponse.setId(id);
            libroResponse.setTitulo("Título Actualizado");
            libroResponse.setAutor("Autor Actualizado");

            when(libroService.actualizar(eq(id), any(Libro.class))).thenReturn(libroResponse);

            // Act & Assert
            mockMvc.perform(put("/api/libros/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libroRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.titulo", is("Título Actualizado")))
                    .andExpect(jsonPath("$.autor", is("Autor Actualizado")));

            verify(libroService, times(1)).actualizar(eq(id), any(Libro.class));
        }

        @Test
        public void actualizar_conIdNoExistente_deberiaRetornarError() throws Exception {
            // Arrange
            Long id = 99L;
            Libro libroRequest = new Libro();
            libroRequest.setTitulo("Título Actualizado");
            libroRequest.setAutor("Autor Actualizado");

            when(libroService.actualizar(eq(id), any(Libro.class)))
                    .thenThrow(new LibroNoEncontradoException(id));

            // Act & Assert
            mockMvc.perform(put("/api/libros/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libroRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Libro no encontrado con ID: " + id)));

            verify(libroService, times(1)).actualizar(eq(id), any(Libro.class));
        }

        @Test
        public void eliminar_conIdExistente_deberiaEliminarLibro() throws Exception {
            // Arrange
            Long id = 1L;
            doNothing().when(libroService).eliminar(id);

            // Act & Assert
            mockMvc.perform(delete("/api/libros/1"))
                    .andExpect(status().isOk());

            verify(libroService, times(1)).eliminar(id);
        }

        @Test
        public void eliminar_conIdNoExistente_deberiaRetornarError() throws Exception {
            // Arrange
            Long id = 99L;
            doThrow(new LibroNoEncontradoException(id)).when(libroService).eliminar(id);

            // Act & Assert
            mockMvc.perform(delete("/api/libros/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Libro no encontrado con ID: " + id)));

            verify(libroService, times(1)).eliminar(id);
        }
    }