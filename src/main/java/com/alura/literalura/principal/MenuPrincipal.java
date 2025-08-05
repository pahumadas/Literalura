package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class MenuPrincipal {
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutorService autorService;

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                \n=== MENÚ PRINCIPAL ===
                1. Buscar libro por título
                2. Listar libros registrados
                3. Listar autores registrados
                4. Listar autores vivos en un año
                5. Listar libros por idioma
                0. Salir
                """);

            try {
                System.out.print("Elige una opción: ");
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción no válida. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Ingresa el título del libro: ");
        String titulo = scanner.nextLine();
        System.out.println("\nBuscando información para: " + titulo);

        libroService.buscarYGuardarLibro(titulo).ifPresentOrElse(
                libro -> {
                    System.out.println("\n=== RESULTADO DE BÚSQUEDA ===");
                    System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor(es): " +
                            libro.getAutores().stream()
                                    .map(Autor::getNombre)
                                    .collect(Collectors.joining(", ")));
                    System.out.println("Idioma: " + libro.getIdioma());
                    System.out.println("Número de descargas: " + libro.getDescargas());
                    System.out.println("============================");
                },
                () -> System.out.println("\nNo se encontró ningún libro con ese título.")
        );
    }

    private void listarLibrosRegistrados() {
        System.out.println("\n--- Libros Registrados ---");
        libroService.listarLibros().forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getDescargas());
            System.out.println("Autor(es): " +
                    libro.getAutores().stream()
                            .map(Autor::getNombre)
                            .collect(Collectors.joining(", ")));
        });
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n--- Autores Registrados ---");
        autorService.listarAutores().forEach(autor -> {
            System.out.println("\nNombre: " + autor.getNombre());
            System.out.println("Nacimiento: " +
                    (autor.getNacimiento() != null ? autor.getNacimiento() : "N/A"));
            System.out.println("Fallecimiento: " +
                    (autor.getFallecimiento() != null ? autor.getFallecimiento() : "N/A"));
        });
    }

    private void listarAutoresVivosEnAnio() {
        try {
            System.out.print("Ingresa el año: ");
            int anio = Integer.parseInt(scanner.nextLine());

            System.out.println("\n--- Autores vivos en el año " + anio + " ---");
            var autores = autorService.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                autores.forEach(autor -> {
                    System.out.println("\nNombre: " + autor.getNombre());
                    System.out.println("Nacimiento: " +
                            (autor.getNacimiento() != null ? autor.getNacimiento() : "N/A"));
                    System.out.println("Fallecimiento: " +
                            (autor.getFallecimiento() != null ? autor.getFallecimiento() : "N/A"));
                });
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingresa un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
            \nSelecciona un idioma:
            1. Español (es)
            2. Inglés (en)
            3. Francés (fr)
            4. Portugués (pt)
            5. Italiano (it)
            """);
        System.out.print("Elige una opción: ");
        String opcion = scanner.nextLine();

        String idioma = switch (opcion) {
            case "1" -> "es";
            case "2" -> "en";
            case "3" -> "fr";
            case "4" -> "pt";
            case "5" -> "it";
            default -> {
                System.out.println("Opción no válida. Usando español por defecto.");
                yield "es";
            }
        };

        var libros = libroService.listarLibrosPorIdioma(idioma);

        if (libros.isEmpty() || libros.get().isEmpty()) {
            System.out.println("\nNo hay libros registrados en " + obtenerNombreIdioma(idioma) + ".");
        } else {
            System.out.println("\n--- Libros en " + obtenerNombreIdioma(idioma) + " ---");
            libros.get().forEach(libro -> {
                System.out.println("\nTítulo: " + libro.getTitulo());
                System.out.println("Autor(es): " +
                        libro.getAutores().stream()
                                .map(Autor::getNombre)
                                .collect(Collectors.joining(", ")));
                System.out.println("Descargas: " + libro.getDescargas());
            });
        }
    }

    private String obtenerNombreIdioma(String codigoIdioma) {
        return switch (codigoIdioma) {
            case "es" -> "español";
            case "en" -> "inglés";
            case "fr" -> "francés";
            case "pt" -> "portugués";
            case "it" -> "italiano";
            default -> "desconocido";
        };
    }
}