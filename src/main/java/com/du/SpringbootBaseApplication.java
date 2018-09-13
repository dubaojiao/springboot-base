package com.du;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages={"com.du.*"})
@EntityScan("com.du.domain")
@EnableJpaRepositories("com.du.repos")
public class SpringbootBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBaseApplication.class, args);
	}
}
