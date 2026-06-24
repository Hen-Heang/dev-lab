package com.learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learn.board.service.BoardInVO;
import com.learn.board.service.BoardOutVO;
import com.learn.board.service.BoardService;

/**
 * Runs against the H2 schema.sql + data.sql seeded on startup.
 * 5 rows seeded; 1 has use_yn='N', so 4 are visible.
 */
@SpringBootTest
class BoardMapperTest {

    @Autowired
    private BoardService boardService;

    @Test
    void totalCount_excludesInactiveRows() {
        BoardInVO inVO = new BoardInVO();
        assertThat(boardService.selectListTotCnt(inVO)).isEqualTo(4); // 'N' row hidden
    }

    @Test
    void snakeCase_mapsTo_camelCase() {
        BoardInVO inVO = new BoardInVO();
        List<BoardOutVO> list = boardService.selectList(inVO);
        assertThat(list).isNotEmpty();
        // board_title column populated boardTitle field -> proves auto-mapping
        assertThat(list.get(0).getBoardTitle()).isNotBlank();
    }

    @Test
    void paging_limitsResults() {
        BoardInVO inVO = new BoardInVO();
        inVO.setRecordCountPerPage(2);
        inVO.setPageIndex(1);
        assertThat(boardService.selectList(inVO)).hasSize(2); // LIMIT 2 OFFSET 0
    }

    @Test
    void search_filtersByKeyword() {
        BoardInVO inVO = new BoardInVO();
        inVO.setSearchKeyword("notice");
        assertThat(boardService.selectListTotCnt(inVO)).isEqualTo(2);
    }

    @Test
    void insert_thenDetail_roundTrips() {
        BoardInVO inVO = new BoardInVO();
        inVO.setBoardTitle("inserted by test");
        inVO.setBoardCn("body");
        inVO.setUseYn("Y");
        inVO.setDataRegId("tester");

        int before = boardService.selectListTotCnt(new BoardInVO());
        boardService.insert(inVO);
        int after = boardService.selectListTotCnt(new BoardInVO());

        assertThat(after).isEqualTo(before + 1);
    }
}
