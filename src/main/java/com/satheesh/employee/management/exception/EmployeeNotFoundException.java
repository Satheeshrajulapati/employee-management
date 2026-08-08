package com.satheesh.employee.management.exception;

public class EmployeeNotFoundException extends RuntimeException {

   public EmployeeNotFoundException(String message){
       super(message);
    }
}
