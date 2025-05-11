package com.biblioteca.SistemaBibliotecaSpring.service;

import com.biblioteca.SistemaBibliotecaSpring.exception.PrestamoNoEncontradoException;
import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import com.biblioteca.SistemaBibliotecaSpring.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        prestamoRepository.deleteById(id);
    }

    @Override
    public Prestamo actualizar(Long id, Prestamo prestamo) {
        if (!prestamoRepository.existsById(id)) {
            throw new PrestamoNoEncontradoException(id);
        }
        prestamo.setId(id);
        return prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> buscarPorUsuarioId(Long usuarioId) {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getUsuario().getId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> buscarPorLibroId(Long libroId) {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getLibro().getId().equals(libroId))
                .collect(Collectors.toList());
    }
}
