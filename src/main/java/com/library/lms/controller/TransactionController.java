package com.library.lms.controller;

import com.library.lms.dto.BorrowRequest;
import com.library.lms.dto.ReturnRequest;
import com.library.lms.dto.TransactionResponse;
import com.library.lms.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // POST /api/transactions/borrow
    @PostMapping("/borrow")
    public ResponseEntity<TransactionResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
        TransactionResponse response = transactionService.borrowBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/transactions/return
    @PostMapping("/return")
    public ResponseEntity<TransactionResponse> returnBook(@Valid @RequestBody ReturnRequest request) {
        TransactionResponse response = transactionService.returnBook(request);
        return ResponseEntity.ok(response);
    }

    // GET /api/transactions/member/{memberId}
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TransactionResponse>> getByMember(@PathVariable Long memberId) {
        List<TransactionResponse> transactions = transactionService.getByMemberId(memberId);
        return ResponseEntity.ok(transactions);
    }

    // GET /api/transactions/book/{bookId}
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<TransactionResponse>> getByBook(@PathVariable Long bookId) {
        List<TransactionResponse> transactions = transactionService.getByBookId(bookId);
        return ResponseEntity.ok(transactions);
    }

    // GET /api/transactions/active
    @GetMapping("/active")
    public ResponseEntity<List<TransactionResponse>> getActive() {
        List<TransactionResponse> transactions = transactionService.getActiveTransactions();
        return ResponseEntity.ok(transactions);
    }
}