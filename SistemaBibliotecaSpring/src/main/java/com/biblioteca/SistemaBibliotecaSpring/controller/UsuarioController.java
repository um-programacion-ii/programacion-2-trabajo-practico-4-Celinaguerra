package com.biblioteca.SistemaBibliotecaSpring.controller;

import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import com.biblioteca.SistemaBibliotecaSpring.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios") //Ruta base
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/usuarios
     * Retorna la lista de todos los usuarios registrados en el sistema.
     *
     * @return lista de usuarios
     */
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    /**
     * GET /api/usuarios/{id}
     * Retorna un usuario específico por su ID.
     *
     * @param id ID del usuario a buscar
     * @return el usuario correspondiente si existe
     */
    @GetMapping("/{email}")
    public Usuario obtenerPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuario objeto usuario a crear
     * @return el usuario creado
     */
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente en el sistema.
     *
     * @param id      ID del usuario a actualizar
     * @param usuario objeto usuario con los nuevos datos
     * @return el usuario actualizado
     */
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario existente en el sistema.
     *
     * @param id ID del usuario a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
