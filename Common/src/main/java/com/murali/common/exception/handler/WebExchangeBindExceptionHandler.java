package com.murali.common.exception.handler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.murali.common.exception.FieldError;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class WebExchangeBindExceptionHandler extends AbstractExceptionHandler<WebExchangeBindException> {

	public WebExchangeBindExceptionHandler() {
		super(WebExchangeBindException.class);
		log.info("Created");
	}

	@Override
	public HttpStatus getStatus(WebExchangeBindException ex) {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public Collection<FieldError> getErrors(WebExchangeBindException ex) {
		List<FieldError> errors = ex.getFieldErrors()
				.stream()
				.map(error -> new FieldError(error.getObjectName(), error.getCode(), error.getDefaultMessage()))
				.collect(Collectors.toList());

		errors.addAll(ex.getFieldErrors()
				.stream()
				.map(fieldError -> new FieldError(fieldError.getObjectName() + "." + fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()))
				.collect(Collectors.toSet()));

		return errors;
	}
}