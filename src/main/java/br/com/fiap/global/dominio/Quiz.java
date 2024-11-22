package br.com.fiap.global.dominio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quiz {
    
    @JsonProperty
    private Long id;
    
    @JsonProperty
    private String titulo;
    
    @JsonProperty
    private String descricao;
    
    @JsonProperty
    private List<Question> perguntas;
    
    public Quiz() {}
    
    public Quiz(Long id, String titulo, String descricao, List<Question> perguntas) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.perguntas = perguntas;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public List<Question> getPerguntas() {
        return perguntas;
    }
    
    public void setPerguntas(List<Question> perguntas) {
        this.perguntas = perguntas;
    }
    
    // Método para adicionar uma pergunta ao quiz
    public void adicionarPergunta(Question pergunta) {
        this.perguntas.add(pergunta);
    }
    
    // Método para remover uma pergunta do quiz
    public void removerPergunta(Long perguntaId) {
        this.perguntas.removeIf(pergunta -> pergunta.getId().equals(perguntaId));
    }
}