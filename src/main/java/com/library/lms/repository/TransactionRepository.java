package com.library.lms.repository;

import com.library.lms.entity.Transaction;
import com.library.lms.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByMemberId(Long memberId);

    List<Transaction> findByBookId(Long bookId);

    Optional<Transaction> findByBookIdAndMemberIdAndStatus(
            Long bookId, Long memberId ,TransactionStatus status);
}
