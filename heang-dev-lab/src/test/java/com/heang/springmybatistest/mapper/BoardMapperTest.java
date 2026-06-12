package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.BoardVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BoardMapperTest — Tests SQL in BoardMapper.xml
 * 실제 DB에 연결하여 SQL 동작 확인
 *
 * @SpringBootTest  → loads full Spring context with real DB
 * @Transactional   → rolls back DB changes after each test (DB stays clean)
 * @Sql             → runs SQL before each test to insert test data
 */
@SpringBootTest
@Transactional  // each test rolls back automatically — no dirty data left
@Sql(statements = {
        "DELETE FROM co_smp_board_m",
        "INSERT INTO co_smp_board_m (board_title, board_cn, use_yn) VALUES ('Test Title 1', 'Content 1', 'Y')",
        "INSERT INTO co_smp_board_m (board_title, board_cn, use_yn) VALUES ('Test Title 2', 'Content 2', 'Y')",
        "INSERT INTO co_smp_board_m (board_title, board_cn, use_yn) VALUES ('Deleted Board', 'Content 3', 'N')"
})
class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    // ====================================================
    // findAll()
    // ====================================================

    @Test
    @DisplayName("findAll: returns only active boards (use_yn = Y)")
    void findAll_returnsOnlyActiveBoards() {
        List<BoardVO> result = boardMapper.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);                               // only Y records
        assertThat(result).allMatch(b -> "Y".equals(b.getUseYn())); // all active
    }

    @Test
    @DisplayName("findAll: returns newest first (ORDER BY board_sn DESC)")
    void findAll_orderedNewestFirst() {
        List<BoardVO> result = boardMapper.findAll();

        assertThat(result.get(0).getBoardSn())
                .isGreaterThan(result.get(1).getBoardSn());
    }

    @Test
    @DisplayName("findAll: deleted board (use_yn=N) does not appear")
    void findAll_doesNotIncludeDeletedBoards() {
        List<BoardVO> result = boardMapper.findAll();

        assertThat(result)
                .noneMatch(b -> "Deleted Board".equals(b.getBoardTitle()));
    }

    // ====================================================
    // findById()
    // ====================================================

    @Test
    @DisplayName("findById: returns board when found")
    void findById_returnsBoard_whenExists() {
        Long boardSn = boardMapper.findAll().get(0).getBoardSn();

        BoardVO result = boardMapper.findById(boardSn);

        assertThat(result).isNotNull();
        assertThat(result.getBoardSn()).isEqualTo(boardSn);
        assertThat(result.getBoardTitle()).isNotBlank();
    }

    @Test
    @DisplayName("findById: returns null when not found")
    void findById_returnsNull_whenNotFound() {
        BoardVO result = boardMapper.findById(99999L);

        // null — NOT an exception (selectOne behavior)
        assertThat(result).isNull();
    }

    // ====================================================
    // findByTitle()
    // ====================================================

    @Test
    @DisplayName("findByTitle: returns matching boards by keyword")
    void findByTitle_returnsMatchingBoards() {
        List<BoardVO> result = boardMapper.findByTitle("Test");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(b -> b.getBoardTitle().contains("Test"));
    }

    @Test
    @DisplayName("findByTitle: returns empty list when no match")
    void findByTitle_returnsEmptyList_whenNoMatch() {
        List<BoardVO> result = boardMapper.findByTitle("XXXXXXXXNOTFOUND");

        assertThat(result).isNotNull(); // never null — always a List
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByTitle: partial keyword match works")
    void findByTitle_partialMatch() {
        List<BoardVO> result = boardMapper.findByTitle("Title 1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBoardTitle()).isEqualTo("Test Title 1");
    }

    // ====================================================
    // insert()
    // ====================================================

    @Test
    @DisplayName("insert: saves board and returns generated PK via useGeneratedKeys")
    void insert_savesBoard_andReturnsGeneratedKey() {
        BoardVO newBoard = new BoardVO();
        newBoard.setBoardTitle("New Board Title");
        newBoard.setBoardCn("New Content");
        newBoard.setUseYn("Y");

        boardMapper.insert(newBoard);

        // useGeneratedKeys="true" fills boardSn automatically
        assertThat(newBoard.getBoardSn()).isNotNull();
        assertThat(newBoard.getBoardSn()).isGreaterThan(0L);

        // verify saved in DB
        BoardVO saved = boardMapper.findById(newBoard.getBoardSn());
        assertThat(saved).isNotNull();
        assertThat(saved.getBoardTitle()).isEqualTo("New Board Title");
        assertThat(saved.getUseYn()).isEqualTo("Y");
    }

    // ====================================================
    // update()
    // ====================================================

    @Test
    @DisplayName("update: updates title and content, returns 1")
    void update_updatesBoard_successfully() {
        BoardVO board = boardMapper.findAll().get(0);
        board.setBoardTitle("Updated Title");
        board.setBoardCn("Updated Content");

        int rows = boardMapper.update(board);

        assertThat(rows).isEqualTo(1); // 1 row updated

        BoardVO updated = boardMapper.findById(board.getBoardSn());
        assertThat(updated.getBoardTitle()).isEqualTo("Updated Title");
        assertThat(updated.getBoardCn()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("update: returns 0 when board not found")
    void update_returnsZero_whenNotFound() {
        BoardVO board = new BoardVO();
        board.setBoardSn(99999L);
        board.setBoardTitle("Title");
        board.setBoardCn("Content");

        int rows = boardMapper.update(board);

        assertThat(rows).isEqualTo(0); // nothing updated
    }

    // ====================================================
    // delete() — soft delete
    // ====================================================

    @Test
    @DisplayName("delete: sets use_yn = N (soft delete), returns 1")
    void delete_setsUseYnToN() {
        Long boardSn = boardMapper.findAll().get(0).getBoardSn();

        int rows = boardMapper.delete(boardSn);

        assertThat(rows).isEqualTo(1);

        // board no longer appears in findAll (filtered by use_yn = 'Y')
        BoardVO deleted = boardMapper.findById(boardSn);
        assertThat(deleted).isNull();
    }

    @Test
    @DisplayName("delete: returns 0 when board not found")
    void delete_returnsZero_whenNotFound() {
        int rows = boardMapper.delete(99999L);

        assertThat(rows).isEqualTo(0);
    }
}
