package com.murali.product.core;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.murali.product.type.ErrorResponse;

import reactor.core.publisher.Mono;

@Component
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
							if(validator.validate(obj).isEmpty()) {
								return fun.apply(Mono.just(obj));
							} else {
								List<String> error = errors.stream().map(val -> val.getMessage()).collect(Collectors.toList());
								return ServerResponse.badRequest().body(BodyInserters.fromObject(error));
							}
						}
				);
	}
}
