package com.systextil.relatorio.domain.pdf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("pdf")
public class PdfController {

	private PdfRepository repository;
	private PdfService service;
	
	public PdfController(PdfRepository repository, PdfService service) {
		this.repository = repository;
		this.service = service;
	}
	
	@Transactional
	@PostMapping("create-empty")
	public ResponseEntity<Long> createNoDataPdf(@RequestBody @Nullable String pdfTitle) throws URISyntaxException, IOException {
		Long noDataPdfId = service.createNoDataPdf(pdfTitle);
    	
    	return ResponseEntity.created(new URI("")).body(noDataPdfId);
	}
	
    @PutMapping("set-data")
    public ResponseEntity<Pdf> generatePdf(@RequestBody PdfSaving pdfSaving) throws IOException {
    	Pdf noDataPdf = service.setStatusGerandoPdf(pdfSaving.pdfId());
    	service.generatePdf(pdfSaving, noDataPdf);

    	return ResponseEntity.ok().build();
    }

    @PostMapping("preview")
    public ResponseEntity<byte[]> previewPdf(@RequestBody MicroserviceRequest microserviceRequest) {
    	byte[] response = service.previewPdf(microserviceRequest);

    	return ResponseEntity.ok()
    			.contentType(MediaType.APPLICATION_PDF)
    			.body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PdfListing>> listPdfs() {
    	List<PdfListing> pdfsList =  repository.findAttributesForList();
    	
    	return ResponseEntity.ok().body(pdfsList);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<byte[]> getPdfBody(@PathVariable Long id) throws IOException {
        String pdfPath = repository.findPathById(id);
        Path filePath = Paths.get(pdfPath);
        byte[] fileData = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                .body(fileData);
    }
}