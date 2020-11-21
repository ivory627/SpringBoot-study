# Day 5. spring-boot-devtools 그리고 릴로딩

#### Property Default

> 개발에 편리한 기본 설정
>
> 가령 캐시를 끄거나, H2 디비 툴을 띄워주는 설정 활성화

캐시 옵션은 보통 `application.properties`파일에 설정하는데,

`spring-boot-devtools`모듈을 추가해주면 자동으로 적용해준다.



resources/static/index.html

```html
<!DOCTYPE html>
<html>
<head>
<title>Getting Started: Serving Web Content</title>
<meta http-equiv="Content-Type" content="text/html;" charset="UTF-8">
</head>
<body>
<p>Get your greeting <a href="/greeting">here</a></p>
</body>
</html>
```

`<p>`태그 안 문구를 바꾸면서 devtools를 사용할 때와 안할 때를 비교하려했는데

사용하나 안하나 새로고침시 너무나 잘 적용되버려서 테스트 실패 (... ㅋㅋㅋ)



#### Automatic Restart

> 클래스패스를 바꿀때마다 자동적으로 restart
>
> static 자원, view template 들과 같은 resource들은 restart 할 필요가 없다.
>
> 단  `spring-boot`, `spring-boot-devtools`,`spring-boot-autoconfigure`,`spring-boot-actuator`,`spring-boot-starter`로 시작하는 이름들은 restart되지 않음



예를들어 이 서비스를

```java
@Service
public class HelloService {

    public String getMessage() {
        return "Hello Spring Boot!";
    }
}
```

이렇게 메인에서 불러와 실행시킨 후

```java
@SpringBootApplication
@RestController
public class Application {
    
    @Autowired
    HelloService helloService;
    
    @RequestMapping("/")
    public String hello() {
        return helloService.getMessage();
    }
}
```

화면에서 확인하고 코드를 살짝 수정하여 저장하면

```java
@Service
public class HelloService {

    public String getMessage() {
        return "Hello Spring Boot 2.0";
    }
}
```

바로 restart되어 화면에서 변경된 것을 확인할 수 있다.



#### :bulb: cold start / restart / reload 비교

- cold-start : 완전히 껐다가 재시작

 

- Restart

  2개의 classloader를 이용.

  - JAR 파일은 base 클래스 로더로 읽기

  - 애플리케이션 클래스들은 restart 클래스 로더로 읽고 모니터링 하다가 그 클래스 로더를 버리고 다시 만든다.

  어플리케이션을 재시작할 때 restart 클래스로더만 새로 읽어들인다.

  그래서 cold start 보다 빠름

 

- Reload

  여전히 느리다거나, 클래스로더에 이슈가 발생한다면 reload를 이용할 수 있다.

  - `JRebel`이 제공해줌

  스태틱 리소스가 바뀔 때 브라우저 reload 시켜주는 서버를 자동으로 띄워준다

  - 사용하는 브라우저에 LiveLoad플러그인을 설치해야 동작함



#### Logging 비활성화

```
spring.devtools.restart.log-condition-evaluation-delta=false
```



#### Resources 제외

특정 파일이 변경될때 restart trigger 비활성화

기본적으로 `/META-INF/maven`, `/META-INF/resources`, `/resources`, `/static`, `/public`,  `/templates` 리소스를 변경하면 restart가 된다.

ex ) `/static`,`/public`만 제외하고 싶을때

```
spring.devtools.restart.exclude=static/**,public/**
```

ex) 위 기본값은 유지하고 다른 제외를 추가하고 싶을때 속성

```
spring.devtools.restart.additional-exclude
```



#### Watching Additional Paths

클래스 경로에 없는 파일의 변화를 감지해서 애플리케이션을 다시 시작하거나 다시 로드 할 수 있다.  아래 속성을 사용하여 변경 사항을 감시 할 추가 경로를 등록한다.

```
spring.devtools.restart.additional-paths
```



#### Restart 비활성화

`application.properties`파일의 `spring.devtools.restart.enabled`속성 을 사용하여 비활성화

완전히 비활성화하고 싶을 때

```java
public  static  void main (String [] args) { 
	System.setProperty ( "spring.devtools.restart.enabled" , "false" ); 
	SpringApplication.run (MyApp. class , args); 
}
```



#### 특정 파일이 바뀔 때 restart하기

IDE에서 변경사항이 있을때마다 restart되는것이 싫다면

`spring.devtools.restart.trigger-file`속성을 사용하여 파일경로설정



#### Customizing the restart classloader

재시작 기능은 두 클래스 로더(base,restart)를 사용하여 구현되어 있다. 대부분의 어플리케이션에서 이런 방법은 잘 작동하지만 때때로 클래스 로딩 이슈를 유발할 수 있다.

기본적으로 IDE에서 열린 프로젝트는 “restart” 클래스 로더를 사용하여 로드하고 모든 일반 `.jar` 파일은 “base” 클래스 로더를 사용하여 로드한다. 멀티 모듈 프로젝트를 진행하고 각 모듈은 IDE에 포함되어있지 않다면 사용자 정의가 필요하다. `META-INF/spring-devtools.properties` 파일을 생성해서 진행한다.

`spring-devtools.properties` 파일은 `restart.exclude.`와 `restart.include.`를 포함할 수 있다. `include` 요소는 “restart” 클래스로더로 가져오고 `exclude` 요소는 “base” 클래스로더에서 내려와야 하는 요소이다. 속성의 값은 클래스패스를 적용하는 정규식 패턴이다.

```
restart.exclude.companycommonlibs=/mycorp-common-[\\w-]+\.jar
restart.include.projectcommon=/mycorp-myproj-[\\w-]+\.jar
```



#### LiveReload

리소스가 변경될때 브라우저 리프레시를 유발할 수 있는 내장된 LiveReload 서버를 포함한다. LiveReload 브라우저 확장 모듈은 크롬, 파이어폭스, 사파리에 대해서 [livereload.com](http://livereload.com/extensions/)에서 무료로 사용가능하다.

LiveReload 서버를 사용하고 싶지 않으면 어플리케이션을 실행할때 `spring.devtools.livereload.enable` 속성을 `false`로 설정하면 된다.



#### 전역설정

`$HOME`디렉터리에 `.spring-boot-devtools.properties`파일을 추가하여 전역설정을 할 수있다. 이 파일에 추가하는 모든 속성은 DevTools를 사용하는 모든 스프링부트 어플리케이션에 적용된다.

ex) 아래 속성을 추가하여 trigger file을 사용해서 항상 restart하기

**~/.spring-boot-devtools.properties.** 

```
spring.devtools.reload.trigger-file=.reloadtrigger
```



#### 원격 어플리케이션

스프링부트에 원격지원은 포함되어있고 활성화하려면 `devtools`가 패키지 아카이브에 포함되어있는지 확인해야한다.

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<configuration>
				<excludeDevtools>false</excludeDevtools>
			</configuration>
		</plugin>
	</plugins>
</build>
```

secret속성도 추가

```
spring.devtools.remote.secret=mysecret
```