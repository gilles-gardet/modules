# Spring Modulith Demo

This project demonstrates the use of **Spring Modulith** to build a modular monolith architecture.

## What is Spring Modulith ?

Spring Modulith is a framework that helps implement **modular monolith architectures** by:
- Enforcing module boundaries within a single application
- Providing architectural verification tools and testing capabilities
- Supporting event-driven communication between modules
- Enabling gradual migration paths from monolith to microservices

### Key Principle
Instead of creating microservices from the start (with all the associated network, deployment, and monitoring complexity), Spring Modulith allows you to create a modular application within a single JAR, with the possibility to extract modules into microservices later if needed.

## Project Architecture

This project implements a dummy **library management system** with 3 distinct modules:

- Author Module (`com.ggardet.modulith.author`)
- Book Module (`com.ggardet.modulith.book`)
- Core Module (`com.ggardet.modulith.core`)

## Spring Modulith Annotations Used

`@ApplicationModule`

This annotation explicitly marks a package as a Spring Modulith module, allows advanced module configuration (if needed), improves readability and makes architectural intent clear.

`@NamedInterface`

This annotation defines a named interface for the module, specifying what is exposed to other modules.  
It helps in controlling module boundaries and encapsulation.

**Advantage**: Instead of relying solely on Java package visibility, these annotations make architectural intentions explicit and testable.

## Event System

To avoid tight coupling between modules, the project uses an event-driven communication system provided by Spring Modulith.  
This brings several benefits:

- Strong Decoupling (so no more cyclic dependencies)
- Extensibility (adding functionality without modifying existing code)
- Robustness (events are automatically persisted and can be replayed)

To be noticed that this system also provide transaction safety.  
Indeed the events are published within the same transaction, in case of rollback, events are not processed.

> [!NOTE]
> **Concrete Example in the Project**
>
> When an author is deleted:
>
> 1. **Author Module** → Publishes `AuthorDeleted`
> 2. **Book Module** → Receives the event via `@EventListener`  
> 3. **Book Module** → Automatically deletes all books by that author
> 4. **System** → Logs the operation and persists the event
>
> The Author module doesn't need to know about the Book module's existence, but business consistency is maintained.

## Architectural Verification

The project includes automated tests to verify modular architecture compliance:

```bash
# Verify module boundaries
./mvnw test -Dtest=ModulithArchitectureTests

# Test event-driven communication  
./mvnw test -Dtest=EventCommunicationTest
```

## Resources

- [Spring Modulith Documentation](https://docs.spring.io/spring-modulith/reference/)
- [Events Guide](https://docs.spring.io/spring-modulith/reference/events.html) 
