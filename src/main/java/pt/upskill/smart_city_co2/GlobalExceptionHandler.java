package pt.upskill.smart_city_co2.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Página não encontrada
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception ex, HttpServletRequest request, Model model) {
        logger.error("404 - Página não encontrada: {}", request.getRequestURI());
        model.addAttribute("erro", "A página que procura não existe.");
        model.addAttribute("codigo", "404");
        model.addAttribute("mensagem", "Página não encontrada");
        model.addAttribute("detalhe", "O endereço solicitado não foi encontrado no servidor.");
        model.addAttribute("caminho", request.getRequestURI());
        return "error";
    }

    // 403 - Acesso negado
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, HttpServletRequest request, Model model) {
        logger.error("403 - Acesso negado: {} - {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("erro", "Não tem permissão para aceder a este recurso.");
        model.addAttribute("codigo", "403");
        model.addAttribute("mensagem", "Acesso negado");
        model.addAttribute("detalhe", "Não possui as permissões necessárias para aceder a esta página.");
        model.addAttribute("caminho", request.getRequestURI());
        return "error";
    }

    // 405 - Método não suportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, Model model) {
        logger.error("405 - Método não suportado: {} - {}", request.getMethod(), request.getRequestURI());
        model.addAttribute("erro", "Método de requisição não suportado.");
        model.addAttribute("codigo", "405");
        model.addAttribute("mensagem", "Método não permitido");
        model.addAttribute("detalhe", "O método " + request.getMethod() + " não é suportado para este endpoint.");
        model.addAttribute("caminho", request.getRequestURI());
        return "error";
    }

    // 400 - Parâmetros inválidos
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception ex, HttpServletRequest request, Model model) {
        logger.error("400 - Requisição inválida: {} - {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("erro", "Requisição inválida.");
        model.addAttribute("codigo", "400");
        model.addAttribute("mensagem", "Dados inválidos");
        model.addAttribute("detalhe", ex.getMessage());
        model.addAttribute("caminho", request.getRequestURI());
        return "error";
    }

    // 500 - Erro interno do servidor
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        logger.error("500 - Erro interno do servidor: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        model.addAttribute("erro", "Ocorreu um erro interno no servidor.");
        model.addAttribute("codigo", "500");
        model.addAttribute("mensagem", "Erro interno do servidor");
        model.addAttribute("detalhe", "Por favor, tente novamente mais tarde. Se o problema persistir, contacte o suporte.");
        model.addAttribute("caminho", request.getRequestURI());

        // Opcional: mostrar stack trace em ambiente de desenvolvimento
        // model.addAttribute("trace", Arrays.toString(ex.getStackTrace()));

        return "error";
    }

    // Exceções de negócio personalizadas
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRuntimeException(RuntimeException ex, HttpServletRequest request, Model model) {
        logger.error("Erro de negócio: {} - {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        model.addAttribute("codigo", "400");
        model.addAttribute("mensagem", "Erro na operação");
        model.addAttribute("detalhe", ex.getMessage());
        model.addAttribute("caminho", request.getRequestURI());
        return "error";
    }
}