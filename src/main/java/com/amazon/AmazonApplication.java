package com.amazon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(basePackages = "com.amazon.demo")
//@EnableJpaRepositories("com.amazon.demo.repositories")
//@EntityScan("com.amazon.demo.models")
public class AmazonApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(AmazonApplication.class, args);
		
	}

}