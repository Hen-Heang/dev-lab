package com.learn.board.service;

import java.util.List;

/** Mirrors saas-olv: SmpBoardService (business contract). */
public interface BoardService {

    List<BoardOutVO> selectList(BoardInVO inVO);

    int selectListTotCnt(BoardInVO inVO);

    BoardOutVO selectDetail(BoardInVO inVO);

    void insert(BoardInVO inVO);

    void update(BoardInVO inVO);

    void delete(BoardInVO inVO);
}
