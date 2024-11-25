package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.domain.Totalizer;
import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@SpringBootTest
@TestPropertySource(properties = "database.type=sqlserver")
class ReportDataServiceTest {

	private static final Class<ReportDataService> SERVICE_CLASS = ReportDataService.class;

	@Autowired
	private ReportDataService service;
	
	@MockitoBean
	private QueryData queryData;
	
	@MockitoBean
	Map<String, Totalizer> mockTotalizers;
	
	@AfterAll
	static void tearDownAll() {
		Mockito.clearAllCaches();
	}

	@Test
	@DisplayName("getQueryReturn")
	void cenario1() {
		mockStatic(SqlGenerator.class);
		when(SqlGenerator.generateFinalQuery(anyString(), anyList(), anyList(), anyString(), anyList())).thenReturn("");
		
		when(queryData.totalizers()).thenReturn(mockTotalizers);
		when(queryData.totalizers().isEmpty()).thenReturn(true);
		
		assertThrows(IllegalDataBaseTypeException.class, () -> service.getQueryReturn(queryData));
	}
	
	@Test
	@DisplayName("joinColumnsAndTotalizersResult")
	void cenario3() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", "");
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		List<String> totalizersResults = List.of("100", "50000");
		
		Map<String, Totalizer> totalizers = new LinkedHashMap<>();
		totalizers.put("IDADE", Totalizer.SUM);
		totalizers.put("SALARIO", Totalizer.SUM);
		
		ReportData reportData = new ReportData(columnsNameAndNickName, null, totalizersResults);
		
		Method joinColumnsAndTotalizersResult = SERVICE_CLASS.getDeclaredMethod("joinColumnsAndTotalizersResult", ReportData.class, Map.class);
		joinColumnsAndTotalizersResult.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, String> columnsAndTotalizersResult = (Map<String, String>) joinColumnsAndTotalizersResult.invoke(service, reportData, totalizers);
		
		Map<String, String> expectedColumnsAdnTotalizersResult = new LinkedHashMap<>();
		expectedColumnsAdnTotalizersResult.put("IDADE", "Soma: 100");
		expectedColumnsAdnTotalizersResult.put("PAGAMENTO_MENSAL", "Soma: 50000");
		
		assertEquals(expectedColumnsAdnTotalizersResult, columnsAndTotalizersResult);
	}

	@Test
	@DisplayName("toColumnsNameOrNickName")
	void cenario4() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
		columnsNameAndNickName.put("NOME", "NOME_CLIENTE");
		columnsNameAndNickName.put("IDADE", null);
		columnsNameAndNickName.put("SALARIO", "PAGAMENTO_MENSAL");
		
		Method toColumnsNameOrNickName = SERVICE_CLASS.getDeclaredMethod("toColumnsNameOrNickName", Map.class);
		toColumnsNameOrNickName.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		List<String> columnsNameOrNickName = (List<String>) toColumnsNameOrNickName.invoke(service, columnsNameAndNickName);
		
		List<String> expectedColumnsNameOrNickName = new ArrayList<>();
		expectedColumnsNameOrNickName.add("NOME_CLIENTE");
		expectedColumnsNameOrNickName.add("IDADE");
		expectedColumnsNameOrNickName.add("PAGAMENTO_MENSAL");
		
		assertEquals(expectedColumnsNameOrNickName, columnsNameOrNickName);
	}
	
	@Test
	@DisplayName("joinColumnsNameAndNickName")
	void cenario5() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		QueryDataColumn column1 = new QueryDataColumn("NOME", "NOME_CLIENTE");
		QueryDataColumn column2 = new QueryDataColumn("IDADE", null);
		QueryDataColumn column3 = new QueryDataColumn("SALARIO", "PAGAMENTO_MENSAL");
		List<QueryDataColumn> columns = List.of(column1, column2, column3);
		
		Method joinColumnsNameAndNickName = SERVICE_CLASS.getDeclaredMethod("joinColumnsNameAndNickName", List.class);
		joinColumnsNameAndNickName.setAccessible(true);
				
		@SuppressWarnings("unchecked")
		List<String> joinedColumnsNameAndNickName = (List<String>) joinColumnsNameAndNickName.invoke(service, columns);
		
		List<String> expectedJoinedColumnsNameAndNickName = new ArrayList<>();
		expectedJoinedColumnsNameAndNickName.add("NOME AS \"NOME_CLIENTE\"");
		expectedJoinedColumnsNameAndNickName.add("IDADE");
		expectedJoinedColumnsNameAndNickName.add("SALARIO AS \"PAGAMENTO_MENSAL\"");
		
		assertEquals(expectedJoinedColumnsNameAndNickName, joinedColumnsNameAndNickName);
	}
}