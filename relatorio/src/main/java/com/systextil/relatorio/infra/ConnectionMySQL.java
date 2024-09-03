package com.systextil.relatorio.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {

    private final String user = "user";
    private final String password = "sysestg";
    private final String url = "jdbc:mysql://localhost:3306/db_gerador_relatorio";
    private Connection idConnection;

    public void connect() throws ClassNotFoundException, SQLException {
        this.idConnection = DriverManager.getConnection(url, user, password);
    }

    public void disconnect() throws SQLException {
        if (this.idConnection != null)
            this.idConnection.close();
    }

    public Connection getIdConnection() {
        return idConnection;
    }
}
