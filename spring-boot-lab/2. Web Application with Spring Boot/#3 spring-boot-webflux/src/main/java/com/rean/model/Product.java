package com.learn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * R2DBC entity — uses Spring Data annotations (NOT Jakarta Persistence).
 * R2DBC does not use JPA/Hibernate: no @Entity, no @GeneratedValue.
 * The @Id field is populated by the database (IDENTITY strategy in schema.sql).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_product")
public class Product {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("price")
    private BigDecimal price;

    @Column("category")
    private String category;
}
