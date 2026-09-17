package com.library.lms.dto;

import com.library.lms.entity.TransactionStatus;

import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long memberId,
        String memberName,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        TransactionStatus status
) {}
