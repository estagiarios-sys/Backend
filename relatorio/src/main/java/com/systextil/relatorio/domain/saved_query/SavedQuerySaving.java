package com.systextil.relatorio.domain.saved_query;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.systextil.relatorio.domain.ColumnAndTotalizer;
import com.systextil.relatorio.infra.exception_handler.TotalizerFieldsMismatchException;

import jakarta.validation.constraints.NotBlank;

record SavedQuerySaving(
	@NotBlank
	String queryName,
	@NotBlank
	String finalQuery,
	String totalizersQuery,
	String titlePDF,
	ArrayList<ColumnAndTotalizer> totalizers
) {

	private void validateTotalizerFields() {
		if (!this.totalizersQuery.isBlank() && this.totalizers.isEmpty()) {
			throw new TotalizerFieldsMismatchException("TotalizersQuery deve ser fornecido com totalizers");
		} else if (this.totalizersQuery.isBlank() && !this.totalizers.isEmpty()) {
			throw new TotalizerFieldsMismatchException("Totalizers deve ser fornecido com totalizersQuery");
		}
	}
	
	@JsonCreator
	SavedQuerySaving(
			@JsonProperty("queryName") String queryName,
			@JsonProperty("finalQuery") String finalQuery,
            @JsonProperty("totalizersQuery") String totalizersQuery,
            @JsonProperty("titlePDF") String titlePDF,
            @JsonProperty("totalizers") ArrayList<ColumnAndTotalizer> totalizers) {
		this.queryName = queryName;
		this.finalQuery = finalQuery;
		this.totalizersQuery = totalizersQuery == null ? "" : totalizersQuery;
		this.totalizers = totalizers == null ? new ArrayList<>() : totalizers;
		this.titlePDF = titlePDF;
		validateTotalizerFields();
	}
}
