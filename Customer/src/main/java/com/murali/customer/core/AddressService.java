package com.murali.customer.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.murali.common.exception.ResourceNotFoundException;
import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;
import com.murali.customer.repo.AddressRepository;
import com.murali.customer.repo.CustomerRepository;

@Component
public class AddressService {

	@Autowired
    private AddressRepository repository;
	
	@Autowired
    private CustomerRepository customerRepo;
	
	public Address getAddress(Long id) {
    	return repository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
	
	public List<Address> getCustomerAddress(Long id) {
		return customerRepo.findById(id)
				.map(cust -> cust.getAddress())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }
	
	public Customer saveAddress(Address address, Long customerId) {
		return customerRepo.findById(customerId)
		.map(cust -> {
			List<Address> addresses = cust.getAddress();
			addresses.add(address);
			Customer customer = Customer.builder()
					.id(customerId)
					.customerId(cust.getCustomerId())
					.email(cust.getEmail())
					.mobile(cust.getMobile())
					.name(cust.getName())
					.surName(cust.getSurName())
					.address(addresses)
					.build();
			return customerRepo.save(customer);
		})
		.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
	}
}
