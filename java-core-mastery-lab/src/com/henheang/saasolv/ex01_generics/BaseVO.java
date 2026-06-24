package com.henheang.saasolv.ex01_generics;

/**
 * Mirrors saas-olv: egovframework.com.cmm.CmmVO
 *
 * A common base VO holding paging + search fields that every concrete InVO
 * inherits. Here it also serves as the upper bound for the generic request
 * wrapper (see {@link ApiRequest}).
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
}
