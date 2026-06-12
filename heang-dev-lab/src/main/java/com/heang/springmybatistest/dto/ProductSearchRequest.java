package com.heang.springmybatistest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product 검색 요청 DTO (페이징 + 필터링)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {

    // 검색 조건
    private String keyword;       // 상품명 검색어
    private Long categoryId;      // 카테고리 필터
    private Integer minPrice;     // 최소 가격
    private Integer maxPrice;     // 최대 가격

    // 정렬
    private String sortBy;        // 정렬 기준 (name, price, stock, createdAt)
    private String sortOrder;     // 정렬 순서 (ASC, DESC)

    // 페이징
    private Integer page;         // 현재 페이지 (1부터 시작)
    private Integer size;         // 페이지당 개수

    // 페이징 계산용 메서드
    public int getOffset() {
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;
        return (page - 1) * size;
    }

    public int getLimit() {
        if (size == null || size < 1) size = 10;
        return size;
    }
}
