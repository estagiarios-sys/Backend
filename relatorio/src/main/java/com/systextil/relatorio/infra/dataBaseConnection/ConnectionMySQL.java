package com.systextil.relatorio.infra.dataBaseConnection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class ConnectionMySQL extends DataBaseConnection {

    private final String user = "root";
    private final String password = "ppnica1929";
    private final String url = "jdbc:mysql://localhost:3306/db_gerador_relatorio";
    
    public ConnectionMySQL() {
    	fillAttributes(user, password, url);
    }
}