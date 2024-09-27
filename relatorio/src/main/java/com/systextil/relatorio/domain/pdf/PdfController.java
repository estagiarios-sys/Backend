package com.systextil.relatorio.domain.pdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	private PdfRepository repository;
	
	public PdfController(PdfRepository repository) {
		this.repository = repository;
	}
	
    @PostMapping("/generate")
    public ResponseEntity<Pdf> generatePdf(@RequestBody @Valid PdfSaving pdfSaving) throws URISyntaxException {
    	LocalDateTime requestTime = LocalDateTime.now();
    	RestTemplate restTemplate = new RestTemplate();

    	// Configura os cabeçalhos da requisição
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);

    	// Converte PdfSaving para JSON
    	HttpEntity<PdfSaving> request = new HttpEntity<>(pdfSaving, headers);

    	// Faz a requisição POST para o microserviço Node.js
    	ResponseEntity<byte[]> response = restTemplate.exchange(
    			"http://localhost:3001/generate-pdf", // URL do microserviço Node.js
    			HttpMethod.POST,
    			request,
    			byte[].class
    			);
    	LocalDateTime generatedPdfTime = LocalDateTime.now();
    	Pdf pdf = new Pdf(pdfSaving, requestTime, generatedPdfTime, response.getBody());
    	repository.save(pdf);

    	return ResponseEntity.created(new URI("")).body(pdf);
    }

    @PostMapping("/preview")
    public ResponseEntity<byte[]> previewPdf(@RequestBody PdfSaving pdfSaving) {
    	RestTemplate restTemplate = new RestTemplate();

    	// Configura os cabeçalhos da requisição
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<PdfSaving> request = new HttpEntity<>(pdfSaving, headers);

    	// Faz a requisição POST para o microserviço Node.js
    	ResponseEntity<byte[]> response = restTemplate.exchange(
    			"http://localhost:3001/preview-pdf", // URL do microserviço Node.js
    			HttpMethod.POST,
    			request,
    			byte[].class
    			);

    	// Retorna o PDF como resposta
    	return ResponseEntity.ok()
    			.contentType(MediaType.APPLICATION_PDF)
    			.body(response.getBody());
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<PdfListing>> listPdfs() {
    	List<PdfListing> pdfsList =  repository.findAttributesForList();
    	return ResponseEntity.ok().body(pdfsList);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getPdfBody(@PathVariable Long id) {
    	return ResponseEntity.ok()
    			.contentType(MediaType.APPLICATION_PDF)
    			.body(repository.findBodyById(id));
    }
}