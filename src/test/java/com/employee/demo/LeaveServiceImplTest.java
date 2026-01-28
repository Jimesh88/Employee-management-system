package com.employee.demo;


import com.employee.demo.config.NotificationProducer;
import com.employee.demo.entities.Employee;
import com.employee.demo.entities.LeaveRequest;
import com.employee.demo.entities.LeaveRequest.LeaveStatus;
import com.employee.demo.Exception.LeaveRequestNotFoundException;
import com.employee.demo.repository.LeaveRequestRepository;
import com.employee.demo.service.LeaveServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    @Mock
    private LeaveRequestRepository leaveRepository;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    // ---------- applyLeave ----------

    @Test
    void applyLeave_shouldSetStatusToPending_andSave() {

        LeaveRequest leave = new LeaveRequest();
        leave.setStatus(null);

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LeaveRequest saved = leaveService.applyLeave(leave);

        assertThat(saved.getStatus()).isEqualTo(LeaveStatus.PENDING);
        verify(leaveRepository).save(leave);
        verifyNoInteractions(notificationProducer);
    }

    // ---------- updateLeaveStatus ----------

    @Test
    void updateLeaveStatus_shouldUpdateStatus_andSendNotification() {

        Employee employee = new Employee();
        employee.setFullName("John Doe");

        LeaveRequest leave = new LeaveRequest();
        leave.setId(1L);
        leave.setEmployee(employee);
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now().plusDays(2));
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(leave));

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenReturn(leave);

        LeaveRequest result =
                leaveService.updateLeaveStatus(1L, LeaveStatus.APPROVED);

        assertThat(result.getStatus()).isEqualTo(LeaveStatus.APPROVED);

        verify(notificationProducer)
                .sendNotification(contains("LEAVE UPDATE"));
        verify(leaveRepository).save(leave);
    }

    @Test
    void updateLeaveStatus_whenLeaveNotFound_shouldThrowException() {

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                leaveService.updateLeaveStatus(1L, LeaveStatus.REJECTED))
                .isInstanceOf(LeaveRequestNotFoundException.class)
                .hasMessage("Leave request not found");

        verify(notificationProducer, never()).sendNotification(any());
        verify(leaveRepository, never()).save(any());
    }

    // ---------- getLeavesByEmployee ----------

    @Test
    void getLeavesByEmployee_shouldReturnLeaveList() {

        when(leaveRepository.findByEmployee_Id(1L))
                .thenReturn(List.of(new LeaveRequest(), new LeaveRequest()));

        List<LeaveRequest> result =
                leaveService.getLeavesByEmployee(1L);

        assertThat(result).hasSize(2);
        verify(leaveRepository).findByEmployee_Id(1L);
    }
}
