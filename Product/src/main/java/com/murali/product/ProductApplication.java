package com.murali.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.murali.product.entity.Product;
import com.murali.product.repo.ProductRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableWebFlux
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Bean
	CommandLineRunner loadProduct(ProductRepository repository) {
		return args -> {
			Flux<Product> productFlux = Flux.just(
					new Product(null, "Dell", "Electronics", "Touchpad", "74000.00", "Dell Laptop"),
					new Product(null, "Lenova", "Electronics", "Y40", "47000.00", "Lenova Laptop"))
					.flatMap(repository::save);
			
			productFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		};
	}
}
