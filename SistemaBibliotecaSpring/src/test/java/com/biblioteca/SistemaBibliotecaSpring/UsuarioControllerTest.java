package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.controller.UsuarioController;
import com.biblioteca.SistemaBibliotecaSpring.exception.GlobalExceptionHandler;
import com.biblioteca.SistemaBibliotecaSpring.exception.UsuarioNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.service.UsuarioService;
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

@WebMvcTest(controllers = {UsuarioController.class, GlobalExceptionHandler.class})
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void obtenerTodos_deberiaRetornarListaDeUsuarios() throws Exception {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana García");
        usuario2.setEmail("ana@example.com");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[0].email", is("juan@example.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Ana García")))
                .andExpect(jsonPath("$[1].email", is("ana@example.com")));

        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    public void obtenerPorEmail_conEmailExistente_deberiaRetornarUsuario() throws Exception {
        // Arrange
        String email = "juan@example.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail(email);

        when(usuarioService.buscarPorEmail(email)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is(email)));

        verify(usuarioService, times(1)).buscarPorEmail(email);
    }

    @Test
    public void obtenerPorEmail_conEmailNoExistente_deberiaRetornarNotFound() throws Exception {
        // Arrange
        String email = "noexiste@example.com";
        when(usuarioService.buscarPorEmail(email)).thenThrow(new UsuarioNoEncontradoException(email));

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/" + email))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuario no encontrado con email: " + email)));

        verify(usuarioService, times(1)).buscarPorEmail(email);
    }

    @Test
    public void crear_conDatosValidos_deberiaCrearUsuario() throws Exception {
        // Arrange
        Usuario usuarioRequest = new Usuario();
        usuarioRequest.setNombre("Nuevo Usuario");
        usuarioRequest.setEmail("nuevo@example.com");

        Usuario usuarioResponse = new Usuario();
        usuarioResponse.setId(3L);
        usuarioResponse.setNombre("Nuevo Usuario");
        usuarioResponse.setEmail("nuevo@example.com");

        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Nuevo Usuario")))
                .andExpect(jsonPath("$.email", is("nuevo@example.com")));

        verify(usuarioService, times(1)).guardar(any(Usuario.class));
    }

    @Test
    public void actualizar_conDatosValidos_deberiaActualizarUsuario() throws Exception {
        // Arrange
        Long id = 1L;
        Usuario usuarioRequest = new Usuario();
        usuarioRequest.setNombre("Nombre Actualizado");
        usuarioRequest.setEmail("actualizado@example.com");

        Usuario usuarioResponse = new Usuario();
        usuarioResponse.setId(id);
        usuarioResponse.setNombre("Nombre Actualizado");
        usuarioResponse.setEmail("actualizado@example.com");

        when(usuarioService.actualizar(eq(id), any(Usuario.class))).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Nombre Actualizado")))
                .andExpect(jsonPath("$.email", is("actualizado@example.com")));

        verify(usuarioService, times(1)).actualizar(eq(id), any(Usuario.class));
    }

    @Test
    public void actualizar_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        // Arrange
        Long id = 99L;
        Usuario usuarioRequest = new Usuario();
        usuarioRequest.setNombre("Nombre Actualizado");
        usuarioRequest.setEmail("actualizado@example.com");

        when(usuarioService.actualizar(eq(id), any(Usuario.class)))
                .thenThrow(new UsuarioNoEncontradoException(id));

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuario no encontrado con id: " + id)));

        verify(usuarioService, times(1)).actualizar(eq(id), any(Usuario.class));
    }

    @Test
    public void eliminar_conIdExistente_deberiaEliminarUsuario() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(usuarioService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).eliminar(id);
    }

    @Test
    public void eliminar_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
        // Arrange
        Long id = 99L;
        doThrow(new UsuarioNoEncontradoException(id)).when(usuarioService).eliminar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuario no encontrado con id: " + id)));

        verify(usuarioService, times(1)).eliminar(id);
    }
}