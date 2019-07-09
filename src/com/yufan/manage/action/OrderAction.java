package com.yufan.manage.action;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbDistributionAddr;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbOrderDetail;
import com.yufan.pojo.TbSecretkey;
import com.yufan.util.DatetimeUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.OrderCondition;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2017-12-05 14:43
 * 功能介绍: 订单管理
 */
@Scope("prototype")
@Controller("orderAction")
@Namespace("/manage")
@ParentPackage("common")
public class OrderAction extends LotFilterAction {
    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    /**
     * 查询订单列表
     */
    @Action(value = "loadOrderDataList")
    public void loadOrderDataList() {
        try {
            initData();
            if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }
            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }
            OrderCondition orderCondition = new OrderCondition();
            String orderId = request.getParameter("orderId");
            if (StringUtils.isNotEmpty(orderId)) {
                orderCondition.setOrderId(Integer.parseInt(orderId.trim()));
            }
            String goodsId = request.getParameter("goodsId");
            if (StringUtils.isNotEmpty(goodsId)) {
                orderCondition.setGoodsId(Integer.parseInt(goodsId.trim()));
            }
            String goodsName = request.getParameter("goodsName");
            if (StringUtils.isNotEmpty(goodsName)) {
                orderCondition.setGoodsName(goodsName.trim());
            }
            String orderNo = request.getParameter("orderNo");
            if (StringUtils.isNotEmpty(orderNo)) {
                orderCondition.setOrderNo(orderNo.trim());
            }
            String userName = request.getParameter("userName");
            if (StringUtils.isNotEmpty(userName)) {
                orderCondition.setUserName(userName.trim());
            }
            String phone = request.getParameter("phone");
            if (StringUtils.isNotEmpty(phone)) {
                orderCondition.setPhone(phone.trim());
            }
            String businessType = request.getParameter("businessType");
            if (StringUtils.isNotEmpty(businessType) && !"-1".equals(businessType)) {
                orderCondition.setBusinessType(businessType.trim());
            }
            String orderStatus = request.getParameter("orderStatus");
            if (StringUtils.isNotEmpty(orderStatus) && !"-1".equals(orderStatus)) {
                orderCondition.setOrderStatus(Integer.parseInt(orderStatus));
            }
            String partnersName = request.getParameter("partnersName");
            if (StringUtils.isNotEmpty(partnersName)) {
                orderCondition.setPartnersName(partnersName.trim());
            }
            String beginTime = request.getParameter("beginTime");
            if (StringUtils.isNotEmpty(beginTime)) {
                orderCondition.setBeginTime(beginTime);
            }
            String endTime = request.getParameter("endTime");
            if (StringUtils.isNotEmpty(endTime)) {
                orderCondition.setEndTime(endTime);
            }
            String adPayType = request.getParameter("adPayType");
            if (StringUtils.isNotEmpty(adPayType) && !"-1".equals(adPayType)) {
                orderCondition.setAdPayType(adPayType);
            }
            String getWay = request.getParameter("getWay");
            if (StringUtils.isNotEmpty(getWay)) {
                orderCondition.setGetWay(Integer.parseInt(getWay));
            }
            page = findInfoDao.loadOrderPageList(page, orderCondition);
            response.getWriter().write(write_Json_result("ok", page));

        } catch (Exception e) {

        }
    }

    /**
     * 查询订单详情列表
     */
    @Action(value = "loadOrderDetailListMap")
    public void loadOrderDetailListMap() {
        initData();
        try {
            int orderId = Integer.parseInt(request.getParameter("order_id"));
            List<Map<String, Object>> listDetail = findInfoDao.loadOrderDetailListByOrderId(orderId);
            List<Map<String, Object>> listDetailProp = findInfoDao.loadDetailPropBuyOrderId(orderId);
            JSONObject obj = new JSONObject();
            obj.put("listDetail", listDetail);
            obj.put("listDetailProp", listDetailProp);
            response.getWriter().write(write_Json_result("msg", obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询订单详情属性列表
     */
    @Action(value = "loadOrderDetailPropListMap")
    public void loadOrderDetailPropListMap() {
        initData();
        try {
            int detailId = Integer.parseInt(request.getParameter("detail_id"));
            List<Map<String, Object>> listDetailProp = findInfoDao.loadDetailPropBuyDetailId(detailId);
            response.getWriter().write(write_Json_result("msg", listDetailProp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action(value = "listOrderBusinessTypeParam")
    public void listOrderBusinessTypeParam() {
        try {
            initData();
            List<Map<String, Object>> listBusinessType = findInfoDao.listParamMap("business_type");
            response.getWriter().write(write_Json_result("msg", listBusinessType));
        } catch (Exception e) {
            LOG.info("-->" + e);
        }
    }

    @Action(value = "listOrderStatusParam")
    public void listOrderStatusParam() {
        try {
            initData();
            List<Map<String, Object>> listStatus = findInfoDao.listParamMap("order_status");
            response.getWriter().write(write_Json_result("msg", listStatus));
        } catch (Exception e) {
            LOG.info("-->" + e);
        }
    }

    /**
     * 跳转到订单详情页面
     *
     * @return
     */
    @Action(value = "toOrderDetailPage", results = {
            @Result(name = "success", location = "/jsp/manage/order_deatil.jsp")})
    public String toOrderDetailPage() {
        initData();
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            OrderCondition orderCondition = new OrderCondition();
            orderCondition.setOrderId(orderId);
            //查询详情
            List<Map<String, Object>> listOrder = findInfoDao.loadOrderListMap(orderCondition);
            List<Map<String, Object>> listDetail = findInfoDao.loadOrderDetailListByOrderId(orderId);
            List<Map<String, Object>> listDetailProp = findInfoDao.loadDetailPropBuyOrderId(orderId);
            request.setAttribute("listOrder", listOrder);
            request.setAttribute("listDetail", listDetail);
            request.setAttribute("listDetailProp", listDetailProp);

            //查询订单状态
            List<Map<String, Object>> listStatus = findInfoDao.listParamMap("order_status");
            request.setAttribute("listStatus", listStatus);

            //查询快递公司
            List<Map<String, Object>> postComp = findInfoDao.listParamMap("post_company");
            for (int i = 0; i < postComp.size(); i++) {
                String key = postComp.get(i).get("param_key").toString();
                String postCompName = postComp.get(i).get("param_value").toString();
                if (key.equals(listOrder.get(0).get("post_name").toString())) {
                    request.setAttribute("postCompName", postCompName);
                }
            }
            request.setAttribute("postComp", postComp);

            //查询状态变化记录
            List<Map<String, Object>> listStatusRecord = findInfoDao.listStatusRecord(listOrder.get(0).get("order_no").toString());
            request.setAttribute("listStatusRecord", listStatusRecord);

            return "success";
        } catch (Exception e) {
            LOG.info("------------->" + e);
        }
        return null;
    }

    /**
     * 修改订单状态
     */
    @Action(value = "updateOrderStatus")
    public void updateOrderStatus() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int orderStatus = Integer.parseInt(request.getParameter("orderStatus"));
            String serviceRemark = request.getParameter("serviceRemark");
            String refundRemark = request.getParameter("refundRemark");
            //13已退款(客服)12已取消(客服)11已完成(客服)0待付款(系统)1已付款(用户)2确认中(系统)3已失败(客服)
            //4待发货(客服)5待收货(用户)6已完成(用户)7已取消(用户)8已删除(用户)9退款中(系统)10已退款(客服)
            TbOrder order = findInfoDao.loadTbOrderById(orderId);
            order.setOrderStatus(orderStatus);
            order.setLastaltertime(new Date());
            order.setLastalterman(user.getLoginName());
            order.setStatusOpration(1);
            if (null != serviceRemark) {
                order.setServiceRemark(serviceRemark);
            }
            if (null != refundRemark) {
                order.setRefundRemark(refundRemark);
            }
            saveInfoDao.saveOrUpdateObj(order);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            LOG.info("------>" + e);
        }
    }


    /**
     * 订单发货
     */
    @Action(value = "updateOrderPostStatus")
    public void updateOrderPostStatus() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            //13已退款(客服)12已取消(客服)11已完成(客服)0待付款(系统)1已付款(用户)2确认中(系统)3已失败(客服)
            //4待发货(客服)5待收货(用户)6已完成(用户)7已取消(用户)8已删除(用户)9退款中(系统)10已退款(客服)
            TbOrder order = findInfoDao.loadTbOrderById(orderId);
            order.setOrderStatus(5);
            order.setPostTime(new Date());
            order.setLastaltertime(new Date());
            order.setLastalterman(user.getLoginName());
            if (order.getPostWay().intValue() == 1) {
                //配送方式 1.快递邮寄4.用户自取5商家配送
                String postNum = request.getParameter("postNum");
                String postCompCode = request.getParameter("postCompCode");
                order.setPostName(postCompCode);
                order.setPostNo(postNum);
            } else {
                String postNum = "RS" + DatetimeUtil.getNow("yyyyMMddHHmmss") + System.currentTimeMillis();
                String postCompCode = "platform_service";
                order.setPostName(postCompCode);
                order.setPostNo(postNum);
                Integer userAddrId = order.getUserAddrId();
                if (null != userAddrId && 0 != userAddrId) {
                    TbDistributionAddr distributionAddr = findInfoDao.loadTbDistributionAddrInfo(userAddrId);
                    order.setPostPhone(distributionAddr.getResponsiblePhone());
                    order.setPostMan(distributionAddr.getResponsibleMan());
                }
            }
            String serviceRemark = request.getParameter("serviceRemark");
            order.setStatusOpration(1);
            if (StringUtils.isNotEmpty(serviceRemark)) {
                order.setServiceRemark(order.getServiceRemark() + "`\n" + serviceRemark);
            }
            saveInfoDao.saveOrUpdateObj(order);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            LOG.info("------>" + e);
        }
    }

    /**
     * 需要密钥才能修改
     */
    @Action(value = "updateOrderInfo")
    public void updateOrderInfo() {
        initData();
        try {
            //验证密钥
            String paramKey = request.getParameter("paramKey");
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                response.getWriter().write(write_Json_result("false", "密钥无效"));
                return;
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            TbOrder order = findInfoDao.loadTbOrderById(orderId);
            if (StringUtils.isNotEmpty(request.getParameter("orderStatus")) && !"-1".equals(request.getParameter("orderStatus"))) {
                int orderStatus = Integer.parseInt(request.getParameter("orderStatus"));
                order.setOrderStatus(orderStatus);
            }

            String refundPrice = request.getParameter("refundPrice");
            if (StringUtils.isNotEmpty(refundPrice)) {
                order.setRefundPrice(new BigDecimal(refundPrice.trim()).doubleValue());
            }
            String refundPemark = request.getParameter("refundPemark");
            if (null != refundPemark) {
                order.setRefundRemark(refundPemark);
            }
            String postPhone = request.getParameter("postPhone");
            if (null != postPhone) {
                order.setPostPhone(postPhone);
            }
            String postMan = request.getParameter("postMan");
            if (null != postMan) {
                order.setPostMan(postMan);
            }
            String serviceRemark = request.getParameter("serviceRemark");
            if (null != serviceRemark) {
                order.setServiceRemark(serviceRemark);
            }

            order.setStatusOpration(1);
            order.setLastaltertime(new Date());
            order.setLastalterman(user.getLoginName());

            saveInfoDao.saveOrUpdateObj(order);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            LOG.info("---->" + e);
        }
    }


    /**
     * 修改订单详情状态
     */
    @Action(value = "updateOrderDetailStatus")
    public void updateOrderDetailStatus() {
        initData();
        try {
            int detailId = Integer.parseInt(request.getParameter("detailId"));
            int detailStatus = Integer.parseInt(request.getParameter("detailStatus"));
            TbOrderDetail orderDetail = findInfoDao.loadTbOrderDetailByDetailId(detailId);
            orderDetail.setDetailStatus(detailStatus);
            orderDetail.setLastaltertime(new Timestamp(new Date().getTime()));
            saveInfoDao.saveOrUpdateObj(orderDetail);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
            //0未取货1已取货2已取消3已完成4已退货
        } catch (Exception e) {
            LOG.info("------>" + e);
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

    /**
     * 修改密钥检验
     *
     * @param updateKey
     * @return
     */
    public boolean checkUpdateKey(String updateKey) {
        TbSecretkey secretkey = findInfoDao.loadTbSecretkey(updateKey);
        if (null == secretkey) {
            LOG.info("---->密钥不存在");
            return true;
        }
        //判断时间 2个小时有效
        String nowTime = DatetimeUtil.getNow();//当前时间
        String secretkeyTime = DatetimeUtil.convertDateToStr(secretkey.getPassTime(), "yyyy-MM-dd HH:mm:ss");
        String secretkeyTimeAdd2H = DatetimeUtil.addHourTime(secretkeyTime, 2, "yyyy-MM-dd HH:mm:ss");//当前时间增加2个小时
        LOG.info("------>当前时间=" + nowTime + "  密钥过期时间=" + secretkeyTime + "  密钥过期时间增加2小时=" + secretkeyTimeAdd2H);
        if (DatetimeUtil.compareDateTime(nowTime, secretkeyTimeAdd2H) > -1) {
            LOG.info("---->密钥已过期");
            return true;
        }
        return false;
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

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }
}
