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
		Mono<Product> productinput = serverRequest.bodyToMono(Product.class);
		log.debug("save product ["+ productinput + "]");
		return productinput.flatMap (
				product -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(repository.save(product), Product.class));
	}

	public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		log.debug("Update the product by id["+ id + "]");
		Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
		Mono<Product> existingProductMono = repository.findById(id);

		return existingProductMono.zipWith(productMono, 
				(product, existingProduct) -> new Product(existingProduct.getId(), product.getName(), product.getDescription(), product.getCategory(),product.getProductId(), product.getPrice()))

				.flatMap(product -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(repository.save(product), Product.class))

				.switchIfEmpty(ServerResponse.notFound().build());
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
