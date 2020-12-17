package me.ivory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.ivory.user.HelloService;
import me.ivory.user.UserService;

@SpringBootApplication
@RestController
public class Application {
	
//	@Autowired
//	UserService userService;
	
	@Autowired
	HelloService helloService;
	
	@RequestMapping("/")
	public String hello() {
		return helloService.getMsg();
	}
	
	public static void main(String[] args) {
		//1. SpringApplication 객체를 생성해서 사용하는 방법
		SpringApplication app = new SpringApplication(Application.class);
		//배너설정
		app.setBanner(new MyBanner());
		//리스너설정
		app.addListeners(new MyListener());
		//웹환경설정
		//app.setWebApplicationType(WebApplicationType.NONE);
		//app.setWebApplicationType(WebApplicationType.REACTIVE);
		//app.setWebApplicationType(WebApplicationType.SERVLET);
		
		//커맨드라인 arguments
//		app.run("--hello=Hello","--hello=World");
		app.run();
		
		//2. 한줄로 끝내는 방법
		//SpringApplication.run(Application.class, args);
		
		//차이점은 위처럼 set~를 통해 세부 설정을 할 수 있다.
	}
}
