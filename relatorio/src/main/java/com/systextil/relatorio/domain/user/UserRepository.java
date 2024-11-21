package com.systextil.relatorio.domain.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.OracleConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {
	
    private final OracleConnection connection;
    
    public UserRepository(OracleConnection connection) {
    	this.connection = connection;
    }

    boolean exists(String userName, int codigoEmpresa) throws SQLException {
        connection.connect();
        
        String sql = "SELECT 1 FROM HDOC_030 WHERE USUARIO = '" + userName + "' AND EMPRESA = " + codigoEmpresa;
        boolean exists = false;
        
        try(PreparedStatement ps = connection.getIdConnection().prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            exists = dados.next();
        }
        connection.disconnect();

        return exists;
    }

    String getSenha(String userName, int codigoEmpresa) throws SQLException {
        connection.connect();

        String sql = "SELECT SENHA FROM HDOC_030 WHERE USUARIO = '" + userName + "' AND EMPRESA = " + codigoEmpresa;
        String senha = null;
        
        try(PreparedStatement ps = connection.getIdConnection().prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            dados.next();
            senha = dados.getString(1);
        }
        connection.disconnect();

        return senha;
    }

    public UserDetails getUser(String login) throws SQLException {
        connection.connect();

        String sql = "SELECT USUARIO, SENHA FROM HDOC_030 WHERE USUARIO = '" + login + "'";
        String userName = null;
        String password = null;
        
        try(PreparedStatement ps = connection.getIdConnection().prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            dados.next();
            userName = dados.getString(1);
            password = dados.getString(2);
        }
        UserDetails userDetails = new User(userName, password, Collections.emptyList());

        connection.disconnect();

        return userDetails;
    }
    
    List<Company> getCompanies() throws SQLException {
    	connection.connect();
    	
    	List<Company> companies = new ArrayList<>();
    	String sql = "SELECT CODIGO_EMPRESA, NOME_EMPRESA FROM FATU_500";
    	
    	try(PreparedStatement ps = connection.getIdConnection().prepareStatement(sql)) {
    		ResultSet dados = ps.executeQuery();
    		
    		while(dados.next()) {
    			Company company = new Company(dados.getInt(1), dados.getString(2));
    			companies.add(company);
    		}
    	}
    	connection.disconnect();
    	
    	return companies;
    }
}