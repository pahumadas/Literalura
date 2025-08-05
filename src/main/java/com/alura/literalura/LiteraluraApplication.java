package com.alura.literalura;

import com.alura.literalura.principal.MenuPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.alura.literalura.repository")
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private MenuPrincipal menuPrincipal;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        menuPrincipal.mostrarMenu();
    }
}
