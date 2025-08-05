package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;  // <-- Agregar este import

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAnio(int anio) {
        return autorRepository.buscarAutoresVivosEnAnio(anio);
    }

    public Optional<Autor> buscarPorNombre(String nombre) {
        return autorRepository.findByNombre(nombre);
    }
}