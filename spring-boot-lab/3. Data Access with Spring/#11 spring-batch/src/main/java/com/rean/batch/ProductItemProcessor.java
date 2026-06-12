package com.learn.batch;

import com.learn.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * ItemProcessor — transforms a CSV row into a Product entity.
 * Also enriches the data: sets status based on price.
 */
@Slf4j
@Component
public class ProductItemProcessor implements ItemProcessor<ProductCsvRow, Product> {

    @Override
    public Product process(ProductCsvRow row) {
        String status = row.getPrice().compareTo(new BigDecimal("100")) > 0 ? "EXPENSIVE" : "CHEAP";

        Product product = Product.builder()
                .name(row.getName().toUpperCase()) // normalize name
                .price(row.getPrice())
                .category(row.getCategory())
                .status(status)
                .build();

        log.info("Processing: {} -> status={}", row.getName(), status);
        return product;
    }
}
