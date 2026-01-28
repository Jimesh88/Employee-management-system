package com.employee.demo.controller;

import com.employee.demo.dto.LeaveRequestDto;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveRequest.LeaveStatus;
import com.employee.demo.mapper.LeaveRequestMapper;
import com.employee.demo.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final LeaveRequestMapper leaveRequestMapper;

    // Submit Leave Request (USER)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveRequestDto> submitLeave(
            @RequestBody @Valid LeaveRequestDto leaveRequestDto) {

        LeaveRequest leaveRequest =
                leaveRequestMapper.toEntity(leaveRequestDto);

        LeaveRequest saved =
                leaveService.applyLeave(leaveRequest);

        return ResponseEntity.ok(
                leaveRequestMapper.toDto(saved)
        );
    }

    // Update Leave Status (ADMIN)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveRequestDto> updateLeaveStatus(
            @PathVariable Long id,
            @RequestParam LeaveStatus status) {

        LeaveRequest updated =
                leaveService.updateLeaveStatus(id, status);

        return ResponseEntity.ok(
                leaveRequestMapper.toDto(updated)
        );
    }

    //  View Employee Leaves ( ADMIN)
    @GetMapping("/employee/{empId}")
    @PreAuthorize("hasRole('USER','ADMIN')")
    public ResponseEntity<List<LeaveRequestDto>> getEmployeeLeaves(
            @PathVariable Long empId) {

        List<LeaveRequestDto> leaves =
                leaveService.getLeavesByEmployee(empId)
                        .stream()
                        .map(leaveRequestMapper::toDto)
                        .toList();

        return ResponseEntity.ok(leaves);
    }
}
