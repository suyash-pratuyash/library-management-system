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
    // TODO: TransactionService has no method for this yet.
    // Needs something like: List<TransactionResponse> getByMemberId(Long memberId);
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TransactionResponse>> getByMember(@PathVariable Long memberId) {
        throw new UnsupportedOperationException(
                "TransactionService needs a getByMemberId(Long) method before this endpoint can work");
    }

    // GET /api/transactions/book/{bookId}
    // TODO: TransactionService has no method for this yet.
    // Needs something like: List<TransactionResponse> getByBookId(Long bookId);
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<TransactionResponse>> getByBook(@PathVariable Long bookId) {
        throw new UnsupportedOperationException(
                "TransactionService needs a getByBookId(Long) method before this endpoint can work");
    }

    // GET /api/transactions/active
    // TODO: TransactionService has no method for this yet.
    // Needs something like: List<TransactionResponse> getActiveTransactions();
    @GetMapping("/active")
    public ResponseEntity<List<TransactionResponse>> getActive() {
        throw new UnsupportedOperationException(
                "TransactionService needs a getActiveTransactions() method before this endpoint can work");
    }
}