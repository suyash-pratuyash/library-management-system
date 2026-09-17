package com.library.lms.service;

import com.library.lms.dto.BookRequest;
import com.library.lms.dto.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse create(BookRequest request);
    BookResponse getById(Long id);
    List<BookResponse> getAll();
    List<BookResponse> search(String keyword);
    BookResponse update(Long id, BookRequest request);
    void delete(Long id);
}
