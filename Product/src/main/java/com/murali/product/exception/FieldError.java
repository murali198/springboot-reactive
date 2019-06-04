package com.murali.product.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @AllArgsConstructor @ToString
public class FieldError {

	private String field;
	private String code;
	private String message;

}
