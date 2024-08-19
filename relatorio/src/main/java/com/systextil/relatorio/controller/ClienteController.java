package com.systextil.relatorio.controller;

import com.systextil.relatorio.cliente.Cliente;
import com.systextil.relatorio.cliente.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> retornaCliente() {
        return clienteRepository.findPrimeiro();
    }

}
