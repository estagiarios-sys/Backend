package com.systextil.relatorio.controller;

import com.systextil.relatorio.record.ListagemSavedQuery;
import com.systextil.relatorio.entity.SavedQuery;
import com.systextil.relatorio.record.TableData;
import com.systextil.relatorio.repositories.SavedQueryRepository;
import com.systextil.relatorio.repositories.MainRepository;
import com.systextil.relatorio.service.SQLGenerator;
import com.systextil.relatorio.service.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class Controller {

    @Autowired
    private SavedQueryRepository savedQueryRepository;

    private MainRepository mainRepository;

    @GetMapping("find")
    public Object[] queryReturn(@RequestBody String json) throws RuntimeException {
        JsonConverter convertJson = new JsonConverter();
        mainRepository = new MainRepository();
        TableData tabela = convertJson.jsonTable(json);
        String sql = SQLGenerator.finalQuery(tabela.name(), tabela.columns(), tabela.conditions(), tabela.orderBy(), tabela.joins());
        List<Object[]> objectsFind;

        try {
            objectsFind = mainRepository.findObjectsByQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new Object[]{sql, objectsFind};
    }

    @GetMapping("find/table")
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

    @GetMapping("find/relationship")
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

    @GetMapping("find/table/{tableName}")
    public ResponseEntity<Object> getTableData(
            @PathVariable String tableName,
            @RequestParam(required = false) List<String> columns,
            @RequestParam(required = false) List<String> conditions,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) List<String> joins
    ) {
        mainRepository = new MainRepository();

        try {
            ArrayList<String> colList = columns != null && !columns.isEmpty() ? new ArrayList<>(columns) : new ArrayList<>(List.of("*"));
            ArrayList<String> condList = conditions != null ? new ArrayList<>(conditions) : new ArrayList<>();
            ArrayList<String> joinList = joins != null ? new ArrayList<>(joins) : new ArrayList<>();
            String order = orderBy != null ? orderBy : "";
            String sqlQuery = SQLGenerator.finalQuery(tableName, colList, condList, order, joinList);
            List<Object[]> tableData = mainRepository.findObjectsByQuery(sqlQuery);

            return ResponseEntity.ok(tableData);
        } catch (SQLException | ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar os dados: " + e.getMessage());
        }
    }

    @GetMapping("find/saved-query")
    public List<ListagemSavedQuery> getSQL() {
        List<SavedQuery> queriesList = savedQueryRepository.findAll();

        return queriesList.stream()
                .map(ListagemSavedQuery::new)
                .toList();
    }

    @PostMapping("save")
    public ResponseEntity<Void> saveSQL(@RequestBody String json) {
        JsonConverter convertJson = new JsonConverter();
        savedQueryRepository.save(convertJson.jsonSavedQuery(json));

        return ResponseEntity.created(null).build();
    }

    /** Método privado que será usado periodicamente */
    private Map<String, String[]> getTablesAndColumnsFromDatabase() throws Exception {
        mainRepository = new MainRepository();

        return mainRepository.getTablesAndColumns();
    }

    /** Método privado que será usado periodicamente */
    private ArrayList<Object> getRelationshipsFromDatabase() throws SQLException, ClassNotFoundException {
        mainRepository = new MainRepository();

        return mainRepository.getRelationship();
    }
}
