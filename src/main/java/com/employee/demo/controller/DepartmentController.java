package com.employee.demo.controller;


import com.employee.demo.dto.DepartmentDto;
import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.mapper.DepartmentMapper;
import com.employee.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    // List All Departments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DepartmentDto> getDepartments() {
        return departmentService.getAllDepartments()
                .stream()
                .map(departmentMapper::toDto)
                .toList();
    }

    // Create Department
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> createDepartment(
            @RequestBody @Valid DepartmentDto departmentDto) {

        Department department = departmentMapper.toEntity(departmentDto);
        Department saved = departmentService.createDepartment(department);

        return ResponseEntity.ok(departmentMapper.toDto(saved));
    }

    //  Get Employees by Department
    @GetMapping("/{id}/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Employee> getEmployeesByDepartment(
            @PathVariable Long id,
            Pageable pageable) {

        return departmentService.getEmployeesByDepartment(id, pageable);
    }
}
