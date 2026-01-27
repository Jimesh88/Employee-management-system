package com.employee.demo.service;

import com.employee.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<Employee> getEmployees(Long departmentId, Pageable pageable);

    Employee getEmployeeById(Long id);

    Employee getCurrentEmployee(String email);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}