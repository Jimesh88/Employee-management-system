package com.employee.demo.controller;


import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.service.DepartmentService;
import com.employee.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // 1️⃣ List All Departments
    @GetMapping
    public List<Department> getDepartments() {
        return departmentService.getAllDepartments();
    }

    // 2️⃣ Create Department
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody @Valid Department department) {
        return ResponseEntity.ok( departmentService.createDepartment(department));

    }

    @GetMapping("/{id}/employees")
    public Page<Employee> getEmployeesByDepartment(
            @PathVariable Long id,
            Pageable pageable) {

        return departmentService.getEmployeesByDepartment(id, pageable);
    }

}
