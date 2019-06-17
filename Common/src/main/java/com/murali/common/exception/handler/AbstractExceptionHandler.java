package com.murali.common.exception.handler;

import java.util.Collection;

import org.springframework.http.HttpStatus;

import com.murali.common.exception.ErrorResponse;
import com.murali.common.exception.FieldError;
import com.murali.common.exception.ErrorResponse.ErrorResponseBuilder;

public abstract class AbstractExceptionHandler<T extends Throwable> {

	private Class<?> exceptionClass;

	public AbstractExceptionHandler(Class<?> exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public Class<?> getExceptionClass() {
		return exceptionClass;
	}

	protected String getExceptionId(T ex) {
		return "Need to implement";
	}

	protected String getMessage(T ex) {
		return ex.getMessage();
	}

	protected HttpStatus getStatus(T ex) {
		return HttpStatus.UNPROCESSABLE_ENTITY;
	}

	protected abstract Collection<FieldError> getErrors(T ex);

	public ErrorResponse getErrorResponse(T ex) {
		
		ErrorResponseBuilder response = ErrorResponse.builder()
				.exceptionId(getExceptionId(ex))
				.message(getMessage(ex));
		
		HttpStatus status = getStatus(ex);
		if (status != null) {
			response = response.status(status.value()).error(status.getReasonPhrase());
		}
		response.errors(getErrors(ex));
		return response.build();
	}
}