package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dao.BoardDAO;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.vo.BoardVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * BoardServiceTest — Tests business logic in BoardServiceImpl
 * 비즈니스 로직 단위 테스트 (No real DB — DAO is mocked)
 *
 * @ExtendWith(MockitoExtension.class) → enables Mockito
 * @Mock    → creates a fake BoardDAO (no real DB needed)
 * @InjectMocks → creates BoardServiceImpl and injects the fake DAO
 * <p>
 * Why mock the DAO?
 * Service tests should only test business logic — not SQL
 * Mocking makes tests fast and isolated
 */
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardDAO boardDAO;          // fake DAO — no real DB

    @InjectMocks
    private BoardServiceImpl boardService; // real service with fake DAO injected

    private BoardVO sampleBoard;

    @BeforeEach
    void setUp() {
        // prepare a sample board used across tests
        sampleBoard = new BoardVO();
        sampleBoard.setBoardSn(1L);
        sampleBoard.setBoardTitle("Sample Title");
        sampleBoard.setBoardCn("Sample Content");
        sampleBoard.setUseYn("Y");
    }

    // ====================================================
    // findAll() tests
    // ====================================================

    @Test
    @DisplayName("findAll: should return list from DAO")
    void findAll_returnsListFromDAO() {
        // given — fake DAO returns 2 boards
        List<BoardVO> fakeList = Arrays.asList(sampleBoard, new BoardVO());
        when(boardDAO.findAll()).thenReturn(fakeList);

        // when
        List<BoardVO> result = boardService.findAll();

        // then
        assertThat(result).hasSize(2);
        verify(boardDAO, times(1)).findAll(); // DAO was called exactly once
    }

    @Test
    @DisplayName("findAll: should return empty list when no boards")
    void findAll_returnsEmptyList_whenNoBoards() {
        // given
        when(boardDAO.findAll()).thenReturn(Collections.emptyList());

        // when
        List<BoardVO> result = boardService.findAll();

        // then — empty list, NOT null
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    // ====================================================
    // findById() tests
    // ====================================================

    @Test
    @DisplayName("findById: should return board when found")
    void findById_returnsBoard_whenFound() {
        // given — fake DAO returns sampleBoard
        when(boardDAO.findById(1L)).thenReturn(sampleBoard);

        // when
        BoardVO result = boardService.findById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBoardSn()).isEqualTo(1L);
        assertThat(result.getBoardTitle()).isEqualTo("Sample Title");
    }

    @Test
    @DisplayName("findById: should throw NotFoundException when not found")
    void findById_throwsNotFoundException_whenNotFound() {
        // given — fake DAO returns null (not found)
        when(boardDAO.findById(99L)).thenReturn(null);

        // then — expect NotFoundException
        assertThatThrownBy(() -> boardService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99"); // error message contains the ID
    }

    // ====================================================
    // insert() tests
    // ====================================================

    @Test
    @DisplayName("insert: should set useYn = Y when not provided")
    void insert_setsDefaultUseYn_whenNull() {
        // given — board with null useYn
        BoardVO board = new BoardVO();
        board.setBoardTitle("Title");
        board.setBoardCn("Content");
        board.setUseYn(null); // not set

        // when
        boardService.insert(board);

        // then — service sets default Y
        assertThat(board.getUseYn()).isEqualTo("Y");
        verify(boardDAO, times(1)).insert(board);
    }

    @Test
    @DisplayName("insert: should keep useYn when already provided")
    void insert_keepsUseYn_whenAlreadySet() {
        // given
        BoardVO board = new BoardVO();
        board.setBoardTitle("Title");
        board.setUseYn("N"); // explicitly set

        // when
        boardService.insert(board);

        // then — service does NOT override it
        assertThat(board.getUseYn()).isEqualTo("N");
    }

    // ====================================================
    // update() tests
    // ====================================================

    @Test
    @DisplayName("update: should update when board exists")
    void update_updatesBoard_whenExists() {
        // given — board exists
        when(boardDAO.findById(1L)).thenReturn(sampleBoard);
        when(boardDAO.update(sampleBoard)).thenReturn(1);

        // when
        int rows = boardService.update(sampleBoard);

        // then
        assertThat(rows).isEqualTo(1);
        verify(boardDAO, times(1)).update(sampleBoard);
    }

    @Test
    @DisplayName("update: should throw NotFoundException when board not found")
    void update_throwsNotFoundException_whenNotFound() {
        // given — board does NOT exist
        when(boardDAO.findById(99L)).thenReturn(null);

        BoardVO board = new BoardVO();
        board.setBoardSn(99L);
        board.setBoardTitle("Title");

        // then — expect NotFoundException before update runs
        assertThatThrownBy(() -> boardService.update(board))
                .isInstanceOf(NotFoundException.class);

        // verify DAO.update was NEVER called (exception thrown before it)
        verify(boardDAO, never()).update(any());
    }

    // ====================================================
    // delete() tests
    // ====================================================

    @Test
    @DisplayName("delete: should soft delete when board exists")
    void delete_softDeletes_whenExists() {
        // given — DAO returns 1 (success)
        when(boardDAO.delete(1L)).thenReturn(1);

        // when
        int rows = boardService.delete(1L);

        // then
        assertThat(rows).isEqualTo(1);
        verify(boardDAO, times(1)).delete(1L);
    }

    @Test
    @DisplayName("delete: should throw NotFoundException when board not found")
    void delete_throwsNotFoundException_whenNotFound() {
        // given — DAO returns 0 (nothing deleted)
        when(boardDAO.delete(99L)).thenReturn(0);

        // then
        assertThatThrownBy(() -> boardService.delete(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}