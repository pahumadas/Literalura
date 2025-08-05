package com.alura.literalura.service;

import com.alura.literalura.dto.DatosAutor;
import com.alura.literalura.dto.DatosLibro;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final ApiService apiService;

    @Autowired
    public LibroService(LibroRepository libroRepository,
                        AutorRepository autorRepository,
                        ApiService apiService) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.apiService = apiService;
    }

    public Optional<Libro> buscarYGuardarLibro(String titulo) {
        try {
            var respuesta = apiService.buscarLibros(titulo);

            if (respuesta == null || respuesta.libros() == null || respuesta.libros().isEmpty()) {
                return Optional.empty();
            }

            DatosLibro datosLibro = respuesta.libros().get(0);
            Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibro.titulo());
            if (libroExistente.isPresent()) {
                return libroExistente;
            }

            Libro libro = new Libro();
            libro.setTitulo(datosLibro.titulo());
            libro.setIdioma(datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()
                    ? datosLibro.idiomas().get(0)
                    : "es");
            libro.setDescargas(datosLibro.descargas() != null ? datosLibro.descargas() : 0);

            Libro libroGuardado = libroRepository.save(libro);

            List<Autor> autores = new ArrayList<>();
            for (DatosAutor datosAutor : datosLibro.autores()) {
                Autor autor = autorRepository.findByNombre(datosAutor.nombre())
                        .orElseGet(() -> {
                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombre(datosAutor.nombre());
                            nuevoAutor.setNacimiento(datosAutor.nacimiento());
                            nuevoAutor.setFallecimiento(datosAutor.fallecimiento());
                            return autorRepository.save(nuevoAutor);
                        });
                autores.add(autor);
            }

            libroGuardado.setAutores(autores);
            return Optional.of(libroRepository.save(libroGuardado));

        } catch (Exception e) {
            System.err.println("Error al buscar el libro: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public Optional<List<Libro>> listarLibrosPorIdioma(String idioma) {
        List<Libro> libros = libroRepository.buscarPorIdioma(idioma);
        if (libros == null || libros.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(libros);
    }
}