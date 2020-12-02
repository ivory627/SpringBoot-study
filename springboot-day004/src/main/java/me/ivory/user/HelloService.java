package me.ivory.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
	
	@Autowired
	ApplicationArguments args;
	
	/*
	 * --hello=Hello --hello=World
	 * => ["Hello", "World"]
	 * => "Hello, World" return
	 */
	public String getMsg() {
		List<String> helloValues = args.getOptionValues("hello");
		return helloValues.stream().collect(Collectors.joining(", "));
	}

}
