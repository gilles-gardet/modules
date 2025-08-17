# Spring Modulith Demo

This project is a comprehensive demonstration application showcasing **Spring Modulith** framework capabilities in a real-world blog system scenario.

## About Spring Modulith

Spring Modulith is a framework that helps implement modular monolith architectures by:
- Enforcing module boundaries within a single application
- Providing architectural verification and testing tools
- Supporting event-driven communication between modules
- Enabling gradual migration paths from monolith to microservices

## Project Overview

This demo implements a **library management system** with Spring Modulith modular architecture, featuring:

### 🏗️ **Modular Architecture**
The application is organized into **3 distinct modules** focused on core domains:

1. **👤 User Module** (`com.ggardet.modulith.user`)
   - User management and profiles
   - User CRUD operations
   - **API**: `UserApi` for cross-module communication
   - **Events**: User creation, login, and authentication events

2. **📚 Book Module** (`com.ggardet.modulith.book`)
   - Book and author management
   - Complete CRUD operations for books and authors
   - **API**: `BookApi` for cross-module access
   - **Events**: Book and author lifecycle events

3. **🔧 Shared Module** (`com.ggardet.modulith.shared`)
   - Security configuration with Basic Authentication
   - Password encoding utilities
   - Event system configuration and monitoring
   - Cross-cutting concerns and utilities

### 📡 **Event-Driven Communication**

The application demonstrates **decoupled module communication** using Spring Events:

#### User Module Events:
- `UserCreated` - Published when a new user is registered

#### Book Module Events:
- `BookCreated` - Published when a new book is added to the library
- `BookUpdated` - Published when book information is modified
- `BookDeleted` - Published when a book is removed from the library
- `AuthorCreated` - Published when a new author is added
- `AuthorUpdated` - Published when author information is modified
- `AuthorDeleted` - Published when an author is removed

**Benefits**:
- ✅ **No circular dependencies** between modules
- ✅ **Asynchronous communication** capabilities
- ✅ **Loose coupling** - modules can evolve independently
- ✅ **Extensible** - new modules can listen to existing events

### 🔄 **Event Persistence & Replay System**

The application implements **Spring Modulith Event Publication Registry** for robust event handling:

#### Event Persistence:
- **Automatic persistence** of all published events to database
- **Event serialization** using Jackson JSON for storage
- **Transaction-safe** event publishing with rollback support
- **Database table** `event_publication` to store event lifecycle

#### Automatic Event Replay:
- **Crash recovery** - Events are automatically replayed after application restart
- **Incomplete event detection** - System identifies events that weren't fully processed
- **Zero configuration** - Spring Modulith handles replay logic automatically
- **Monitoring & diagnostics** - Real-time event system health monitoring

#### Key Components:
- **`EventConfig`** - Configures event serialization and async processing
- **`EventReplayService`** - Monitors event system health and provides diagnostics
- **`EventManagementController`** - Admin endpoints for event system management
- **Database migration** - Liquibase creates required `event_publication` table

#### Event System Benefits:
- ✅ **Fault tolerance** - No lost events due to application crashes
- ✅ **Reliability** - Guaranteed event delivery and processing
- ✅ **Observability** - Real-time monitoring of event system health
- ✅ **Administrative control** - API endpoints for system diagnostics
- ✅ **Automatic cleanup** - Periodic removal of old processed events

### 🗄️ **Database & Data Management**

- **PostgreSQL** database with Docker Compose
- **Liquibase** migrations for schema versioning
- **Sample data**: 3 users, 3 authors and 5 books pre-loaded
- **JPA/Hibernate** with Kotlin data classes

### 🧪 **Architecture Verification**

The project includes comprehensive testing:

1. **`ModulithArchitectureTests`** - Verifies module boundaries and structure
2. **`EventCommunicationTest`** - Tests event-driven communication
3. **`EventBasedArchitectureDemo`** - Demonstrates the working architecture
4. **`EventPersistenceTest`** - Tests event persistence and replay system
5. **Module Integration Tests** - End-to-end functionality verification

## Technology Stack

- **Spring Boot** 3.5.4
- **Spring Modulith** 1.4.1 
- **Spring Security** 6.5.2
- **Kotlin** 1.9.25
- **Java** 21
- **PostgreSQL** 15 (Docker)
- **Liquibase** for database migrations
- **Maven** build system

## Getting Started

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven (or use the included wrapper)

### Running the Application

1. **Start the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Access the application**:
   - API: http://localhost:8080
   - Database: localhost:5433 (user: `modulith`, password: `password`)

