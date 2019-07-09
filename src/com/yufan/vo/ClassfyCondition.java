package com.yufan.vo;

/**
 * 功能名称: 开发人: lirf 开发时间: 2017下午8:45:26 其它说明：
 */
public class ClassfyCondition {

    private Integer propId;
    private String valueName;
    private String propName;
    private String propCode;
    private Integer status;
    private Integer leveId;
    private Integer catogryId;

    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLeveId() {
        return leveId;
    }

    public void setLeveId(Integer leveId) {
        this.leveId = leveId;
    }

    public Integer getCatogryId() {
        return catogryId;
    }

    public void setCatogryId(Integer catogryId) {
        this.catogryId = catogryId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropCode() {
        return propCode;
    }

    public void setPropCode(String propCode) {
        this.propCode = propCode;
    }

}
