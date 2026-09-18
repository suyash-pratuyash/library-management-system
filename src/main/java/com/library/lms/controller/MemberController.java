package com.library.lms.controller;

import com.library.lms.dto.MemberRequest;
import com.library.lms.dto.MemberResponse;
import com.library.lms.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // POST /api/members - Create a new member
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/members/{id} - Get a member by id
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse response = memberService.getById(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/members - Get all members
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getAll();
        return ResponseEntity.ok(members);
    }

    // PUT /api/members/{id} - Update an existing member
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id, @Valid @RequestBody MemberRequest request) {

        MemberResponse response = memberService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/members/{id} - Delete a member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}