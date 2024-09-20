package com.systextil.relatorio.infra.dataBaseConnection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class ConnectionMySQL extends DataBaseConnection {

    private final String user = "";
    private final String password = "";
    private final String url = "";
    
    public ConnectionMySQL() {
    	fillAttributes(user, password, url);
    }
}