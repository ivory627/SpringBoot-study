# Day 6. 배너 그리고 SpringApplication

#### SpringApplication

SpringApplication 클래스는 Spring application을 main메서드에서 실행하는 편리한 방법을 제공해준다.

```java
public static void main(String[] args) {
	SpringApplication.run(MySpringConfiguration.class, args);
}
```

<img width="435" alt="1" src="https://user-images.githubusercontent.com/58761162/100322044-01eac700-3007-11eb-8c11-30dba3c190cc.PNG">

기본적으로 INFO 레벨의 로깅 메세지를 보여준다.

다른 레벨의 로깅 메세지를 보이고 싶다면,

Spring환경설정파일 (ex. `application.properties`)에서

`logging.level.<logger-name>=<level>`이러한 방식으로 작성해준다.

level에는 **TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF **가 있다.

`root` logeer는 `logging.level.root`

ex)

```properties
logging.level.root=WARN
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
```



**application.properties**

```properties
logging.level.root=WARN
```

<img width="401" alt="2" src="https://user-images.githubusercontent.com/58761162/100322055-06af7b00-3007-11eb-90bc-b8d947a20970.PNG">

로깅 레벨을 WARN만 설정해줬더니 아무것도 찍히지 않는 모습을 볼 수 있다.

(WARN외에 찍히지 않는것. 경고할게 없어서 WARN도 찍히지 않는것)

---

#### Startup Failure

어플리케이션 실행시 실패한다면, 등록된 `FailureAnalyzers`가

에러 메세지와 함께 어떻게 고쳐야하는지 방법을 알려준다.

예를들어 이미 사용중인 포트번호8080을 통해 실행시킨다면 아래와 같이 출력된다.

```
***************************
APPLICATION FAILED TO START
***************************

Description:

The Tomcat connector configured to listen on port 8080 failed to start. The port may already be in use or the connector may be misconfigured.

Action:

Verify the connector's configuration, identify and stop any process that's listening on port 8080, or configure this application to listen on another port.
```

스프링부트는 여러  `FailureAnalyzers`를 제공해주는데 직접 추가도 가능

 `FailureAnalyzers`가 예외를 잡지못하는 경우에는

`org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener`에 DEBUG 로깅을 설정해주거나 args에 --debug로 확인

```properties
org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener=DEBUG
```

```
$ java -jar myproject-0.0.1-SNAPSHOT.jar --debug
```



서비스 클래스에 추가

```java
//빈을 만들고나서 자동으로 호출되는 애노테이션
	@PostConstruct
	public void init() {
		throw new RuntimeException("Intended Exception");
	}
```



메인클래스에 추가

```java
@Autowired
UserService userService;
```



실행시키면

<img width="747" alt="3" src="https://user-images.githubusercontent.com/58761162/100322056-07481180-3007-11eb-9b27-b363dce097c7.PNG">

이렇게 오류에 관한 full conditions report를 보여준다.

---

#### Banner 커스터마이징하기

**텍스트**로 설정시

파일명 `banner.txt`

경로 `spring.banner.location`

인코딩은 UTF-8로 설정이 안되어있을 경우 `spring.banner.charset`로 바꿔준다.





**이미지**로 설정시

파일명 `banner.gif`, `banner.jpg`, `banner.png`

경로 `spring.banner.image.location`

이미지같은 경우 ASCII로 변환되어 텍스트배너 위에 프린트된다.



`banner.txt` 에 다음과같은 `placeholder`들을 사용할 수 있다.

| Variable                                                     | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `${application.version}`                                     | MANIFEST.MF 에 있는 Application version                      |
| `${application.formatted-version}`                           | MANIFEST.MF 에 있는 Application formatted version `Implementation-Version`로 추가 |
| `${spring-boot.version}`                                     | 스프링부트버전 ex.`2.0.2.RELEASE`                            |
| `${spring-boot.formatted-version}`                           | 스프링부트 형식화버전 ex.`(v2.0.2.RELEASE)`                  |
| `${Ansi.NAME}` (or `${AnsiColor.NAME}`, `${AnsiBackground.NAME}`, `${AnsiStyle.NAME}`) | ANSI코드사용 ex. Ansi.YELLOW                                 |
| `${application.title}`                                       | MANIFEST.MF 에 있는 Application title `Implementation-Title`로 추가 |



[배너만들기](http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20)

Font : ANSI Shadow

**resources/banner.txt**

```
██╗██╗   ██╗ ██████╗ ██████╗ ██╗   ██╗
██║██║   ██║██╔═══██╗██╔══██╗╚██╗ ██╔╝
██║██║   ██║██║   ██║██████╔╝ ╚████╔╝ 
██║╚██╗ ██╔╝██║   ██║██╔══██╗  ╚██╔╝  
██║ ╚████╔╝ ╚██████╔╝██║  ██║   ██║   
╚═╝  ╚═══╝   ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
Spring boot : ${spring-boot.formatted-version}
```

