package com.systextil.relatorio.controller;

import com.systextil.relatorio.cliente.ClienteRepository;
import com.systextil.relatorio.cliente.RepositoryImpl;
import com.systextil.relatorio.notaFiscal.NotaFiscalRepository;
import com.systextil.relatorio.service.Conversor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Object[]> retornaCliente() {

        //tratamento JSON
        String tabela = "cliente";
        List<String> colunas = new ArrayList<String>();
        colunas.add("cpf");
        colunas.add("nome");
        String stringColunas = Conversor.listToQuery(colunas);

        RepositoryImpl repository = new RepositoryImpl();
        List<Object[]> clientesEncontrados = null;
        try {
            clientesEncontrados = repository.findObjectByColumns(stringColunas, tabela);
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
