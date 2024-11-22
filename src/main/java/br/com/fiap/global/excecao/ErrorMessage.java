package br.com.fiap.global.excecao;

public class ErrorMessage {
    
    private String mensagem;
    
    public ErrorMessage() {}
    
    public ErrorMessage(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}