요렇게 작성해주고 실행시키면 배너가 바뀌는 모습을 볼 수 있다.

<img width="359" alt="4" src="https://user-images.githubusercontent.com/58761162/100322057-07e0a800-3007-11eb-8ece-62c84824d548.PNG">

`${application.formatted-version}`은 안찍힐텐데

MANIFEST.MF 에서 가지고 오기때문에 jar로 패키징해야한다.



**pom.xml**에 추가

```xml
<project>
  ...
 <build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludeDevtools>false</excludeDevtools>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
						<manifestEntries>
							<Implementation-Version>1.0.0</Implementation-Version>
						</manifestEntries>
					</archive>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>2.19.1</version>
				<configuration>
					<testFailureIgnore>true</testFailureIgnore>
				</configuration>
			</plugin>
		</plugins>
	</build>
	<pluginRepositories>
		<pluginRepository>
			<id>spring-snapshots</id>
			<url>https://repo.spring.io/snapshot</url>
		</pluginRepository>
		<pluginRepository>
			<id>spring-milestones</id>
			<url>https://repo.spring.io/milestone</url>
		</pluginRepository>
	</pluginRepositories>
</project>
```

쌤은 maven jar plugin과 plugrepository만 추가해줬는데

나는 package할때 자꾸 maven surefire plugin 오류가 나서 방법을 찾아 추가해줬다.

성공적으로 packge한후 jar파일을 실행하면

![6](https://user-images.githubusercontent.com/58761162/100322477-acfb8080-3007-11eb-8a6d-20b977578586.png)

요렇게 Application version도 잘 뜬다.. :happy:





배너를 txt파일이 아닌 코드로도 작성할 수 있는데

**MyBanner.java**

```java
package me.ivory;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

public class MyBanner implements Banner {

	@Override
	public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
		out.println("my little banner");
	}

}
```

**Main class**

```java
public static void main(String[] args) {
		//1. SpringApplication 객체를 생성해서 사용하는 방법
		SpringApplication app = new SpringApplication(Application.class);
		app.setBanner(new MyBanner());
		app.run(args);
		
		//2. 한줄로 끝내는 방법
		SpringApplication.run(Application.class, args);
		
		//같은방법이지만 차이점은 객체를 생성하면 위처럼 set~를 통해 세부설정(커스터마이징)을 할 수 있다.
	}
```

`setBanner`와 `Banner.txt`를 같이 사용하면 txt파일이 출력된다.





**배너모드**

`spring.main.banner-mode`속성 설정

- System.out(console)
- logger(log)
- off

---

#### SpringApplication 커스터마이징하기

**배너 비활성화하기**

```java
public static void main(String[] args) {
	SpringApplication app = new SpringApplication(MySpringConfiguration.class);
	app.setBannerMode(Banner.Mode.OFF);
	app.run(args);
}
```



**application.properties사용하기**

ch24에서 다시



**계층구조만들기**

고전.. 요즘은 안쓰인다고한다.

`ApplicationContext` 계층(부모/자식관계)을  빌드해야할때

`SpringApplicationBuilder`사용하면 다음과 같이 계층을 생성 할 수있는 여러 메서드 호출과 포함 `parent`및 `child`메서드를 연결할 수 있다.

```java
new SpringApplicationBuilder()
		.sources(Parent.class)
		.child(Application.class)
		.bannerMode(Banner.Mode.OFF)
		.run(args);
```

**제한사항**

Web 컴포넌트는 `child` 내에 포함 되어야 하며

`child`와 `parent`는 동일한 `Environment` 사용



#### Application 이벤트, 리스너

이벤트종류

- ApplicationStartingEvent
- ApplicationEnvironmentPreparedEvent
- ApplicationPreparedEvent
- ApplicationStartedEvent
- ApplicationReadyEvent
- ApplicationFailedEvent



리스너가 bean으로 등록되지 않을 수 있는데, 모든 bean들은 **ApplicationContext** 에서 관리가 된다.

일부 이벤트들은 **ApplicationContext** 가 생성되기 전에 트리거 되기 때문에 리스너가 빈으로 등록되지 않는다.

따라서 다른 방법으로 등록해야 한다.

#### 리스너 등록하는 방법

1. `SpringApplication.addListeners(…)`
2. `SpringApplicationBuilder.listeners(…)`
3. `META-INF/spring.factories` 에 `org.springframework.context.ApplicationListener=com.example.project.MyListener` 추가





**MyListener.java**

```java
package me.ivory;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class MyListener implements ApplicationListener<ApplicationStartedEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		System.out.println("APPLICATION IS STARTED");
	}

}
```



**Main Class**

```java
public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBanner(new MyBanner());
		app.addListeners(new MyListener());
		app.run(args);
	}
```



**실행화면**

<img width="260" alt="5" src="https://user-images.githubusercontent.com/58761162/100322058-08793e80-3007-11eb-9fbb-57e86f39aaff.PNG">