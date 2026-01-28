package com.employee.demo.mapper;


import com.employee.demo.dto.EmployeeDto;
import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.repository.DepartmentRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmployeeMapper {

    @Autowired
    protected DepartmentRepository departmentRepository;

    @Mapping(target = "department", source = "departmentId")
    public abstract Employee toEntity(EmployeeDto dto);

    @Mapping(target = "departmentId", source = "department.id")
    public abstract EmployeeDto toDto(Employee entity);

    protected Department map(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
    }
}

