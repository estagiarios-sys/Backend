package com.systextil.relatorio.controller;

import com.systextil.relatorio.entity.Tabela;
import com.systextil.relatorio.object.RepositoryImpl;
import com.systextil.relatorio.service.SQLGenerator;
import com.systextil.relatorio.service.ConvertJson;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
@RequestMapping("procurar")
public class Controller {

    @GetMapping
    public List<Object[]> queryReturn(@RequestBody String json) throws IOException {

        ConvertJson convertJson = new ConvertJson();

        Tabela tabela = convertJson.convertJson(json);

        String sql = SQLGenerator.finalQuery(tabela.getNome(), tabela.getColunas(), tabela.getCondicoes(), tabela.getOrderBy(), tabela.getJoin());

        RepositoryImpl repository = new RepositoryImpl();
        List<Object[]> clientesEncontrados = null;
        try {
            clientesEncontrados = repository.findObjectsByQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clientesEncontrados;
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath.toString());
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
            throw new RuntimeException("Arquivo não encontrado ou não legível: " + filePath.toString());
        }
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
