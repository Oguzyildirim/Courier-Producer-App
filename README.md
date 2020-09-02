#  Courier producer Application

## Development

To start your application in the dev profile, run:

    ./gradlew

App has a very basic UI and there is a link for REST endpoint of open API docs, and under /api/users you can find User related endpoints(i will share postman docs) and at /management you can find spring actuator endpoints.

### API-First development

OpenAPI-Generator is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:

```bash
./gradlew openApiGenerate
```

## Building for production

### Packaging as jar

    To run MySQL in a docker I have dockerized application above you can find docker commands

    ./gradlew -Pprod clean bootJar

    To run packaged app:
		java -jar build/libs/*.jar


## Testing

Tests:

    ./gradlew test tests jacocoTestReport

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Sonar analysis

```
// I have an error here, I cloudn't solve it, working on it.
./gradlew -Pprod clean check jacocoTestReport sonarqube
```



## Using Docker to simplify development (optional)

Start a mysql database:

    docker-compose -f src/main/docker/mongodb.yml up -d

To stop it and remove:

    docker-compose -f src/main/docker/mysql.yml down

Fully dockerize application

    ./gradlew bootJar -Pprod jibDockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d

## Continuous Integration (optional)

For simplicity there is no CI/CD pipeline but since this application already dockerized, Service instance per container pattern would be great.

## Possible improvements

- One of the best improvement for scalability, we must be running this application on a non-blocking I/O, since the app  using classic Spring MVC we can adopt Reactive Spring (Spring WebFlux, Spring Cloud Stream, Spring Data Reactive Repositories and Spring Security Reactive) but also we can also use other platforms(Eclipse vertx etc.).

- The app using swagger and open api standarts but does not have UI, we should consume swagger endpoint with service discovery and implement a swagger UI.

- The app using spring actuator the create healt check points and metrics, and again we should have a tools like prometheus.

- We may add liquibase for version control database.

- We must add circuit breaker(resilience4j, hystrix, failsafe etc.) to handle failure.

- Since it is demo app there is no cache strategy.

- I was considering adding ELK stack with docker but I thought that it is over complex for this application but I can write a ElasticSearchConfig. And also in a distributed architecture centralized logging is must.

- To improve java performance and run it faster we can use GraalVM

- We should use external configuration for this app spring cloud config would be a good choice.

- The app has no security mechanism but we should use advantage of Statelessness of application so when it comes to implementing security we should use token based auth.

- In a distributed architectures there are lots of ways to implements application, I believe real benefits of this kind of architectures is gives you to flexibility but as a cost it returns with great complexity so there should be a lot to say like adding message queues or to maintain data consistency implement Saga pattern etc.


