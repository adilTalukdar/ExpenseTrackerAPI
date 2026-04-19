# Expense Tracker REST API

A RESTful API built with **Spring Boot** and **MySQL** for managing personal expenses. Supports full CRUD operations, category filtering, date range queries, pagination, and total spending calculations.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| Database | MySQL |
| ORM | Spring Data JPA + Hibernate |
| Validation | Spring Boot Validation (Jakarta) |
| Build Tool | Maven |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/adil/ExpenseTracker/
├── controller/
│   └── ExpenseController.java
├── service/
│   └── ExpenseService.java
├── repository/
│   └── ExpenseRepository.java
├── model/
│   └── Expense.java
├── dto/
│   ├── ExpenseRequest.java
│   ├── ExpenseResponse.java
│   ├── TotalResponse.java
│   └── ErrorResponse.java
├── enums/
│   └── Category.java
└── exception/
    ├── ExpenseNotFoundException.java
    ├── DuplicateExpenseException.java
    ├── DailyLimitExceededException.java
    └── GlobalExceptionHandler.java
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven
- MySQL 8.x
- IntelliJ IDEA / Spring Tool Suite

### 1. Clone the Repository

```bash
git clone https://github.com/adilTalukdar/ExpenseTrackerAPI.git
cd ExpenseTrackerAPI
```

### 2. Create MySQL Database

Open MySQL Workbench or any MySQL client and run:

```sql
CREATE DATABASE expense_tracker;
```

### 3. Configure Database Credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or run `ExpenseTrackerApplication.java` directly from your IDE.

The server starts at: `http://localhost:8080`

---

## API Endpoints

### Base URL
```
http://localhost:8080/api/expenses
```

---

### Create Expense
```
POST /api/expenses
```
**Request Body:**
```json
{
  "title": "Lunch at Cafe",
  "amount": 250.50,
  "category": "FOOD",
  "date": "2026-04-19"
}
```
**Response: `201 Created`**

---

### Get All Expenses
```
GET /api/expenses
```
**Query Parameters:**

| Parameter | Type | Description |
|---|---|---|
| `category` | String | Filter by `FOOD`, `TRAVEL`, `SHOPPING`, `OTHER` |
| `startDate` | `yyyy-MM-dd` | Filter from this date |
| `endDate` | `yyyy-MM-dd` | Filter until this date |
| `page` | int | Page number (default: 0) |
| `size` | int | Records per page (default: 10) |
| `sortBy` | String | Field to sort (default: `date`) |
| `sortDir` | String | `asc` or `desc` (default: `desc`) |

**Examples:**
```
GET /api/expenses
GET /api/expenses?category=FOOD
GET /api/expenses?startDate=2026-04-01&endDate=2026-04-30
GET /api/expenses?category=TRAVEL&page=0&size=5&sortDir=asc
```

**Response: `200 OK`** — paginated list of expenses

---

### Get Expense by ID
```
GET /api/expenses/{id}
```
**Response: `200 OK`**

---

### Update Expense
```
PUT /api/expenses/{id}
```
**Request Body:** Same as Create

**Response: `200 OK`**

---

### Delete Expense
```
DELETE /api/expenses/{id}
```
**Response: `204 No Content`**

---

### Get Total Spending
```
GET /api/expenses/total
```
Supports same optional filters as Get All (`category`, `startDate`, `endDate`)

**Response: `200 OK`**
```json
{
  "total": 6000.50,
  "count": 4
}
```

---

## Data Model

| Field | Type | Constraints |
|---|---|---|
| `id` | Long | Auto-generated |
| `title` | String | Min 3 characters, required |
| `amount` | Double | Must be > 0, required |
| `category` | Enum | `FOOD`, `TRAVEL`, `SHOPPING`, `OTHER` |
| `date` | LocalDate | Required |
| `createdAt` | LocalDateTime | Auto-set on creation |

---

## Business Rules

- Title must be **at least 3 characters**
- Amount must be **greater than 0**
- Category must be one of: `FOOD`, `TRAVEL`, `SHOPPING`, `OTHER`
- Maximum **10 expenses per day** across all entries
- **Duplicate rejected** — same title + amount + date is not allowed

---

## Error Responses

All errors return a consistent JSON format:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Title must be at least 3 characters"
}
```

| Status | Meaning |
|---|---|
| `200` | Success |
| `201` | Created |
| `204` | Deleted successfully |
| `400` | Validation error / invalid input |
| `404` | Expense not found |
| `409` | Duplicate expense |
| `422` | Daily limit of 10 exceeded |
| `500` | Internal server error |

---

## License

This project is for learning and portfolio purposes.
