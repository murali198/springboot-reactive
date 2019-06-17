package com.murali.customer;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.murali.common.exception.ErrorResponseComposer;
import com.murali.common.exception.handler.AbstractExceptionHandler;
import com.murali.customer.exception.CustomerErrorAttribute;

import lombok.extern.slf4j.Slf4j;

@Configuration
@AutoConfigureBefore({ValidationAutoConfiguration.class})
@ComponentScan(basePackageClasses=AbstractExceptionHandler.class)
@Slf4j
public class CustomerExceptionAutoConfiguration {

	public CustomerExceptionAutoConfiguration() {
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
		return new CustomerErrorAttribute<T>(errorResponseComposer);
	}
	
}
