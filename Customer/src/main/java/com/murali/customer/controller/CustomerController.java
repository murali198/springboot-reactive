package com.murali.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.murali.customer.entity.Customer;
import com.murali.customer.repo.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {

	@Autowired
    private CustomerRepository repository;
	
	@GetMapping("/customer")
    public List<Customer> getCustomer() {
    	log.debug("Getting all the customer..");
        return repository.findAll();
    }

    @GetMapping("/customer/{id}")
    public Customer getProduct(@PathVariable("id") Long id) {
    	log.debug("Getting the Customer with id ["+ id +"]");
    	Customer customer = repository.findById(id).orElse(new Customer(-1L, "Not Available", "Not avilable", "Not avilable", "Not avilable"));
        return customer;
    }

    @PostMapping("/customer")
    public Customer save(@RequestBody Customer customer) {
    	log.debug("Persisting the Customer details ["+ customer +"]");
        return repository.save(customer);
    }
}
