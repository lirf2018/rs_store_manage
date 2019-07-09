package com.yufan.manage.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbExchangeJurisdiction;
import com.yufan.pojo.TbTicket;
import com.yufan.util.DatetimeUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 卡券兑换权限管理
 * @作者 lirongfan
 * @时间 2016年9月8日 下午4:36:09
 */
@Scope("prototype")
@Service("exchangeControl")
@Namespace("/manage")
@ParentPackage("common")
public class ExchangeControl extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;// 标识
    private String op;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private List<Map<String, Object>> listTicket;
    private List<Map<String, Object>> listPartners;

    /**
     * 查询条件
     */
    private String ticketName;
    private String partnersName;
    private String shopName;


    /**
     * 加载规则列表
     */
    @Action(value = "loadExchangeListPage")
    public void loadExchangeListPage() {
        try {
            initData();
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request
                        .getParameter("currentPage"));
            }
            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }
            String isMakeSure = request.getParameter("isMakeSure");
            page = findInfoDao.loadExchangeJurisdictionPage(page, partnersName, ticketName, shopName, isMakeSure);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改页面
     *
     * @return
     */
    @Action(value = "toaddorupdateexchangerelpage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-exchangerel.jsp")})
    public String toaddorupdateexchangerelpage() {
        initData();
        return "success";
    }


    /**
     * 增加或者修改兑换权限
     */
    @Action(value = "addOrUpdateExchange")
    public void addOrUpdateExchange() {
        initData();
        try {

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            //partnersIds 得到选择的商家
            String partnersIds[] = request.getParameterValues("partnersIds");
            Integer ticketId = Integer.parseInt(request.getParameter("ticketId"));
            Integer validDate = Integer.parseInt(request.getParameter("validDate"));

            //时间处理
            String format = "yyyy-MM-dd HH:mm:ss";
            String beginTime = request.getParameter("beginTime");
            Timestamp be = null;
            if (null != beginTime && !"".equals(beginTime)) {
                try {
                    be = new Timestamp(DatetimeUtil.convertStrToDate(beginTime, format).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String endTime = request.getParameter("endTime");
            Timestamp en = null;
            if (null != endTime && !"".equals(endTime)) {
                try {
                    en = new Timestamp(DatetimeUtil.convertStrToDate(endTime, format).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < partnersIds.length; i++) {

                Integer partnersId = Integer.parseInt(partnersIds[i]);
                //删除原有的
                saveInfoDao.delExchangeId(null, ticketId, partnersId);
                //新增
                TbExchangeJurisdiction e = new TbExchangeJurisdiction();
                e.setPartnersId(partnersId);
                e.setCreateman(user.getLoginName());
                e.setCreatetime(new Timestamp(System.currentTimeMillis()));
                e.setValidDate(validDate);
                e.setStartTime(be);
                e.setEndTime(en);
                e.setTikcetId(ticketId);
                e.setIsMakeSure(0);
                saveInfoDao.saveObject(e);
            }

            response.getWriter().write(write_Json_result("ok", "操作成功"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载卡券列表
     */
    @Action(value = "listTicketsMap")
    public void listTicketsMap() {
        initData();
        try {
            TbTicket ticket = new TbTicket();
            ticket.setStatus(1);
            String ticketName = request.getParameter("ticketName");
            if (null != ticketName && !"".equals(ticketName.trim())) {
                ticket.setTikcetName(ticketName);
            }
            listTicket = findInfoDao.listTicketsMap(ticket);
            response.getWriter().write(write_Json_result("ok", listTicket));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载商家列表
     */
    @Action(value = "listPartners")
    public void listPartners() {
        initData();
        try {
            String pName = request.getParameter("pName");
            Integer ticketId = null;
            if (null != request.getParameter("ticketId")) {
                ticketId = Integer.parseInt(request.getParameter("ticketId"));
            }
            listPartners = findInfoDao.listTicketPartnersByTicketId(ticketId, pName);
            response.getWriter().write(write_Json_result("ok", listPartners));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 逻辑删除规则
     */
    @Action(value = "delexchangestatus")
    public void delexchangestatus() {
        initData();
        try {
            boolean flag = saveInfoDao.delExchangeId(Integer.parseInt(id), null, null);
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "删除失败"));
            }
        } catch (IOException e) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getPartnersName() {
        return partnersName;
    }

    public void setPartnersName(String partnersName) {
        this.partnersName = partnersName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Map<String, Object>> getListTicket() {
        return listTicket;
    }

    public void setListTicket(List<Map<String, Object>> listTicket) {
        this.listTicket = listTicket;
    }

    public List<Map<String, Object>> getListPartners() {
        return listPartners;
    }

    public void setListPartners(List<Map<String, Object>> listPartners) {
        this.listPartners = listPartners;
    }

}
