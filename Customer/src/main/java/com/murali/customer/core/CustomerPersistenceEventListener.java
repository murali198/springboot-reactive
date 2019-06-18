package com.murali.customer.core;

import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.murali.common.exception.ResourceNotFoundException;
import com.murali.customer.entity.Customer;
import com.murali.customer.repo.CustomerRepository;
import com.murali.customer.type.CustomerEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerPersistenceEventListener {

	@Autowired
	private CustomerRepository repository;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Async
	@EventListener(condition = "#event.action eq 'save'")
	public void save(CustomerEvent event) {
		log.debug("called save customer event method, input is["+ event +"]");
		Optional<Customer> customer = Stream.of(event).map(cust -> {
			return Customer.builder()
					.customerId(cust.getCustomerId())
					.email(cust.getEmail())
					.mobile(cust.getMobile())
					.name(cust.getName())
					.surName(cust.getSurName()).build();
		})
		.map(entity -> repository.save(entity))
		.collect(toSingleton());
		
		customer.ifPresentOrElse(cust -> eventPublisher.publishEvent(cust), () -> new ResourceNotFoundException("Problem is registering customer"));
		log.debug("doen customer registration");
	}
	
	@Async
	@EventListener(condition = "#event.action eq 'update'")
	public void update(CustomerEvent event) {
		log.debug("called update customer event method, input is["+ event +"]");
		Customer customer = Optional.<Long>of(event.getId())
			.flatMap(custId -> repository.findById(custId))
			.map(cust -> {
				return Customer.builder()
						.id(cust.getId())
						.customerId(event.getCustomerId())
						.email(event.getEmail())
						.mobile(event.getMobile())
						.name(event.getName())
						.surName(event.getSurName())
						.address(cust.getAddress())
						.build();
			})
			.map(entity -> repository.save(entity))
			.orElseThrow(() -> new ResourceNotFoundException());
		
		eventPublisher.publishEvent(customer);
		
	}

	public static <T> Collector<T, ?, Optional<T>> toSingleton() {
		return Collectors.collectingAndThen (
					Collectors.toList(),
					list -> list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty()
				);
	}
}
