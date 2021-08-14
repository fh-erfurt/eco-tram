![Header](readme-header.jpg "Eco Tram Header")

# Eco-Tram
### Program Description
* data model and base business logic for a tram management system
* uses event-based communication between single entities of the tram network, eg: Tram, Station, TrafficManager, etc.
* integrated simulation for existing lines with passenger trams passing by each station 
* Open API with CRUD-Functionality (Create, Read, Update, Delete)
* Frontend Dashboard for managing all models and simulation
* written with extensibility in mind
---
### Installation
<details>
  <summary>Dependencies</summary>

* [JRE 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html)
* [JDK 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html)
* [Maven](https://maven.apache.org/)
* [J-Unit](https://mvnrepository.com/artifact/junit/junit)
* [Spring-Framework](https://spring.io/projects/spring-framework)
* [MySQL Connector Java](https://dev.mysql.com/downloads/connector/j/)
* [H2 Database](https://www.h2database.com/html/main.html)
* [Lombok](https://projectlombok.org/)
* [SocketIO](https://github.com/mrniko/netty-socketio)
</details>
<details>
  <summary>Installation Steps</summary>

* `git clone https://github.com/fh-erfurt/eco-tram.git`
* `cd backend`  
* `mvn build`
* `mvn test`
* `cd ../frontend`
* `npm install` or `yarn install`
* `npm run serve` pr `yarn serve`
</details>

---
### Ports

Backend-Server and API: `Port 8081`

Frontend-Server and Dashboard: `Port 8080`

---
### General Design Decisions
* Usage of Autowire for easier class injection and initialization (with beans)
* Usage of JPA-Repositories for easier testing and entity instantiation
* Consistent design of handlers for models connected with controllers (usage of middleware)
* Usage of uniformly pagination result for easier model pagination in controller  
* Configurable simulation for testing lines, stations and passenger trams
* User friendly frontend dashboard for managing all models  
* Testing of single functionalities through unit tests for easier development and debugging
