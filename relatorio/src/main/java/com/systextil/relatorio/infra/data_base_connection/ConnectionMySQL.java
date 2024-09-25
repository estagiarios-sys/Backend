package com.systextil.relatorio.infra.data_base_connection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class ConnectionMySQL extends DataBaseConnection {

    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String URL = "";
    
    public ConnectionMySQL() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}