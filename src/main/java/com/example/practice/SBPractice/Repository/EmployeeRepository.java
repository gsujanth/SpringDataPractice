package com.example.practice.SBPractice.Repository;

import com.example.practice.SBPractice.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public List<Employee> findById(long id);

    /*@Query(value = "SELECT * FROM TBL_EMPLOYEES WHERE LOWER(first_name) = LOWER(:fname)",nativeQuery = true)
    List<Employee> findByFirstName(@Param("fname") String fname);*/

    @Query(value = "SELECT e FROM Employee e WHERE LOWER(e.first_name) = LOWER(:fname)")
    List<Employee> findByFirstName(@Param("fname") String fname);
}
