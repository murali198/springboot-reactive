package com.murali.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.murali.customer.core.AddressService;
import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
    @GetMapping("/customer/{id}/address")
    public List<Address> getCustomerAddress(@PathVariable("id") Long id) {
    	log.debug("Getting the Customer address with id ["+ id +"]");
        return addressService.getCustomerAddress(id);
    }

    @GetMapping("/address/{id}")
    public Address getAddress(@PathVariable("id") Long id) {
    	log.debug("Getting the address with id ["+ id +"]");
    	return addressService.getAddress(id);
    }
    
    @PostMapping("/customer/{id}/address")
    public Customer saveAddress(@RequestBody @Validated Address address, @PathVariable("id") Long id) {
    	log.debug("save address for the customer id ["+ id +"]");
    	return addressService.saveAddress(address, id);
    }
}
