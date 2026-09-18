package com.library.lms.service.impl;

import com.library.lms.dto.BorrowRequest;
import com.library.lms.entity.Book;
import com.library.lms.entity.Member;
import com.library.lms.entity.MembershipStatus;
import com.library.lms.exception.BusinessRuleException;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.MemberRepository;
import com.library.lms.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Book book;
    private Member member;
    private BorrowRequest borrowRequest;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("9780132350884")
                .genre("Tech")
                .totalCopies(1)
                .availableCopies(1)
                .build();

        member = Member.builder()
                .id(1L)
                .name("Suyash")
                .email("suyash@example.com")
                .phoneNumber("9876543210")
                .status(MembershipStatus.ACTIVE)
                .build();

        borrowRequest = new BorrowRequest(1L, 1L);
    }

    @Test
    void borrowBook_shouldThrowBusinessRuleException_whenNoAvailableCopies() {
        book.setAvailableCopies(0);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> transactionService.borrowBook(borrowRequest))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("no available copies");

        verify(bookRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void borrowBook_shouldThrowBusinessRuleException_whenMemberNotActive() {
        member.setStatus(MembershipStatus.SUSPENDED);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> transactionService.borrowBook(borrowRequest))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("not ACTIVE");

        verify(bookRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}