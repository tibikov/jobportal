package org.demo.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.demo.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
public class JobportalApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(JobportalApiExceptionHandler.class);

	private static final String SEPARATOR = ": ";
	private static final String CLIENT_EMAIL_UNIQUE = "client_email_unique";
	private static final String VALIDATION_FAILED = "Validation failed";
	private static final String EMAIL_EMAIL_ADDRESS_IS_ALREADY_IN_USE = ".email: Email address is already in use";

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException constrException,
			WebRequest request) {
		List<String> errors = constrException.getConstraintViolations().stream()
				.map(err -> err.getRootBeanClass().getSimpleName() + "." + err.getPropertyPath() + SEPARATOR
						+ err.getMessage())
				.collect(Collectors.toList());

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, VALIDATION_FAILED, errors);

		logger.error(VALIDATION_FAILED + " " + String.join(", ", errors));
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ org.hibernate.exception.ConstraintViolationException.class })
	public ResponseEntity<Object> handleHibernateConstraintViolation(
			org.hibernate.exception.ConstraintViolationException constrException, WebRequest request) {

		String constraintName = constrException.getConstraintName().toLowerCase();
		String error = constrException.getMessage();
		if (constraintName.contains(CLIENT_EMAIL_UNIQUE)) {
			error = Client.class.getSimpleName() + EMAIL_EMAIL_ADDRESS_IS_ALREADY_IN_USE;
		}

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, VALIDATION_FAILED, error);

		logger.error(VALIDATION_FAILED + " " + error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException typeException,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final String error = String.format("Type error of property '{}' with value '{}', required type is '{}'",
				typeException.getPropertyName(), typeException.getValue(), typeException.getRequiredType());

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, typeException.getLocalizedMessage(),
				error);

		logger.error("Type mismatch error: {}", error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, ex.getMessage(),
				ex.getLocalizedMessage());

		logger.error(ex.toString());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	
	@ExceptionHandler({ JobportalInvalidApikeyException.class })
	public ResponseEntity<Object> handleInvalidApiKey(JobportalInvalidApikeyException invalidApiKeyException,
			WebRequest request) {
		JobportalApiError apiError = new JobportalApiError(HttpStatus.BAD_REQUEST, invalidApiKeyException.getMessage(),
				invalidApiKeyException.getLocalizedMessage());
		
		logger.error(invalidApiKeyException.getMessage());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}