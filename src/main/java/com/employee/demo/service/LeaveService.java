package com.employee.demo.service;

import com.employee.demo.entities.LeaveRequest;

import java.util.List;

public interface LeaveService {

    LeaveRequest applyLeave(LeaveRequest leaveRequest);

    LeaveRequest updateLeaveStatus(Long leaveId, LeaveRequest.LeaveStatus status);

    List<LeaveRequest> getLeavesByEmployee(Long employeeId);
}
