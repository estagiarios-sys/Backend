package com.systextil.relatorio.domain.saved_query;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class OneSavedQueryListingTest {

	@Test
	@DisplayName("equals: Mesma instância")
	void cenario1() {
		OneSavedQueryListing oneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		
		boolean equals = oneSavedQueryListing.equals(oneSavedQueryListing);
		
		assertTrue(equals);
	}

	@Test
	@DisplayName("equals: Parâmetro nulo")
	void cenario2() {
		OneSavedQueryListing oneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		OneSavedQueryListing comparedOneSavedQueryListing = null;
		
		boolean equals = oneSavedQueryListing.equals(comparedOneSavedQueryListing);
		
		assertFalse(equals);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	@DisplayName("equals: Classe diferente")
	void cenario3() {
		OneSavedQueryListing oneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		Object[] comparedOneSavedQueryListing = {"", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of()};
		
		boolean equals = oneSavedQueryListing.equals(comparedOneSavedQueryListing);
		
		assertFalse(equals);
	}
	
	@Test
	@DisplayName("equals: Instâncias diferentes e conteúdo igual")
	void cenario4() {
		OneSavedQueryListing oneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		OneSavedQueryListing comparedOneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		
		boolean equals = oneSavedQueryListing.equals(comparedOneSavedQueryListing);
		
		assertTrue(equals);
	}
	
	@Test
	@DisplayName("equals: Instâncias diferentes e conteúdo diferente")
	void cenario5() {
		OneSavedQueryListing oneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {1, 2, 3}, List.of(), List.of(), List.of(), Map.of());
		OneSavedQueryListing comparedOneSavedQueryListing = new OneSavedQueryListing("", "", "", new byte[] {3, 2, 1}, List.of(), List.of(), List.of(), Map.of());
		
		boolean equals = oneSavedQueryListing.equals(comparedOneSavedQueryListing);
		
		assertFalse(equals);
	}
}