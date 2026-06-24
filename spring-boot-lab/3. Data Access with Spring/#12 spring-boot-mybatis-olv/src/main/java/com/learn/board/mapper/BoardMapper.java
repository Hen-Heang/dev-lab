package com.learn.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.learn.board.service.BoardInVO;
import com.learn.board.service.BoardOutVO;

/**
 * Mirrors saas-olv: SmpBoardMapper.
 *
 * @Mapper interface — MyBatis generates the implementation at runtime. Each
 * method name matches an <id> in BoardMapper.xml, whose namespace equals this
 * interface's fully-qualified class name.
 */
@Mapper
public interface BoardMapper {

    List<BoardOutVO> selectList(BoardInVO inVO);

    int selectListTotCnt(BoardInVO inVO);

    BoardOutVO selectDetail(BoardInVO inVO);

    void insert(BoardInVO inVO);

    void update(BoardInVO inVO);

    void delete(BoardInVO inVO);
}
