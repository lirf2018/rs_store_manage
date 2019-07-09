package com.yufan.common.bean;

import java.util.List;
import java.util.Map;

public class PageUtil {

    private int currentPage = 1;// 当前页数

    public int totalPages = 0;// 总页数

    private int pageSize = 0;// 每页显示数

    private int totalRows = 0;// 总数据数

    private int startNum = 0;// 开始记录

    private int nextPage = 0;// 下一页

    private int previousPage = 0;// 上一页

    private int firstPage = 1;//第一页

    private int lastPage = 0;//尾页

    private boolean hasNextPage = false;// 是否有下一页

    private boolean hasPreviousPage = false;// 是否有前一页

    private Object extendsObj;

    private String extendsString;

    private List<Object> list;

    private String sqlCount;//自定义查询总数


    private Map<String, Object> paramMap;//sql查询条件

    private List<Map<String, Object>> resultListMap;//查询出来的数据集合

    public PageUtil(int pageSize, int currentPage, int totalRows) {

        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalRows = totalRows;

        if ((totalRows % pageSize) == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = totalRows / pageSize + 1;
        }

        if (currentPage >= totalPages) {
            hasNextPage = false;
            currentPage = totalPages;
        } else {
            hasNextPage = true;
        }

        if (currentPage <= 1) {
            hasPreviousPage = false;
            currentPage = 1;
        } else {
            hasPreviousPage = true;
        }
        firstPage = 1;
        lastPage = totalPages;

        startNum = (currentPage - 1) * pageSize;

        nextPage = currentPage + 1;

        if (nextPage >= totalPages) {
            nextPage = totalPages;
        }

        previousPage = currentPage - 1;

        if (previousPage <= 1) {
            previousPage = 1;
        }

    }

    public PageUtil(int pageSize, int currentPage, int totalRows, String sqlCount) {

        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalRows = totalRows;
        this.sqlCount = sqlCount;

        if ((totalRows % pageSize) == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = totalRows / pageSize + 1;
        }

        if (currentPage >= totalPages) {
            hasNextPage = false;
            currentPage = totalPages;
        } else {
            hasNextPage = true;
        }

        if (currentPage <= 1) {
            hasPreviousPage = false;
            currentPage = 1;
        } else {
            hasPreviousPage = true;
        }
        firstPage = 1;
        lastPage = totalPages;

        startNum = (currentPage - 1) * pageSize;

        nextPage = currentPage + 1;

        if (nextPage >= totalPages) {
            nextPage = totalPages;
        }

        previousPage = currentPage - 1;

        if (previousPage <= 1) {
            previousPage = 1;
        }

    }

    public boolean isHasNextPage() {

        return hasNextPage;

    }

    public boolean isHasPreviousPage() {

        return hasPreviousPage;

    }

    /**
     * @return the nextPage
     */
    public int getNextPage() {
        return nextPage;
    }

    /**
     * @param nextPage the nextPage to set
     */
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * @return the previousPage
     */
    public int getPreviousPage() {
        return previousPage;
    }

    /**
     * @param previousPage the previousPage to set
     */
    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return the totalRows
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @param totalRows the totalRows to set
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    /**
     * @param hasNextPage the hasNextPage to set
     */
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * @param hasPreviousPage the hasPreviousPage to set
     */
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    /**
     * @return the startNum
     */
    public int getStartNum() {
        return startNum;
    }

    /**
     * @param startNum the startNum to set
     */
    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public Object getExtendsObj() {
        return extendsObj;
    }

    public void setExtendsObj(Object extendsObj) {
        this.extendsObj = extendsObj;
    }

    public String getExtendsString() {
        return extendsString;
    }

    public void setExtendsString(String extendsString) {
        this.extendsString = extendsString;
    }

    public List<Map<String, Object>> getResultListMap() {
        return resultListMap;
    }

    public void setResultListMap(List<Map<String, Object>> resultListMap) {
        this.resultListMap = resultListMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getSqlCount() {
        return sqlCount;
    }

    public void setSqlCount(String sqlCount) {
        this.sqlCount = sqlCount;
    }
}
