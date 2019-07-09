package com.yufan.vo;

/**
 * @功能名称 规则查询条件
 * @作者 lirongfan
 * @时间 2016年8月12日 下午2:24:25
 */
public class RuleCondition {

    // 查询条件
    private String shopName;//合作商
    private String ruleName;//
    private String ruleCode;//
    private Integer ruleType;// 规则类型 0:店铺规则1商品规则2卡券规则
    private Integer status;
    private Integer isMakeSure;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsMakeSure() {
        return isMakeSure;
    }

    public void setIsMakeSure(Integer isMakeSure) {
        this.isMakeSure = isMakeSure;
    }
}
