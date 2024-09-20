package com.systextil.relatorio.domain.dataBaseData;

/** Record usado no DataBaseDataController.loadQuery() para retornar um LoadedQueryData */
public record ToLoadQueryData(
		String finalQuery,
		QueryWithTotalizers queryWithTotalizers,
		byte[] imgPDF,  // Representa a imagem em formato de byte array
		String titlePDF // Representa o título do PDF
) {}
