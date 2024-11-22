package br.com.fiap.global.infra.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.fiap.global.dominio.Quiz;
import br.com.fiap.global.dominio.RepositorioQuiz;

public class QuizDAO implements RepositorioQuiz {
    
    private static final Logger logger = Logger.getLogger(QuizDAO.class.getName());
    
    @Override
    public void adicionar(Quiz quiz) {
        try (Connection conexao = new ConnectionFactory().getConnection()) {
            Long quizId = obterProximoId(conexao);
            logger.log(Level.INFO, "Quiz ID obtido: {0}", quizId);
            if (quizId == null) {
                throw new IllegalStateException("Não foi possível obter o ID do quiz.");
            }
            quiz.setId(quizId);
            logger.log(Level.INFO, "Inserindo quiz com ID: {0}", quizId);

            String sqlQuiz = "INSERT INTO quizzes (id, titulo, descricao) VALUES (?, ?, ?)";
            try (PreparedStatement stmtQuiz = conexao.prepareStatement(sqlQuiz)) {
                stmtQuiz.setLong(1, quizId);
                stmtQuiz.setString(2, quiz.getTitulo());
                stmtQuiz.setString(3, quiz.getDescricao());
                stmtQuiz.executeUpdate();
            }

            if (quiz.getPerguntas() != null) {
                for (br.com.fiap.global.dominio.Question pergunta : quiz.getPerguntas()) {
                    logger.log(Level.INFO, "Adicionando pergunta: {0}", pergunta.getTexto());
                    adicionarPergunta(pergunta, quizId, conexao);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao adicionar quiz", e);
            throw new RuntimeException(e);
        }
    }
    
    private void adicionarPergunta(br.com.fiap.global.dominio.Question pergunta, Long quizId, Connection conexao) {
        logger.log(Level.INFO, "Iniciando adicionar Pergunta com quizId: {0}", quizId);
        try {
            if (quizId == null) {
                throw new IllegalArgumentException("quizId não pode ser null");
            }
            logger.log(Level.INFO, "Inserindo pergunta para quizId: {0}", quizId);

            Long perguntaId = obterProximoIdPerguntas(conexao);
            if (perguntaId == null) {
                throw new IllegalStateException("Não foi possível obter o ID da pergunta.");
            }
            pergunta.setId(perguntaId);
            logger.log(Level.INFO, "Inserindo pergunta com ID: {0}", perguntaId);

            String sqlPergunta = "INSERT INTO perguntas (id, texto, resposta_correta, quiz_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtPergunta = conexao.prepareStatement(sqlPergunta)) {
                stmtPergunta.setLong(1, perguntaId);
                stmtPergunta.setString(2, pergunta.getTexto());
                stmtPergunta.setInt(3, pergunta.getRespostaCorreta());
                stmtPergunta.setLong(4, quizId);
                stmtPergunta.executeUpdate();
            }

            for (String opcaoTexto : pergunta.getOpcoes()) {
                adicionarOpcao(opcaoTexto, perguntaId, conexao);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao adicionar pergunta", e);
            throw new RuntimeException(e);
        }
    }
    
    private void adicionarOpcao(String texto, Long perguntaId, Connection conexao) {
        try {
            Long opcaoId = obterProximoIdOpcoes(conexao);
            String sqlOpcao = "INSERT INTO opcoes (id, texto, pergunta_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmtOpcao = conexao.prepareStatement(sqlOpcao)) {
                stmtOpcao.setLong(1, opcaoId);
                stmtOpcao.setString(2, texto);
                stmtOpcao.setLong(3, perguntaId);
                stmtOpcao.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao adicionar opção", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Quiz buscarPorId(Long id) {
        Quiz quiz = null;
        String sql = "SELECT * FROM quizzes WHERE id = ?";
        try (Connection conexao = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    quiz = new Quiz();
                    quiz.setId(rs.getLong("id"));
                    quiz.setTitulo(rs.getString("titulo"));
                    quiz.setDescricao(rs.getString("descricao"));
                    quiz.setPerguntas(buscarPerguntasPorQuizId(id, conexao));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar quiz por ID", e);
            throw new RuntimeException(e);
        }
        return quiz;
    }
    
    private List<br.com.fiap.global.dominio.Question> buscarPerguntasPorQuizId(Long quizId, Connection conexao) throws SQLException {
        List<br.com.fiap.global.dominio.Question> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM perguntas WHERE quiz_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    br.com.fiap.global.dominio.Question pergunta = new br.com.fiap.global.dominio.Question();
                    pergunta.setId(rs.getLong("id"));
                    pergunta.setTexto(rs.getString("texto"));
                    pergunta.setRespostaCorreta(rs.getInt("resposta_correta"));
                    pergunta.setOpcoes(buscarOpcoesPorPerguntaId(pergunta.getId(), conexao));
                    perguntas.add(pergunta);
                }
            }
        }
        return perguntas;
    }
    
