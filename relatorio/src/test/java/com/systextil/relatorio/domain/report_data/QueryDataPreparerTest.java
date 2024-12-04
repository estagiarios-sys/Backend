package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class QueryDataPreparerTest {

	@Autowired
	private QueryDataPreparer queryDataPreparer;
	
	@MockitoBean
	private ReportDataStorageAccessor storageAccessor;
	
	@Test
	@DisplayName("joinColumnsNameAndNickName")
	void cenario1() {
		QueryDataColumn column1 = new QueryDataColumn("NOME", "NOME_CLIENTE");
		QueryDataColumn column2 = new QueryDataColumn("IDADE", null);
		QueryDataColumn column3 = new QueryDataColumn("SALARIO", "PAGAMENTO_MENSAL");
		List<QueryDataColumn> columns = List.of(column1, column2, column3);
				
		List<String> joinedColumnsNameAndNickName = queryDataPreparer.joinColumnsNameAndNickName(columns);
		
		List<String> expectedJoinedColumnsNameAndNickName = new ArrayList<>();
		expectedJoinedColumnsNameAndNickName.add("NOME AS \"NOME_CLIENTE\"");
		expectedJoinedColumnsNameAndNickName.add("IDADE");
		expectedJoinedColumnsNameAndNickName.add("SALARIO AS \"PAGAMENTO_MENSAL\"");
		
		assertEquals(expectedJoinedColumnsNameAndNickName, joinedColumnsNameAndNickName);
	}
}