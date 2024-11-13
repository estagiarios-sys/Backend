package com.systextil.relatorio.domain.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {

    private static Connection connection;

    boolean exists(String userName, int codigoEmpresa) throws SQLException {
        connect();

        String sql = "SELECT 1 FROM HDOC_030 WHERE USUARIO = '" + userName + "' AND EMPRESA = " + codigoEmpresa;
        boolean exists = false;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            exists = dados.next();
        }
        disconnect();

        return exists;
    }

    String getSenha(String userName, int codigoEmpresa) throws SQLException {
        connect();

        String sql = "SELECT SENHA FROM HDOC_030 WHERE USUARIO = '" + userName + "' AND EMPRESA = " + codigoEmpresa;
        String senha = null;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            dados.next();
            senha = dados.getString(1);
        }
        disconnect();

        return senha;
    }

    public UserDetails getUser(String login) throws SQLException {
        connect();

        String sql = "SELECT USUARIO, SENHA FROM HDOC_030 WHERE USUARIO = '" + login + "'";
        String userName = null;
        String password = null;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            dados.next();
            userName = dados.getString(1);
            password = dados.getString(2);
        }
        UserDetails userDetails = new User(userName, password, Collections.emptyList());

        disconnect();

        return userDetails;
    }
    
    List<Company> getCompanies() throws SQLException {
    	connect();
    	
    	List<Company> companies = new ArrayList<>();
    	String sql = "SELECT CODIGO_EMPRESA, NOME_EMPRESA FROM FATU_500";
    	
    	try(PreparedStatement ps = connection.prepareStatement(sql)) {
    		ResultSet dados = ps.executeQuery();
    		
    		while(dados.next()) {
    			Company company = new Company(dados.getInt(1), dados.getString(2));
    			companies.add(company);
    		}
    	}
    	disconnect();
    	
    	return companies;
    }


    private static void connect() throws SQLException {
    	String user = "systextil";
        String password = "3rp#_SYSTEXTIL2021";
        String url = "jdbc:oracle:thin:@10.10.1.2:1521/S001ERP.public.pocvcn.oraclevcn.com";
        connection = DriverManager.getConnection(url, user, password);
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }
}