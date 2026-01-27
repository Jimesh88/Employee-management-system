package com.employee.demo.Exception;

public class EmployeeNotFoundException extends  RuntimeException{
    public  EmployeeNotFoundException(String message){
        super(message);
    }
}
