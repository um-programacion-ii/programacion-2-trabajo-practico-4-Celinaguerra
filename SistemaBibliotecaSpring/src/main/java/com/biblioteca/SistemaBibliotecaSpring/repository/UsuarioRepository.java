package com.biblioteca.SistemaBibliotecaSpring.repository;

import com.biblioteca.SistemaBibliotecaSpring.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}