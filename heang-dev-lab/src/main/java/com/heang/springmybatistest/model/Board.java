package com.heang.springmybatistest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Board Entity (게시판 엔티티)
 * Maps 1:1 to co_smp_board_m table
 * <p>
 * Table: co_smp_board_m
 * - board_sn: Primary Key (기본키)
 * - board_title: Post title (제목)
 * - board_cn: Post content (내용)
 * - use_yn: Active flag Y/N (사용여부) — soft delete
 * - data_reg_dt: Created timestamp (등록일시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private Long          boardSn;      // PK
    private String        boardTitle;   // Title (제목)
    private String        boardCn;      // Content (내용)
    private String        useYn;        // Y = active, N = deleted (사용여부)
    private LocalDateTime dataRegDt;    // Created timestamp (등록일시)
}
