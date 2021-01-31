#Eco-Tram
###Program Description
* data model and base business logic for a tram management system
* uses event-based communication between single entities of the tram network, eg: Tram, Station, TrafficManager, etc.
* written with extensibility in mind
---
###Installation
<details>
  <summary>Dependencies</summary>

  * JRE 15
  * JDK 15
  * Maven
  * J-Unit
</details>
<details>
  <summary>Installation Steps</summary>

* `git clone https://github.com/fh-erfurt/eco-tram.git`
* `mvn build`
* `mvn test`
</details>

---
###General Design Decisions
* Usage of Singleton-Pattern for extensibility of currently static objects
* Usage of Repository- and Factory-Pattern for easier testing and entity instantiation
* Loose coupling of entities through usage of an EventManager as communication interface
* Testing of single functionalities through unit tests for easier development and debugging

---
###Program Flow Diagrams
####Event Dispatch
```plantuml
@startuml
    actor Tram
    TrafficManager -> EventManager : register Listener for Tram: `Line finsihed`
    TrafficManager -> Tram : assign Tram to Line
    activate Tram
    Tram -> EventManager : dispatch Event: `Line finsihed`
    deactivate Tram
    activate EventManager
    EventManager -> Tram : Listener assignes Tram to new line
    deactivate EventManager
    activate Tram
@enduml
```

<details>
  <summary>See more</summary>

####General Tram Movement
```plantuml
@startuml
    actor Tram
    -> Tram : addLine(Line)
    -> Tram : moveForward()
    activate Tram
    Tram -> TramInternal : Traversable left?
    TramInternal -> Tram : true
    Tram -> TramInternal : Increment position
    <-- Tram : true
    deactivate Tram
    -> Tram : moveForward()
    activate Tram
    Tram -> TramInternal : Traversable left?
    TramInternal -> Tram : false
    Tram -> TramInternal : Path left?
    TramInternal -> Tram : true
    Tram -> TramInternal : Reset positionIndex, set currentPath
    <-- Tram : true
    deactivate Tram
    -> Tram : moveForward()
    activate Tram
    Tram -> TramInternal : Traversable left?
    TramInternal -> Tram : false
    Tram -> TramInternal : Path left?
    TramInternal -> Tram : false
    <-- Tram : false
    deactivate Tram
@enduml
```
####RepositoryFactory
```plantuml
@startuml
    actor Caller
    actor Connection
    Caller -> RepositoryFactory : getConnectionRepository()
    RepositoryFactory -> Caller : ConnectionRepository
    Caller -> ConnectionRepository : getEntityById(id)
    ConnectionRepository -> Caller : Connection
    activate Connection
    Caller -> Connection : use Connection
@enduml
```
</details>

---
###Code Examples
####Registering an EventListener
```java
Tram tram;
Line continuationLine;

// get the event entity tied to the trams traffic manager
// it is the instance the event will be emitted to
// see: Tram.java:205
var eventEntity = tram
        .getEventManager()
        .getEventEntity(tram.getTrafficManager());

// add listener for tram finished event
eventEntity.addListener("TRAM_PATH_END_REACHED",
        (entity, event, data) -> {
            // the line will have finished the line and get a new line
            // assigned for continuation
            ((Tram) entity).addLine(continuationLine);
        }
);
```
<details>
    <summary>See more</summary>

####Usage of Repositories
```java
var tramRepository = RepositoryFactory.getInstance().getTramRepository();
var lineRepository = RepositoryFactory.getInstance().getLineRepository();

var tram = (PassengerTram) tramRepository.getEntityById(1);
var line = (Line) lineRepository.getEntityById(1);

// assigns the tram to it's first path
tram.addLine(line);

// the tram moves forward until no traversables are left
while (true)
{
    // check if the tram could move forward
    if (tram.moveForward());
        System.out.println(tram.getCurrentPosition().getName());
    else {
        System.out.println("Reached end of line."); // an event will also fire for this
        break;
    }
}
```

</details>

---