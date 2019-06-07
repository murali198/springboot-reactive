package com.murali.customer;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;
import com.murali.customer.repo.AddressRepository;
import com.murali.customer.repo.CustomerRepository;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
	
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private AddressRepository addressRepo;
	
	@Override
	public void run(String... args) throws Exception {
		Iterable<Customer> custList =  Arrays.asList(
				new Customer(1L, "101", "Murali", "P", "murali@gmail.com"),
				new Customer(2L, "102", "Raja", "Guru", "raja@gmail.com"),
				new Customer(3L, "103", "Muthu", "Kumaran", "muthu@gmail.com")
		);
		customerRepo.saveAll(custList);
		
		Iterable<Address> addressList =  Arrays.asList(
				Address.builder().id(1L).city("Chennai").country("India").customer(1L).number("9/10").street("Palaniyappa Street").name("Gray").build(),
				Address.builder().id(1L).city("Chennai").country("India").customer(1L).number("1/44").street("Devikarumari Street").name("Raja").build(),
				Address.builder().id(2L).city("Chennai").country("India").customer(1L).number("9/10").street("Periyar Street").name("Gray").build(),
				Address.builder().id(3L).city("Chennai").country("India").customer(1L).number("9/10").street("Ambethkar Street").name("Raja").build()
		);
		addressRepo.saveAll(addressList);
	}
	
}
