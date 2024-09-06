package com.systextil.relatorio.infra.dataBaseConnection;

public class ConnectionOracle extends DataBaseConnection {

    private final String user = "systextil";
    private final String password = "oracle";
    private final String url = "jdbc:oracle:thin:@10.10.1.100:1521/devsprd.public.pocvcn.oraclevcn.com";

    public ConnectionOracle() {
    	fillAttributes(user, password, url);
    }
}