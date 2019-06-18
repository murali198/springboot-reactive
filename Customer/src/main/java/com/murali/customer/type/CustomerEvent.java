package com.murali.customer.type;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEvent {
	
	private Long id;
	
	@NotEmpty
	private String customerId;
	
	@NotEmpty
	private String name;
	
	private String surName;
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String mobile;
	
	private String action;
}
