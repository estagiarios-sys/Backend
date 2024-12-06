package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.domain.RelationshipData;

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
	
	@Test
	@DisplayName("findJoinsByTablesPairs")
	void cenario2() throws IOException {
		List<RelationshipData> relationshipsData = new ArrayList<>();
		RelationshipData relationshipData = new RelationshipData("10 e 20", "ON 10 = 20");
		RelationshipData relationshipData1 = new RelationshipData("10 e 50", "ON 10 = 50");
		RelationshipData relationshipData2 = new RelationshipData("20 e 60", "ON 20 = 60");
		RelationshipData relationshipData3 = new RelationshipData("60 e 10", "ON 10 = 60");
		
		relationshipsData.add(relationshipData);
		relationshipsData.add(relationshipData1);
		relationshipsData.add(relationshipData2);
		relationshipsData.add(relationshipData3);
		
		when(storageAccessor.findRelationshipData()).thenReturn(relationshipsData);
		
		List<String> joins = queryDataPreparer.findJoinsByTablesPairs("10", List.of("10 e 20", "10 e 50", "20 e 60"));
		
		List<String> expectedJoins = List.of("ON 10 = 20", "ON 10 = 50", "ON 20 = 60 AND 10 = 60");
		
		assertEquals(expectedJoins, joins);
	}
}