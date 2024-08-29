package com.systextil.relatorio.dataBaseData;

import com.systextil.relatorio.service.SQLGenerator;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("find")
public class DataBaseDataController {

    private DataBaseDataRepository dataBaseDataRepository;

    @PostMapping
    public Object[] getQueryReturn(@RequestBody @Valid QueryData queryData) throws RuntimeException {
        String sql = SQLGenerator.finalQuery(queryData.table(), queryData.columns(), queryData.conditions(), queryData.orderBy(), queryData.joins());
        List<Object[]> objectsFind;
        objectsFind = loadQuery(sql);

        return new Object[]{sql, objectsFind};
    }

    @GetMapping("table")
    public ResponseEntity<Resource> getTablesAndColumns() throws IOException {
        Path filePath = Paths.get("src/main/resources/listaBD.json");
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
        }
    }

    @GetMapping("relationship")
    public ResponseEntity<Resource> getRelationships() throws IOException {
        Path filePath = Paths.get("src/main/resources/relationships.json");
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resource);
        } else {
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath);
        }
    }

    @GetMapping("table/{tableName}")
    public ResponseEntity<Object> getTableData(
            @PathVariable String tableName,
            @RequestParam(required = false) List<String> columns,
            @RequestParam(required = false) String conditions,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) List<String> joins
    ) {
        dataBaseDataRepository = new DataBaseDataRepository();

        try {
            ArrayList<String> colList = columns != null && !columns.isEmpty() ? new ArrayList<>(columns) : new ArrayList<>(List.of("*"));
            String condList = conditions != null ? conditions : "";
            ArrayList<String> joinList = joins != null ? new ArrayList<>(joins) : new ArrayList<>();
            String order = orderBy != null ? orderBy : "";
            String sqlQuery = SQLGenerator.finalQuery(tableName, colList, condList, order, joinList);
            List<Object[]> tableData = dataBaseDataRepository.findDataByQuery(sqlQuery);

            return ResponseEntity.ok(tableData);
        } catch (SQLException | ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar os dados: " + e.getMessage());
        }
    }
    
    @PostMapping("loadedQuery")
    public List<Object[]> loadQuery(@RequestBody String sql) throws RuntimeException {
        dataBaseDataRepository = new DataBaseDataRepository();
        List<Object[]> objectsFind;

        try {
            objectsFind = dataBaseDataRepository.findDataByQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return objectsFind;
    }

    /** Método privado que será usado periodicamente */
    @SuppressWarnings("unused")
    private Map<String, String[]> getTablesAndColumnsFromDatabase() throws Exception {
        dataBaseDataRepository = new DataBaseDataRepository();

        return dataBaseDataRepository.getTablesAndColumns();
    }

    /** Método privado que será usado periodicamente */
    @SuppressWarnings("unused")
    private ArrayList<Object> getRelationshipsFromDatabase() throws SQLException, ClassNotFoundException {
        dataBaseDataRepository = new DataBaseDataRepository();

        return dataBaseDataRepository.getRelationships();
    }
}
