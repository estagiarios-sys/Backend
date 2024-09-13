package com.systextil.relatorio.domain.pdf;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody String htmlContent) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Configura os cabeçalhos da requisição
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(htmlContent, headers);

            // Faz a requisição POST para o microserviço Node.js
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    "http://localhost:3001/generate-pdf", // URL do microserviço Node.js
                    HttpMethod.POST,
                    request,
                    byte[].class
            );

            // Retorna o PDF como resposta
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
