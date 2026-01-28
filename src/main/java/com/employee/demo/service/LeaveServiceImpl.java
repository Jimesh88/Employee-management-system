package com.employee.demo.service;


import com.employee.demo.Exception.EmployeeNotFoundException;
import com.employee.demo.Exception.LeaveRequestNotFoundException;
import com.employee.demo.config.NotificationProducer;
import com.employee.demo.dto.LeaveRequestCreateDto;
import com.employee.demo.entities.Employee;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveStatus;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRepository;
    private final NotificationProducer notificationProducer;
    private final EmployeeRepository employeeRepository;

    public LeaveRequest applyLeave(LeaveRequestCreateDto dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(dto.getEmployeeId().toString()));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());

        // backend-controlled
        leave.setStatus(LeaveStatus.PENDING);

        return leaveRepository.save(leave);
    }



    @Override
    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status) {
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

        // optional: validate employee exists
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }

        return leaveRepository.findByEmployee_Id(employeeId);
    }

}
