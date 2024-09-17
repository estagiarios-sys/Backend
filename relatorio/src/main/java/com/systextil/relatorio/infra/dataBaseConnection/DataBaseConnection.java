package com.systextil.relatorio.infra.dataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Classe pai das classes que farão conexão com banco de dados */
class DataBaseConnection {

    private String user;
    private String password;
    private String url;
    private Connection idConnection;

    public void connect() throws ClassNotFoundException, SQLException {
    	//try {
    		this.idConnection = DriverManager.getConnection(url, user, password);
    	//} catch () {}
    }

    public void disconnect() throws SQLException {
        if (this.idConnection != null)
            this.idConnection.close();
    }

    public Connection getIdConnection() {
        return idConnection;
    }
    
    /** Método usado para as classes filhas configurarem a conexão */
    protected void fillAttributes(String user, String password, String url) {
    	this.user = user;
    	this.password = password;
    	this.url = url;
    }
}