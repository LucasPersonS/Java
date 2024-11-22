package br.com.fiap.global.service;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.global.dominio.Question;
import br.com.fiap.global.dominio.Quiz;

public class QuizServiceTest {
    
    private QuizService quizService;
    
    @BeforeEach
    void setUp() {
        quizService = new QuizService();
    }
    
    @Test
    void criarEBuscarQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitulo("Energia Sustentável");
        quiz.setDescricao("Teste de conhecimentos sobre energia sustentável.");
        
        Question pergunta1 = new Question();
        pergunta1.setTexto("Qual a fonte de energia mais sustentável?");
        pergunta1.setOpcoes(Arrays.asList("Solar", "Nuclear", "Fóssil", "Eólica"));
        pergunta1.setRespostaCorreta(0);
        
        quiz.setPerguntas(Arrays.asList(pergunta1));
        
        quizService.adicionarQuiz(quiz);
        
        Quiz quizBuscado = quizService.buscarQuizPorId(quiz.getId());
        assertNotNull(quizBuscado);
        assertEquals("Energia Sustentável", quizBuscado.getTitulo());
        assertEquals(1, quizBuscado.getPerguntas().size());
    }
}