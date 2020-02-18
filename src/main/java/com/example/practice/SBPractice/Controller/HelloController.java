package com.example.practice.SBPractice.Controller;

import com.example.practice.SBPractice.Model.Employee;
import com.example.practice.SBPractice.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Value("${profile.selected}")
    private String selectedProfile;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String sayHello(){
        return "Hello "+selectedProfile;
    }

    @RequestMapping(path = "/employees/id/{id}", method = RequestMethod.GET)
    public List<Employee> getEmployeeById(@PathVariable("id") long id){
        return employeeService.findById(id);
    }

    @RequestMapping(path = "/employees/first_name/{first_name}", method = RequestMethod.GET)
    public List<Employee> getEmployeeByFName(@PathVariable("first_name") String first_name){
        return employeeService.findByFirstName(first_name);
    }
}
