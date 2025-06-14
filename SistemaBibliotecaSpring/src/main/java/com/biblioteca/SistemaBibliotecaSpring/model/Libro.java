package com.biblioteca.SistemaBibliotecaSpring.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private EstadoLibro estado;
}

