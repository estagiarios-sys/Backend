package com.systextil.relatorio.infra.data_base_connection;

import org.springframework.stereotype.Component;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
@Component
public class OracleConnection extends DataBaseConnection {

    private static final String USER = "systextil";
    private static final String PASSWORD = "3rp#_SYSTEXTIL2021";
    private static final String URL = "jdbc:oracle:thin:@10.10.1.2:1521/S001ERP.public.pocvcn.oraclevcn.com";

    public OracleConnection() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}