package com.biblioteca.SistemaBibliotecaSpring.service;

import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario buscarPorEmail(String email);
    List<Usuario> obtenerTodos();
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    Usuario actualizar(Long id, Usuario usuario);
}
