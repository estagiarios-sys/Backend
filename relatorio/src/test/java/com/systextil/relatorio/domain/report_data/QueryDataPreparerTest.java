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
	@DisplayName("findJoinsByTablesPairs")
	void cenario1() throws IOException {
		List<RelationshipData> relationshipsData = new ArrayList<>();
		RelationshipData relationshipData1 = new RelationshipData("BASI_010 e BASI_020", "INNER JOIN BASI_020 ON BASI_010 = BASI_020");
		RelationshipData relationshipData2 = new RelationshipData("BASI_020 e BASI_010", "INNER JOIN BASI_010 ON BASI_020 = BASI_010");
		RelationshipData relationshipData3 = new RelationshipData("BASI_010 e BASI_050", "INNER JOIN BASI_050 ON BASI_010 = BASI_050");
		RelationshipData relationshipData4 = new RelationshipData("BASI_050 e BASI_010", "INNER JOIN BASI_010 ON BASI_050 = BASI_010");
		RelationshipData relationshipData5 = new RelationshipData("BASI_020 e BASI_060", "INNER JOIN BASI_060 ON BASI_020 = BASI_060");
		RelationshipData relationshipData6 = new RelationshipData("BASI_060 e BASI_020", "INNER JOIN BASI_020 ON BASI_060 = BASI_020");
		RelationshipData relationshipData7 = new RelationshipData("BASI_010 e BASI_060", "INNER JOIN BASI_060 ON BASI_010 = BASI_060");
		RelationshipData relationshipData8 = new RelationshipData("BASI_060 e BASI_010", "INNER JOIN BASI_010 ON BASI_060 = BASI_010");
		
		relationshipsData.add(relationshipData1);
		relationshipsData.add(relationshipData2);
		relationshipsData.add(relationshipData3);
		relationshipsData.add(relationshipData4);
		relationshipsData.add(relationshipData5);
		relationshipsData.add(relationshipData6);
		relationshipsData.add(relationshipData7);
		relationshipsData.add(relationshipData8);
		
		when(storageAccessor.findRelationshipData()).thenReturn(relationshipsData);
		
		List<String> joins = queryDataPreparer.findJoinsByTablesPairs("BASI_010", List.of("BASI_010 e BASI_020", "BASI_010 e BASI_050", "BASI_020 e BASI_060"));
		
		List<String> expectedJoins = List.of("INNER JOIN BASI_020 ON BASI_010 = BASI_020", "INNER JOIN BASI_050 ON BASI_010 = BASI_050", "INNER JOIN BASI_060 ON BASI_020 = BASI_060 AND BASI_060 = BASI_010");
		
		assertEquals(expectedJoins, joins);
	}
	
	@Test
	@DisplayName("joinColumnsNameAndNickName")
	void cenario2() {
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