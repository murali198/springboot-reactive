package com.murali.product.core;

import java.util.Set;
import java.util.function.Function;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProductValidationHandler {

	private Validator validator;

	public ProductValidationHandler(Validator validator) {
		this.validator = validator;
	}

	public <T> Mono<ServerResponse> process(Function<Mono<T>, Mono<ServerResponse>> fun, ServerRequest request, Class<T> elementClass) {
		return request
				.bodyToMono(elementClass)
				.flatMap(
					obj -> {
						Set<ConstraintViolation<T>> errors = validator.validate(obj);
						log.debug("Validated obj["+ obj.getClass() + "] and is error empty ["+errors.isEmpty()+"]");
						if(errors.isEmpty())
							return fun.apply(Mono.just(obj));
						throw new ConstraintViolationException(errors);
				}
			);
	}
}
