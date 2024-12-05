package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;

import static com.systextil.relatorio.domain.report_data.SqlWithDateConverter.*;

import org.junit.jupiter.api.Test;

class SqlWithDateConverterTest {

	@Test
	@DisplayName("toSqlWithDdMMyyyy: SQL tem data no formato yyyy-MM-dd")
	void cenario1() throws ParseException {
		String sqlWithYyyyMMdd ="SELECT COLUNA_DATA FROM TABELA WHERE COLUNA_DATA > '2001-01-01' AND COLUNA_DATA < '3001-01-01'";
		
		String sqlWithDdMMyyyy = toSqlWithDdMMyyyy(sqlWithYyyyMMdd);
		
		String expectedSqlWithDdMMyyyy = "SELECT COLUNA_DATA FROM TABELA WHERE COLUNA_DATA > '01-01-2001' AND COLUNA_DATA < '01-01-3001'";
		
		assertEquals(expectedSqlWithDdMMyyyy, sqlWithDdMMyyyy);
	}
	
	@Test
	@DisplayName("toSqlWithDdMMyyyy: SQL não tem data no formato yyyy-MM-dd")
	void cenario2() throws ParseException {
		String sqlWithoutYyyyMMdd ="SELECT COLUNA_DATA FROM TABELA WHERE COLUNA_DATA > '01-01-2001' AND COLUNA_DATA < '01-01-3001'";
		
		String sqlWithDdMMyyyy = toSqlWithDdMMyyyy(sqlWithoutYyyyMMdd);
		
		assertEquals(sqlWithoutYyyyMMdd, sqlWithDdMMyyyy);
	}
}