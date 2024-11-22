package br.com.fiap.global.dominio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {
    
    @JsonProperty
    private Long id;
    
    @JsonProperty
    private String texto;
    
    @JsonProperty
    private List<String> opcoes;
    
    @JsonProperty
    private int respostaCorreta; // Índice da resposta correta
    
    public Question() {}
    
    public Question(Long id, String texto, List<String> opcoes, int respostaCorreta) {
        this.id = id;
        this.texto = texto;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public List<String> getOpcoes() {
        return opcoes;
    }
    
    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes;
    }
    
    public int getRespostaCorreta() {
        return respostaCorreta;
    }
    
    public void setRespostaCorreta(int respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }
    
    // Método para verificar se uma opção está correta
    public boolean isRespostaCorreta(int indice) {
        return this.respostaCorreta == indice;
    }
    
    // Método para adicionar uma opção de resposta
    public void adicionarOpcao(String opcao) {
        this.opcoes.add(opcao);
    }
}