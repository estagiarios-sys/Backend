package com.systextil.relatorio.infra.data_base_connection;

import org.springframework.stereotype.Component;

@Component
public class H2Connection extends DataBaseConnection {

	private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:h2:mem:testdb";
    
    public H2Connection() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}