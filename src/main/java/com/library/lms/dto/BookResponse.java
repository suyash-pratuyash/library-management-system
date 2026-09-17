package com.library.lms.dto;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String genre,
        int totalCopies,
        int availableCopies
) {}
