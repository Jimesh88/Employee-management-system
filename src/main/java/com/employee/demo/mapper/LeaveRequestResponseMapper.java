package com.employee.demo.mapper;

import com.employee.demo.dto.LeaveRequestResponseDto;
import com.employee.demo.entities.LeaveRequest;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestResponseMapper {

    public LeaveRequestResponseDto toDto(LeaveRequest entity) {
        LeaveRequestResponseDto dto = new LeaveRequestResponseDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setReason(entity.getReason());
        dto.setLeaveStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}