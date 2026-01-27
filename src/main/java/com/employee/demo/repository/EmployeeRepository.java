package com.employee.demo.repository;

import com.employee.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment_Id(Long departmentId, Pageable pageable);

    Optional<Employee> findByEmail(String email);

}