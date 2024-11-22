package br.com.fiap.global.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.fiap.global.dominio.Quiz;
import br.com.fiap.global.service.QuizService;

@Path("quizzes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuizController {
    
    private QuizService quizService;
    
    public QuizController() {
        this.quizService = new QuizService();
    }
    
    @POST
    public Response criarQuiz(Quiz quiz) {
        quizService.adicionarQuiz(quiz);
        return Response.status(Response.Status.CREATED).entity(quiz).build();
    }
    
    @GET
    public Response obterTodosQuizzes() {
        List<Quiz> quizzes = quizService.listarTodosQuizzes();
        return Response.status(Response.Status.OK).entity(quizzes).build();
    }
    
    @GET
    @Path("/{id}")
    public Response obterQuizPorId(@PathParam("id") Long id) {
        Quiz quiz = quizService.buscarQuizPorId(id);
        if (quiz == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Quiz não encontrado para o ID: " + id).build();
        }
        return Response.status(Response.Status.OK).entity(quiz).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response atualizarQuiz(@PathParam("id") Long id, Quiz quizAtualizado) {
        Quiz quizExistente = quizService.buscarQuizPorId(id);
        if (quizExistente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Quiz não encontrado para o ID: " + id).build();
        }
        quizAtualizado.setId(id);
        quizService.atualizarQuiz(quizAtualizado);
        return Response.status(Response.Status.OK).entity(quizAtualizado).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response removerQuiz(@PathParam("id") Long id) {
        Quiz quiz = quizService.buscarQuizPorId(id);
        if (quiz == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Quiz não encontrado para o ID: " + id).build();
        }
        quizService.removerQuiz(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    // Endpoint para adicionar uma pergunta a um quiz específico
    @POST
    @Path("/{quizId}/perguntas")
    public Response adicionarPergunta(@PathParam("quizId") Long quizId, 
                                      br.com.fiap.global.dominio.Question pergunta) {
        quizService.adicionarPerguntaAoQuiz(quizId, pergunta);
        return Response.status(Response.Status.CREATED).entity(pergunta).build();
    }
    
    // Endpoint para remover uma pergunta de um quiz específico
    @DELETE
    @Path("/{quizId}/perguntas/{perguntaId}")
    public Response removerPergunta(@PathParam("quizId") Long quizId, 
                                    @PathParam("perguntaId") Long perguntaId) {
        quizService.removerPerguntaDoQuiz(quizId, perguntaId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}