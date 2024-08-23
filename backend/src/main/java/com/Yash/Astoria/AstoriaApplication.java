package com.Yash.Astoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.Yash.Astoria")
public class AstoriaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AstoriaApplication.class, args);
	}
}
