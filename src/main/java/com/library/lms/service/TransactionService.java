package com.library.lms.service;

import com.library.lms.dto.BorrowRequest;
import com.library.lms.dto.TransactionResponse;

public interface TransactionService {
    TransactionResponse borrowBook(BorrowRequest request);
}
