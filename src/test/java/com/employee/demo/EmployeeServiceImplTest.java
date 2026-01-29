package com.employee.demo;


import com.employee.demo.Exception.EmployeeDeletionException;
import com.employee.demo.Exception.EmployeeNotFoundException;
import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.LeaveRequestRepository;
import com.employee.demo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveRequestRepository leaveRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private NotificationProducer notificationProducer;

    // ---------- getEmployees ----------

    @Test
    void getEmployees_withDepartmentId_shouldReturnFilteredEmployees() {

        Page<Employee> page =
                new PageImpl<>(List.of(new Employee()));

        when(employeeRepository.findByDepartment_Id(eq(1L), any()))
                .thenReturn(page);

        Page<Employee> result =
                employeeService.getEmployees(1L, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(employeeRepository).findByDepartment_Id(eq(1L), any());
    }

    @Test
    void getEmployees_withoutDepartmentId_shouldReturnAllEmployees() {

        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Employee())));

        Page<Employee> result =
                employeeService.getEmployees(null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(employeeRepository).findAll(any(Pageable.class));
    }

    // ---------- getEmployeeById ----------

    @Test
    void getEmployeeById_whenFound_shouldReturnEmployee() {

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getEmployeeById_whenNotFound_shouldThrowException() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getEmployeeById(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found");
    }

    // ---------- getCurrentEmployee ----------

    @Test
    void getCurrentEmployee_whenFound_shouldReturnEmployee() {

        Employee employee = new Employee();
        employee.setEmail("test@test.com");

        when(employeeRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(employee));

        Employee result =
                employeeService.getCurrentEmployee("test@test.com");

        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void getCurrentEmployee_whenNotFound_shouldThrowException() {

        when(employeeRepository.findByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeService.getCurrentEmployee("missing@test.com"))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    // ---------- createEmployee ----------

    @Test
    void createEmployee_shouldSaveEmployee() {

        Employee employee = new Employee();

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        Employee saved = employeeService.createEmployee(employee);

        assertThat(saved).isNotNull();
        verify(employeeRepository).save(employee);
        verify(notificationProducer)
                .sendNotification(contains("Employee created"));
    }

    // ---------- updateEmployee ----------

    @Test
    void updateEmployee_shouldUpdateAllFields() {

        Employee existing = new Employee();
        existing.setId(1L);

        Employee updated = new Employee();
        updated.setFullName("John");
        updated.setEmail("john@test.com");
        updated.setDepartment(new Department());
        updated.setSalary(BigDecimal.valueOf(50000));
        updated.setJoiningDate(LocalDate.now());

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(employeeRepository.save(existing))
                .thenReturn(existing);

        Employee result =
                employeeService.updateEmployee(1L, updated);

        assertThat(result.getFullName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john@test.com");
        assertThat(result.getSalary()).isEqualTo(BigDecimal.valueOf(50000));
    }

    // ---------- deleteEmployee ----------

    @Test
    void deleteEmployee_whenNoLeaves_shouldDelete() {

        when(leaveRepository.existsByEmployeeId(1L))
                .thenReturn(false);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_whenLeavesExist_shouldThrowException() {

        when(leaveRepository.existsByEmployeeId(1L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                employeeService.deleteEmployee(1L))
                .isInstanceOf(EmployeeDeletionException.class)
                .hasMessageContaining("Cannot delete employee");

        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
