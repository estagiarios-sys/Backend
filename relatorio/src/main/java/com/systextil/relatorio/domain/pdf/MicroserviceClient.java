package com.systextil.relatorio.domain.pdf;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class MicroserviceClient {

	private final RestTemplate restTemplate;
	private final HttpHeaders httpHeaders;
	
	MicroserviceClient() {
		this.restTemplate = new RestTemplate();
		this.httpHeaders = new HttpHeaders();
	}
	
	ResponseEntity<byte[]> generatePdf(MicroserviceRequest microserviceRequest) {
    	httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, httpHeaders);

    	return restTemplate.exchange(
        			"http://localhost:3001/generate-pdf",
        			HttpMethod.POST,
        			request,
        			byte[].class
        			);
    }
	
	ResponseEntity<byte[]> previewPdf(MicroserviceRequest microserviceRequest) {
    	httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<MicroserviceRequest> request = new HttpEntity<>(microserviceRequest, httpHeaders);

    	return restTemplate.exchange(
    			"http://localhost:3001/preview-pdf",
    			HttpMethod.POST,
    			request,
    			byte[].class
    			);
    }
}