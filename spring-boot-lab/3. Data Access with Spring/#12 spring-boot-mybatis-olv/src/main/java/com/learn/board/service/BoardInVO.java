package com.learn.board.service;

/**
 * Mirrors saas-olv: SmpBoardInVO (extends CmmVO). Input/search/form params.
 */
public class BoardInVO extends BaseVO {

    private long boardSn;
    private String boardTitle;
    private String boardCn;
    private String useYn = "Y";
    private String dataRegId;

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
}
