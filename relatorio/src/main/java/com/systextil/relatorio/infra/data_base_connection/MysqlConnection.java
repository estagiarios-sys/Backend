package com.systextil.relatorio.infra.data_base_connection;

import org.springframework.stereotype.Component;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
@Component
public class MysqlConnection extends DataBaseConnection {

    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String URL = "";
    
    public MysqlConnection() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}