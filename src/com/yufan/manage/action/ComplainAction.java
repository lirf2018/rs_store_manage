package com.yufan.manage.action;

import com.opensymphony.xwork2.interceptor.annotations.Allowed;
import com.yufan.common.action.LotFilterAction;
import com.yufan.common.bean.PageUtil;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbComplain;
import com.yufan.util.RsConstants;
import com.yufan.vo.ShopCondition;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 创建人: lirf
 * 创建时间:  2018/8/9 14:55
 * 功能介绍:
 */
@Scope("prototype")
@Service("complainAction")
@Namespace("/manage")
@ParentPackage("common")
public class ComplainAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private Integer currentPage;// 当前页数

    /**
     * 加载店铺列表
     *
     * @return
     */
    @Action(value = "loadComplainPage")
    public void loadComplainPage() {
        System.out.println("=======rolelist===========");
        try {
            initData();
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }

            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }

            TbComplain complain = new TbComplain();

            String information = request.getParameter("information");
            if (StringUtils.isNotEmpty(information)) {
                complain.setInformation(information);
            }
            String status = request.getParameter("status");
            if (StringUtils.isNotEmpty(status)) {
                complain.setStatus(Integer.parseInt(status));
            }
            String isRead = request.getParameter("isRead");
            if (StringUtils.isNotEmpty(isRead)) {
                complain.setIsRead(Integer.parseInt(isRead));
            }

            page = findInfoDao.loadComplainPage(page, complain);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Action(value = "updateReadMark")
    public void updateReadMark() {
        initData();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            TbComplain complain = findInfoDao.loadTbComplainById(id);
            complain.setIsRead(1);
            complain.setLastaltertime(new Date());
            saveInfoDao.saveOrUpdateObj(complain);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Action(value = "updateStatus")
    public void updateStatus() {
        initData();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            TbComplain complain = findInfoDao.loadTbComplainById(id);
            complain.setStatus(status);
            complain.setLastaltertime(new Date());
            saveInfoDao.saveOrUpdateObj(complain);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FindInfoDao getFindInfoDao() {
        return findInfoDao;
    }

    public void setFindInfoDao(FindInfoDao findInfoDao) {
        this.findInfoDao = findInfoDao;
    }

    public SaveInfoDao getSaveInfoDao() {
        return saveInfoDao;
    }

    public void setSaveInfoDao(SaveInfoDao saveInfoDao) {
        this.saveInfoDao = saveInfoDao;
    }

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
