package com.learn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The REPOSITORY layer - your gateway to the database.
 *
 * Notice this is an INTERFACE with no implementation. You never write the
 * code. By extending JpaRepository, Spring Data JPA generates a working
 * implementation at runtime and registers it as a bean.
 *
 * JpaRepository<Employee, Long> means:
 *   - it manages the Employee entity,
 *   - whose primary key (@Id) is of type Long.
 *
 * For free you get: save(), findById(), findAll(), deleteById(), count(), ...
 *
 * @Repository is optional here (Spring Data detects the interface anyway),
 * but it documents intent and enables JPA exception translation.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // You can add custom queries just by declaring method names, e.g.:
    //   List<Employee> findByLastName(String lastName);
    // Spring Data parses the name and writes the query for you.
}
