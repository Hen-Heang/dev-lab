package com.learn.board.service;

import java.io.Serializable;

/**
 * Mirrors saas-olv: SmpBoardOutVO (query result only).
 * Field names are camelCase; DB columns are snake_case. MyBatis maps them
 * automatically via map-underscore-to-camel-case (see application.yml).
 */
public class BoardOutVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long boardSn;
    private String boardTitle;
    private String boardCn;
    private String useYn;
    private String dataRegId;
    private String dataRegDt;

    public long getBoardSn() { return boardSn; }
    public void setBoardSn(long boardSn) { this.boardSn = boardSn; }

    public String getBoardTitle() { return boardTitle; }
    public void setBoardTitle(String boardTitle) { this.boardTitle = boardTitle; }

    public String getBoardCn() { return boardCn; }
    public void setBoardCn(String boardCn) { this.boardCn = boardCn; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }

    public String getDataRegId() { return dataRegId; }
    public void setDataRegId(String dataRegId) { this.dataRegId = dataRegId; }

    public String getDataRegDt() { return dataRegDt; }
    public void setDataRegDt(String dataRegDt) { this.dataRegDt = dataRegDt; }
}
