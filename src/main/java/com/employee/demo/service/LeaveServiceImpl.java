package com.employee.demo.service;


import com.employee.demo.Exception.LeaveRequestNotFoundException;
import com.employee.demo.config.NotificationProducer;
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
    private final NotificationProducer notificationProducer;

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
        // 🔔 RabbitMQ Notification
        String message = String.format(
                "LEAVE UPDATE | RequestId=%d | Employee=%s | Dates=%s to %s | Status=%s",
                leave.getId(),
                leave.getEmployee().getFullName(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getStatus()
        );

        notificationProducer.sendNotification(message);
        return leaveRepository.save(leave);
    }

    @Override
    public List<LeaveRequest> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployee_Id(employeeId);
    }
}
