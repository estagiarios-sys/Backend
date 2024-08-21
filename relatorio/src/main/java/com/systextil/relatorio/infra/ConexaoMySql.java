package com.systextil.relatorio.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySql {

    private String usuario = "root";
    private String senha = "root";
    private String url = "jdbc:mysql://localhost:3306/db_gerador_relatorio";
    private Connection idConexao;

    public void conectar() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.idConexao = DriverManager.getConnection(url, usuario, senha);
    }

    public void desconectar() throws SQLException {
        if (this.idConexao != null)
            this.idConexao.close();
    }

    public Connection getIdConexao() {
        return idConexao;
    }
}
