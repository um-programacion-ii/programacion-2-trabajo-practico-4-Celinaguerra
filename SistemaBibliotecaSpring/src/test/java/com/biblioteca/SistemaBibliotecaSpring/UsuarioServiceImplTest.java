package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.exception.UsuarioNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.repository.UsuarioRepository;
import com.biblioteca.SistemaBibliotecaSpring.service.UsuarioServiceImpl;
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
  Tests unitarios para UsuarioServiceImpl
  Casos cubiertos:
   - Guardado de usuario
   - Búsqueda por email (existente y no existente)
   - Actualización válida e inválida
   - Eliminación
   - Listado de todos los usuarios
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Celi Guerra");
        usuario.setEmail("celi@gmail.com");
    }

    @Test
    void guardarUsuario_guardarYDevolverUsuario() {
        // Arrange
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.guardar(usuario);

        // Assert
        assertEquals(usuario, resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void buscarPorEmailExistente_devolverUsuario() {
        // Arrange
        when(usuarioRepository.findByEmail("celi@gmail.com")).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.buscarPorEmail("celi@gmail.com");

        // Assert
        assertEquals(usuario, resultado);
        verify(usuarioRepository).findByEmail("celi@gmail.com");
    }

    @Test
    void buscarPorEmailInexistente_lanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail("celiii@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.buscarPorEmail("celiii@gmail.com"));
        verify(usuarioRepository).findByEmail("celiii@gmail.com");
    }

    @Test
    void actualizarExistente_actualizarYDevolverUsuario() {
        // Arrange
        Usuario actualizado = new Usuario();
        actualizado.setNombre("Celina");

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);

        // Act
        Usuario resultado = usuarioService.actualizar(1L, actualizado);

        // Assert
        assertEquals("Celina", resultado.getNombre());
        verify(usuarioRepository).existsById(1L);
        verify(usuarioRepository).save(actualizado);
    }

    @Test
    void actualizarInexistente_lanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.actualizar(999L, usuario));
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    void eliminar_llamarAEliminarEnRepositorio() {
        // Arrange
        usuarioService.eliminar(1L);

        // Act & Assert
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void obtenerTodos_retornarListaDeUsuarios() {
        // Arrange
        List<Usuario> lista = List.of(usuario);
        when(usuarioRepository.findAll()).thenReturn(lista);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(usuario, resultado.get(0));
        verify(usuarioRepository).findAll();
    }
}
