package com.murali.customer.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.murali.common.exception.ResourceNotFoundException;
import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;
import com.murali.customer.repo.AddressRepository;
import com.murali.customer.repo.CustomerRepository;
import com.murali.customer.type.CustomerEvent;

@Service
public class CustomerService {

	@Autowired
    private CustomerRepository repository;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public List<Customer> getCustomer() {
        return repository.findAll();
    }
	
	public Customer getCustomer(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }
	
	public String save(CustomerEvent customer) {
		customer.setAction("save");
		eventPublisher.publishEvent(customer);
		return "Customer registration request submited!!";
	}
	
	public String update(CustomerEvent customer) {
		customer.setAction("update");
		eventPublisher.publishEvent(customer);
		return "Customer updation request submited!!";
	}
}
