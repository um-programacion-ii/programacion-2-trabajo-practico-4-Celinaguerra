package com.biblioteca.SistemaBibliotecaSpring.controller;

import com.biblioteca.SistemaBibliotecaSpring.model.Libro;
import com.biblioteca.SistemaBibliotecaSpring.service.LibroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros") // Ruta base
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * GET /api/libros
     * Retorna la lista de todos los libros registrados en el sistema.
     *
     * @return lista de libros
     */
    @GetMapping
    public List<Libro> obtenerTodos() {
        return libroService.obtenerTodos();
    }

    /**
     * GET /api/libros/{id}
     * Retorna un libro específico por su ID.
     *
     * @param id ID del libro a buscar
     * @return el libro correspondiente si existe
     */
    @GetMapping("/{id}")
    public Libro obtenerPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id);
    }

    /**
     * POST /api/libros
     * Crea un nuevo libro en el sistema.
     *
     * @param libro objeto libro a crear
     * @return el libro creado
     */
    @PostMapping
    public Libro crear(@RequestBody Libro libro) {
        return libroService.guardar(libro);
    }

    /**
     * PUT /api/libros/{id}
     * Actualiza un libro existente en el sistema.
     *
     * @param id ID del libro a actualizar
     * @param libro objeto libro con los nuevos datos
     * @return el libro actualizado
     */
    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.actualizar(id, libro);
    }

    /**
     * DELETE /api/libros/{id}
     * Elimina un libro del sistema.
     *
     * @param id ID del libro a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
    }
}