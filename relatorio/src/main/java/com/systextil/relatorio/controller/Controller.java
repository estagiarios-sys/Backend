package com.systextil.relatorio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.cliente.ClienteRepository;
import com.systextil.relatorio.object.RepositoryImpl;
import com.systextil.relatorio.notaFiscal.NotaFiscalRepository;
import com.systextil.relatorio.service.Conversor;
import com.systextil.relatorio.service.ConvertJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("procurar")
public class Controller {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @GetMapping
    public List<Object[]> retornaCliente() throws IOException {

        ConvertJson convertJson = new ConvertJson();

        String sql = Conversor.finalQuery(convertJson.getColunas(), convertJson.getTabela(), convertJson.getWhere(), convertJson.getOrderBy(), convertJson.getJoin());

        RepositoryImpl repository = new RepositoryImpl();
        List<Object[]> clientesEncontrados = null;
        try {
            clientesEncontrados = repository.findObjectByColumns(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clientesEncontrados;
    }

    @GetMapping("tabela")
    public Map<String, String[]> getTablesAndColumns() throws Exception {
        RepositoryImpl repository = new RepositoryImpl();
        return repository.getTablesAndColumns();
    }
}
