package com.yufan.manage.action;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * 创建人: lirf
 * 创建时间:  2017-10-13 10:53
 * 功能介绍:
 */
@Scope("prototype")
@Controller("timeGoodsAction")
@Namespace("/manage")
@ParentPackage("common")
public class TimeGoodsAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private Integer status;
    private String goodsName;
    private String partnersName;
    private Integer isMakeSure;

    private Integer goodsId;
    private TbTimeGoods timeGoods;
    private String pageFrom;

    /**
     * 查询抢购商品列表
     */
    @Action(value = "loadTimeGoodsPage")
    public void loadTimeGoodsPage() {
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
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");

            page = findInfoDao.loadTimeGoodsPage(page, goodsName, partnersName, status, user.getShopId(), isMakeSure);
            response.getWriter().write(write_Json_result("ok", page));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消抢购商品
     */
    @Action(value = "timeGoodsCancel")
    public void timeGoodsCancel() {
        try {
            initData();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            saveInfoDao.updateTimeGoodsStatus(goodsId, user.getLoginName());
            //修改购物车商品状态
            saveInfoDao.updateOrderCart(goodsId, 2);
            response.getWriter().write(write_Json_result("ok", "取消成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到抢购商品设置页面
     *
     * @return
     */
    @Action(value = "toAddTimeGoodsPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-timegoods.jsp")})
    public String toAddTimeGoodsPage() {
        try {
            initData();
            TbGoods goods = findInfoDao.loadTbGoodsInfo(goodsId);
            timeGoods = findInfoDao.loadTbTimeGoodsBuyGoodsId(goodsId);
            if (null == timeGoods) {
                timeGoods = new TbTimeGoods();
            }
            if (timeGoods.getLimitBeginTime() == null) {
                timeGoods.setLimitBeginTime(new Date());
            }
            request.setAttribute("goods", goods);
            String path = RsConstants.phone_url;
            request.setAttribute("perfixUrl", path);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增加或者修改抢购商品
     */
    @Action(value = "addTimeGoods")
    public void addTimeGoods() {
        try {
            initData();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (null != timeGoods.getId()) {
                TbTimeGoods timeGoods_ = findInfoDao.loadTbTimeGoodsBuyGoodsId(goodsId);
                timeGoods.setCreateman(timeGoods_.getCreateman());
                timeGoods.setCreatetime(timeGoods_.getCreatetime());
                timeGoods.setWeight(timeGoods_.getWeight());
                timeGoods.setLastalterman(user.getLoginName());
                timeGoods.setLastaltertime(new Date());
            } else {
                //新增
                timeGoods.setCreateman(user.getLoginName());
                timeGoods.setCreatetime(new Date());
                timeGoods.setWeight(0);
            }
            timeGoods.setStatus(1);
            timeGoods.setGoodsId(goodsId);
            timeGoods.setIsMakeSure(0);
            saveInfoDao.saveOrUpdateObj(timeGoods);
            saveInfoDao.updateGoodsTimeGoods(goodsId, 1);
            //修改购物车商品状态
            saveInfoDao.updateOrderCart(goodsId, 2);
            response.getWriter().write(write_Json_result("ok", "设置成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置页码
     *
     * @return
     */
    public void setPages() {
        if (null != currentPage && !"".equals(currentPage)) {
            page.setCurrentPage(currentPage);
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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public TbTimeGoods getTimeGoods() {
        return timeGoods;
    }

    public void setTimeGoods(TbTimeGoods timeGoods) {
        this.timeGoods = timeGoods;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPartnersName() {
        return partnersName;
    }

    public void setPartnersName(String partnersName) {
        this.partnersName = partnersName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    public Integer getIsMakeSure() {
        return isMakeSure;
    }

    public void setIsMakeSure(Integer isMakeSure) {
        this.isMakeSure = isMakeSure;
    }
}
