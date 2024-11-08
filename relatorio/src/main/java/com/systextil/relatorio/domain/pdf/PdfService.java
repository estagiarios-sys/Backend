package com.systextil.relatorio.domain.pdf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
class PdfService {

	private PdfRepository repository;
	
    @Value("${pdf.storage.location}")
    private String storageLocation;
    
    PdfService(PdfRepository repository) {
		this.repository = repository;
	}

    @Transactional
    Long createNoDataPdf(String pdfTitle) throws IOException {
    	LocalDateTime requestTime = LocalDateTime.now();

		if (pdfTitle == null || pdfTitle.isBlank()) {
			pdfTitle = "Sem título";
		}
    	
    	if (repository.count() == 10) {
    		Long oldestEntry = repository.getOldestEntry();
    		String pdfPath = repository.findPathById(oldestEntry);
    		repository.deleteById(oldestEntry);
    		
    		if (pdfPath != null) {
    			Files.delete(Paths.get(pdfPath));
    		}
    	}
    	Pdf pdf = new Pdf(pdfTitle, requestTime);
    	return repository.save(pdf).getId();
    }
    
    void generatePdf(PdfSaving pdfSaving) throws IOException {
    	Pdf noDataPdf = repository.getReferenceById(pdfSaving.pdfId());
    	noDataPdf.update(PdfStatus.GERANDO_PDF);
    	repository.save(noDataPdf);
    	MicroserviceRequest microserviceRequest = new MicroserviceRequest(pdfSaving.fullTableHTML(), noDataPdf.getPdfTitle(), pdfSaving.imgPDF());
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, headers);

    	try {
    		ResponseEntity<byte[]> response = restTemplate.exchange(
        			"http://localhost:3001/generate-pdf",
        			HttpMethod.POST,
        			request,
        			byte[].class
        			);
        	LocalDateTime generatedPdfTime = LocalDateTime.now();
        	String filePath = savePdf(response.getBody(), noDataPdf.getPdfTitle());
        	noDataPdf.update(generatedPdfTime, filePath);
        	repository.save(noDataPdf);
    	} catch (HttpClientErrorException exception) {
    		noDataPdf.update(PdfStatus.ERRO);
    		repository.save(noDataPdf);
    		throw new HttpClientErrorException(exception.getStatusCode(), exception.getLocalizedMessage());
    	}
    }
    
    ResponseEntity<byte[]> previewPdf(MicroserviceRequest microserviceRequest) {
    	RestTemplate restTemplate = new RestTemplate();

    	// Configura os cabeçalhos da requisição
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, headers);

    	// Faz a requisição POST para o microserviço Node.js
    	return restTemplate.exchange(
    			"http://localhost:3001/preview-pdf", // URL do microserviço Node.js
    			HttpMethod.POST,
    			request,
    			byte[].class
    			);
    }
    
    private String savePdf(byte[] fileBytes, String pdfTitle) throws IOException {
        Path storagePath = Paths.get(storageLocation);
        
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        String fileName = UUID.randomUUID() + "_" + pdfTitle + ".pdf";
        Path filePath = storagePath.resolve(fileName);
        Files.write(filePath, fileBytes);

        return filePath.toString();
    }
}