package com.murali.customer;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;

import com.murali.customer.entity.Address;
import com.murali.customer.entity.Customer;
import com.murali.customer.repo.CustomerRepository;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
	
	@Bean(name = "applicationEventMulticaster")
	public ApplicationEventMulticaster applicationEventMulticaster() {
	    SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
	    eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor("customer-app"));
	    eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER);
	    return eventMulticaster;
	}
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public void run(String... args) throws Exception {
		Iterable<Customer> custList =  Arrays.asList(
				Customer.builder().customerId("101").name("Murali").surName("P").email("murali@gmail.com").address(
						Arrays.asList(
								Address.builder().city("chennai").country("India").number("9/10").street("Palaniyappa Street").name("Gray").build(),
								Address.builder().city("chennai").country("India").number("1/44").street("Devikarumari Street").name("Raja").build()
						)
				).build(),
				Customer.builder().customerId("102").name("Raja").surName("Guru").email("raja@gmail.com").address(
						Arrays.asList(
								Address.builder().city("Chennai").country("India").number("9/10").street("Periyar Street").name("Gray").build()
						)
				).build(),
				Customer.builder().customerId("103").name("Muthu").surName("Kumaran").email("muthu@gmail.com").build()
		);
		customerRepo.saveAll(custList);
	}
}
