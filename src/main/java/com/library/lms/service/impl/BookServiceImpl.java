package com.library.lms.service.impl;

import com.library.lms.dto.BookRequest;
import com.library.lms.dto.BookResponse;
import com.library.lms.entity.Book;
import com.library.lms.exception.BusinessRuleException;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.repository.BookRepository;
import com.library.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BusinessRuleException("A book with ISBN " + request.isbn() + " already exists");
        }

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .genre(request.genre())
                .totalCopies(request.totalCopies())
                .availableCopies(request.totalCopies())
                .build();

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }
    @Override
    public BookResponse getById(Long id) {
        Book book = findBookOrThrow(id);
        return toResponse(book);
    }

    @Override
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BookResponse> search(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = findBookOrThrow(id);

        boolean isbnChanged = !book.getIsbn().equals(request.isbn());
        if (isbnChanged && bookRepository.existsByIsbn(request.isbn())) {
            throw new BusinessRuleException("A book with ISBN " + request.isbn() + " already exists");
        }

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setGenre(request.genre());
        book.setTotalCopies(request.totalCopies());
        // availableCopies intentionally left untouched — managed by borrow/return logic

        Book updated = bookRepository.save(book);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = findBookOrThrow(id);
        bookRepository.delete(book);
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getGenre(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

}
