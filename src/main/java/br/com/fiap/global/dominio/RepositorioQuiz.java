package br.com.fiap.global.dominio;

import java.util.List;

public interface RepositorioQuiz {
    
    void adicionar(Quiz quiz);
    void atualizar(Quiz quiz);
    void remover(Long id);
    Quiz buscarPorId(Long id);
    List<Quiz> listarTodos();
    void fechar();
}