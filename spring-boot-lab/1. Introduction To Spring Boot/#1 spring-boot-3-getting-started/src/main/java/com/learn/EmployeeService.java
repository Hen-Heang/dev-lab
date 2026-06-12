package com.learn;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * The SERVICE layer - where business logic lives.
 *
 * The classic Spring layering is:  Controller -> Service -> Repository
 *   - Controller: handles HTTP, knows nothing about the database.
 *   - Service:    the rules ("if id is null, create; otherwise update").
 *   - Repository: talks to the database.
 * Keeping logic here (not in the controller) makes it reusable and testable.
 *
 * @Service marks this as a Spring-managed bean (a specialized @Component).
 * @Slf4j is Lombok: it creates a logger field called `log` for you.
 * @RequiredArgsConstructor is Lombok: it generates a constructor with one
 *   parameter for each `final` field - which is how the repository gets
 *   injected below (constructor injection, the preferred DI style).
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeService {

    // `final` + @RequiredArgsConstructor => Spring injects this automatically.
    // (Constructor injection is preferred over field injection: it makes the
    //  dependency explicit, allows `final`, and is easy to mock in tests.)
    public final EmployeeRepository employeeRepository;

    /** Create a new employee (no id) OR update an existing one (id present). */
    public Employee createOrUpdate(Employee employee) {
        if(employee.getId() == null){
            // No id yet -> this is a brand new record, just save it.
            employeeRepository.save(employee);
            return employee;
        }
        // Id present -> look it up and update its fields if it exists.
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        if(employeeOptional.isPresent()) {
            Employee employeeInstance = employeeOptional.get();
            employeeInstance.setFirstName(employee.getFirstName());
            employeeInstance.setLastName(employee.getLastName());
            employeeInstance.setEmail(employee.getEmail());
            employeeRepository.save(employeeInstance);  // save() does an UPDATE when the id exists
            return employeeInstance;
        }
        // if no data we can response invalid request with employee id {}
        employeeRepository.save(employee);
        return employee;
    }

    /**
     * Delete by id. Throws a checked exception if the id doesn't exist, so the
     * caller is forced to handle the "not found" case.
     */
    public void delete(Long id) throws EmployeeRecordNotFoundException {
       // employeeRepository.deleteById(id);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isPresent()){
            employeeRepository.delete(employeeOptional.get());
        }else {
            log.info("Employee id {} not found", id);   // {} is replaced by `id` - efficient logging
            throw new EmployeeRecordNotFoundException("Employee id not found");
        }
    }

    /** Return every employee. */
    public List<Employee> getAll() {
        return employeeRepository.findAll(); // if you have a lot of data you have to custom with pagination
    }

    /** Find one employee or throw if the id is unknown. */
    public Employee findById(Long id) throws EmployeeRecordNotFoundException {
        // Optional is Java's "maybe a value, maybe nothing" wrapper - it forces
        // you to think about the empty case instead of risking a NullPointerException.
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isPresent()){
            return employeeOptional.get();
        }
        log.info("Employee id {} not found", id);
        throw new EmployeeRecordNotFoundException("Employee id not found");
    }

}
