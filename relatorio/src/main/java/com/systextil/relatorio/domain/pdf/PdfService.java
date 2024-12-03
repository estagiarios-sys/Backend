package com.systextil.relatorio.domain.pdf;

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
	private final PdfMicroserviceClient microserviceClient;
	private final PdfStorageAccessor storageAccessor;
    
    PdfService(PdfRepository repository, PdfMicroserviceClient microserviceClient, PdfStorageAccessor storageAccessor) {
		this.repository = repository;
		this.microserviceClient = microserviceClient;
		this.storageAccessor = storageAccessor;
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
    			storageAccessor.deleteFile(pdfPath);
    		}
    	}
    	Pdf pdf = new Pdf(pdfTitle, requestTime);
    	return repository.save(pdf).getId();
    }

    @Transactional
    Pdf setStatusGerandoPdf(Long pdfId) {
    	Pdf noDataPdf = repository.getReferenceById(pdfId);
    	noDataPdf.update(PdfStatus.GERANDO_PDF);
    	repository.save(noDataPdf);
    	
    	return noDataPdf;
    }

    void generatePdf(PdfSaving pdfSaving, Pdf noDataPdf) throws IOException {
    	ResponseEntity<byte[]> response = null;
    	
    	try {
    		response = microserviceClient.generatePdf(
        			new MicroserviceRequest(
        					pdfSaving.fullTableHTML(),
        					noDataPdf.getPdfTitle(),
        					pdfSaving.imgPDF()
        			)
        	);
    	} catch (HttpClientErrorException exception) {
    		noDataPdf.update(PdfStatus.ERRO);
        	repository.save(noDataPdf);
        	throw new HttpClientErrorException(exception.getLocalizedMessage(), exception.getStatusCode(), exception.getStatusText(), null, null, null);
		} catch (HttpServerErrorException exception) {
			noDataPdf.update(PdfStatus.ERRO);
        	repository.save(noDataPdf);
        	throw new HttpServerErrorException(exception.getLocalizedMessage(), exception.getStatusCode(), exception.getStatusText(), null, null, null);
		}
    	if (response.getStatusCode().is2xxSuccessful()) {
    		LocalDateTime generatedPdfTime = LocalDateTime.now();
            String filePath = storageAccessor.savePdf(response.getBody(), noDataPdf.getPdfTitle());
            noDataPdf.update(generatedPdfTime, filePath);
            repository.save(noDataPdf);
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
    	} else {
    		throw new UnsupportedHttpStatusException(response.getStatusCode());
    	}
    }
}