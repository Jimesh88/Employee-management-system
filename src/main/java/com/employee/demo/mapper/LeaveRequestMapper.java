package com.employee.demo.mapper;


import com.employee.demo.dto.LeaveRequestDto;
import com.employee.demo.entities.Employee;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.repository.EmployeeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class LeaveRequestMapper {

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Mapping(target = "employee", source = "employeeId")
    public abstract LeaveRequest toEntity(LeaveRequestDto dto);

    @Mapping(target = "employeeId", source = "employee.id")
    public abstract LeaveRequestDto toDto(LeaveRequest entity);

    protected Employee map(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    }
}

