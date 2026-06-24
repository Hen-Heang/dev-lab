package com.henheang.saasolv.ex01_generics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EXERCISE 01 — Bounded generics + static factory method.
 * Concept source: saas-olv CmmInVO&lt;T extends CmmVO&gt; / CmmOutVO.of(...)
 *
 * Run this file's main() to see it work.
 */
public class Ex01Generics {

    /** A concrete InVO, just like SmpBoardInVO extends CmmVO. */
    static class BoardInVO extends BaseVO {
        private String boardTitle;
        public String getBoardTitle() { return boardTitle; }
        public void setBoardTitle(String boardTitle) { this.boardTitle = boardTitle; }
    }

    public static void main(String[] args) {
        // --- build a typed request (T is inferred as BoardInVO) ---
        BoardInVO inVO = new BoardInVO();
        inVO.setSearchKeyword("notice");
        inVO.setBoardTitle("Welcome");

        ApiRequest<BoardInVO> request = new ApiRequest<>();
        request.setBody(inVO);

        // because of <T extends BaseVO>, the compiler KNOWS body has VO methods:
        System.out.println("search keyword = " + request.getBody().getSearchKeyword());
        System.out.println("page (never null) = " + request.getPage());

        // --- build a typed response via the static factory ---
        List<String> rows = List.of("row-1", "row-2", "row-3");
        Map<String, Object> page = new HashMap<>();
        page.put("totalRecordCount", 3);
        page.put("pageIndex", 1);

        ApiResponse<List<String>> response = ApiResponse.of(rows, page);
        System.out.println("body = " + response.getBody());
        System.out.println("page = " + response.getPage());

        // The line below would NOT compile — that's the value of bounded generics:
        // ApiRequest<String> bad = new ApiRequest<>();   // String does not extend BaseVO

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a second InVO type (e.g. UserInVO) and reuse ApiRequest with it.
         *  - Add ApiResponse.error(int status, String msg) as another factory.
         *  - Make ApiResponse implement a toJson() and print it.
         */
    }
}
