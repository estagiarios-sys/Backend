package com.systextil.relatorio.domain.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;

@Repository
public class UserRepository {

    private static Connection connection;

    boolean exists(String userName) throws SQLException {
        connect();

        String sql = "SELECT 1 FROM HDOC_030 WHERE USUARIO = '" + userName + "'";
        boolean exists = false;
        
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
        	ResultSet dados = ps.executeQuery();
            exists = dados.next();
        }
        disconnect();

        return exists;
    }

    String getSenha(String userName) throws SQLException {
        connect();

        String sql = "SELECT SENHA FROM HDOC_030 WHERE USUARIO = '" + userName + "'";
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


    private static void connect() throws SQLException {
    	String user = "systextil";
        String password = "oracle";
        String url = "jdbc:oracle:thin:@10.10.1.100:1521/devsprd.public.pocvcn.oraclevcn.com";
        connection = DriverManager.getConnection(url, user, password);
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }
}