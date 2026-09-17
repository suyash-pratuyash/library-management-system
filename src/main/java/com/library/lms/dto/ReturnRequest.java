package com.library.lms.dto;

import jakarta.validation.constraints.NotNull;

public record ReturnRequest(
        @NotNull(message = "Book ID is required")
        Long bookId,

        @NotNull(message = "Member ID is required")
        Long memberId
) {
}
