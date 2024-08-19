package com.systextil.relatorio.controller;

import com.systextil.relatorio.cliente.Cliente;
import com.systextil.relatorio.cliente.ClienteRepository;
import com.systextil.relatorio.cliente.ClienteRepositoryImpl;
import com.systextil.relatorio.notaFiscal.NotaFiscalRepository;
import com.systextil.relatorio.service.Conversor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("procurar")
public class Controller {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @GetMapping
    public List<Object[]> retornaCliente() {
        ClienteRepositoryImpl repository = new ClienteRepositoryImpl();
        List<String> colunas = new ArrayList<String>();
        colunas.add("cpf");
        colunas.add("nome");
        colunas.add("data_nascimento");
        String stringColunas = Conversor.listToQuery(colunas);
        List<Object[]> clientesEncontrados = null;
        try {
            clientesEncontrados = repository.findClientesByColumns(stringColunas);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clientesEncontrados;
    }

}
