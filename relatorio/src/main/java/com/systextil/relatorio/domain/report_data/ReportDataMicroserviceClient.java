package com.systextil.relatorio.domain.report_data;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class ReportDataMicroserviceClient {

	private final RestTemplate restTemplate;
	private final HttpHeaders httpHeaders;
	
	ReportDataMicroserviceClient() {
		this.restTemplate = new RestTemplate();
		this.httpHeaders = new HttpHeaders();
	}
	
	ResponseEntity<Integer> getQueryAnalysis(String query) {
		Query query2 = new Query(query);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Query> request = new HttpEntity<>(query2, httpHeaders);

		return restTemplate.exchange(
				"http://localhost:3002/busca-time",
				HttpMethod.POST,
				request,
				int.class
				);
	}
	
	private record Query(String query) {}
}