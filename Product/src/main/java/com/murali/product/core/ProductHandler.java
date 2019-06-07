package com.murali.product.core;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.murali.product.entity.Product;
import com.murali.product.repo.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ProductHandler {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ProductValidationHandler handler;
	
	public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
		
		Optional<String> nameOpt = serverRequest.queryParam("name");
		log.debug("Getting the product name ["+ nameOpt +"]");
		
		return nameOpt.map(name -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(repository.findByName(name), Product.class))

				.orElseGet(() -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(repository.findAll(), Product.class)); 
	}
	
	public Mono<ServerResponse> getProductById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		log.debug("Getting the product by id["+ id + "]");
		return repository.findById(id)
				.flatMap (
						product -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(product))
						)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {
		return handler.process(productinput -> {
					log.debug("save product ["+ productinput + "]");
					return productinput.flatMap (
							product -> ServerResponse.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(repository.save(product), Product.class));
				}, 
				serverRequest, 
				Product.class);
		/*
		Mono<Product> productinput = serverRequest.bodyToMono(Product.class);
		log.debug("save product ["+ productinput + "]");
		return productinput.flatMap (
				product -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(repository.save(product), Product.class));
		*/
	}

	public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
		return handler.process(productMono -> {
					String id = serverRequest.pathVariable("id");
					log.debug("Update the product by id["+ id + "]");
					Mono<Product> existingProductMono = repository.findById(id);
					
					return existingProductMono.zipWith(productMono, 
							(existingProduct, product) -> new Product(existingProduct.getId(), product.getName(), product.getCategory(),product.getProductId(), product.getPrice(), product.getDescription()))
							.flatMap(product -> ServerResponse.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(repository.save(product), Product.class))
		
							.switchIfEmpty(ServerResponse.notFound().build());
				}, 
				serverRequest, 
				Product.class);
		/*
		String id = serverRequest.pathVariable("id");
		log.debug("Update the product by id["+ id + "]");
		Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
		Mono<Product> existingProductMono = repository.findById(id);

		return existingProductMono.zipWith(productMono, 
				(existingProduct, product) -> new Product(existingProduct.getId(), product.getName(), product.getCategory(),product.getProductId(), product.getPrice(), product.getDescription()))

				.flatMap(product -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(repository.save(product), Product.class))

				.switchIfEmpty(ServerResponse.notFound().build());
		*/
	}

	public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		log.debug("delete the product by id["+ id + "]");
		return repository.findById(id).
				flatMap (
						product -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.build(repository.delete(product))
						)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

}
