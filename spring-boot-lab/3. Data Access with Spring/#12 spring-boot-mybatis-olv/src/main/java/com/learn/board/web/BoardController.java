package com.learn.board.web;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.board.service.BoardInVO;
import com.learn.board.service.BoardOutVO;
import com.learn.board.service.BoardService;

/**
 * Mirrors saas-olv: SmpBoardController (here REST/JSON instead of Thymeleaf so
 * you can test it with curl). The list endpoint returns { body, page } just like
 * CmmOutVO.of(list, paginationInfo).
 */
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /** GET /board/list?searchKeyword=&pageIndex=1&recordCountPerPage=10 */
    @GetMapping("/list")
    public Map<String, Object> list(@ModelAttribute BoardInVO inVO) {
        List<BoardOutVO> body = boardService.selectList(inVO);
        int total = boardService.selectListTotCnt(inVO);

        Map<String, Object> page = new LinkedHashMap<>();
        page.put("totalRecordCount", total);
        page.put("pageIndex", inVO.getPageIndex());
        page.put("recordCountPerPage", inVO.getRecordCountPerPage());

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("body", body);
        out.put("page", page);
        return out;
    }

    /** GET /board/{boardSn} */
    @GetMapping("/{boardSn}")
    public BoardOutVO detail(@PathVariable long boardSn) {
        BoardInVO inVO = new BoardInVO();
        inVO.setBoardSn(boardSn);
        return boardService.selectDetail(inVO);
    }

    /** POST /board  (JSON body) */
    @PostMapping
    public Map<String, Object> create(@RequestBody BoardInVO inVO) {
        boardService.insert(inVO);
        return Map.of("success", true);
    }

    /** PUT /board/{boardSn} */
    @PutMapping("/{boardSn}")
    public Map<String, Object> update(@PathVariable long boardSn, @RequestBody BoardInVO inVO) {
        inVO.setBoardSn(boardSn);
        boardService.update(inVO);
        return Map.of("success", true);
    }

    /** DELETE /board/{boardSn} */
    @DeleteMapping("/{boardSn}")
    public Map<String, Object> delete(@PathVariable long boardSn) {
        BoardInVO inVO = new BoardInVO();
        inVO.setBoardSn(boardSn);
        boardService.delete(inVO);
        return Map.of("success", true);
    }
}
