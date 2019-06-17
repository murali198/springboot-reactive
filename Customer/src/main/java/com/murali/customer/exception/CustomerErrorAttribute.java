package com.murali.customer.exception;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.murali.common.exception.ErrorResponseComposer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerErrorAttribute<T extends Throwable> extends DefaultErrorAttributes {
	
	private ErrorResponseComposer<T> errorResponseComposer;
	
    public CustomerErrorAttribute(ErrorResponseComposer<T> errorResponseComposer) {
		this.errorResponseComposer = errorResponseComposer;
		log.info("Created");
	}

    public Map<String, Object> getErrorAttributes(WebRequest request, boolean includeStackTrace) {
    	Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);		
		addDetails(errorAttributes, request);
		return errorAttributes;
	}
    
	@SuppressWarnings("unchecked")
	protected void addDetails(Map<String, Object> errorAttributes, WebRequest request) {
		
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