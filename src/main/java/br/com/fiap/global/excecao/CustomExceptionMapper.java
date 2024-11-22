package br.com.fiap.global.excecao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(CustomExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        logger.log(Level.SEVERE, "Erro Interno do Servidor: ", exception);
        
        if(exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorMessage(exception.getMessage()))
                           .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(new ErrorMessage("Erro interno do servidor."))
                       .build();
    }
}