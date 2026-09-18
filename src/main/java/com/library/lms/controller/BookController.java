package com.library.lms.controller;

import com.library.lms.dto.BookRequest;
import com.library.lms.dto.BookResponse;
import com.library.lms.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // POST /api/books - Create a new book
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/books/{id} - Get a book by id
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse response = bookService.getById(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/books?search=... - Get all books, or search by title/author
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @RequestParam(required = false) String search) {

        List<BookResponse> books = (search == null || search.isBlank())
                ? bookService.getAll()
                : bookService.search(search);

        return ResponseEntity.ok(books);
    }

    // PUT /api/books/{id} - Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id, @Valid @RequestBody BookRequest request) {

        BookResponse response = bookService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/books/{id} - Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}