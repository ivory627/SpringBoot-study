package me.ivory.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.ivory.user.UserService;

//설정파일 등록하는 방법 2 : @ComponentScan + @Configuration파일 @Bean
//설정파일 등록하는 방법 3 : @Import + @Configuration파일 @Bean
//@Configuration
public class ServiceConfig {
	
	@Bean
	public UserService userService() {
		return new UserService();
	}
}
