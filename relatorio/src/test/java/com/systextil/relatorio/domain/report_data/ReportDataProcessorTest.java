package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static com.systextil.relatorio.domain.report_data.ReportDataProcessor.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.systextil.relatorio.domain.Totalizer;

class ReportDataProcessorTest {

	@Test
	@DisplayName("joinColumnsAndTotalizersResult")
	void cenario1() {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", "");
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		List<String> totalizersResults = List.of("100", "50000");
		
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("IDADE", Totalizer.SUM);
		totalizers.put("SALARIO", Totalizer.SUM);
		
		ReportData reportData = new ReportData(columnsNameAndNickName, null, totalizersResults);
		
		Map<String, String> columnsAndTotalizersResult = joinColumnsAndTotalizersResult(reportData, totalizers);
		
		Map<String, String> expectedColumnsAdnTotalizersResult = new LinkedHashMap<>();
		expectedColumnsAdnTotalizersResult.put("IDADE", "Soma: 100");
		expectedColumnsAdnTotalizersResult.put("PAGAMENTO_MENSAL", "Soma: 50000");
		
		assertEquals(expectedColumnsAdnTotalizersResult, columnsAndTotalizersResult);
	}

	@Test
	@DisplayName("toColumnsNameOrNickName")
	void cenario2() {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", null);
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		List<String> columnsNameOrNickName = toColumnsNameOrNickName(columnsNameAndNickName);
		
		List<String> expectedColumnsNameOrNickName = new ArrayList<>();
		expectedColumnsNameOrNickName.add("NOME_CLIENTE");
		expectedColumnsNameOrNickName.add("IDADE");
		expectedColumnsNameOrNickName.add("PAGAMENTO_MENSAL");
		
		assertEquals(expectedColumnsNameOrNickName, columnsNameOrNickName);
	}
}