package com.systextil.relatorio.domain.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collections;

@Repository
public class UserRepository {

    private static Connection connection;
    private static String user = "systextil";
    private static String password = "oracle";
    private static String url = "jdbc:oracle:thin:@10.10.1.100:1521/devsprd.public.pocvcn.oraclevcn.com";

    boolean validateUser(String userName) throws SQLException {
        connect();

        String sql = "SELECT 1 FROM HDOC_030 WHERE USUARIO = '" + userName + "'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet dados = ps.executeQuery();
        boolean usuarioExiste = dados.next();

        disconnect();

        return usuarioExiste;
    }

    String getSenha(String userName) throws SQLException {
        connect();

        String sql = "SELECT SENHA FROM HDOC_030 WHERE USUARIO = '" + userName + "'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet dados = ps.executeQuery();
        dados.next();
        String senha = dados.getString(1);

        disconnect();

        return senha;
    }

    public UserDetails getUser(String login) throws SQLException {
        connect();

        String sql = "SELECT USUARIO, SENHA FROM HDOC_030 WHERE USUARIO = '" + login + "'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet dados = ps.executeQuery();
        dados.next();
        String userName = dados.getString(1);
        String password = dados.getString(2);
        UserDetails user = new User(userName, password, Collections.emptyList());

        disconnect();

        return user;
    }


    private static void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }



}
