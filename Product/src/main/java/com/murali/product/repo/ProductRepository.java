package com.murali.product.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.murali.product.entity.Product;

import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
	
	Flux<Product> findByName(String name);
	
	Flux<Product> findByCategory(String category);

}
