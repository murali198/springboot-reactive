package com.murali.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.murali.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
