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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	private PdfRepository repository;
	
	public PdfController(PdfRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping("/create-empty")
	public ResponseEntity<Long> createNoDataPdf(@RequestBody @Nullable String pdfTitle) throws URISyntaxException {
		LocalDateTime requestTime = LocalDateTime.now();

		if (pdfTitle == null || pdfTitle.isBlank()) {
			pdfTitle = "Sem título";
		}
    	
    	if (repository.count() == 10) {
    		repository.deleteById(repository.getOldestEntry());
    	}
    	Pdf pdf = new Pdf(pdfTitle, requestTime);
    	Long noDataPdfId = repository.save(pdf).getId();
    	
    	return ResponseEntity.created(new URI("")).body(noDataPdfId);
	}
	
    @PutMapping("/set-data")
    public ResponseEntity<Pdf> generatePdf(@RequestBody PdfSaving pdfSaving) {
    	Pdf noDataPdf = repository.getReferenceById(pdfSaving.pdfId());
    	MicroserviceRequest microserviceRequest = new MicroserviceRequest(pdfSaving.fullTableHTML(), noDataPdf.getPdfTitle(), pdfSaving.imgPDF());
    	RestTemplate restTemplate = new RestTemplate();

    	// Configura os cabeçalhos da requisição
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);

    	// Converte PdfSaving para JSON
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, headers);

    	try {
    		ResponseEntity<byte[]> response = restTemplate.exchange(
        			"http://localhost:3001/generate-pdf", // URL do microserviço Node.js
        			HttpMethod.POST,
        			request,
        			byte[].class
        			);
        	LocalDateTime generatedPdfTime = LocalDateTime.now();
        	noDataPdf.update(generatedPdfTime, response.getBody());
        	repository.save(noDataPdf);
    	} catch (HttpClientErrorException exception) {
    		noDataPdf.update();
    		repository.save(noDataPdf);
    		throw new HttpClientErrorException(exception.getStatusCode(), exception.getLocalizedMessage());
    	}

    	return ResponseEntity.ok().build();
    }

    @PostMapping("/preview")
    public ResponseEntity<byte[]> previewPdf(@RequestBody MicroserviceRequest microserviceRequest) {
    	RestTemplate restTemplate = new RestTemplate();

    	// Configura os cabeçalhos da requisição
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, headers);

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