package com.example.practice.SBPractice.Service;

import com.example.practice.SBPractice.Model.Employee;
import com.example.practice.SBPractice.Repository.EmployeeRepository;
import com.example.practice.SBPractice.Repository.EmployeeRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeRepository2 employeeRepository2;

    public List<Employee> findById(long id){
        return employeeRepository2.findById(id);
    }

    public List<Employee> findByFirstName(String fname){
        return employeeRepository.findByFirstName(fname);
    }
}
