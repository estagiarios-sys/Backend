package com.systextil.relatorio.domain.saved_query;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SavedQueryRepositoryTest {
	
	@Autowired
	private SavedQueryRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;

	@BeforeEach
	void setUp() {
		SavedQuery oldestSavedQuery = new SavedQuery(new SavedQuerySaving("", "", "", "", List.of(), List.of(), List.of(), Map.of()), new byte[] {1, 1, 1});
		SavedQuery middleSavedQuery = new SavedQuery(new SavedQuerySaving("", "", "", "", List.of(), List.of(), List.of(), Map.of()), new byte[] {2, 2, 2});
		SavedQuery newestSavedQuery = new SavedQuery(new SavedQuerySaving("", "", "", "", List.of(), List.of(), List.of(), Map.of()), new byte[] {3, 3, 3});
		
		entityManager.persist(oldestSavedQuery);
		entityManager.persist(middleSavedQuery);
		entityManager.persist(newestSavedQuery);
	}
	
	@Test
	@DisplayName("findAllForListing")
	void cenario1() {
		List<AllSavedQueriesListing> allAllSavedQueriesListing = repository.findAllForListing();
		
		AllSavedQueriesListing firstExpectedAllSavedQueriesListing = new AllSavedQueriesListing(1L, "");
		AllSavedQueriesListing secondExpectedAllSavedQueriesListing = new AllSavedQueriesListing(2L, "");
		AllSavedQueriesListing thirdExpectedAllSavedQueriesListing = new AllSavedQueriesListing(3L, "");
		
		List<AllSavedQueriesListing> expectedAllAllSavedQueriesListing = List.of(firstExpectedAllSavedQueriesListing, secondExpectedAllSavedQueriesListing, thirdExpectedAllSavedQueriesListing);
		
		assertEquals(expectedAllAllSavedQueriesListing, allAllSavedQueriesListing);
	}
}