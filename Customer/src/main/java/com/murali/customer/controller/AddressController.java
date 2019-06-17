package com.murali.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.murali.customer.entity.Address;
import com.murali.customer.repo.AddressRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AddressController {

	@Autowired
    private AddressRepository repository;
	
	@GetMapping("/address")
    public List<Address> getAddress() {
    	log.debug("Getting all the address..");
        return repository.findAll();
    }

    @GetMapping("/address/{id}")
    public Address getAddress(@PathVariable("id") Long id) {
    	log.debug("Getting the address with id ["+ id +"]");
    	return repository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
}
