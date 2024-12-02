package com.systextil.relatorio.domain.relationship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.systextil.relatorio.domain.RelationshipData;

@SpringBootTest
class MultiJoinReducerTest {

	@Autowired
	private MultiJoinReducer multiJoinReducer;
	
	@Test
	@DisplayName("findDuplicates")
	void cenario1() {
		List<String> tablesPairs = List.of("TABELA1 e TABELA2", "TABELA 3 e TABELA4", "TABELA1 e TABELA2", "TABELA5 e TABELA6");
		
		List<String> duplicates = multiJoinReducer.findDuplicates(tablesPairs);
		
		List<String> expectedDuplicates = List.of("TABELA1 e TABELA2");
		
		assertEquals(expectedDuplicates, duplicates);
	}
	
	@Test
	@DisplayName("cutDuplicates")
	void cenario2() {
		List<String> duplicates = List.of("TABELA1 e TABELA2");
		List<RelationshipData> relationships = new ArrayList<>();
		relationships.add(new RelationshipData("TABELA3 e TABELA4", "INNER JOIN TABELA4 ON COLUNA1 = COLUNA2"));
		relationships.add(new RelationshipData("TABELA1 e TABELA2", "INNER JOIN TABELA4 ON COLUNA1 = COLUNA2"));
		relationships.add(new RelationshipData("TABELA1 e TABELA2", "INNER JOIN TABELA4 ON COLUNA3 = COLUNA4"));
		
		relationships = multiJoinReducer.cutDuplicates(duplicates, relationships);
		
		List<RelationshipData> expectedRelationships = new ArrayList<>();
		expectedRelationships.add(new RelationshipData("TABELA3 e TABELA4", "INNER JOIN TABELA4 ON COLUNA1 = COLUNA2"));
		expectedRelationships.add(new RelationshipData("TABELA1 e TABELA2", "INNER JOIN TABELA4 ON COLUNA1 = COLUNA2 AND COLUNA3 = COLUNA4"));
		
		assertEquals(expectedRelationships, relationships);
	}
}