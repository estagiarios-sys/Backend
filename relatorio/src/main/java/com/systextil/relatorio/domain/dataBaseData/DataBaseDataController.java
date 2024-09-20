package com.systextil.relatorio.domain.dataBaseData;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("find")
public class DataBaseDataController {

    private DataBaseDataRepository dataBaseDataRepository;
    
    @Value("${tables.json.file.path}")
	private String tablesJsonFilePath;
    
    @Value("${relationships.json.file.path}")
    private String relationshipsJsonFilePath;

    // 0 para oracle e 1 para MySQL
    private final int oracleMySQL = 0;
    private final String fileNotFoundMessage = "Arquivo não encontrado ou não legível: ";
    
    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws SQLException {
        String finalQuery = SQLGenerator.generateFinalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        QueryWithTotalizers queryWithTotalizers = null;

        if (!queryData.totalizers().isEmpty()) {

            queryWithTotalizers = SQLGenerator.generateTotalizersQuery(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        }

        // Se você ainda não tem imgPDF e titlePDF, pode passar null ou valores padrão
        byte[] imgPDF = null; // Exemplo de valor nulo ou carregar a imagem se necessário
        String titlePDF = "Título Padrão"; // Exemplo de título padrão, pode ser modificado conforme necessário

        // Agora criamos ToLoadQueryData com todos os parâmetros esperados
        ToLoadQueryData toLoadQueryData = new ToLoadQueryData(finalQuery, queryWithTotalizers, imgPDF, titlePDF);

        // Carregar os dados

        LoadedQueryData loadedQueryData = loadQuery(toLoadQueryData);
        Map<String, String> columnsNameAndNickName = loadedQueryData.columnsNameAndNickName();
        ArrayList<String> columnsNameOrNickName = new ArrayList<>();

        for (Map.Entry<String, String> columnNameAndNickName : columnsNameAndNickName.entrySet()) {
            if (columnNameAndNickName.getValue() != null) {
                columnsNameOrNickName.add(columnNameAndNickName.getValue());
            } else {
                columnsNameOrNickName.add(columnNameAndNickName.getKey());
            }
        }

        ArrayList<Object[]> foundObjects = loadedQueryData.foundObjects();

        // Processar totalizadores, se estiverem disponíveis
        if (loadedQueryData.totalizersResults() != null) {
            ArrayList<String> totalizersResults = loadedQueryData.totalizersResults();
            int totalizersResultsCounter = 0;
            Map<String, String> columnsAndTotalizers = new HashMap<>();

            for (Map.Entry<String, Totalizer> totalizer : queryData.totalizers().entrySet()) {
                String columnsAndTotalizersColumn = null;

                for (Map.Entry<String, String> columnNameAndNickName : columnsNameAndNickName.entrySet()) {
                    if (totalizer.getKey().equalsIgnoreCase(columnNameAndNickName.getKey())) {
                        if (columnNameAndNickName.getValue() != null) {
                            columnsAndTotalizersColumn = columnNameAndNickName.getValue();
                        } else {
                            columnsAndTotalizersColumn = columnNameAndNickName.getKey();
                        }
                    }
                }

                columnsAndTotalizers.put(columnsAndTotalizersColumn, totalizersResults.get(totalizersResultsCounter));
                totalizersResultsCounter++;
            }

            return new Object[]{finalQuery, queryWithTotalizers.query(), columnsNameOrNickName, foundObjects, columnsAndTotalizers};
        }

        // Retorno padrão quando não há totalizadores
        return new Object[]{finalQuery, "", columnsNameOrNickName, foundObjects, ""};
    }
    
    @PostMapping("analysis")
    public double getQueryAnalysis(@RequestBody @Valid QueryData queryData) throws SQLException {
    	dataBaseDataRepository = new DataBaseDataRepository();
    	int actualTime = 0;
    	
    	if (oracleMySQL == 1) {
    		String finalQueryAnalysis = SQLGenerator.generateFinalQueryAnalysisFromMySQLDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        	String totalizersQueryAnalysis = null;
        	
        	if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SQLGenerator.generateTotalizersQueryAnalysisFromMySQLDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
        	actualTime = dataBaseDataRepository.getActualTimeFromQueriesAnalysisFromMySQLDataBase(finalQueryAnalysis, totalizersQueryAnalysis);
    	} else {
    		String[] finalQueryAnaysis = SQLGenerator.generateFinalQueryAnalysisFromOracleDataBase(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
    		String[] totalizersQueryAnalysis = null;
    		
    		if (!queryData.totalizers().isEmpty()) {
        		totalizersQueryAnalysis = SQLGenerator.generateTotalizersQueryAnalysisFromOracleDataBase(queryData.totalizers(), queryData.table(), queryData.conditions(), queryData.joins());
        	}
    		actualTime = dataBaseDataRepository.getActualTimeFromQueriesAnalysisFromOracleDataBase(finalQueryAnaysis, totalizersQueryAnalysis);
    	}
    	
    	return actualTime;
    }


    @GetMapping("table")
    public ResponseEntity<Resource> getTablesAndColumns() throws IOException {
    	Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new RuntimeException(fileNotFoundMessage + filePath);
        }
    }

    @GetMapping("relationship")
    public ResponseEntity<Resource> getRelationships() throws IOException {
        Path filePath = Paths.get(relationshipsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new RuntimeException(fileNotFoundMessage + filePath);
        }
    }

    @PostMapping("loadedQuery")
    public LoadedQueryData loadQuery(@RequestBody ToLoadQueryData toLoadQueryData) throws SQLException {
        dataBaseDataRepository = new DataBaseDataRepository();

        LoadedQueryData loadedQueryData = null;
        
        if (oracleMySQL == 1) {
        	  LoadedQueryData loadedQueryData = dataBaseDataRepository.findDataByQueryFromMySQLDatabase(
                toLoadQueryData.finalQuery(),
                toLoadQueryData.queryWithTotalizers(),
                toLoadQueryData.imgPDF(),
                toLoadQueryData.titlePDF()
        );
        } else {
        	loadedQueryData = dataBaseDataRepository.findDataByQueryFromOracleDataBase(toLoadQueryData.finalQuery(), toLoadQueryData.queryWithTotalizers());
        }
                

        return loadedQueryData;
    }

    @PutMapping("update/table")
    public void setTablesAndColumnsFromDatabaseIntoJson() throws Exception {
        Path filePath = Paths.get(tablesJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	ObjectMapper objectMapper = new ObjectMapper();
        	FileWriter fileWriter = new FileWriter(resource.getFile());
          
        	Map<String, Map<String, String>> tablesAndColumns = null;
        	
            if (oracleMySQL == 1) {
            	tablesAndColumns = dataBaseDataRepository.getTablesAndColumnsFromMySQLDatabase();
            } else {
            	tablesAndColumns = dataBaseDataRepository.getTablesAndColumnsFromOracleDataBase();
            }
            String json = objectMapper.writeValueAsString(tablesAndColumns);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new RuntimeException(fileNotFoundMessage + filePath);
        }
    }

    @PutMapping("update/relationship")
    public void setRelationshipsFromDatabaseIntoJson() throws SQLException, ClassNotFoundException, IOException {
        Path filePath = Paths.get(relationshipsJsonFilePath);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.isReadable() && resource.exists()) {
        	dataBaseDataRepository = new DataBaseDataRepository();
        	ObjectMapper objectMapper = new ObjectMapper();
        	FileWriter fileWriter = new FileWriter(resource.getFile());
        	ArrayList<RelationshipData> relationships = null;
        	
        	if (oracleMySQL == 1) {
        		relationships = dataBaseDataRepository.getRelationshipsFromMySQLDataBase();
        	} else {
        		relationships = dataBaseDataRepository.getRelationshipsFromOracleDataBase();
        	}
        	String json = objectMapper.writeValueAsString(relationships);
        	fileWriter.write(json);
        	fileWriter.close();
        } else {
        	throw new RuntimeException(fileNotFoundMessage + filePath);
        }
    }
}