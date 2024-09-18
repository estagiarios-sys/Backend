package com.systextil.relatorio.domain.savedQuery;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.systextil.relatorio.infra.exceptionHandler.TotalizerFieldsMismatchException;

import jakarta.validation.constraints.NotBlank;

record SavedQuerySaving(
	@NotBlank
	String queryName,
	@NotBlank
	String finalQuery,
	String totalizersQuery,
	byte[] imgPDF,
	String titlePDF,
	ArrayList<TotalizerSaving> totalizers
) {

	private void validateTotalizerFields() {
		if (this.totalizersQuery != null && this.totalizers == null) {
			throw new TotalizerFieldsMismatchException("totalizersQuery deve ser fornecido com totalizers");
		} else if (this.totalizersQuery == null && this.totalizers != null) {
			throw new TotalizerFieldsMismatchException("totalizers deve ser fornecido com totalizersQuery");
		}
	}
	
	@JsonCreator
	SavedQuerySaving(
			@JsonProperty("queryName") String queryName,
			@JsonProperty("finalQuery") String finalQuery,
            @JsonProperty("totalizersQuery") String totalizersQuery,
			@JsonProperty("imgPDF") byte[] imgPDF,
            @JsonProperty("titlePDF") String titlePDF,
            @JsonProperty("totalizers") ArrayList<TotalizerSaving> totalizers) {
		this.queryName = queryName;
		this.finalQuery = finalQuery;
		this.totalizersQuery = totalizersQuery;
		this.totalizers = totalizers;
		this.imgPDF = imgPDF;
		this.titlePDF = titlePDF;
		validateTotalizerFields();
	}
}
