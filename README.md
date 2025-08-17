# Spring Modulith Library Management Demo

This project demonstrates the use of **Spring Modulith** to build a robust modular monolith architecture in the context of a library management system.

## What is Spring Modulith?

**Spring Modulith** is a framework that helps implement modular monolith architectures by:
- **Enforcing module boundaries** within a single application
- **Providing architectural verification tools** and testing capabilities
- **Supporting event-driven communication** between modules
- **Enabling gradual migration paths** from monolith to microservices

### Key Principle
Instead of creating microservices from the start (with all the associated network, deployment, and monitoring complexity), Spring Modulith allows you to create a modular application within a single JAR, with the possibility to extract modules into microservices later if needed.

## Project Architecture

This project implements a **library management system** with 3 distinct modules:

### Author Module (`com.ggardet.modulith.author`)
- Author management (creation, modification, deletion)
- **Public API**: `AuthorApi.kt` for DTOs and events
- **Published events**: `AuthorCreated`, `AuthorUpdated`, `AuthorDeleted`

### Book Module (`com.ggardet.modulith.book`) 
- Book management (complete CRUD)
- **Reacts to events**: Listens to `AuthorDeleted` to remove orphaned books
- **Public API**: `BookApi.kt` for DTOs and events  
- **Published events**: `BookCreated`, `BookUpdated`, `BookDeleted`

### Core Module (`com.ggardet.modulith.core`)
- Security configuration and event system management
- Shared utilities and cross-cutting concerns

## Spring Modulith Annotations Used

### `@ApplicationModule`
```kotlin
@PackageInfo
@NamedInterface(name = ["book"])
@ApplicationModule
class ModuleMetadata
```

The `@ApplicationModule` annotation:
- **Explicitly marks a package as a Spring Modulith module**
- Allows **advanced module configuration** (if needed)
- **Improves readability** and makes architectural intent clear
- **Activates module-specific verifications** during tests

### `@NamedInterface`
```kotlin
@NamedInterface(name = ["book"])
```

The `@NamedInterface` annotation:
- **Defines a named interface** for the module
- **Explicitly controls what is exposed** to other modules
- **Improves documentation** and understanding of inter-module APIs
- **Facilitates architecture testing** and documentation generation

**Advantage**: Instead of relying solely on Java package visibility, these annotations make architectural intentions explicit and testable.

## Event System

### How it Works

#### 1. Event Publication
```kotlin
// In AuthorService.kt
@Transactional
fun deleteAuthor(id: Long) {
    val author = findById(id) ?: throw IllegalArgumentException("Author not found")
    eventPublisher.publishEvent(AuthorDeleted(author.id, "${author.firstName} ${author.lastName}"))
    authorRepository.deleteById(id)
}
```

#### 2. Event Listening
```kotlin
// In BookService.kt
@EventListener
@Transactional
fun handleAuthorDeleted(event: AuthorDeleted) {
    val deletedCount = bookRepository.deleteByAuthorId(event.authorId)
    if (deletedCount > 0) {
        logger.info("Deleted {} book(s) associated with author {} (ID: {})", 
                   deletedCount, event.name, event.authorId)
    }
}
```

### Event System Benefits

#### Strong Decoupling
- Modules don't know each other directly
- No cyclic dependencies
- Each module can evolve independently

#### Extensibility
- New modules can listen to existing events
- Adding functionality without modifying existing code
- Open/closed architecture principle

#### Transaction Safety
- Events are published within the same transaction
- In case of rollback, events are not processed
- Data consistency guaranteed

#### Robustness
- Spring Modulith automatically persists events
- Automatic replay in case of crash
- No event loss

#### Observability
- Built-in event system monitoring
- Administration APIs for diagnostics
- Real-time statistics

### Concrete Example in the Project

When an author is deleted:

1. **Author Module** → Publishes `AuthorDeleted`
2. **Book Module** → Receives the event via `@EventListener`  
3. **Book Module** → Automatically deletes all books by that author
4. **System** → Logs the operation and persists the event

**Advantage**: The Author module doesn't need to know about the Book module's existence, but business consistency is maintained.

## Module Structure

```
src/main/kotlin/com/ggardet/modulith/
├── author/                         # Author Module
│   ├── AuthorApi.kt               # Public API (events, DTOs)
│   ├── ModuleMetadata.kt          # Module configuration
│   ├── controller/                # Internal controllers
│   ├── service/                   # Business logic
│   ├── repository/                # Data access  
│   └── model/                     # JPA entities
├── book/                          # Book Module
│   ├── BookApi.kt                 # Public API
│   ├── ModuleMetadata.kt          # Module configuration
│   ├── service/BookService.kt     # Service with @EventListener
│   └── ...                       # Other internal components
└── core/                          # Core Module
    └── ...                       # Shared utilities and configuration
```

**Public**: Accessible by other modules  
**Private**: Encapsulated within the module

## Architectural Verification

The project includes automated tests to verify modular architecture compliance:

```bash
# Verify module boundaries
./mvnw test -Dtest=ModulithArchitectureTests

# Test event-driven communication  
./mvnw test -Dtest=EventCommunicationTest
```

## Technologies

- **Spring Boot** 3.5.4
- **Spring Modulith** 1.4.1
- **Kotlin** 1.9.25 + **Java** 21
- **PostgreSQL** with **Liquibase**
- **Maven** for build

## Quick Start

1. **Start the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Access**:
   - API: http://localhost:8080
   - Database: localhost:5432 (`modulith` / `password`)

3. **Test the architecture**:
   ```bash
   ./mvnw test
   ```

The application automatically starts PostgreSQL via Docker Compose, runs Liquibase migrations, and loads sample data (7 authors, 18 books).

## Key Endpoints

### Author Management
- `GET /api/authors` - Paginated list
- `POST /api/authors` - Create author  
- `DELETE /api/authors/{id}` - Delete (triggers book deletion)

### Book Management  
- `GET /api/books` - Paginated list
- `GET /api/books/author/{id}` - Books by author
- `POST /api/books` - Create book

### Event Monitoring
- `GET /api/events/stats` - Statistics
- `GET /api/events/health` - System status

## Resources

- [Spring Modulith Documentation](https://docs.spring.io/spring-modulith/reference/)
- [Events Guide](https://docs.spring.io/spring-modulith/reference/events.html) 
- [Project Instructions (CLAUDE.md)](./CLAUDE.md)