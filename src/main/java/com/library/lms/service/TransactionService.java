package com.library.lms.service;

import com.library.lms.dto.BorrowRequest;
import com.library.lms.dto.ReturnRequest;
import com.library.lms.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse borrowBook(BorrowRequest request);
    TransactionResponse returnBook(ReturnRequest request);
    List<TransactionResponse> getByMemberId(Long memberId);
    List<TransactionResponse> getByBookId(Long bookId);
    List<TransactionResponse> getActiveTransactions();
}
