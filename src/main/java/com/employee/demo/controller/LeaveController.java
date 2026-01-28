package com.employee.demo.controller;

import com.employee.demo.dto.LeaveRequestCreateDto;
import com.employee.demo.dto.LeaveRequestResponseDto;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveStatus;
import com.employee.demo.mapper.LeaveRequestResponseMapper;
import com.employee.demo.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final LeaveRequestResponseMapper leaveRequestResponseMapper;

    // Submit Leave Request (USER)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveRequestResponseDto> submitLeave(
            @RequestBody @Valid LeaveRequestCreateDto dto) {

        LeaveRequest saved = leaveService.applyLeave(dto);

        LeaveRequestResponseDto response =
                leaveRequestResponseMapper.toDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


    // Update Leave Status (ADMIN)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveRequestResponseDto> updateLeaveStatus(
            @PathVariable Long id,
            @RequestParam LeaveStatus status) {

        LeaveRequest updated =
                leaveService.updateLeaveStatus(id, status);

        return ResponseEntity.ok(
                leaveRequestResponseMapper.toDto(updated)
        );
    }


    //  View Employee Leaves ( ADMIN)
    @GetMapping("/employee/{empId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<LeaveRequestResponseDto>> getEmployeeLeaves(
            @PathVariable Long empId) {

        List<LeaveRequestResponseDto> leaves =
                leaveService.getLeavesByEmployee(empId)
                        .stream()
                        .map(leaveRequestResponseMapper::toDto)
                        .toList();

        return ResponseEntity.ok(leaves);
    }

}
