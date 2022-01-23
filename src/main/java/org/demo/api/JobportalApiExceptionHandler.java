package org.demo.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
public class JobportalApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException argInvalidException,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();

		argInvalidException.getBindingResult().getFieldErrors()
				.forEach(err -> errors.add(err.getField() + ": " + err.getDefaultMessage()));

		argInvalidException.getBindingResult().getGlobalErrors()
				.forEach(err -> errors.add(err.getObjectName() + ": " + err.getDefaultMessage()));

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST,
				argInvalidException.getLocalizedMessage(), errors);

		return handleExceptionInternal(argInvalidException, apiError, headers, apiError.getStatus(), request);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException constrException,
			WebRequest request) {
		List<String> errors = constrException.getConstraintViolations().stream()
				.map(err -> err.getRootBeanClass().getName() + " " + err.getPropertyPath() + ": " + err.getMessage())
				.collect(Collectors.toList());

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST,
				constrException.getLocalizedMessage(), errors);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException typeException,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final String error = String.format("Type error of property '{}' with value '{}', required type is '{}'",
				typeException.getPropertyName(), typeException.getValue(), typeException.getRequiredType());
		
		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, typeException.getLocalizedMessage(),
				error);
		
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}