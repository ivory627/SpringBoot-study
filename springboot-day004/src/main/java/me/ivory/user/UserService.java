package me.ivory.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	//@Autowired
	//생성자가 하나일 경우 생략해도 프레임워크가 의존성을 알아서 주입해준다.
	//그러나 웬만하면 명시적으로 써주자!
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}
}
