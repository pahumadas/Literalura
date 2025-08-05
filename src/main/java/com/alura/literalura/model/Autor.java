package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public void setNacimiento(Integer nacimiento) { this.nacimiento = nacimiento; }
    public Integer getFallecimiento() { return fallecimiento; }
    public void setFallecimiento(Integer fallecimiento) { this.fallecimiento = fallecimiento; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}