The application automatically:
- Starts PostgreSQL container via Docker Compose
- Runs Liquibase migrations
- Loads sample data

### Architecture Verification

Run the architecture tests to verify Spring Modulith compliance:

```bash
./mvnw test -Dtest=ModulithArchitectureTests
```

### Event Communication Testing

Test the event-driven architecture:

```bash
./mvnw test -Dtest=EventCommunicationTest
```

### Event Persistence Testing

Test the event persistence and replay system:

```bash
./mvnw test -Dtest=EventPersistenceTest
```

## Key Spring Modulith Concepts Demonstrated

### 1. **Module Definition**
- Modules defined by package structure (`com.ggardet.modulith.{module}`)
- Each module has clear boundaries and responsibilities

### 2. **API Exposure**
- Public APIs exposed in module root packages
- Internal components hidden in sub-packages (`service`, `repository`, `controller`)

### 3. **Event-Driven Architecture**
- `ApplicationEventPublisher` for publishing events
- `@EventListener` for handling cross-module events (removed to avoid cycles)
- Events in root packages for cross-module visibility

### 4. **Architectural Testing**
- `ApplicationModules.verify()` for boundary validation
- Automatic documentation generation
- Module structure analysis and reporting

### 5. **Dependency Management**
- **Avoided circular dependencies** through event-driven design
- Clean separation of concerns between modules
- Proper encapsulation of internal module logic

## Sample API Endpoints

### User Management
- `GET /api/users/{id}/public-profile` - Get user public profile
- `GET /api/users/profile` - Get current user profile (authenticated)
- `PUT /api/users/profile` - Update user profile

### Book Management
- `GET /api/books` - List all books with pagination
- `GET /api/books/available` - List available books only
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/isbn/{isbn}` - Get book by ISBN
- `GET /api/books/search?title={title}` - Search books by title
- `GET /api/books/author/{authorId}` - Get books by author
- `GET /api/books/genre/{genre}` - Get books by genre
- `POST /api/books` - Create new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Author Management
- `GET /api/authors` - List all authors with pagination
- `GET /api/authors/{id}` - Get author by ID
- `GET /api/authors/search?name={name}` - Search authors by name
- `GET /api/authors/nationality/{nationality}` - Get authors by nationality
- `GET /api/authors/birth-year?startYear={start}&endYear={end}` - Filter authors by birth year
- `POST /api/authors` - Create new author
- `PUT /api/authors/{id}` - Update author
- `DELETE /api/authors/{id}` - Delete author

### Event System Monitoring
- `GET /api/events/stats` - Get event system statistics
- `GET /api/events/health` - Check event system health status
- `GET /api/events/incomplete/count` - Count of incomplete events

### Authentication
- Uses **HTTP Basic Authentication** for all secured endpoints
- No forms or complex authentication flows - simple username/password via HTTP headers

## Project Structure

```
src/main/kotlin/com/ggardet/modulith/
├── user/                          # User Module
│   ├── UserApi.kt                 # Public API
│   ├── UserEvents.kt              # User-related events
│   ├── controller/                # REST controllers
│   ├── service/                   # Business logic
│   └── repository/                # Data access
├── book/                          # Book Module
│   ├── BookApi.kt                 # Public API
│   ├── BookDtos.kt                # Data Transfer Objects
│   ├── controller/                # Book and Author controllers
│   │   ├── BookController.kt      # Book REST API
│   │   └── AuthorController.kt    # Author REST API
│   ├── service/                   # Business logic
│   │   ├── BookService.kt         # Book operations
│   │   └── AuthorService.kt       # Author operations
│   ├── repository/                # Data access
│   │   ├── BookRepository.kt      # Book data access
│   │   └── AuthorRepository.kt    # Author data access
│   └── domain/                    # Domain entities
│       ├── Book.kt                # Book entity and DTOs
│       └── Author.kt              # Author entity
└── shared/                        # Shared Module
    ├── SecurityConfig.kt          # Basic auth configuration
    ├── PasswordConfig.kt          # Password encoding
    ├── UserDetailsServiceImpl.kt  # Authentication service
    ├── EventConfig.kt             # Event persistence configuration
    ├── EventReplayService.kt      # Event monitoring and diagnostics
    └── EventManagementController.kt # Event system API
```

## Resources

- [Spring Modulith Documentation](https://docs.spring.io/spring-modulith/reference/)
- [Spring Modulith Events Guide](https://docs.spring.io/spring-modulith/reference/events.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Project Documentation (CLAUDE.md)](./CLAUDE.md)
