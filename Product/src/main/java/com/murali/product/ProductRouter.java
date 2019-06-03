package com.murali.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import com.murali.product.core.ProductHandler;

@Configuration
public class ProductRouter {

	@Bean
	public RouterFunction<ServerResponse> routes(ProductHandler handler) {
		return route(GET("/product").and(accept(MediaType.APPLICATION_JSON)), handler::getProduct)
				.andRoute(GET("/product/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getProductById)
				.andRoute(POST("/product").and(accept(MediaType.APPLICATION_JSON)).and(contentType(MediaType.APPLICATION_JSON)), handler::saveProduct)
				.andRoute(PUT("/product/{id}").and(accept(MediaType.APPLICATION_JSON)).and(contentType(MediaType.APPLICATION_JSON)), handler::updateProduct)
				.andRoute(DELETE("/product/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::deleteProduct);
	}
	
	/*@Bean
	public RouterFunction<ServerResponse> routes(ProductHandler handler) {
		return nest(path("/product"), 
				nest(accept(MediaType.APPLICATION_JSON).or(contentType(MediaType.APPLICATION_JSON)), 
						route(GET("/"), handler::getProduct)
						.andRoute(POST("/"), handler::saveProduct)
						.andRoute(PUT("/"), handler::updateProduct)
						.andNest(path("/{id}"), 
								route(GET("/"), handler:: getProductById)
								.andRoute(DELETE("/"), handler::deleteProduct)
							)
					)
			);
	} */
}
