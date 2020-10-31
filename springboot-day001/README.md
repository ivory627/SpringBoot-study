# Day1. 스프링부트 시작하기



강의에서 참고하는 Reference :page_facing_up:

[Springboot Reference 2.0.2](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#getting-started-maven-installation)



## 설치

**spring-boot-*.jar** 파일을  classpath에 추가하면 스프링부트가 설치된다.

즉, Gradle이나 Maven을 통해 의존성을 추가하면 된다.



 maven은 pom.xml에서 상속관계를 만들 수 있는데,

`<parent>`를 사용하여 부모를 명시해두면

부모 프로젝트에 있는 pom.xml 안 내용들을 자식프로젝트가 상속받을 수 있다.

=> **의존성 관리 차원**

dependency들의 spring-boot 버전들을 명시하지 않아도

parent의 version을 따르도록 해준다.

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
</parent>
```



## 실습

#### 1. pom.xml에 의존성 추가하기

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>me.ivory</groupId>
	<artifactId>springboot-day001</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<!-- Inherit defaults from Spring Boot -->
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.2.RELEASE</version>
	</parent>

	<!-- Add typical dependencies for a web application -->
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>

	<!-- Package as an executable jar -->
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```



#### 2. Controller 생성하기

```java
package me.ivory.springboot_day001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Example {
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Example.class, args);
	}
}

```



#### 3. 실행하기

<img width="344" alt="2" src="https://user-images.githubusercontent.com/58761162/97782567-8e0af980-1bd5-11eb-839d-baf88efe1d50.PNG">

주소창에 `localhost:8080`입력

<img width="181" alt="1" src="https://user-images.githubusercontent.com/58761162/97782614-e04c1a80-1bd5-11eb-9ee9-b663df0f178c.PNG">

성공적 :clap:



## Stereotype annotation

- `@Controller` : mvc컨트롤러임을 명시
- `@Service` : 비즈니스로직 서비스레이어 명시
- `@Repository` : DAO 클래스 명시
- `@Component` : 스프링이 관리하는 컴포넌트 명시



+++슨생님 보충설명

`@RestController` (스프링 애노테이션): 해당 클래스가 컨트롤러라고 스프링한테 알려주는 애노테이션 (Controller+ResponseBody)

`@RequestMapping` (스프링 애노테이션): HTTP 요청 라우팅 애노테이션 

`@EnableAutoConfiguration` (스프링 부트 애노테이션): 스프링 부트의 기본 설정을 적용하도록 명시하는 애노테이션으로,

그 매커니즘 중 하나로 프로젝트의 의존성에 따라 특정한 기본 설정 파일이 적용이 된다.

