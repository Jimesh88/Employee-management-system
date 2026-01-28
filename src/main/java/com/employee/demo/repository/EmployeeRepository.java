package com.employee.demo.repository;

import com.employee.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartment_Id(Long departmentId, Pageable pageable);

    Optional<Employee> findByEmail(String email);

    @Query("""
    select e from Employee e
    join fetch e.department
    where e.department.id = :deptId
""")
    Page<Employee> findEmployeesByDepartment(
            @Param("deptId") Long deptId,
            Pageable pageable
    );

}