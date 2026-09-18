package com.library.lms.service.impl;

import com.library.lms.dto.BorrowRequest;
import com.library.lms.dto.ReturnRequest;
import com.library.lms.dto.TransactionResponse;
import com.library.lms.entity.*;
import com.library.lms.exception.BusinessRuleException;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.MemberRepository;
import com.library.lms.repository.TransactionRepository;
import com.library.lms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    private static final int LOAN_PERIOD_DAYS = 14;

    @Override
    @Transactional
    public TransactionResponse borrowBook(BorrowRequest request) {

        // 1. Look up Book and Member
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with id: " + request.bookId()));

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + request.memberId()));

        // 2. Check member status is ACTIVE
        if (member.getStatus() != MembershipStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "Member with id " + member.getId() + " is not ACTIVE (current status: "
                            + member.getStatus() + ") and cannot borrow books");
        }

        // 3. Check availableCopies > 0
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessRuleException(
                    "Book '" + book.getTitle() + "' has no available copies");
        }

        // 4. Decrement availableCopies and save
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // 5. Create Transaction with status BORROWED
        Transaction transaction = Transaction.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS))
                .returnDate(null)
                .status(TransactionStatus.BORROWED)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TransactionResponse returnBook(ReturnRequest request) {

        // Find the open (BORROWED) transaction for this book + member
        Transaction transaction = transactionRepository
                .findByBookIdAndMemberIdAndStatus(
                        request.bookId(), request.memberId(), TransactionStatus.BORROWED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active BORROWED transaction found for book id "
                                + request.bookId() + " and member id " + request.memberId()));

        // Set returnDate and status
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(TransactionStatus.RETURNED);
        Transaction saved = transactionRepository.save(transaction);

        // Increment the book's availableCopies
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return mapToResponse(saved);
    }

    @Override
    public List<TransactionResponse> getByMemberId(Long memberId) {
        return transactionRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TransactionResponse> getByBookId(Long bookId) {
        return transactionRepository.findByBookId(bookId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TransactionResponse> getActiveTransactions() {
        return transactionRepository.findByStatus(TransactionStatus.BORROWED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Shared response-mapping helper — used by all five methods above
    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getBook().getId(),
                transaction.getBook().getTitle(),
                transaction.getMember().getId(),
                transaction.getMember().getName(),
                transaction.getBorrowDate(),
                transaction.getDueDate(),
                transaction.getReturnDate(),
                transaction.getStatus()
        );
    }
}