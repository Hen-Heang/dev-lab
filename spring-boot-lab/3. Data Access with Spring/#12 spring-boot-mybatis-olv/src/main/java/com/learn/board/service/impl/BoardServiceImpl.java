package com.learn.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learn.board.mapper.BoardMapper;
import com.learn.board.service.BoardInVO;
import com.learn.board.service.BoardOutVO;
import com.learn.board.service.BoardService;

/** Mirrors saas-olv: SmpBoardServiceImpl (delegates to the mapper). */
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<BoardOutVO> selectList(BoardInVO inVO) {
        return boardMapper.selectList(inVO);
    }

    @Override
    public int selectListTotCnt(BoardInVO inVO) {
        return boardMapper.selectListTotCnt(inVO);
    }

    @Override
    public BoardOutVO selectDetail(BoardInVO inVO) {
        return boardMapper.selectDetail(inVO);
    }

    @Override
    public void insert(BoardInVO inVO) {
        boardMapper.insert(inVO);
    }

    @Override
    public void update(BoardInVO inVO) {
        boardMapper.update(inVO);
    }

    @Override
    public void delete(BoardInVO inVO) {
        boardMapper.delete(inVO);
    }
}
