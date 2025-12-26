package com.supnum.middle.exception;

import com.supnum.middle.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions pour l'API REST.
 * 
 * Cette classe intercepte toutes les exceptions levées par les controllers
 * et retourne des réponses d'erreur standardisées.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Gère les erreurs de validation (MethodArgumentNotValidException).
	 * 
	 * @param ex L'exception de validation
	 * @param request La requête HTTP
	 * @return ResponseEntity avec ErrorResponse (400 Bad Request)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		
		log.warn("Erreur de validation: {}", ex.getMessage());
		
		// Construire le message d'erreur à partir des erreurs de validation
		String errorMessage = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Bad Request")
				.message("Erreur de validation: " + errorMessage)
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	/**
	 * Gère les RuntimeException (erreurs générales).
	 * 
	 * @param ex L'exception runtime
	 * @param request La requête HTTP
	 * @return ResponseEntity avec ErrorResponse (500 Internal Server Error)
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(
			RuntimeException ex,
			HttpServletRequest request) {
		
		log.error("RuntimeException interceptée: {}", ex.getMessage(), ex);
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.message(ex.getMessage() != null ? ex.getMessage() : "Une erreur interne s'est produite")
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	/**
	 * Gère toutes les autres exceptions non gérées.
	 * 
	 * @param ex L'exception
	 * @param request La requête HTTP
	 * @return ResponseEntity avec ErrorResponse (500 Internal Server Error)
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
			Exception ex,
			HttpServletRequest request) {
		
		log.error("Exception non gérée interceptée: {}", ex.getMessage(), ex);
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.message(ex.getMessage() != null ? ex.getMessage() : "Une erreur inattendue s'est produite")
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}

