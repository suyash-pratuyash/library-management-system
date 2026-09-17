package com.library.lms.repository;

import com.library.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book, Long>
{
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    // add to BookRepository
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}
