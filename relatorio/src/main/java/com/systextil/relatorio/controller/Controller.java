package com.systextil.relatorio.controller;

import com.systextil.relatorio.cliente.ClienteRepository;
import com.systextil.relatorio.notaFiscal.NotaFiscalRepository;
import com.systextil.relatorio.service.Conversor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Object[] retornaCliente() {

        List<String> colunas = new ArrayList<String>();
        colunas.add("cpf");
        colunas.add("nome");
        String query = Conversor.listToQuery(colunas);
        System.out.println(query);
        return clienteRepository.findClientes(query);
    }

}
