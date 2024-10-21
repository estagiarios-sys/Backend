package com.systextil.relatorio.infra.data_base_connection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class OracleConnection extends DataBaseConnection {

    private static final String USER = "felipeV";
    private static final String PASSWORD = "oracle";
    private static final String URL = "jdbc:oracle:thin:@10.10.1.2:1521/s003trn.public.pocvcn.oraclevcn.com";

    public OracleConnection() {
    	fillAttributes(USER, PASSWORD, URL);
    }
}