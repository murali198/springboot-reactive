package com.murali.product.exception;

import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalErrorAttribute<T extends Throwable> extends DefaultErrorAttributes {
	
	private ErrorResponseComposer<T> errorResponseComposer;
	
    public GlobalErrorAttribute(ErrorResponseComposer<T> errorResponseComposer) {
		this.errorResponseComposer = errorResponseComposer;
		log.info("Created");
	}

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);		
		addDetails(errorAttributes, request);
		return errorAttributes;
	}
	
	@SuppressWarnings("unchecked")
	protected void addDetails(Map<String, Object> errorAttributes, ServerRequest request) {
		
		Throwable ex = getError(request);
		errorResponseComposer.compose((T)ex).ifPresent(errorResponse -> {
			
			if (errorResponse.getExceptionId() != null)
				errorAttributes.put("exceptionId", errorResponse.getExceptionId());

			if (errorResponse.getMessage() != null)
				errorAttributes.put("message", errorResponse.getMessage());
			
			Integer status = errorResponse.getStatus();
			
			if (status != null) {
				errorAttributes.put("status", status);
				errorAttributes.put("error", errorResponse.getError());
			}

			if (errorResponse.getErrors() != null)
				errorAttributes.put("errors", errorResponse.getErrors());			
		});
		
		if (errorAttributes.get("exceptionId") == null)
			errorAttributes.put("exceptionId", "Need to implement");		
	}
}