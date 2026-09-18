# Library Management System (lms)

A simple Spring Boot REST API to manage books, members and borrowing transactions.

## Tech stack
- Java 21
- Spring Boot 4.1.1 (WebMVC, Data JPA, Validation)
- Jakarta Bean Validation (jakarta.validation)
- Lombok
- MySQL (configured in application.properties)
- Maven
- JUnit for tests

## Project structure / Architecture
This project follows a Controller → Service → Repository layered architecture:

- Controller (com.library.lms.controller)
  - Exposes REST endpoints and request/response mapping (DTOs).
- Service (com.library.lms.service + impl)
  - Implements business rules and transactional boundaries.
- Repository (com.library.lms.repository)
  - Spring Data JPA interfaces for persistence.

Cross-cutting: DTOs in com.library.lms.dto, entities in com.library.lms.entity, and global exception handling in com.library.lms.exception.

## Setup
Prerequisites
- Java 21
- Maven
- MySQL (or change datasource in src/main/resources/application.properties)

Steps
1. Clone:
   git clone https://github.com/suyash-pratuyash/library-management-system.git
   cd lms

2. Configure database in src/main/resources/application.properties (example entries already present):
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management_system?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   spring.jpa.hibernate.ddl-auto=update

3. Build & run:
   mvn clean package
   java -jar target/lms-0.0.1-SNAPSHOT.jar

4. Run tests:
   mvn test

## API endpoints
Base path: /api

- Books
  - POST   /api/books            — Create a book (BookRequest)
  - GET    /api/books            — List all books (optional ?search=keyword)
  - GET    /api/books/{id}       — Get book by id
  - PUT    /api/books/{id}       — Update book
  - DELETE /api/books/{id}       — Delete book

- Members
  - POST   /api/members          — Create a member (MemberRequest)
  - GET    /api/members          — List members
  - GET    /api/members/{id}     — Get member by id
  - PUT    /api/members/{id}     — Update member
  - DELETE /api/members/{id}     — Delete member

- Transactions (borrowing/returning)
  - POST   /api/transactions/borrow  — Borrow a book (BorrowRequest)
  - POST   /api/transactions/return  — Return a book (ReturnRequest)
  - GET    /api/transactions/member/{memberId} — Transactions by member
  - GET    /api/transactions/book/{bookId}     — Transactions by book
  - GET    /api/transactions/active            — Active (BORROWED) transactions

## DTO / payload shapes
- BookRequest
  { title, author, isbn, genre, totalCopies }
- BookResponse
  { id, title, author, isbn, genre, totalCopies, availableCopies }
- MemberRequest
  { name, email, phoneNumber }
- MemberResponse
  { id, name, email, phoneNumber, membershipDate, status }
- BorrowRequest / ReturnRequest
  { bookId, memberId }
- TransactionResponse
  { id, bookId, bookTitle, memberId, memberName, borrowDate, dueDate, returnDate, status }

All request DTOs use Jakarta Validation annotations — invalid requests return a validation error.

## Business rules enforced (implemented in services)
- Book
  - ISBN is unique; creating/updating a book with an existing ISBN fails.
  - totalCopies must be >= 1 (validated).
  - availableCopies is initialized from totalCopies and is adjusted only by borrow/return flows.
  - Deleting a book uses repository.delete (hard delete).

- Member
  - Email is unique; creating/updating with an existing email fails.
  - New members are created with MembershipStatus.ACTIVE.
  - Deleting a member uses repository.delete (hard delete).

- Transactions (borrowing / returning)
  - Borrow allowed only if member exists and membership status is ACTIVE.
  - Borrow allowed only if book.availableCopies > 0.
  - On borrow: availableCopies is decremented and a Transaction with status BORROWED is created.
  - On return: the matching BORROWED transaction for the book+member is updated (returnDate set, status -> RETURNED) and book.availableCopies is incremented.
  - Default loan period = 14 days (defined in TransactionServiceImpl).
  - Borrow and return flows are @Transactional to keep counts and transactions consistent.

- Validation & errors
  - ResourceNotFoundException for missing book/member/transaction resources.
  - BusinessRuleException for enforced business constraints (duplicate ISBN/email, no available copies, inactive member, etc.).
  - GlobalExceptionHandler maps exceptions to meaningful HTTP responses.

## Tests
- Unit tests exist under src/test and cover TransactionServiceImpl behavior.

## Notes & recommendations
- There is no authentication/authorization in this codebase. Add Spring Security and role checks if needed.
- Consider switching hard-deletes to soft-deletes if historical audit is required.
- Add OpenAPI/Swagger for interactive API documentation.

---

This README was generated and added to the project root to document the current implementation and how to run it.
