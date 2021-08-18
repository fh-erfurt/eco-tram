![Header](readme-header.jpg "Eco Tram Header")

# Eco-Tram

### Program Description

* data model and base business logic for a passengerTram management system
* uses event-based communication between single entities of the passengerTram network, eg: Tram, Station, TrafficManager, etc.
* integrated simulation for existing lines with passenger trams passing by each station 
* Open API with CRUD-Functionality (Create, Read, Update, Delete)
* Frontend Dashboard for managing all models and simulation
* Powerful simulation with custom parameters
* written with extensibility in mind

---
### Installation

<details>
  <summary>Dependencies</summary>

* [JRE 15/JDK 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html)
* [Maven](https://maven.apache.org/)
* [J-Unit](https://mvnrepository.com/artifact/junit/junit)
* [Spring-Framework](https://spring.io/projects/spring-framework)
* [MySQL Connector Java](https://dev.mysql.com/downloads/connector/j/)
* [H2 Database](https://www.h2database.com/html/main.html)
* [Lombok](https://projectlombok.org/)
* [SocketIO](https://github.com/mrniko/netty-socketio)
* [JSON](https://mvnrepository.com/artifact/org.json/json)
* [Vue.JS](https://vuejs.org/)
* [SockJS](https://github.com/sockjs/sockjs-client)

</details>

<details>
  <summary>Installation Steps</summary>

#### Installation Backend

* install mysql 8.0
* run mysql
* create test user
* clone repo (`git clone https://github.com/fh-erfurt/eco-passengerTram.git`)
* import test db (`database.sql`)
* build and run with Maven

#### Installation Frontend

* install dependencies (`npm install` or `yarn install`)
* run server (`npm run serve` or `yarn serve`)

</details>

---
### Ports

Backend-Server and API: `Port 8080`  
Frontend-Server and Dashboard: `Port 8081`

---
### Troubleshooting

*Question*: I cannot see any statistics and other data in the frontend

*Answer*: Maybe you have set the wrong port in the configuration. You can edit everything in the given frontend/src/config.ts file

---

*Question*: In simulation the window seems to freeze

*Answer*: Your browser might not be optimized for such big datapacks which are transferred every tick. Maybe increase the tick interval or reduce other values

---
### General Design Decisions

* Usage of Autowire for easier class injection and initialization (with beans)
* Usage of JPA-Repositories for easier testing and entity instantiation
* Consistent design of handlers for models connected with controllers (usage of middleware)
* Usage of uniformly pagination result for easier model pagination in controller
* Configurable simulation for testing lines, stations and passenger trams
* User friendly frontend dashboard for managing all models
* Testing of single functionalities through unit tests for easier development and debugging
