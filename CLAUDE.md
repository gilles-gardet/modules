# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Modulith demo project written in Kotlin that explores modular monolith architecture patterns. The project uses Spring Boot 3.5.4 with Java 21 and Kotlin 1.9.25.

## Development Commands

### Build and Run
- `./mvnw spring-boot:run` - Run the application
- `./mvnw clean compile` - Compile the project
- `./mvnw clean package` - Build the project and create JAR
- `./mvnw clean install` - Install dependencies and build

### Testing
- `./mvnw test` - Run all tests
- `./mvnw test -Dtest=ClassName` - Run specific test class
- `./mvnw test -Dtest=ClassName#methodName` - Run specific test method

### Docker
- The project includes `compose.yaml` but currently has no services defined
- Application requires Docker Compose services to be added before it can start

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.4 with Spring Security
- **Language**: Kotlin (source in `src/main/kotlin`, tests in `src/test/kotlin`)
- **Modulith**: Spring Modulith 1.4.1 for modular monolith patterns
- **Database**: PostgreSQL with Liquibase migrations
- **Build Tool**: Maven with wrapper (`./mvnw`)
- **Java Version**: 21

### Modular Structure
The application follows Spring Modulith patterns with these modules:

- **User Module** (`com.ggardet.modulith.user`)
  - Domain: User entity and DTOs
  - Repository: UserRepository with JPA operations
  - Service: UserService for business logic
  - Controller: User profile management endpoints

- **Blog Module** (`com.ggardet.modulith.blog`)
  - Domain: BlogPost entity and DTOs
  - Repository: BlogPostRepository with complex queries
  - Service: BlogPostService for CRUD and publishing logic
  - Controller: Public blog endpoints and authenticated post management

- **Auth Module** (`com.ggardet.modulith.auth`)
  - Service: AuthenticationService implementing UserDetailsService
  - Controller: Registration, login, and auth status endpoints

- **Admin Module** (`com.ggardet.modulith.admin`)
  - Service: AdminService for system-wide operations
  - Controller: Admin-only endpoints for user and content management

### Key Dependencies
- `spring-modulith-starter-core` - Core Spring Modulith functionality
- `spring-modulith-starter-test` - Testing support for modulith architecture
- `spring-boot-starter-web` - Web layer
- `spring-boot-starter-security` - Security configuration
- `spring-boot-starter-data-jpa` - JPA and database access
- `postgresql` - PostgreSQL database driver
- `liquibase-core` - Database migration management
- `kotlin-test-junit5` - Kotlin testing with JUnit 5

### Database Schema
- **users** table: User authentication and profile data
- **blog_posts** table: Blog content with author relationship
- Sample data includes 3 users and 5 blog posts
- Password for sample users: "password" (BCrypt encoded)

## Important Notes

- Each module maintains clear boundaries following Spring Modulith principles
- Database migrations are managed through Liquibase in `src/main/resources/db/changelog/`
- PostgreSQL database runs via Docker Compose on port 5432
- Security configuration separates public, authenticated, and admin routes
- Services handle cross-module communication while maintaining modularity