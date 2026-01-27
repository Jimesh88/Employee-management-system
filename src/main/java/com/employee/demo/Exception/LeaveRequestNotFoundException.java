package com.employee.demo.Exception;

public class LeaveRequestNotFoundException extends RuntimeException{
    public  LeaveRequestNotFoundException(String message){
        super(message);
    }
}
