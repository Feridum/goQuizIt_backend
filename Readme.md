# GoQuizIt Backend

Restful CRUD API for a GoQuizIt application using Spring Boot, PostgreSQL, JPA and Hibernate.

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. Mysql - 5.x.x

## Steps to Setup

**1. Create PostgreSQL database**
```bash
create database postgres
```

**3. Change PostgreSQL username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn spring-boot:run
```

**4. Application api**
http://localhost:8082/swagger-ui.html

The app will start running at <http://localhost:8082>.
