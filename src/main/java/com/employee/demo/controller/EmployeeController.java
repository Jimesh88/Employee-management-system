package com.employee.demo.controller;

import com.employee.demo.dto.EmployeeDto;
import com.employee.demo.entities.Employee;
import com.employee.demo.mapper.EmployeeMapper;
import com.employee.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    // List Employees (optional department filter)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<EmployeeDto> getEmployees(
            @RequestParam(required = false) Long departmentId,
            Pageable pageable) {

        return employeeService.getEmployees(departmentId, pageable)
                .map(employeeMapper::toDto);
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employeeMapper.toDto(employee);
    }

    //  Get Logged-in Employee Profile
    @GetMapping("/me")
    public EmployeeDto getMyProfile(Authentication auth) {
        Employee employee = employeeService.getCurrentEmployee(auth.getName());
        return employeeMapper.toDto(employee);
    }

    // Create Employee
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> create(
            @RequestBody @Valid EmployeeDto employeeDto) {

        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee saved = employeeService.createEmployee(employee);

        return ResponseEntity.ok(employeeMapper.toDto(saved));
    }

    // Update Employee
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> update(
            @PathVariable Long id,
            @RequestBody @Valid EmployeeDto employeeDto) {

        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee updated = employeeService.updateEmployee(id, employee);

        return ResponseEntity.ok(employeeMapper.toDto(updated));
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
