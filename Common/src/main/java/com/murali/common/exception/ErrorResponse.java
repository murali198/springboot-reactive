package com.murali.common.exception;

import java.util.Collection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class ErrorResponse {

	private String exceptionId;
	private String error;
	private String message;
	private Integer status;
	private Collection<FieldError> errors;
	
}
