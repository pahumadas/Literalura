package com.alura.literalura.service;

import com.alura.literalura.dto.RespuestaGutendex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GutendexService implements ApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.gutendex.url}")
    private String apiUrl;

    @Override
    public RespuestaGutendex buscarLibros(String titulo) {
        String url = apiUrl + "?search=" + titulo.replace(" ", "%20");
        return restTemplate.getForObject(url, RespuestaGutendex.class);
    }
}
