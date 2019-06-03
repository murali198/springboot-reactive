package com.murali.product.entity;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {

	@Id
	private String id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String category;
	@NotEmpty
	private String productId;
	@NotEmpty
	private String price;
	private String description;
	
}
