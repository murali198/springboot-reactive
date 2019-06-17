package com.murali.common.exception.handler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.murali.common.exception.FieldError;
import com.murali.common.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class ResourceNotFoundExceptionHandler extends AbstractExceptionHandler<ResourceNotFoundException> {

	public ResourceNotFoundExceptionHandler() {
		super(ResourceNotFoundException.class);
		log.info("Created");
	}

	@Override
	public HttpStatus getStatus(ResourceNotFoundException ex) {
		return HttpStatus.NOT_FOUND;
	}

	@Override
	public Collection<FieldError> getErrors(ResourceNotFoundException ex) {
		return Collections.emptyList();
	}
}