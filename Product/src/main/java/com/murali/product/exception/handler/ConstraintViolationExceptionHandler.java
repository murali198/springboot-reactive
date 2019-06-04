package com.murali.product.exception.handler;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.murali.product.exception.FieldError;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConstraintViolationExceptionHandler extends AbstractValidationExceptionHandler<ConstraintViolationException> {

	public ConstraintViolationExceptionHandler() {
		super(ConstraintViolationException.class);
		log.info("Created");
	}

	@Override
	public Collection<FieldError> getErrors(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		return constraintViolations
				.stream()
				.map(constraintViolation -> {
					String value = constraintViolation.getPropertyPath().toString();
					String field = StringUtils.substringAfter(value, ".");
					if(field.isEmpty())
						field = value;
					return new FieldError(field, constraintViolation.getMessageTemplate(), constraintViolation.getMessage());
				})
				.collect(Collectors.toList());	
	}

}