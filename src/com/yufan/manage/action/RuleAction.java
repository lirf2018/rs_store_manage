package com.yufan.manage.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.yufan.pojo.TbPartners;
import com.yufan.pojo.TbRule;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.RuleCondition;

/**
 * 功能名称: 规则管理
 * 开发人: lirf
 * 开发时间: 2016上午1:16:34
 * 其它说明：
 */
@Scope("prototype")
@Service("ruleAction")
@Namespace("/manage")
@ParentPackage("common")
public class RuleAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;// 标识
    private String op;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbRule rule = new TbRule();

    private List<Map<String, Object>> listPartners = new ArrayList<Map<String, Object>>();


    /**
     * 加载规则列表
     */
    @Action(value = "loadRuleListPage")
    public void loadRuleListPage() {
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

            RuleCondition rulec = new RuleCondition();
            String status = request.getParameter("status");
            if (null != status && !"null".equals(status) && !"".equals(status)) {
                rulec.setStatus(Integer.parseInt(status));
            }
            String ruleType = request.getParameter("ruleType");
            if (null != ruleType && !"null".equals(ruleType) && !"".equals(ruleType)) {
                rulec.setRuleType(Integer.parseInt(ruleType));
            }
            String ruleCode = request.getParameter("ruleCode");
            if (null != ruleCode && !"null".equals(ruleCode) && !"".equals(ruleCode)) {
                rulec.setRuleCode(ruleCode);
            }
            String shopName = request.getParameter("shopName");
            if (null != shopName && !"null".equals(shopName) && !"".equals(shopName)) {
                rulec.setShopName(shopName);
            }
            String ruleName = request.getParameter("ruleName");
            if (null != ruleName && !"null".equals(ruleName) && !"".equals(ruleName)) {
                rulec.setRuleName(ruleName);
            }
            String isMakeSure = request.getParameter("isMakeSure");
            if (null != isMakeSure) {
                rulec.setIsMakeSure(Integer.parseInt(isMakeSure));
            }

            page = findInfoDao.loadTbRulePage(page, rulec);
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
    @Action(value = "toaddorupdaterulepage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-rule.jsp")})
    public String toaddorupdaterulepage() {
        initData();
        try {
            TbPartners partners = new TbPartners();
            partners.setStatus(1);
            listPartners = findInfoDao.listPartnersMap(partners);
            if ("update".equals(op)) {
                rule = findInfoDao.loadTbRuleByIdInfo(Integer.parseInt(id));
                rule.setIsMakeSure(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 增加或者修改规则
     */
    @Action(value = "addOrUpdateRule")
    public void addOrUpdateRule() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(op)) {// 增加
                rule.setCreatetime(new Timestamp(System.currentTimeMillis()));
                rule.setCreateman(user.getLoginName());
                rule.setIsMakeSure(0);
                flag = saveInfoDao.saveOrUpdateObj(rule);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {// 修改
                TbRule rule_ = new TbRule();
                rule_ = findInfoDao.loadTbRuleByIdInfo(rule.getRuleId());
                rule.setCreateman(rule_.getCreateman());
                rule.setCreatetime(rule_.getCreatetime());
                rule.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                rule.setLastalterman(user.getLoginName());
                rule.setIsMakeSure(0);
                flag = saveInfoDao.saveOrUpdateObj(rule);
                if (flag) {
                    //删除对应的所有规则关联
                    saveInfoDao.delRuleRuleId(rule.getRuleId());

                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除规则
     */
    @Action(value = "delRulestatus")
    public void delRulestatus() {
        initData();
        try {
            String status = request.getParameter("status");

            boolean flag = saveInfoDao.updateTbRuleStatus(status, id);
            //删除对应的所有规则关联
            saveInfoDao.delRuleRuleId(Integer.parseInt(id));

            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败"));
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

    public TbRule getRule() {
        return rule;
    }

    public void setRule(TbRule rule) {
        this.rule = rule;
    }

    public List<Map<String, Object>> getListPartners() {
        return listPartners;
    }

    public void setListPartners(List<Map<String, Object>> listPartners) {
        this.listPartners = listPartners;
    }


}
