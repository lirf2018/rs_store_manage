package com.yufan.common.bean;

import java.util.List;

/**
 * 本分页BEAN 用于Extjs
 * Author: Administrator
 * CreateDate :2014年12月12日
 */
public class PageBean {
    private int totalProperty;
    private List root;
    private String hql; //hql 语句
    private String orderBy; //排序
    private int pageSize = 20;//每页显示数
    private int start;       //查询时每页开始数

    public PageBean() {
    }

    public PageBean(int totalProperty, List root) {
        super();
        this.totalProperty = totalProperty;
        this.root = root;
    }

    public PageBean(int start, int limit) {
        this.start = start;
        this.pageSize = limit;
    }

    public List getRoot() {
        return root;
    }

    public void setRoot(List root) {
        this.root = root;
    }

    public int getTotalProperty() {
        return totalProperty;
    }

    public void setTotalProperty(int totalProperty) {
        this.totalProperty = totalProperty;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }


}
