package com.murali.customer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.murali.customer.core.CustomerService;
import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;
import com.murali.customer.type.CustomerEvent;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {

	@Autowired
    private CustomerService service;
	
	@GetMapping("/customer")
    public List<Customer> getCustomer() {
    	log.debug("Getting all the customer..");
        return service.getCustomer();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
    	log.debug("Getting the Customer with id ["+ id +"]");
        return service.getCustomer(id);
    }
    
    @PostMapping("/customer")
    public Map save(@RequestBody @Validated CustomerEvent customer) {
    	log.debug("Persisting the Customer details ["+ customer +"]");
    	String value = service.save(customer);
    	return Map.of(
    		"message", value,
    		"_link", "/customer"
    	);
    }
    
    @PutMapping("/customer/{id}")
    public Map update(@RequestBody @Validated CustomerEvent customer, @PathVariable("id") Long id) {
    	customer.setId(id);
    	log.debug("updating the Customer details ["+ customer +"]");
    	String value = service.update(customer);
    	return Map.of(
    		"message", value,
    		"_link", "/customer/"+id
    	);
    }
}
