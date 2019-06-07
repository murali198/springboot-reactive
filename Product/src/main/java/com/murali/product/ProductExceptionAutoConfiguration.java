package com.murali.product;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.murali.product.exception.GlobalErrorAttribute;
import com.murali.product.exception.ErrorResponseComposer;
import com.murali.product.exception.handler.AbstractExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@AutoConfigureBefore({ValidationAutoConfiguration.class})
@ComponentScan(basePackageClasses=AbstractExceptionHandler.class)
@Slf4j
public class ProductExceptionAutoConfiguration {

	public ProductExceptionAutoConfiguration() {
		log.info("Created");
	}
	
	@Bean
	@ConditionalOnMissingBean(ErrorResponseComposer.class)
	public <T extends Throwable> ErrorResponseComposer<T> errorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {
        log.info("Configuring ErrorResponseComposer");       
		return new ErrorResponseComposer<T>(handlers);
	}
	
	@Bean
	@ConditionalOnMissingBean(ErrorAttributes.class)
	public <T extends Throwable> ErrorAttributes errorAttributes(ErrorResponseComposer<T> errorResponseComposer) {
        log.info("Configuring ErrorAttribute");       
		return new GlobalErrorAttribute<T>(errorResponseComposer);
	}
	
}