    private List<String> buscarOpcoesPorPerguntaId(Long perguntaId, Connection conexao) throws SQLException {
        List<String> opcoes = new ArrayList<>();
        String sql = "SELECT texto FROM opcoes WHERE pergunta_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, perguntaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    opcoes.add(rs.getString("texto"));
                }
            }
        }
        return opcoes;
    }
    
    @Override
    public void atualizar(Quiz quiz) {
        String sql = "UPDATE quizzes SET titulo = ?, descricao = ? WHERE id = ?";
        try (Connection conexao = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, quiz.getTitulo());
            stmt.setString(2, quiz.getDescricao());
            stmt.setLong(3, quiz.getId());
            stmt.executeUpdate();
            
            // Implement logic to update perguntas if necessary
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar quiz", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void remover(Long id) {
        String sqlPerguntas = "DELETE FROM perguntas WHERE quiz_id = ?";
        String sql = "DELETE FROM quizzes WHERE id = ?";
        try (Connection conexao = new ConnectionFactory().getConnection();
             PreparedStatement stmtPerguntas = conexao.prepareStatement(sqlPerguntas);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmtPerguntas.setLong(1, id);
            stmtPerguntas.executeUpdate();
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao remover quiz", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Quiz> listarTodos() {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes";
        try (Connection conexao = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(rs.getLong("id"));
                quiz.setTitulo(rs.getString("titulo"));
                quiz.setDescricao(rs.getString("descricao"));
                quiz.setPerguntas(buscarPerguntasPorQuizId(quiz.getId(), conexao));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar todos os quizzes", e);
            throw new RuntimeException(e);
        }
        return quizzes;
    }
    
    @Override
    public void fechar() {
        // No implementation needed
    }
    
    private Long obterProximoId(Connection conexao) throws SQLException {
        Long id = null;
        String sql = "SELECT SEQ_QUIZZES.NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                id = rs.getLong(1);
                logger.log(Level.INFO, "Obtido quizId: {0}", id);
                if (rs.wasNull()) {
                    id = null;
                }
            }
        }
        if (id == null) {
            logger.log(Level.SEVERE, "SEQ_QUIZZES.NEXTVAL retornou null");
        }
        return id;
    }
    
    private Long obterProximoIdPerguntas(Connection conexao) throws SQLException {
        Long id = null;
        String sql = "SELECT SEQ_PERGUNTAS.NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                id = rs.getLong(1);
                logger.log(Level.INFO, "Obtido perguntaId: {0}", id);
                if (rs.wasNull()) {
                    id = null;
                }
            }
        }
        if (id == null) {
            logger.log(Level.SEVERE, "SEQ_PERGUNTAS.NEXTVAL retornou null");
        }
        return id;
    }
    
    private Long obterProximoIdOpcoes(Connection conexao) throws SQLException {
        Long id = null;
        String sql = "SELECT SEQ_OPCOES.NEXTVAL FROM DUAL";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                id = rs.getLong(1);
                logger.log(Level.INFO, "Obtido opcaoId: {0}", id);
                if (rs.wasNull()) {
                    id = null;
                }
            }
        }
        if (id == null) {
            logger.log(Level.SEVERE, "SEQ_OPCOES.NEXTVAL retornou null");
        }
        return id;
    }
}