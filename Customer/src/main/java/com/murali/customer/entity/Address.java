package com.murali.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
@Entity
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long customer;
	private String name;
	private String surName;
	private String number;
	private String street;
	private String city;
	private String country;
	private String phone;
	private String email;
	
}
