package com.yufan.common.bean;

/**
 * 本分页bean  适用JSP页面分页
 *
 * @author zhangsy
 * @project tbnm_console
 * @corporation sunspeedy@ALL RIGHTS RECEIVED
 * @Date 2010-2-27
 */

public class Pagination {
    public int currentPage = 1;

    public int pageSize = 12;

    public String pageAction = "first";


    public int startIndex;

    public int totalNum = 0;

    public int totalPage = 1;

    public boolean lastPage;


    public boolean firstPage;


    public Pagination() {
    }

    /**
     * @return the currentPage
     * @uml.property name="currentPage"
     */
    public int getCurrentPage() {
        return currentPage < 1 ? 1 : currentPage;
    }

    /**
     * @return the lastPage
     * @uml.property name="lastPage"
     */
    public boolean isLastPage() {
        return currentPage == totalPage;
    }

    /**
     * @return the firstPage
     * @uml.property name="firstPage"
     */
    public boolean isFirstPage() {
        return currentPage == 0 || currentPage == 1;
    }

    /**
     * @return the pageAction
     * @uml.property name="pageAction"
     */
    public String getPageAction() {
        return pageAction;
    }

    /**
     * @return the pageSize
     * @uml.property name="pageSize"
     */
    public int getPageSize() {

        return pageSize < 1 ? 15 : pageSize;
    }

    /**
     * @return the totalNum
     * @uml.property name="totalNum"
     */
    public int getTotalNum() {
        return totalNum;
    }

    /**
     * @return the totalPage
     * @uml.property name="totalPage"
     */
    public int getTotalPage() {
        return totalPage < 1 ? 1 : totalPage;
    }

    /**
     * @param totalPage the totalPage to set
     * @uml.property name="totalPage"
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @param totalNum the totalNum to set
     * @uml.property name="totalNum"
     */
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * @param pageSize the pageSize to set
     * @uml.property name="pageSize"
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @param pageAction the pageAction to set
     * @uml.property name="pageAction"
     */
    public void setPageAction(String pageAction) {
        this.pageAction = pageAction;
    }

    /**
     * @param lastPage the lastPage to set
     * @uml.property name="lastPage"
     */
    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @param firstPage the firstPage to set
     * @uml.property name="firstPage"
     */
    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * @param currentPage the currentPage to set
     * @uml.property name="currentPage"
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage < 1 ? 1 : currentPage;
    }


    public void turnpage() {
        if (this.getTotalNum() == 0) {
            this.setCurrentPage(1);
            this.setTotalPage(1);
            this.startIndex = 0;
            return;
        }
        this.setTotalPage(this.getTotalNum() % this.getPageSize() == 0 ? (this
                .getTotalNum() / this.getPageSize()) : (this.getTotalNum())
                / this.getPageSize() + 1);
        if (this.getTotalPage() == 0) {
            this.setTotalPage(1);
        }

        if (this.getTotalPage() < this.getCurrentPage())
            this.setCurrentPage(this.getTotalPage());

        if ("toPage".equalsIgnoreCase(this.getPageAction())) { // ��ת��ָ��ҳ
            // do nothing
        }
        if ("toIndex".equalsIgnoreCase(this.getPageAction())) { // ��ת��ָ����¼
            // do nothing
            return;
        }
        if ("first".equalsIgnoreCase(this.getPageAction())) {
            this.setCurrentPage(1);
        }
        if ("last".equalsIgnoreCase(this.getPageAction())) {
            this.setCurrentPage(this.getTotalPage());
        }
        if ("next".equalsIgnoreCase(this.getPageAction())) {
            if (this.getCurrentPage() < this.getTotalPage()) {
                this.setCurrentPage(this.getCurrentPage() + 1);
            }
            if (this.getCurrentPage() > this.getTotalPage()) {
                this.setCurrentPage(this.getTotalPage());
            }

        }
        if ("pre".equalsIgnoreCase(this.getPageAction())) {
            if (this.getCurrentPage() > 1) {
                this.setCurrentPage(this.getCurrentPage() - 1);
            }
            if (this.getCurrentPage() < 0) {
                this.setCurrentPage(0);
            }

        }
        this.startIndex = (this.getCurrentPage() - 1) * this.getPageSize();
        if (this.startIndex < 0) {
            this.startIndex = 0;
        }
    }

    /**
     * @return the startIndex
     * @uml.property name="startIndex"
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @param startIndex the startIndex to set
     * @uml.property name="startIndex"
     */
    public void setStartIndex(int startIndex) {
        this.pageAction = "toIndex";
        this.startIndex = startIndex;
    }

}
