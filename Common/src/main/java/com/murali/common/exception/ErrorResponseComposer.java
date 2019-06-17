package com.murali.common.exception;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.murali.common.exception.handler.AbstractExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorResponseComposer<T extends Throwable> {
	
	private final Map<Class<?>, AbstractExceptionHandler<T>> handlers;
	
	public ErrorResponseComposer(List<AbstractExceptionHandler<T>> exHandlers) {
		this.handlers = exHandlers.stream().collect(
	            Collectors.toMap(AbstractExceptionHandler::getExceptionClass,
	            		Function.identity(), (handler1, handler2) -> {
	            			return AnnotationAwareOrderComparator
	            					.INSTANCE.compare(handler1, handler2) < 0 ?
	            					handler1 : handler2;
	            		}));
		log.info("Created");
	}

	public Optional<ErrorResponse> compose(T ex) {

		Throwable throwable = ex;
		AbstractExceptionHandler<T> handler = null;
		while (throwable != null) {
			handler = handlers.get(ex.getClass());
			if (handler != null)
				break;
			
			throwable = (T) throwable.getCause();			
		}
        if (handler != null)
        	return Optional.of(handler.getErrorResponse(ex));
        
        ErrorResponse response = ErrorResponse.builder()
        		.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
        		.message(ex.getMessage())
        		.exceptionId("Un implemented")
        		.error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
        		.build();
        return Optional.of(response);
	}
}
