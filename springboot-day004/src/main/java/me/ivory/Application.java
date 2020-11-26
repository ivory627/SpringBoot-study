package me.ivory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.ivory.user.UserService;

@SpringBootApplication
@RestController
public class Application {
	
//	@Autowired
//	UserService userService;
	
	@RequestMapping("/")
	public String hello() {
		return "Hello";
	}
	
	public static void main(String[] args) {
		//1. SpringApplication 객체를 생성해서 사용하는 방법
		SpringApplication app = new SpringApplication(Application.class);
		app.setBanner(new MyBanner());
		app.addListeners(new MyListener());
		app.run(args);
		
		//2. 한줄로 끝내는 방법
		//SpringApplication.run(Application.class, args);
		
		//차이점은 위처럼 set~를 통해 세부 설정을 할 수 있다.
	}
}
