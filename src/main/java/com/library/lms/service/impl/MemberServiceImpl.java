package com.library.lms.service.impl;

import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;
import com.library.lms.entity.Member;
import com.library.lms.entity.MembershipStatus;
import com.library.lms.exception.BusinessRuleException;
import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.repository.MemberRepository;
import com.library.lms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse create(MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("A member with email " + request.email() + " already exists");
        }

        Member member = Member.builder()
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .membershipDate(LocalDate.now())
                .status(MembershipStatus.ACTIVE)
                .build();

        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    @Override
    public MemberResponse getById(Long id) {
        Member member = findMemberOrThrow(id);
        return toResponse(member);
    }

    @Override
    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = findMemberOrThrow(id);

        boolean emailChanged = !member.getEmail().equals(request.email());
        if (emailChanged && memberRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("A member with email " + request.email() + " already exists");
        }

        member.setName(request.name());
        member.setEmail(request.email());
        member.setPhoneNumber(request.phoneNumber());
        // membershipDate and status intentionally untouched — not part of general update

        Member updated = memberRepository.save(member);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Member member = findMemberOrThrow(id);
        memberRepository.delete(member);
    }

    private Member findMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getMembershipDate(),
                member.getStatus()
        );
    }

}
