package com.murali.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.murali.customer.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	
}
