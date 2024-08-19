package com.systextil.relatorio.cliente;

import com.systextil.relatorio.infra.ConexaoMySql;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ClienteRepositoryImpl implements ClienteRepository{

    ConexaoMySql conexao;

    @Override
    public ArrayList<Object[]> findClientesByColumns(String colunas) throws SQLException, ClassNotFoundException {
        conexao = new ConexaoMySql();
        conexao.conectar();

        String sql = "SELECT " + colunas + " FROM cliente";
        PreparedStatement comando = conexao.getIdConexao().prepareStatement(sql);
        ResultSet dados = comando.executeQuery();
        ArrayList<Object[]> listaClientes = new ArrayList<>();
        int columnsNumber = dados.getMetaData().getColumnCount();

        while(dados.next()) {
            Object[] cliente = new Object[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
                 cliente[i - 1] = dados.getString(i);
            }
            listaClientes.add(cliente);
        }

        conexao.desconectar();
        return listaClientes;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Cliente> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Cliente> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Cliente> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Cliente getOne(String s) {
        return null;
    }

    @Override
    public Cliente getById(String s) {
        return null;
    }

    @Override
    public Cliente getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Cliente> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Cliente> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Cliente> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Cliente> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Cliente> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Cliente> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Cliente, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Cliente> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Cliente> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Cliente> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Cliente> findAll() {
        return List.of();
    }

    @Override
    public List<Cliente> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Cliente entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Cliente> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Cliente> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return null;
    }




}
