# Employee Leave Management System

## 1. Overview

This is a Spring Boot–based **Employee Leave Management System** that allows employees to apply for leave and administrators to manage and approve/reject those leave requests. The application follows clean architecture principles with clear separation of concerns, validation, role-based access control, and unit testing.

---

## 2. Tech Stack

* **Java**: 17+
* **Spring Boot**: Web, Data JPA, Security
* **Hibernate / JPA**
* **PostgreSQL** (can be replaced with H2 for local testing)
* **Maven**
* **JUnit 5 & Mockito**
* **Lombok**

---

## 3. Setup Instructions

### 3.1 Prerequisites

* Java 17 or later
* Maven 3.8+
* PostgreSQL (optional if using H2)
* IDE (IntelliJ / Eclipse / VS Code)

---

### 3.2 Database Setup (PostgreSQL)

Create a database:

```sql
CREATE DATABASE leave_management;
```

Update `application.yml` or `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/leave_management
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 3.3 Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

---

## 4. API Documentation

### 4.1 Apply for Leave (USER)

**Endpoint**

```
POST /leaves
```

**Request Body**

```json
{
  "employeeId": 1,
  "startDate": "2026-02-01",
  "endDate": "2026-02-05",
  "reason": "Medical leave"
}
```

**Response (201 CREATED)**

```json
{
  "id": 10,
  "employeeId": 1,
  "startDate": "2026-02-01",
  "endDate": "2026-02-05",
  "reason": "Medical leave",
  "leaveStatus": "PENDING",
  "createdAt": "2026-01-28T14:30:00"
}
```

---

### 4.2 Update Leave Status (ADMIN)

**Endpoint**

```
PUT /leaves/{id}/status?status=APPROVED
```

**Response (200 OK)**

```json
{
  "id": 10,
  "employeeId": 1,
  "startDate": "2026-02-01",
  "endDate": "2026-02-05",
  "reason": "Medical leave",
  "leaveStatus": "APPROVED",
  "createdAt": "2026-01-28T14:30:00"
}
```

---

### 4.3 Get Leaves by Employee (USER / ADMIN)

**Endpoint**

```
GET /leaves/employee/{employeeId}
```

**Response (200 OK)**

```json
[
  {
    "id": 10,
    "employeeId": 1,
    "startDate": "2026-02-01",
    "endDate": "2026-02-05",
    "reason": "Medical leave",
    "leaveStatus": "PENDING",
    "createdAt": "2026-01-28T14:30:00"
  }
]
```

---

## 5. Architecture Overview

### 5.1 Layered Architecture

```
Controller Layer
 └── REST APIs, validation, security

Service Layer
 └── Business logic, transactions

Repository Layer
 └── Database access using JPA

Domain / Entity Layer
 └── JPA entities and enums

DTO & Mapper Layer
 └── Request/Response DTOs and mapping
```

---

### 5.2 DTO Strategy

* **LeaveRequestCreateDto** → Used for incoming requests
* **LeaveRequestResponseDto** → Used for API responses
* No DTO is injected as a Spring bean
* Mapping handled via a dedicated mapper class

---

### 5.3 Security

* Role-based authorization using `@PreAuthorize`
* Roles supported:

    * `ROLE_USER`
    * `ROLE_ADMIN`

---

## 6. Assumptions

* Employee must exist before applying for leave
* Leave status is **always set by backend**, never by client
* Default leave status is `PENDING`
* An employee can have multiple leave requests
* Date overlap validation is not implemented (can be added later)
* Authentication is assumed to be handled externally (e.g., JWT / OAuth)

---

## 7. Testing Instructions

### 7.1 Unit Testing

* Tests are written using **JUnit 5** and **Mockito**
* Focus on service-layer business logic

Run tests:

```bash
mvn test
```

---

### 7.2 Covered Test Scenarios

* Apply leave – status defaults to `PENDING`
* Employee not found scenarios
* Fetch leaves by employee
* Leave status update by admin

---
## 8. Docker instructions

Build the application JAR using mvn clean package

Build Docker images using docker compose build app

Start all services using docker compose up -d

Access the application at http://localhost:8080

Access RabbitMQ management UI at http://localhost:15672 (guest / guest)

PostgreSQL runs on port 5432 with database emsdb

Stop all services using docker compose down

Remove containers and volumes using docker compose down -v

## 8. Future Enhancements

* Pagination & sorting for leave history
* Date overlap validation
* Email / notification integration
* Swagger / OpenAPI documentation
* Audit logs for leave status changes

---

## 9. Author

Developed by **Jimesh Naik**
