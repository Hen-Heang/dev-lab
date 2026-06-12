package com.learn.service;

import com.learn.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto findByEmployeeNo(String empNo);

    List<EmployeeDto> getAll();

    EmployeeDto update(EmployeeDto employeeDto, String empNo);

    void delete(String empNo);
}
