package com.learn;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An ENTITY - a plain Java class that maps to a database table.
 *
 * The JPA annotations (@Entity, @Table, @Id, @Column) tell Hibernate
 * (the JPA implementation Spring Boot ships with) how to turn this object
 * into rows in a table and back again - so you never write SQL by hand.
 *
 * The Lombok annotations generate boilerplate code AT COMPILE TIME so the
 * source stays short:
 *   @Data            -> getters, setters, toString(), equals(), hashCode()
 *   @NoArgsConstructor  -> an empty constructor (JPA requires one)
 *   @AllArgsConstructor -> a constructor with every field
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity                          // "this class is a database table"
@Table(name = "tbl_employees")   // the actual table name (defaults to class name if omitted)
public class Employee {

    @Id                                              // the primary key column
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB auto-increments the id on insert
    private Long id;

    // @Column lets you customize the column. nullable=false -> NOT NULL constraint.
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)  // length -> VARCHAR(100)
    private String lastName;

    @Column(name = "email", unique = true)           // unique=false would allow duplicates
    private String email;

}
