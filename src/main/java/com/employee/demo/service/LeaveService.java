package com.employee.demo.service;

import com.employee.demo.dto.LeaveRequestCreateDto;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveStatus;

import java.util.List;

public interface LeaveService {

    LeaveRequest applyLeave(LeaveRequestCreateDto leaveRequest);

    LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status);

    List<LeaveRequest> getLeavesByEmployee(Long employeeId);
}
