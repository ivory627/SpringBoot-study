package me.ivory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.ivory.configuration.ServiceConfig;

@RestController
/*
@EnableAutoConfiguration
@Configuration
@ComponentScan
위 3개를 다 포함하는 어노테이션 =@SpringBootApplication
*/
//@Import(ServiceConfig.class)
@ImportResource("application.xml")
@SpringBootApplication
public class Example {
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Example.class, args);
	}
}
