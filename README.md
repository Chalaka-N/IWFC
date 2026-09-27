# IWFC Management System

Java prototype for CMP 7001 Advanced Programming — Intelligent Wellness and Fitness Center (IWFC).

## Current architecture focus
- OOP: abstraction, encapsulation, inheritance, polymorphism
- Collections: `HashMap`, `ArrayList`
- Generics: `GenericRepository<T>`
- Creational Pattern: Factory (`UserFactory`)
- Structural Pattern: Facade (`IWFCFacade`)
- Behavioural Pattern: Observer (`MaintenanceObserver` + `NotificationService`)
- Custom exceptions
- JUnit 5 unit tests
- Console demonstration

## Build
```bash
mvn clean test
```

## Run
```bash
mvn -q exec:java -Dexec.mainClass=com.iwfc.app.ConsoleApp
```

Note: the exec plugin has not been added yet; for now, run `ConsoleApp` directly from VS Code.
