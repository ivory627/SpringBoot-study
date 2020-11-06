# Day3. 스프링 부트 스타터

:heavy_check_mark:**official** 

`spring-boot-starter-*`로 시작,  *에는 특정 애플리케이션의 타입이 들어간다.


:heavy_check_mark:**Custom starter**

third party : 일반적으로 프로젝트 이름으로 시작,
예를들어 프로젝트 이름이 `thirdpartyproject`라면 `thirdpartyproject-spring-boot-starter`이다.



Spring Boot starter는 편리한 의존성 관리를 위해 다음 구성요소들이 포함된 라이브러리이다.

- `autoconfigure` : 자동 설정 코드가 포함 된 모듈
- `starter` :  autoconfigure 모듈을 포함한 다른 다수의 의존성들도 제공한다. starter를 추가함으로써 모든 라이브러리가 제공된다.



#### :link:[Spring Boot applications starters](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/htmlsingle/#using-boot-starter)



#### Spring Boot technical starters

:heavy_check_mark:**servlet container**

`spring-boot-starter-tomcat` : default . jetty나 undertow로 바꿀 수 있다

`spring-boot-starter-jetty`

`spring-boot-starter-undertow`

`spring-boot-starter-reactor-netty`

:heavy_check_mark:**logger**

`spring-boot-starter-logging` : default (Logback). log4j2로 바꿀 수 있다.

`spring-boot-starter-log4j2`



## 코드 구조화

Spring Boot는 특정 코드구조를 강요하지 않는다.

그러나 다음 사항들을 권장한다.



#### :heavy_check_mark:Packaging

디폴트 패키지는 권장하지않으니 패키지를 만들어줘야한다.
`@ComponentScan`,`@EntityScan`,`@SpringBootApplication`어노테이션을 사용할 때
디폴트 패키지에있는 모든 클래스를 검사하게되기때문이다.



#### :heavy_check_mark:Main Application Class의 위치

root 패키지에 위치시키는 것을 권장한다.
보통 메인클래스에 `@SpringBootApplication`,`@EnableAutoConfiguration `어노테이션을 두어 기본적으로 검색할 패키지가 되므로, 그 패키지 아래서만 `@Entity`가 붙은 클래스를 스캔한다.

```
com 
 +- example
     +- myapplication (Root package)
         +- Application.java (Main Application Class)
         |
         +- customer
         |   +- Customer.java
         |   +- CustomerController.java
         |   +- CustomerService.java
         |   +- CustomerRepository.java
         |
         +- order
             +- Order.java
             +- OrderController.java
             +- OrderService.java
             +- OrderRepository.java
```





## Configuration Classes

설정방법은 Java , XML 이 있는데, 스프링 부트 에서는 Java로 설정하기를 추천한다.

하나의 @Configuration 클래스를 가지는것을 추천하며, 보통 main메소드가 있는 클래스에 추가한다.



#### Bean 설정방법 (설정파일 추가방법) 3가지

1. @ComponentScan + stereotype annotation (@Service, @Repository, @Controller ...)

   ```java
   @RestController
   @EnableAutoConfiguration
   @Configuration
   @ComponentScan
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

   ```java
   @Service
   public class UserService {
   
   }
   ```

2. @ComponentScan + @Configuration + @Bean

   ```java
   @RestController
   @EnableAutoConfiguration
   @Configuration
   @ComponentScan
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

   ```java
   @Configuration
   public class ServiceConfig {
   	
   	@Bean
   	public UserService userService() {
   		return new UserService();
   	}
   }
   ```

3. @Import + @Configuration + @Bean

   ```java
   @RestController
   @EnableAutoConfiguration
   @Configuration
   @Import(ServiceConfig.class)
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

   ```java
   @Configuration
   public class ServiceConfig {
   	
   	@Bean
   	public UserService userService() {
   		return new UserService();
   	}
   }
   ```

   

이 중 한가지 방법으로 통일하여 사용하는것이 좋다.
