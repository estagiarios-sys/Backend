package com.systextil.relatorio.infra.data_base_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.systextil.relatorio.infra.exception_handler.DataBaseConnectionException;


/** Classe pai das classes que farão conexão com banco de dados */
class DataBaseConnection {

    private String user;
    private String password;
    private String url;
    private Connection idConnection;

    public void connect() throws DataBaseConnectionException {
    	try {
			this.idConnection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new DataBaseConnectionException("Não foi possível conectar ao banco");
		}
	}

    public void disconnect() throws SQLException {
        if (this.idConnection != null) {
        	this.idConnection.close();
        }
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