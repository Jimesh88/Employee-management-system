package com.employee.demo.controller;

import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveRequest.LeaveStatus;
import com.employee.demo.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final NotificationProducer notificationProducer;

    //  Submit Leave Request (USER)
    @PostMapping
    public LeaveRequest submitLeave(@RequestBody @Valid LeaveRequest leaveRequest) {
        return leaveService.applyLeave(leaveRequest);
    }

    //  Update Leave Status (ADMIN)
    @PutMapping("/{id}/status")
    public LeaveRequest updateLeaveStatus(
            @PathVariable Long id,
            @RequestParam LeaveStatus status) {

        LeaveRequest updated = leaveService.updateLeaveStatus(id, status);

        // 🔔 RabbitMQ Notification
        String message = String.format(
                "LEAVE UPDATE | RequestId=%d | Employee=%s | Dates=%s to %s | Status=%s",
                updated.getId(),
                updated.getEmployee().getFullName(),
                updated.getStartDate(),
                updated.getEndDate(),
                updated.getStatus()
        );

        notificationProducer.sendNotification(message);

        return updated;
    }

    //  View Employee Leaves (USER / ADMIN)
    @GetMapping("/employee/{empId}")
    public List<LeaveRequest> getEmployeeLeaves(@PathVariable Long empId) {
        return leaveService.getLeavesByEmployee(empId);
    }
}
