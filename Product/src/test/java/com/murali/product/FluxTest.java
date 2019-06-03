package com.murali.product;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class FluxTest {

	public static void main(String[] args) throws Exception {
		
		//Hooks.onOperatorDebug();
		/*
		System.out.println("------- range ------------");
		Flux.range(1, 7)
		.map(val -> {
			System.out.println("first map["+ Thread.currentThread().getName()+"] val ["+ val +"]");
			if(val <= 5) return val;
			else throw new RuntimeException("reached 6");
		})
		.elapsed()
		.checkpoint()
		.publishOn(Schedulers.newSingle("my-single"))
		.checkpoint()
		.map(val -> {
			System.out.println("second map["+ Thread.currentThread().getName()+"] val ["+ val +"]");
			return val;
		})
		.limitRate(2)
		.limitRequest(7)
		.subscribeOn(Schedulers.newParallel("my-parallel-schedular"))
		.checkpoint()
		.subscribe(
				(val) -> {
					System.out.println("subscribe consumer["+ Thread.currentThread().getName()+"] ["+ val+"]");
					
				}, 
				error -> {
					error.printStackTrace();
					//System.err.println("subscribe error["+ Thread.currentThread().getName()+"] ["+error.getMessage()+"]");
				}, 
				() -> System.out.println("subscribe finished["+ Thread.currentThread().getName()+"] "),
				sub -> {
					sub.request(8);
				}
		);
		*/
		
		/*
		System.out.println("------- generate ------------");
		Flux.generate(AtomicLong::new, 
				(num, generator) -> {
					long val = num.getAndIncrement();
					generator.next("3 x " + val + " = " + 3*val);
					if(val == 10) generator.complete();
					return num;
				}
		)
		.subscribe(System.out::println);
		*/
		
		/*
		Flux.interval(Duration.ofMillis(250))
			.map(val -> {
				System.out.println(Thread.currentThread().getName());
				if(val < 5) return "value is "+ val;
				else throw new RuntimeException("boom");
			})
			.retry(1)
			.elapsed()
			.subscribe(System.out::println, System.out::println);
		Thread.sleep(2100);
		*/
		
		/*
		Flux<String> flux = Flux	
			    .<String>error(new IllegalArgumentException("retry")) 
			    .doOnError(err -> {
			    	System.out.println("---- do on error -----");
			    })
			    .log()
			    .retryWhen(errorFlux -> {
			    	System.out.println("---- retryWhen -----");
			    	return errorFlux.map( error -> {
			    		System.out.println("---- inside retryWhen errorFlux ["+ error.getMessage() +"] ------");
			    		Optional<String> errorOpt = Optional.ofNullable(error.getMessage());
			    		return errorOpt.filter(x -> x.contains("retry"))
			    				.map(message -> {
			    					System.out.println("---- inside retryWhen errorOpt map ["+ message +"] ------");
			    					return Flux.just("empty");
			    				})
			    				.orElseGet(() -> {
			    					System.out.println("---- inside retryWhen errorOpt orElseGet ------");
			    					return Flux.just("empty");
			    				});
			    	});
			    });
		flux.subscribe(System.out::println, System.err::println);
		*/
		
		/*
		Flux<String> flux = Flux
			    .<String>error(new IllegalArgumentException("retry"))
			    .log()
			    .retryWhen(errorFlux -> errorFlux
			    .zipWith(Flux.<Integer>range(1, 5), 
			    	(message, value) -> {
			    		if(message.equals("retry")) return value;
			    		else return message;
			    	})
			    );
		flux.subscribe(System.out::println, Throwable::printStackTrace);
		*/
		
		AtomicInteger ai = new AtomicInteger();
		
		Function<Flux<String>, Flux<String>> filterAndMap = f -> {
			int val = ai.incrementAndGet();
			if (val == 1) {
				return f.filter(color -> { 
						System.out.println(val + " - "+ color);
						return !color.equals("orange");
					})
					.map(String::toUpperCase);
			}
			return f.filter(color -> { 
						System.out.println(val + " - "+ color);
						return !color.equals("purple"); 
					})
			        .map(String::toUpperCase);
		};

		Flux<String> composedFlux = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
		    .doOnNext(System.out::println)
		    .transform(filterAndMap);
		composedFlux.log().subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :"+d));
		System.out.println("----------------------------------");
		composedFlux.log().subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: "+d));
	}
}
