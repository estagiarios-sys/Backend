package com.systextil.relatorio.domain.pdf;

import static com.systextil.relatorio.domain.pdf.StorageAccessor.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.systextil.relatorio.infra.exception_handler.UnsupportedHttpStatusException;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
class PdfService {

	private final PdfRepository repository;
	private final MicroserviceClient microserviceClient;
    
    PdfService(PdfRepository repository, MicroserviceClient microserviceClient) {
		this.repository = repository;
		this.microserviceClient = microserviceClient;
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
    	
    	ResponseEntity<byte[]> response = microserviceClient.generatePdf(
    			new MicroserviceRequest(
    					pdfSaving.fullTableHTML(),
    					noDataPdf.getPdfTitle(),
    					pdfSaving.imgPDF()
    			)
    	);
    	if (response.getStatusCode().is2xxSuccessful()) {
    		LocalDateTime generatedPdfTime = LocalDateTime.now();
            String filePath = savePdf(response.getBody(), noDataPdf.getPdfTitle());
            noDataPdf.update(generatedPdfTime, filePath);
            repository.save(noDataPdf);
    	} else if (response.getStatusCode().is4xxClientError()) {
    		noDataPdf.update(PdfStatus.ERRO);
        	repository.save(noDataPdf);
        	throw new HttpClientErrorException(response.getStatusCode());
    	} else if (response.getStatusCode().is5xxServerError()) {
    		noDataPdf.update(PdfStatus.ERRO);
        	repository.save(noDataPdf);
        	throw new HttpServerErrorException(response.getStatusCode());
    	} else {
    		noDataPdf.update(PdfStatus.ERRO);
        	repository.save(noDataPdf);
        	throw new UnsupportedHttpStatusException(response.getStatusCode());
    	}
    }
    
    byte[] previewPdf(MicroserviceRequest microserviceRequest) {
    	ResponseEntity<byte[]> response = microserviceClient.previewPdf(microserviceRequest);
    	
    	if (response.getStatusCode().is2xxSuccessful()) {
    		return response.getBody();
    	} else if (response.getStatusCode().is4xxClientError()) {
    		throw new HttpClientErrorException(response.getStatusCode());
    	} else if (response.getStatusCode().is5xxServerError()) {
    		throw new HttpServerErrorException(response.getStatusCode());
    	} else {
    		throw new UnsupportedHttpStatusException(response.getStatusCode());
    	}
    }
}