package com.employee.demo.service;

import com.employee.demo.Exception.EmployeeDeletionException;
import com.employee.demo.Exception.EmployeeNotFoundException;
import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.Employee;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRepository;
    private final NotificationProducer notificationProducer;


    @Override
    public Page<Employee> getEmployees(Long departmentId, Pageable pageable) {
        if (departmentId != null) {
            return employeeRepository.findByDepartment_Id(departmentId, pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }

    @Override
    public Employee getCurrentEmployee(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        Employee savedEmployee= employeeRepository.save(employee);
        // Build a meaningful message
        String message = String.format(
                "Employee created | id=%d | department=%s | full name=%s | email =%s",
                savedEmployee.getId(),
                savedEmployee.getDepartment(),
                savedEmployee.getFullName(),
                savedEmployee.getEmail()
        );

        notificationProducer.sendNotification(message);

        return savedEmployee;
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = getEmployeeById(id);

        existing.setFullName(employee.getFullName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(employee.getDepartment());
        existing.setSalary(employee.getSalary());
        existing.setJoiningDate(employee.getJoiningDate());

        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (leaveRepository.existsByEmployeeId(id)) {
            throw new EmployeeDeletionException(
                    "Cannot delete employee because leave requests exist"
            );
        }
        employeeRepository.deleteById(id);
    }
}