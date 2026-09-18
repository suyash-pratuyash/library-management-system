package com.library.lms.service.impl;

import com.library.lms.dto.BorrowRequest;
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

        return new TransactionResponse(
                saved.getId(),
                book.getId(),
                book.getTitle(),
                member.getId(),
                member.getName(),
                saved.getBorrowDate(),
                saved.getDueDate(),
                saved.getReturnDate(),
                saved.getStatus()
        );
    }
}
