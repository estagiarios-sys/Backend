package com.systextil.relatorio.domain.report_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Nested;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.systextil.relatorio.infra.exception_handler.IllegalDataBaseTypeException;

@SpringBootTest
class ReportDataServiceTest {

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestPropertySource(properties = "database.type=sqlserver")
	@DisplayName("database.type=sqlserver")
	class nest1 {
		
		@Autowired
		private ReportDataService service;
		
		@MockitoBean
		private ReportDataOracleRepository oracleRepository;
		
		@MockitoBean
		private ReportDataMysqlRepository mysqlRepository;
		
		@MockitoBean
		private ReportDataMicroserviceClient microserviceClient;
		
		@MockitoBean
		private ReportDataProcessor reportDataProcessor;
		
		@MockitoBean
		private QueryDataPreparer queryDataPreparer;
		
		@BeforeAll
		void setUpAll() {
			mockStatic(SqlGenerator.class);
			mockStatic(SqlWithDateConverter.class);
		}
		
		@AfterAll
		void tearDownAll() {
			Mockito.clearAllCaches();
		}

		@Test
		@DisplayName("getQueryReturn")
		void cenario1() {
			QueryData mockQueryData = mock(QueryData.class);
			
			assertThrows(IllegalDataBaseTypeException.class, () -> service.getQueryReturn(mockQueryData));
		}
		
		@Test
		@DisplayName("getQueryAnalysis")
		void cenario2() {
			QueryData mockQueryData = mock(QueryData.class);

			assertThrows(IllegalDataBaseTypeException.class, () -> service.getQueryAnalysis(mockQueryData));
			verify(microserviceClient, never()).getQueryAnalysis(any());
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestPropertySource(properties = "database.type=oracle")
	@DisplayName("database.type=oracle")
	class nest2 {
		
		@Autowired
		private ReportDataService service;
		
		@MockitoBean
		private ReportDataOracleRepository oracleRepository;
		
		@MockitoBean
		private ReportDataMysqlRepository mysqlRepository;
		
		@MockitoBean
		private ReportDataMicroserviceClient microserviceClient;
		
		@MockitoBean
		private ReportDataProcessor reportDataProcessor;
		
		@MockitoBean
		private QueryDataPreparer queryDataPreparer;
		
		private MockedStatic<SqlWithDateConverter> mockedStaticSqlWithDateConverter;
		
		@BeforeAll
		void setUpAll() {
			mockStatic(SqlGenerator.class);
			mockedStaticSqlWithDateConverter = mockStatic(SqlWithDateConverter.class);
		}
		
		@AfterEach
		void tearDown() {
			mockedStaticSqlWithDateConverter.clearInvocations();
		}
		
		@AfterAll
		void tearDownAll() {
			Mockito.clearAllCaches();
		}

		@Test
		@DisplayName("getQueryReturn")
		void cenario1() throws ParseException, IOException, SQLException {
			ReportData mockReportData = mock(ReportData.class);
			when(oracleRepository.findDataByFinalQuery(any())).thenReturn(mockReportData);
			when(mockReportData.updateData(any(), any())).thenReturn(mockReportData);
			
			QueryData mockQueryData = mock(QueryData.class);
			service.getQueryReturn(mockQueryData);
			
			mockedStaticSqlWithDateConverter.verify(() -> SqlWithDateConverter.toSqlWithDdMMyyyy(any()));
			verify(oracleRepository).findDataByFinalQuery(any());
		}
		
		@Test
		@DisplayName("getQueryAnalysis")
		void cenario2() throws IOException, ParseException {
			ResponseEntity<Integer> response = new ResponseEntity<Integer>(1, HttpStatus.OK);
			when(microserviceClient.getQueryAnalysis(any())).thenReturn(response);
			
			QueryData mockQueryData = mock(QueryData.class);
			service.getQueryAnalysis(mockQueryData);
			
			mockedStaticSqlWithDateConverter.verify(() -> SqlWithDateConverter.toSqlWithDdMMyyyy(any()));
			verify(microserviceClient).getQueryAnalysis(any());
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestPropertySource(properties = "database.type=mysql")
	@DisplayName("database.type=mysql")
	class nest3 {
		
		@Autowired
		private ReportDataService service;
		
		@MockitoBean
		private ReportDataOracleRepository oracleRepository;
		
		@MockitoBean
		private ReportDataMysqlRepository mysqlRepository;
		
		@MockitoBean
		private ReportDataMicroserviceClient microserviceClient;
		
		@MockitoBean
		private ReportDataProcessor reportDataProcessor;
		
		@MockitoBean
		private QueryDataPreparer queryDataPreparer;
		
		private MockedStatic<SqlWithDateConverter> mockedStaticSqlWithDateConverter;
		
		@BeforeAll
		void setUpAll() {
			mockStatic(SqlGenerator.class);
			mockedStaticSqlWithDateConverter = mockStatic(SqlWithDateConverter.class);
		}
		
		@AfterEach
		void tearDown() {
			mockedStaticSqlWithDateConverter.clearInvocations();
		}
		
		@AfterAll
		void tearDownAll() {
			Mockito.clearAllCaches();
		}

		@Test
		@DisplayName("getQueryReturn")
		void cenario1() throws ParseException, IOException, SQLException {
			ReportData mockReportData = mock(ReportData.class);
			when(mysqlRepository.findDataByFinalQuery(any())).thenReturn(mockReportData);
			when(mockReportData.updateData(any(), any())).thenReturn(mockReportData);
			
			QueryData mockQueryData = mock(QueryData.class);
			service.getQueryReturn(mockQueryData);
			
			mockedStaticSqlWithDateConverter.verify(() -> SqlWithDateConverter.toSqlWithDdMMyyyy(any()), never());
			verify(mysqlRepository).findDataByFinalQuery(any());
		}
		
		@Test
		@DisplayName("getQueryAnalysis")
		void cenario2() {
			QueryData mockQueryData = mock(QueryData.class);
			
			assertThrows(UnsupportedOperationException.class, () -> service.getQueryAnalysis(mockQueryData));
		}
	}
}