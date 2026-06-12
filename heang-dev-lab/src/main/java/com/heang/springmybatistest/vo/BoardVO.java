package com.heang.springmybatistest.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * BoardVO — Value Object (게시판 값 객체)
 * <p>
 * Carries data between layers: Controller → Service → DAO
 * 레이어 간 데이터 전달용 객체
 * <p>
 * Used with Pattern A (DAO + SqlSessionTemplate),
 * MyBatis requires:
 *   - @NoArgsConstructor → to create an empty object first
 *   - @Setter → to fill each field from the DB row
 */
@Getter           // generates getBoardSn(), getBoardTitle(), ...
@Setter           // generates setBoardSn(), setBoardTitle(), ... (required by MyBatis)
@NoArgsConstructor  // generates BoardVO() — required by MyBatis to create an object
@AllArgsConstructor // generates BoardVO(boardSn, boardTitle, ...) — for manual creation
@ToString         // generates toString() — useful for logging/debugging
public class BoardVO {

    private Long          boardSn;     // PK (기본키)
    private String        boardTitle;  // Title (제목)
    private String        boardCn;     // Content (내용)
    private String        useYn;       // Y = active, N = deleted (사용여부)
    private LocalDateTime dataRegDt;   // Created timestamp (등록일시)
}