package br.com.fiap.global.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.fiap.global.dominio.Quiz;
import br.com.fiap.global.dominio.Question;
import br.com.fiap.global.dominio.RepositorioQuiz;
import br.com.fiap.global.infra.dao.QuizDAO;

public class QuizService {
    
    private static final Logger logger = Logger.getLogger(QuizService.class.getName());
    
    private RepositorioQuiz repositorioQuiz;
    
    public QuizService() {
        this.repositorioQuiz = new QuizDAO();
    }
    
    // Adicionar um novo quiz
    public void adicionarQuiz(Quiz quiz) {
        try {
            repositorioQuiz.adicionar(quiz);
            logger.log(Level.INFO, "Quiz adicionado com sucesso: {0}", quiz.getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao adicionar quiz", e);
            throw e;
        }
    }
    
    // Atualizar um quiz existente
    public void atualizarQuiz(Quiz quiz) {
        repositorioQuiz.atualizar(quiz);
    }
    
    // Remover um quiz pelo ID
    public void removerQuiz(Long id) {
        repositorioQuiz.remover(id);
    }
    
    // Buscar um quiz pelo ID
    public Quiz buscarQuizPorId(Long id) {
        return repositorioQuiz.buscarPorId(id);
    }
    
    // Listar todos os quizzes
    public List<Quiz> listarTodosQuizzes() {
        return repositorioQuiz.listarTodos();
    }
    
    // Adicionar uma pergunta a um quiz específico
    public void adicionarPerguntaAoQuiz(Long quizId, Question pergunta) {
        Quiz quiz = repositorioQuiz.buscarPorId(quizId);
        if (quiz != null) {
            quiz.adicionarPergunta(pergunta);
            repositorioQuiz.atualizar(quiz);
        }
    }
    
    // Remover uma pergunta de um quiz específico
    public void removerPerguntaDoQuiz(Long quizId, Long perguntaId) {
        Quiz quiz = repositorioQuiz.buscarPorId(quizId);
        if (quiz != null) {
            quiz.removerPergunta(perguntaId);
            repositorioQuiz.atualizar(quiz);
        }
    }
}