package com.systextil.relatorio.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DataBaseConnection {

    private String user;
    private String password;
    private String url;
    private Connection idConnection;

    public void connect() throws ClassNotFoundException, SQLException {
        this.idConnection = DriverManager.getConnection(url, user, password);
    }

    public void disconnect() throws SQLException {
        if (this.idConnection != null)
            this.idConnection.close();
    }

    public Connection getIdConnection() {
        return idConnection;
    }
    
    protected void fillAttributes(String user, String password, String url) {
    	this.user = user;
    	this.password = password;
    	this.url = url;
    }
}