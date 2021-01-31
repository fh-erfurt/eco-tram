# PlantUML Text Diagrams for EcoTram

To convert the text diagrams into uml-images you can use http://www.plantuml.com/.
Paste the contents with `@startuml` and `@enduml` into the textarea and click on submit to generate the diagram.

### Event Dispatch
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
### General Tram Movement
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

### Repository Factory
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