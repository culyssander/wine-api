package br.com.globalsys.wineapi.api.exception;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.globalsys.wineapi.models.exception.EntidadeNaoEncontradaException;
import br.com.globalsys.wineapi.models.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		Problema problema = getProblema(status.value(), ex.getMessage(), null);
		
		return handleExceptionInternal(ex, problema, new org.springframework.http.HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		Problema problema = getProblema(status.value(), ex.getMessage(), null);
		
		return handleExceptionInternal(ex, problema,  new org.springframework.http.HttpHeaders(), status, request);
	}	
	
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			 org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Campo> camposDeErros = new ArrayList<Campo>();
		
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			camposDeErros.add(new Campo(nome, mensagem));
		}
		
		Problema problema = getProblema(
				status.value(), 
				"Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente", 
				camposDeErros);
		
		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}
	
	private Problema getProblema(Integer status, String titulo, List<Campo> campos) {
		return new Problema(status, OffsetDateTime.now(), titulo, campos);
	}
}
