package com.employee.demo.repository;

import com.employee.demo.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_Id(Long employeeId);
    boolean existsByEmployeeId(Long employeeId);

}