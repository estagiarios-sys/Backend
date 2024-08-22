package com.systextil.relatorio.controller;

import com.systextil.relatorio.entity.TableData;
import com.systextil.relatorio.repositories.ConsultaSalvaRepository;
import com.systextil.relatorio.repositories.RepositoryImpl;
import com.systextil.relatorio.service.SQLGenerator;
import com.systextil.relatorio.service.ConvertJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private ConsultaSalvaRepository consultaSalvaRepository;

    @GetMapping("find")
    public Object[] queryReturn(@RequestBody String json) throws IOException {

        ConvertJson convertJson = new ConvertJson();

        TableData tabela = convertJson.jsonTable(json);

        String sql = SQLGenerator.finalQuery(tabela.getName(), tabela.getColumns(), tabela.getConditions(), tabela.getOrderBy(), tabela.getJoin());

        RepositoryImpl repository = new RepositoryImpl();
        List<Object[]> objectsFind = null;
        try {
            objectsFind = repository.findObjectsByQuery(sql);
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath.toString());
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath.toString());
        }
    }

    @PostMapping("save")
    public void saveSQL(@RequestBody String json) throws IOException {

        ConvertJson convertJson = new ConvertJson();

        consultaSalvaRepository.save(convertJson.jsonConsultaSalva(json));
    }

//    @GetMapping("tabela")
//    public Map<String, String[]> gettTablesAndColumns() throws Exception {
//        RepositoryImpl repository = new RepositoryImpl();
//        return repository.getTablesAndColumns();
//    }
//
//    @GetMapping("relacionamento")
//    public ArrayList<Object> gettRelationship() throws Exception {
//        RepositoryImpl repository = new RepositoryImpl();
//        return repository.getRelationship();
//    }
}
