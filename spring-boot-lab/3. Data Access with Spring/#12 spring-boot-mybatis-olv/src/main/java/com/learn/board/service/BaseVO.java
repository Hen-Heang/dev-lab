package com.learn.board.service;

/**
 * Mirrors saas-olv: egovframework.com.cmm.CmmVO (the paging/search parts).
 *
 * Note getFirstIndex(): MyBatis reads #{firstIndex} by calling this getter, so
 * the OFFSET is computed automatically from pageIndex + recordCountPerPage.
 */
public class BaseVO {

    private int pageIndex = 1;
    private int recordCountPerPage = 10;
    private String searchKeyword = "";

    public int getPageIndex() { return pageIndex; }
    public void setPageIndex(int pageIndex) { this.pageIndex = pageIndex; }

    public int getRecordCountPerPage() { return recordCountPerPage; }
    public void setRecordCountPerPage(int recordCountPerPage) { this.recordCountPerPage = recordCountPerPage; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    /** OFFSET for LIMIT/OFFSET paging — derived, no setter needed. */
    public int getFirstIndex() {
        return (pageIndex - 1) * recordCountPerPage;
    }
}
