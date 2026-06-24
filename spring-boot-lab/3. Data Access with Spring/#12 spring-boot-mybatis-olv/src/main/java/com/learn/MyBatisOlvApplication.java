package com.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * saas-olv practice — MyBatis @Mapper interface + XML mapper.
 *
 * Demonstrates the exact data-access pattern from saas-olv:
 *   Controller -> Service -> @Mapper interface -> *_SQL.xml -> DB
 * with snake_case -> camelCase auto-mapping and LIMIT/OFFSET paging.
 *
 * Uses H2 (PostgreSQL mode) so it runs with no external database.
 */
@SpringBootApplication
@MapperScan("com.learn.board.mapper")
public class MyBatisOlvApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisOlvApplication.class, args);
    }
}
