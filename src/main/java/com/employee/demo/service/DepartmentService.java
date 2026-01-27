package com.employee.demo.service;

import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable);
}
