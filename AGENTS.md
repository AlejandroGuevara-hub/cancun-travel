# Repository Guidelines

## Project Structure & Module Organization

This is a Java 25 Spring Boot application built with Maven. Application code lives under `src/main/java/com/cancuntravel/app`; keep new packages beneath `com.cancuntravel.app` so component scanning can find them. Configuration belongs in `src/main/resources/application.properties`. Place Thymeleaf templates in `src/main/resources/templates` and static assets in `src/main/resources/static` as they are added. Tests mirror the application package under `src/test/java`. Maven output goes to `target/` and should not be committed.

## Build, Test, and Development Commands

Use the Maven wrapper from the repository root with JDK 25 installed. On Windows, use `mvnw.cmd`; on Unix-like systems, use `./mvnw`.

- `./mvnw spring-boot:run` starts the application locally.
- `./mvnw test` compiles the project and runs the test suite.
- `./mvnw clean package` removes previous output, runs tests, and builds the application in `target/`.

## Coding Style & Naming Conventions

Follow the existing Java style: tabs for indentation, braces on the declaration line, `UpperCamelCase` for classes, and `lowerCamelCase` for methods and fields. Use lowercase package names and descriptive Spring component names. Keep changes consistent with surrounding code. No formatter or linter is configured in `pom.xml`.

## Testing Guidelines

Tests use JUnit Jupiter and Spring Boot's test support. Name test classes `*Tests` and place them in the corresponding package under `src/test/java`. Add focused tests for new behavior; use `@SpringBootTest` when a full application context is needed. Run `./mvnw test` before submitting changes. No coverage threshold is currently configured.

## Commit & Pull Request Guidelines

Git history is unavailable in this checkout, so no existing commit convention can be verified. Use concise, imperative commit subjects that describe the change. Pull requests should explain the purpose and behavior, link relevant issues, and list checks performed; include screenshots for visible UI changes.

## Configuration

Keep credentials and environment-specific database settings out of committed configuration. Supply secrets through environment variables or local configuration, and document any new required settings without exposing their values.