package com.employee.demo.service;

import com.employee.demo.Exception.DepartmentNotFoundException;
import com.employee.demo.Exception.ResourceNotFoundException;
import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.repository.DepartmentRepository;
import com.employee.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationProducer notificationProducer;


    @Override
    public Department createDepartment(Department department) {

        Department saved = departmentRepository.save(department);

        // Build a meaningful message
        String message = String.format(
                "Department created | id=%d | name=%s",
                saved.getId(),
                saved.getName()
        );

        // Publish notification
        notificationProducer.sendNotification(message);

        return saved;
    }


    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable) {

        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException(
                    "Department not found with id: " + departmentId
            );
        }
       return employeeRepository.findByDepartment_Id(departmentId,pageable);
    }
}
