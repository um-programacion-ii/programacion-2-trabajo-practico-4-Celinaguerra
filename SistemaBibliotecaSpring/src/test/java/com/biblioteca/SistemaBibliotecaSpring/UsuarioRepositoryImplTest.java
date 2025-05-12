package com.biblioteca.SistemaBibliotecaSpring;

import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.repository.UsuarioRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
  Tests unitarios para UsuarioRepositoryImpl
  Casos cubiertos:
   - Guardado de usuario
   - Búsqueda por ID existente y no existente
   - Verificar existencia de usuario por ID
   - Eliminación de usuario
   - Listado de todos los usuario
 */
class UsuarioRepositoryImplTest {

    private UsuarioRepositoryImpl usuarioRepository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();

        usuario = new Usuario();
        usuario.setNombre("Celina Guerra");
        usuario.setEmail("celi@gmail.com");
        usuario.setEstado("ACTIVO");
    }

    @Test
    void guardarUsuarioNuevo_asignarIdYGuardar() {
        Usuario guardado = usuarioRepository.save(usuario);

        assertNotNull(guardado.getId());
        assertEquals("celi@gmail.com", guardado.getEmail());
    }

    @Test
    void encontrarPorIdExistente_retornarUsuario() {
        Usuario guardado = usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Celina Guerra", resultado.get().getNombre());
    }

    @Test
    void encontrarPorIdInexistente_retornarVacio() {
        Optional<Usuario> resultado = usuarioRepository.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void encontrarPorEmailExistente_retornarUsuario() {
        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByEmail("celi@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals("Celina Guerra", resultado.get().getNombre());
    }

    @Test
    void encontrarPorEmailInexistente_retornarVacio() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("celu@gmail.com");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void encontrarTodos_retornarUsuarios() {
        usuarioRepository.save(usuario);
        Usuario otro = new Usuario();
        otro.setNombre("Mili");
        otro.setEmail("mili@gmail.com");
        otro.setEstado("INACTIVO");
        usuarioRepository.save(otro);

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertEquals(2, usuarios.size());
    }

    @Test
    void eliminarIdExistente_eliminarUsuario() {
        Usuario guardado = usuarioRepository.save(usuario);

        usuarioRepository.deleteById(guardado.getId());

        assertTrue(usuarioRepository.findById(guardado.getId()).isEmpty());
    }

    @Test
    void eliminarIdInexistente_noDeberiaRomper() {
        assertDoesNotThrow(() -> usuarioRepository.deleteById(888L));
    }

    @Test
    void idExistente_retornarTrue() {
        Usuario guardado = usuarioRepository.save(usuario);

        assertTrue(usuarioRepository.existsById(guardado.getId()));
    }

    @Test
    void idInexistente_retornarFalse() {
        assertFalse(usuarioRepository.existsById(777L));
    }
}
