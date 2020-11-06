package me.ivory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import me.ivory.Example;
import me.ivory.user.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Example.class)
public class ExampleTest {
	
	@Autowired
	UserService userService;
	
	@Test
	public void di() {
		Assert.assertNotNull(userService);
	}
}
