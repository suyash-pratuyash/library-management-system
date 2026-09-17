package com.library.lms.service;

import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;

import java.util.List;

public interface MemberService {
    MemberResponse create(MemberRequest request);
    MemberResponse getById(Long id);
    List<MemberResponse> getAll();
    MemberResponse update(Long id, MemberRequest request);
    void delete(Long id);
}
