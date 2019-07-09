package com.yufan.manage.action;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbSecretkey;
import com.yufan.util.*;
import com.yufan.common.bean.PageUtil;
import com.yufan.vo.GoodsCondition;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 创建人: lirf
 * 创建时间:  2017-10-10 9:45
 * 功能介绍: 确认和排序
 */
@Scope("prototype")
@Controller("confirmAction")
@Namespace("/manage")
@ParentPackage("common")
public class ConfirmAction extends LotFilterAction {

    private static Logger LOG = Logger.getLogger(ConfirmAction.class);
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;
    private Integer catogryId;
    private Integer leveId;
    private String ruleName;
    private String name;
    private Integer typeId;//查询类型 1商家 2商品3卡券
    private Integer timeLimit;//规则时效

    /**
     * 用户确认
     */
    @Action(value = "userConfirm")
    public void userConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateUserConfirm(ids);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 参数确认
     */
    @Action(value = "paramConfirm")
    public void paramConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateParamConfirm(ids);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParam();
            String reusltMsg = reuslt.getString("msg");

            result.put("flag", "ok");
            result.put("msg", "操作成功(" + reusltMsg + ")");
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 卡券确认
     */
    @Action(value = "ticketConfirm")
    public void ticketConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateTicketConfirm(ids);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品确认
     */
    @Action(value = "goodsConfirm")
    public void goodsConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateGoodsConfirm(ids);
            //修改购物车商品状态
            saveInfoDao.updateOrderCart(ids, 2);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(0);
            String reusltMsg = reuslt.getString("msg");

            result.put("flag", "ok");
            result.put("msg", "操作成功(" + reusltMsg + ")");
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 兑换确认
     */
    @Action(value = "exchangeConfirm")
    public void exchangeConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateExchangeConfirm(ids);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 规则确认
     */
    @Action(value = "ruleConfirm")
    public void ruleConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateRuleConfirm(ids);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品排序确认
     */
    @Action(value = "goodssortConfirm")
    public void goodssortConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String values = request.getParameter("values");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            saveInfoDao.updateGoodssortConfirm(values);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(0);
            String reusltMsg = reuslt.getString("msg");

            result.put("flag", "ok");
            result.put("msg", "操作成功(" + reusltMsg + ")");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 卡券排序确认
     */
    @Action(value = "ticketsortConfirm")
    public void ticketsortConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String values = request.getParameter("values");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            saveInfoDao.updateTicketsortConfirm(values);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 规则关联确认
     */
    @Action(value = "rulerelConfirm")
    public void rulerelConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String ids = request.getParameter("ids");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }
            //确认修改
            ids = ids.substring(0, ids.length() - 1);
            saveInfoDao.updateRuleRelConfirm(ids);
            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 抢购商品排序和确认
     */
    @Action(value = "timeGoodsConfirm")
    public void timeGoodsConfirm() {
        initData();
        try {
            PrintWriter writer = null;
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            result.put("flag", "false");
            result.put("msg", "未知错误");

            String values = request.getParameter("values");
            String paramKey = request.getParameter("paramKey");
            //验证密钥
            boolean flag = checkUpdateKey(paramKey);
            if (flag) {
                result.put("flag", "false");
                result.put("msg", "密钥无效");
                writer.print(result);
                writer.close();
                return;
            }

            //确认修改
            if (!"".equals(values)) {
                saveInfoDao.updatetimeGoodsSortConfirm(values);
            }
            String ids = request.getParameter("ids");
            if (null == ids || "".equals(ids) || ids.split(",").length == 0) {
                result.put("flag", "false");
                result.put("msg", "请选择要确认的商品");
                writer.print(result);
                writer.close();
                return;
            }
            if (!"".equals(ids)) {
                ids = ids.substring(0, ids.length() - 1);
                saveInfoDao.updatetimeGoodsConfirm(ids);
            }

            result.put("flag", "ok");
            result.put("msg", "操作成功");
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载商品列表(根据店铺,只能查登录人所属于的店铺的商品)
     */
    @Action(value = "loadGoodsList")
    public void loadGoodsDataList() {
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
            GoodsCondition goodsc = new GoodsCondition();
            String goods_name = request.getParameter("goods_name");
            if (null != goods_name && !"".equals(goods_name.trim())) {
                goodsc.setGoodsName(goods_name);
            }
            String goods_type = request.getParameter("goods_type");
            if (!"".equals(goods_type) && !"-1".equals(goods_type)) {
                goodsc.setGoodsType(Integer.parseInt(goods_type));
            }
            String goods_property = request.getParameter("goods_property");
            if (!"".equals(goods_property)) {
                goodsc.setGoodsProperty(Integer.parseInt(goods_property));
            }
            String is_putaway = request.getParameter("is_putaway");
            if (!"".equals(is_putaway)) {
                goodsc.setIsPutaway(Integer.parseInt(is_putaway));
            }
            String goods_status = request.getParameter("goods_status");
            if (!"".equals(goods_status)) {
                goodsc.setGoodsStatus(Integer.parseInt(goods_status));
            }
            String status = request.getParameter("status");
            if (!"".equals(status)) {
                goodsc.setStatus(Integer.parseInt(status));
            }
            String pName = request.getParameter("pName");
            if (null != pName && !"".equals(pName.trim())) {
                goodsc.setpName(pName);
            }

            if (null != catogryId && 0 != catogryId) {
                goodsc.setCatogryId(catogryId);
            }
            if (null != leveId && 0 != leveId) {
                goodsc.setLeveId(leveId);
            }
            page = findInfoDao.loadTbGoodsPage(page, goodsc);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载所有规则
     */
    @Action(value = "loadAllRulerel")
    public void loadAllRulerel() {
        try {
            initData();
            page = findInfoDao.loadDataAllRuleRle(page, ruleName, name, typeId, timeLimit, null, null, 0);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (Exception e) {
            e.printStackTrace();
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

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }

    public Integer getCatogryId() {
        return catogryId;
    }

    public void setCatogryId(Integer catogryId) {
        this.catogryId = catogryId;
    }

    public Integer getLeveId() {
        return leveId;
    }

    public void setLeveId(Integer leveId) {
        this.leveId = leveId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
