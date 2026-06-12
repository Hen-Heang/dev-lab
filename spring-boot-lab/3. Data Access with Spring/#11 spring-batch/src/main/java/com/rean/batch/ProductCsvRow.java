package com.learn.batch;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents one row from the CSV file (raw input).
 * The processor maps this to a Product entity.
 */
@Data
public class ProductCsvRow {
    private String name;
    private BigDecimal price;
    private String category;
}
