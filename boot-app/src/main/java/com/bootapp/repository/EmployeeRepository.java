package com.bootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootapp.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{


}