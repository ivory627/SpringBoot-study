# Day 2. Executable JAR 어떻게 만들고 어떻게 동작하는가

Executable jars (**fat jars**) : 컴파일된 클래스와 프로그램 실행에 필요한 모든 의존jar파일을 포함한다.



실행 가능한 jar파일을 만들기 위해서 `spring-boot-maven-plugin`을 pom.xml에 추가해주어야한다.

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```

추가 후 cmd에 `mvn package` 입력

<img width="356" alt="5" src="https://user-images.githubusercontent.com/58761162/97861711-879c8f00-1d47-11eb-9fca-57c99ff1c57e.PNG">

그럼 빌드에 성공한다.

빌드에 실패했다면 **[Window] - [Preferences] - Java - Installed JREs**에서 jre를 jdk로 바꿔준다.

그러고나서도 빌드가 안된다면 **[Project] - Clean** 후 다시 빌드해보기!

빌드에 성공하여 프로젝트폴더 안 target폴더에 가보면

<img width="524" alt="6" src="https://user-images.githubusercontent.com/58761162/97861812-b1ee4c80-1d47-11eb-865c-9c3226c55429.PNG">

이렇게 jar파일이 생겨있다.

실행하는 방법은 cmd에서 jar파일이 있는 target폴더 경로에서 `java -jar jar파일이름.jar`입력

<img width="384" alt="7" src="https://user-images.githubusercontent.com/58761162/97862461-bcf5ac80-1d48-11eb-8859-a1b6b84a3cf5.PNG">

역시나 `localhost:8080`을 주소창에 입력하면 "Hello World!"가 잘 뜬다.

종료방법

1. `ctrl+c`
2. `netstat -ano | find "8080"`으로 특정 포트를 사용하고 있는 프로세스 확인 후
   `taskkill /pid 프로세스ID /f` 로 종료 시키면 된다.



## 스프링부트 사용하기

#### 빌드 시스템

`Dependency Management`
스프링부트가 의존성관리를 지원해준다. 즉 추가하는 의존성들의 버전 명시를 따로 하지 않아도 된다.
버전을 명시해줘도 되지만 스프링 프레임워크의 버전 변경은 권장하지않는다.
스프링부트가 스프링프레임워크에 강력하게 의존하고 있기 때문이다.



`spring-boot-starter-parent` default

- Java 1.8
- UTF-8
- Dependency Manangement section
- resource filtering
- plugin configuration
- resource filtering for `application.properties` and `application.yml`



+++Q&A

라이브러리/프레임워크 버전 차이

- SNAP-SHOT : 데일리 빌드 버전
- M(Milestone) : 테스트버전
- RC(Release Candidate) : 베타버전
- GA(General Availability) : 정식 릴리즈 버전으로 가장 안정적이며 maven 중앙 저장소에 올라간다.
