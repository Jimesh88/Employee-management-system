package com.employee.demo.service;


import com.employee.demo.Exception.LeaveRequestNotFoundException;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.repository.LeaveRequestRepository;
import com.employee.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRepository;

    @Override
    public LeaveRequest applyLeave(LeaveRequest leaveRequest) {
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);
        return leaveRepository.save(leaveRequest);
    }

    @Override
    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveRequest.LeaveStatus status) {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found"));

        leave.setStatus(status);
        return leaveRepository.save(leave);
    }

    @Override
    public List<LeaveRequest> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployee_Id(employeeId);
    }
}
