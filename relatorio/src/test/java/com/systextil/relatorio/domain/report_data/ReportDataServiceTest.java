package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.systextil.relatorio.domain.Totalizer;

class ReportDataServiceTest {

	@Test
	@DisplayName("joinColumnsAndTotalizersResult")
	void cenario1() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", "");
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		List<String> totalizersResults = List.of("100", "50000");
		
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("IDADE", Totalizer.SUM);
		totalizers.put("SALARIO", Totalizer.SUM);
		
		ReportData reportData = new ReportData(columnsNameAndNickName, null, totalizersResults);
		
		Method joinColumnsAndTotalizersResult = ReportDataService.class.getDeclaredMethod("joinColumnsAndTotalizersResult", ReportData.class, Map.class);
		joinColumnsAndTotalizersResult.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, String> columnsAndTotalizersResult = (Map<String, String>) joinColumnsAndTotalizersResult.invoke(ReportDataService.class, reportData, totalizers);
		
		Map<String, String> expectedColumnsAdnTotalizersResult = new LinkedHashMap<>();
		expectedColumnsAdnTotalizersResult.put("IDADE", "Soma: 100");
		expectedColumnsAdnTotalizersResult.put("PAGAMENTO_MENSAL", "Soma: 50000");
		
		assertEquals(expectedColumnsAdnTotalizersResult, columnsAndTotalizersResult);
	}

	@Test
	@DisplayName("toColumnsNameOrNickName")
	void cenario2() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", null);
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		Method toColumnsNameOrNickName = ReportDataService.class.getDeclaredMethod("toColumnsNameOrNickName", Map.class);
		toColumnsNameOrNickName.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		List<String> columnsNameOrNickName = (List<String>) toColumnsNameOrNickName.invoke(ReportDataService.class, columnsNameAndNickName);
		
		List<String> expectedColumnsNameOrNickName = new ArrayList<>();
		expectedColumnsNameOrNickName.add("NOME_CLIENTE");
		expectedColumnsNameOrNickName.add("IDADE");
		expectedColumnsNameOrNickName.add("PAGAMENTO_MENSAL");
		
		assertEquals(expectedColumnsNameOrNickName, columnsNameOrNickName);
	}
	
}