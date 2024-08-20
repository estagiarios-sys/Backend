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
        colunas.add("valor");

        //String where = "";
        String where = "data_nascimento > '2003-01-01'";

        //String orderBy = "";
        String orderBy = "nome";

        //String join = "";
        String join = " nota_fiscal ON cliente.cpf = nota_fiscal.cpf_cliente";

        String sql = Conversor.finalQuery(colunas, tabela, where, orderBy, join);

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
