# Day 8. 프로퍼티와 각종 외부 설정의 우선 순위

### Externalized Configuration

동일한 애플리케이션을 다른 환경에서도 사용할 수 있도록 설정파일을 외부에서도 관리할 수 있게 해줌

- properties 파일
- YAML 파일
- 환경 변수
- command-line arguments

를 통해 관리가 가능하고 `@Value` , Spring `Enviroment`, `@ConfigurationProperties`로 빈을 만들어 접근이 가능하다.

<br/>

#### :heavy_check_mark: 프로퍼티 외부설정의 우선순위

1. devtools 사용시 홈 디렉터리 `~/.spring-boot-devtools.properties`
2. 테스트시  `@TestPropertySource`  
3. 테스트시 `@SpringBootTest#properties`
4. 위와 같은 특별한 경우를 제외시 **Command line arguments**
   - `java -jar --name=ivory`
5. `SPRING_APPLICATION_JSON` 프로퍼티
   - `--SPRING_APPLICATION_JSON={"name":"ivory"}`
6. `ServletConfig` init 파라미터
7. `ServletContext` init 파라미터
8. `java:comp/env` JNDI attributes
9. Java 시스템 프로퍼티 `System.getProperties()`
   - 혹은 `java -jar -Dname=ivory`
10. OS 환경변수
11. `RandomValuePropertySource` (`random.*`)
12. jar 패키지 밖 Profile 어플리케이션  `application-{profile}.properties` and YAML variants
13. jar 패키지 안 Profile 어플리케이션 `application-{profile}.properties` and YAML variants
14. jar 패키지 밖 어플리케이션 프로퍼티 `application.properties` and YAML variants
15. jar 패키지 안 어플리케이션 프로퍼티 `application.properties` and YAML variants
16. `@Configuration` 클래스 안 `@PropertySource`
17. 디폴트 프로퍼티 `SpringApplication.setDefaultProperties`

<br/>

**example** : `@Component`를 사용하여 `name`프로퍼티를 지정할때

application.properties에 `name=ivory` 추가

```java
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;

@Component
public class MyBean {

    @Value("${name}")
    private String name;

    // ...
}
```

어플리케이션 실행

<img width="180" alt="1" src="https://user-images.githubusercontent.com/58761162/102458261-5540ba00-4087-11eb-9398-3a1363a9a8a9.PNG">

<br/>

application.properties의 name을 커맨드라인 argument로 오버라이딩하는방법

`java -jar app.jar --name="Spring"` jar 패키지 실행

```java
app.run("--name=Spring");
```

혹은 메인메서드에서 위와같이 애플리케이션 실행

그럼 우선순위에 따라 커맨드라인 argument가 출력된다.

<img width="182" alt="2" src="https://user-images.githubusercontent.com/58761162/102458263-5671e700-4087-11eb-801e-1e05e22e7607.PNG">

<br/>

#### :heavy_check_mark: Configuring Random Values

properties 파일에 다음과 같은 예시로 넣는다.

```properties
my.secret=${random.value}
my.number=${random.int}
my.bignumber=${random.long}
my.uuid=${random.uuid}
my.number.less.than.ten=${random.int(10)}
my.number.in.range=${random.int[1024,65536]}
```

<br/>

#### :heavy_check_mark: Application Property Files

SpringApplication은 `application.properties`파일을 다음 순서대로 프로퍼티들을 읽어들인다.

1. 현재 디렉터리 아래 있는 `/config` 서브디렉터리 (`file:./config/`)
2. 현재 디렉터리 (`file:./`)
3. 패키지 안 클래스패스 `/config` (`classpath:/config/`)
4. 루트 클래스패스 (`classpath:/`)

`application.properties`파일의 이름을 바꾸고 싶다면,

`spring.config.name` 환경 변수를 바꾼다.

`application.properties`파일의 위치를 바꾸고 싶다면,

`spring.config.location`환경 변수를 바꾼다.

하지만 굳이 바꾸지않고 많은 사람들이 쓰는 위치와 이름을 쓰는것이 좋다!!

**example**

```
$ java -jar myproject.jar --spring.config.name=myproject
```

아래와 같이 2개 이상을 나열하면 뒤에서부터 우선순위가 높다.

```
$ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```

또 다른 오버라이딩 방법은 `spring.config.additional-location` 설정

검색 우선순위는 아래와 같다.

1. `file:./custom-config/`
2. `classpath:custom-config/`
3. `file:./config/`
4. `file:./`
5. `classpath:/config/`
6. `classpath:/`

<br/>

#### :heavy_check_mark: Profile-specific Properties

어플리케이션 프로퍼티 파일과 더불어 다음과 같이 프로파일 설정 프로퍼티도 정의 가능

`application-{profile}.properties` 

액티브할 프로파일이 없다면 `application-default.properties`를 기본으로 읽어들인다.

`spring.profiles.active=A,B`를 설정하면 B가 출력된다.

<br/>

#### :heavy_check_mark: Placeholders in Properties

`application.properties`의 값은 기존의 존재하는 `Enviroment`를 통해 필터링 되므로

이전에 정의된 값을 참조할 수 있다.

```properties
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

**example**

```properties
foo=${random.uuid}
name=ivory ${foo}
```

실행화면

<img width="311" alt="3" src="https://user-images.githubusercontent.com/58761162/102458266-5671e700-4087-11eb-8d53-f13e97ea8c0e.PNG">

