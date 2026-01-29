package com.employee.demo;


import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.Department;
import com.employee.demo.entities.Employee;
import com.employee.demo.Exception.ResourceNotFoundException;
import com.employee.demo.repository.DepartmentRepository;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.service.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;


    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void createDepartment_shouldSaveAndPublishNotification() {

        Department department = Department.builder()
                .name("HR")
                .build();

        Department saved = Department.builder()
                .id(1L)
                .name("HR")
                .build();

        when(departmentRepository.save(department)).thenReturn(saved);

        Department result = departmentService.createDepartment(department);

        assertThat(result.getId()).isEqualTo(1L);
        verify(departmentRepository).save(department);

    }

    @Test
    void getAllDepartments_shouldReturnList() {

        when(departmentRepository.findAll())
                .thenReturn(List.of(new Department(), new Department()));

        List<Department> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(2);
        verify(departmentRepository).findAll();
    }

    @Test
    void getEmployeesByDepartment_whenDepartmentNotFound_shouldThrowException() {

        when(departmentRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() ->
                departmentService.getEmployeesByDepartment(1L, Pageable.unpaged()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getEmployeesByDepartment_whenValid_shouldReturnPage() {

        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.findEmployeesByDepartment(eq(1L), any()))
                .thenReturn(new PageImpl<>(List.of(new Employee())));

        Page<Employee> result =
                departmentService.getEmployeesByDepartment(1L, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
