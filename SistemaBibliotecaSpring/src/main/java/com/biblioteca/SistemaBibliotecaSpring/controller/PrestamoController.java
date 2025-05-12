package com.biblioteca.SistemaBibliotecaSpring.controller;

import com.biblioteca.SistemaBibliotecaSpring.model.Prestamo;
import com.biblioteca.SistemaBibliotecaSpring.service.PrestamoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos") //Ruta base
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    /**
     * GET /api/prestamos
     * Retorna la lista de todos los prestamos registrados en el sistema.
     *
     * @return lista de prestamos
     */
    @GetMapping
    public List<Prestamo> obtenerTodos() {
        return prestamoService.obtenerTodos();
    }


    /**
     * GET /api/prestamos/{id}
     * Retorna un prestamo específico por su ID.
     *
     * @param id ID del prestamo a buscar
     * @return el prestamo correspondiente si existe
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<Prestamo> buscarPorUsuario(@PathVariable Long usuarioId) {
        return prestamoService.buscarPorUsuarioId(usuarioId);
    }

    /**
     * GET /api/prestamos/libro/{libroId}
     * Retorna un prestamo específico por su ID.
     *
     * @param libroId ID del libro a buscar
     * @return el prestamo correspondiente si existe
     */
    @GetMapping("/libro/{libroId}")
    public List<Prestamo> buscarPorLibro(@PathVariable Long libroId) {
        return prestamoService.buscarPorLibroId(libroId);
    }

    /**
     * POST /api/prestamos
     * Crea un nuevo prestamo en el sistema.
     *
     * @param prestamo objeto prestamo a crear
     * @return el prestamo creado
     */
    @PostMapping
    public Prestamo crear(@RequestBody Prestamo prestamo) {
        return prestamoService.guardar(prestamo);
    }

    /**
     * PUT /api/prestamos/{id}
     * Actualiza un prestamo existente en el sistema.
     *
     * @param id ID del prestamo a actualizar
     * @param prestamo objeto prestamo con los nuevos datos
     * @return el prestamo actualizado
     */
    @PutMapping("/{id}")
    public Prestamo actualizar(@PathVariable Long id, @RequestBody Prestamo prestamo) {
        return prestamoService.actualizar(id, prestamo);
    }

    /**
     * DELETE /api/prestamos/{id}
     * Elimina un prestamo del sistema.
     *
     * @param id ID del prestamo a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        prestamoService.eliminar(id);
    }
}
