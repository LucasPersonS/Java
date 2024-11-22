package br.com.fiap.global.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    public Connection getConnection() {
        // Atualize as credenciais conforme necessário
        String urlDeConexao = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
        String login = "rm559186";
        String senha = "fiap24";
        
        try {
            return DriverManager.getConnection(urlDeConexao, login, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
    }
}