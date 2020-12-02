# Day 7. SpringApplication 커스터마이징과 Admin 기능(MBeans)

#### Web Enviroment

ApplicationContext의 타입을 커스터마이징 할 수 있다.

이전에는 `setWebEnvironment(webEnvironment)` 를 사용하여 web이면 true 아니면 false값을 주어 사용했었는데,

현재는 reactive가 추가되어 `setWebApplicationType(WebApplicationType)`을 사용한다.

아래와 같이 3가지 타입이 있다.

```java
public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.setWebApplicationType(WebApplicationType.NONE);
	app.setWebApplicationType(WebApplicationType.REACTIVE);
	app.setWebApplicationType(WebApplicationType.SERVLET);
}
```

 <br/>

#### Application Arguments에 접근하기

**HelloService.java**

```java
@Service
public class HelloService {
	
	@Autowired
	ApplicationArguments args;
	
	/*
	 * --hello=Hello -hello=World
	 * => ["Hello", "World"]
	 * => "Hello, World" return
	 */
	public String getMsg() {
		List<String> helloValues = args.getOptionValues("hello");
		return helloValues.stream().collect(Collectors.joining(", "));
	}

}
```



**Application.java**

```java
@SpringBootApplication
@RestController
public class Application {
    @Autowired
	HelloService helloService;
	
	@RequestMapping("/")
	public String hello() {
		return helloService.getMsg();
	}
    
    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
	}
}
```

`mvn package` 후 `jar`파일 실행하여

`--hello=Hello` `--hello=World` 입력

혹은

main메서드에서 args를 아래와 같이 변경 후 실행

```java
SpringApplication app = new SpringApplication(Application.class);
app.run("--hello=Hello","--hello=World");
```



![image-20201202161105898](C:\Users\psa06\AppData\Roaming\Typora\typora-user-images\image-20201202161105898.png)

또 다른 방법 :  `@Value`를 사용하여 주입하기

```java
@Service
public class HelloService {
	
	@Autowired
	ApplicationArguments args;
	
    @Value("${hello}")
    String[] helloValues;
	/*
	 * --hello=Hello -hello=World
	 * => ["Hello", "World"]
	 * => "Hello, World" return
	 */
	public String getMsg() {
		return Arrays.stream(helloValues).collect(Collectors.joining(", "));
	}

}
```

<br/>

#### ApplicationRunner 또는 CommandLineRunner 사용하기

스프링 어플리케이션이 실행되기전 특정 코드를 실행해야한다면 Runner를 사용한다.

Listener와 비슷한 개념이지만 Listener는 빈으로 사용할 수 없다는 점!



**ApplicationRunner**

```java
@Component
public class MyBean implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
```

**CommandLineRunner**

```java
@Component
public class MyBean implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}
```

둘의 차이는 args의 타입차이인데 ApplicationArguments로 받는것이 더 편리하다.

<br/>

Runner 2개이상 사용시 순서 부여하는 법

**ApplicationRunnerFirst.java**

```java
@Component
@Order(0)//낮은숫자가 우선순위가 높음
public class ApplicationRunnerFirst implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("first runner");
	}

}
```

**ApplicationRunnerSecond.java**

```java
@Component
@Order(1)
public class ApplicationRunnerSecond implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("second runner");
	}

}

```

![image-20201202164857418](C:\Users\psa06\AppData\Roaming\Typora\typora-user-images\image-20201202164857418.png)

순서대로 리스너 - 러너1 - 러너2가 호출됐다.

<br/>

#### Application Exit

Exit Code를 커스텀하여 출력하기

`ExitCodeGenerator`인터페이스를 구현한다.

```java
@SpringBootApplication
public class ExitCodeApplication {

	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 42;
	}

	public static void main(String[] args) {
		System.exit(SpringApplication
				.exit(SpringApplication.run(ExitCodeApplication.class, args)));
	}

}
```

레퍼런스에 있는 이 방법은 run시키자마자 exit시키는 코드이고

[에러가 났을때 ExitCode 사용하는 방법](https://www.logicbig.com/tutorials/spring-framework/spring-boot/app-exit-code.html)

이 방법은 에러를 던지고, 에러 클래스에서 ExitCodeGenerator 인터페이스를 구현하고,

Exit 이벤트 리스너를 만들어서 그 이벤트에서 exit코드를 꺼내 출력하는 방법이다.

<br/>

#### 관리자 기능

`spring.application.admin.enabled` 을 켜서 스프링 부트 어플리케이션의 관리자 기능을 원격으로 사용할 수있다.

property에 `spring.application.admin.enabled=true` 를 추가하고 어플리케이션을 실행한다.

cmd를 통해 `jconsole`로 접속하여 애플리케이션 메모리,쓰레드도 보고 MBeansServer에 등록되어있는 SpringApplication을 찾아서 shutdown 을 시킬 수가 있다.