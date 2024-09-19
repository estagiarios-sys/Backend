package com.systextil.relatorio.infra.dataBaseConnection;

/** 
 * Classe pai: {@link DataBaseConnection}
 */
public class ConnectionOracle extends DataBaseConnection {

    private final String user = "felipeV";
    private final String password = "oracle";
    private final String url = "jdbc:oracle:thin:@10.10.1.2:1521/s003trn.public.pocvcn.oraclevcn.com";

    public ConnectionOracle() {
    	fillAttributes(user, password, url);
    }
}