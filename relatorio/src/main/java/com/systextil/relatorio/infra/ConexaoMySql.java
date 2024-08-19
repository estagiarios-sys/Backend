package com.systextil.relatorio.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe que conectará com o banco (sem Spring Boot)
 */
public class ConexaoMySql {

    /* Atributos necessários para a conexão com o banco */
    private String usuario = "root";
    private String senha = "root";
    private String url = "jdbc:mysql://localhost:3306/db_gerador_relatorio"; /* Nome da base de dados */
    private Connection idConexao;

    /** Método usado para conectar ao banco */
    public void conectar() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.idConexao = DriverManager.getConnection(url, usuario, senha);
        if (this.idConexao != null) {
            System.out.println("Conectado ao banco de dados!");
        }
    }

    /** Método usado para desconectar do banco */
    public void desconectar() throws SQLException {
        if (this.idConexao != null)
            this.idConexao.close();
    }

    public Connection getIdConexao() {
        return idConexao;
    }
}
