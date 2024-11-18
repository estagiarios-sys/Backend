package com.systextil.relatorio.domain.pdf;

import static com.systextil.relatorio.domain.pdf.StorageAccessor.*;

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
import java.time.LocalDateTime;

@Service
class PdfService {

	private final PdfRepository repository;
    
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
    			deleteFile(pdfPath);
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
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, headers);

    	return restTemplate.exchange(
    			"http://localhost:3001/preview-pdf",
    			HttpMethod.POST,
    			request,
    			byte[].class
    			);
    }
}