# CaptureThis - Comments Microservice 💬

This is the standalone Comments Microservice for the **CaptureThis** social media platform. It exposes a clean REST API to handle all comment-related operations, demonstrating a decoupled, microservice-oriented architecture.

## Features
* **RESTful API:** Provides endpoints for fetching, creating, and deleting comments.
* **Soft Deletion:** Implements industry-standard "soft delete" logic (`isDeleted` flag) to preserve comment history for moderation while hiding them from the user interface.
* **Independent Data Layer:** Operates completely independently from the main application's database, promoting loose coupling.

## Tech Stack
* **Java 21**
* **Spring Boot 3** (Spring Web, Spring Data JPA)
* **Database:** MySQL

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 21
* Maven
* MySQL Server (running on default port 3306)

### Installation & Setup
1. **Clone the repository:**
   `git clone https://github.com/dt242/CommentsProject.git`
2. **Configure Database Credentials:**
   The service automatically creates its own database (`comment-section-app`). Check `src/main/resources/application.yaml` to ensure the credentials match your local MySQL setup:

       spring:
         datasource:
           url: jdbc:mysql://localhost:3306/comment-section-app?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true&serverTimezone=UTC
           username: ${DB_USERNAME:root}
           password: ${DB_PASSWORD:root}

3. **Run the Microservice:**
   `mvn spring-boot:run`

### API Endpoints
The service runs on **port 8081** by default to avoid conflict with the main application.

* `GET /api/comments/post/{postId}` - Retrieve all active comments for a specific post.
* `POST /api/comments` - Create a new comment.
* `DELETE /api/comments/{commentId}` - Soft-delete a specific comment.

*Note: This service is designed to be consumed by the main CaptureThis backend via Spring's `RestClient`, rather than directly by a frontend client.*
