package com.systextil.relatorio.infra.data_base_connection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class ConnectionOracle extends DataBaseConnection {

    private static final String USER = "systextil";
    private static final String PASSWORD = "oracle";
    private static final String URL = "jdbc:oracle:thin:@10.10.1.100:1521/devsprd.public.pocvcn.oraclevcn.com";

    public ConnectionOracle() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}