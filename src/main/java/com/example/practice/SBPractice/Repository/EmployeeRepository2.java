package com.example.practice.SBPractice.Repository;

import com.example.practice.SBPractice.Model.Employee;
import com.example.practice.SBPractice.RowMappers.EmployeeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EmployeeRepository2 {

    /*@Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;*/
    private JdbcTemplate jdbcTemplate;

    public EmployeeRepository2 (DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Employee> findById(long id){
        String sql= "SELECT * FROM TBL_EMPLOYEES WHERE id="+id;
        //jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper(Employee.class));
    }
}
