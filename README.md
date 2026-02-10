# Neo4j Spring Boot Library System

A simple library management system built with Spring Boot, demonstrating role-based access control using Spring Security and a graph-based data model with a Neo4j database.

## Features

*   **Role-Based Access Control (RBAC):**
    *   `ROLE_ADMIN`: Can view and add books.
    *   `ROLE_USER`: Can only view books.
*   **Graph Data Model:** Uses Neo4j to naturally model relationships between `Book`, `Author`, and `User` nodes.
*   **REST API:** Simple and secure endpoints for interacting with the library.
*   **Data Seeding:** Automatically creates default `admin` and `user` accounts on startup for immediate testing.

## Technology Stack

*   **Backend:** Spring Boot 3.2.5
*   **Database:** Neo4j 5.x
*   **Security:** Spring Security
*   **Language:** Java 21
*   **Build Tool:** Maven
*   **Testing:** JUnit 5, Mockito, Spring Boot Test

## Prerequisites

Before you begin, ensure you have the following installed:
*   JDK 21 or later
*   Apache Maven
*   Docker
*   A REST Client (e.g., Postman, Insomnia, or `curl`)

## Getting Started

Follow these steps to get the application running.

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd library
```

### 2. Run the Neo4j Database

This project requires a running Neo4j 5.x instance. The easiest way to start one is with Docker.

```bash
docker run \
    --name my-neo4j-db \
    -p 7474:7474 -p 7687:7687 \
    -d \
    -e NEO4J_AUTH=neo4j/password123 \
    neo4j:5-enterprise
```
*   This command starts a Neo4j container.
*   The database credentials will be `neo4j` / `password123`.
*   The database will be accessible on port `7687` (Bolt protocol) and `7474` (HTTP browser).

### 3. (Optional) Corporate Proxy Setup for Maven

If you are running this project from within a corporate network (like Ciena's), Maven may be blocked from downloading dependencies. You must configure Maven to use your company's proxy.

1.  Locate or create the file `C:\Users\<your_username>\.m2\settings.xml`.
2.  Add the following proxy configuration, replacing the host and port with your company's values.

    ```xml
    <settings>
      <proxies>
        <proxy>
          <id>my-company-proxy</id>
          <active>true</active>
          <protocol>http</protocol>
          <host>proxy.mycompany.com</host>
          <port>8080</port>
        </proxy>
      </proxies>
    </settings>
    ```

### 4. Build and Run the Application

Use the Maven wrapper to build and run the Spring Boot application.

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### 5. Default User Accounts

The application automatically creates two users on startup:
*   **Username:** `admin` | **Password:** `adminpass` | **Roles:** `ROLE_ADMIN`, `ROLE_USER`
*   **Username:** `user` | **Password:** `userpass` | **Roles:** `ROLE_USER`

## API Endpoints

All endpoints use HTTP Basic Authentication with the credentials above.

| Method | Endpoint      | Role(s) Required    | Description                     |
|--------|---------------|---------------------|---------------------------------|
| `GET`  | `/api/books`  | `ROLE_USER`, `ROLE_ADMIN` | Retrieves a list of all books.  |
| `POST` | `/api/books`  | `ROLE_ADMIN` only     | Adds a new book to the library. |

#### Sample `curl` Commands:

**Get all books (as USER):**
```bash
curl -u user:userpass http://localhost:8080/api/books
```

**Add a new book (as ADMIN):**
```bash
curl -X POST -u admin:adminpass http://localhost:8080/api/books \
-H "Content-Type: application/json" \
-d '{"title": "Dune", "authorName": "Frank Herbert"}'
```

**Attempt to add a new book (as USER - will be Forbidden):**
```bash
curl -X POST -u user:userpass http://localhost:8080/api/books \
-H "Content-Type: application/json" \
-d '{"title": "Dune", "authorName": "Frank Herbert"}'
```

## Running the Tests

To run the full suite of unit and integration tests, execute the following command:

```bash
./mvnw clean test
```
This ensures the application's logic, endpoints, and security rules are working as expected.