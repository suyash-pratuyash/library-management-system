package com.library.lms.dto;

import com.library.lms.entity.MembershipStatus;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        LocalDate membershipDate,
        MembershipStatus status
) {
}
