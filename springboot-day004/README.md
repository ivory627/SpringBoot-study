# Day 4. @SpringBootApplication과 XML 빈 설정 파일 사용하기

#### Importing XML Configuration

xml로 빈 설정파일 등록하기 (XML Schema-style)

resources/application.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- bean definitions here -->
	<bean id="userService" class="me.ivory.user.UserService"></bean>
    
</beans>
```

main이 있는 클래스에 @ImportResource("application.xml") 추가



## Auto-configuration

@SpringBootApplication 어노테이션 사용

= @Configuration, @EnableAutoConfiguration, @ComponentScan 을 포함



- 적용된 autoConfiguration확인하기

1. cmd에서 확인 방법

   `mvn package` 

   `java -jar target/springboot-day001-0.0.1-SNAPSHOT.jar --debug`

2. console에서 확인 방법

   ```java
   public static void main(String[] args) {
   		SpringApplication.run(Example.class, "--debug");
   	}
   ```



- 특정 autoConfiguration 제외시키기

1. main 클래스에서 어노테이션을 사용해 설정

   ```
   @SpringBootApplication(exclude = ...)
   ```

2. resources/yml , properties에서 설정

   - yml 

     ```
     spring:
       autoconfigure:
         exclude:
     ```

   - properties

     ```
     spring.autoconfigure.exclude
     ```



## Spring Beans & Dependency Injection

Example Test - 4일차만에 새로운 프로젝트 생성 ..

**pom.xml**에 `<parent>`,`<dependency>` 추가

```xml
<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.2.RELEASE</version>
</parent>
```

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
</dependency>
```



Main Class

**Application.java**

```java
package me.ivory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```



**UserService.java**

```java
package me.ivory.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}
}
```

:bulb: 생성자가 하나일 경우 @Autowired가 없어도 프레임워크가 알아서 의존성을 주입해준다.



**UserRepository.java**

```java
package me.ivory.user;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

}
```



**ApplicationTest.java**

```java
package me.ivory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import me.ivory.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	
	@Autowired
	UserService userService;
	
	@Test
	public void testDI() {
		Assert.assertNotNull(userService);
		Assert.assertNotNull(userService.getUserRepository());
		
	}
}
```



## Running Your Application

어플리케이션을 jar파일로 패키징하고 임베디드 HTTP 서버를 사용하면 좋은 점

- 다른 사람이 실행하는 것처럼 어플리케이션을 똑같이 실행할 수 있다.
- 디버깅이 쉽다.
- 다른 IDE 플러그인이나 확장이 필요하지않다.



#### 실행방법

1. IDE : `run`
2. 패키징된 어플리케이션 : `java -jar target/myApplication-0.0.1-SNAPSHOT.jar`
3. Maven : `mvn spring-boot:run`
4. Gradle : `gradle bootRun`



## Developer Tools

Maven

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>
</dependencies>
```

Gradle

```
dependencies {
	compile("org.springframework.boot:spring-boot-devtools")
}
```