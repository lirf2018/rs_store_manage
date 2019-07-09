package com.yufan.vo;

/**
 * @功能名称 卡券查询条件
 * @作者 lirongfan
 * @时间 2016年8月11日 下午6:38:52
 */
public class TicketCondition {

    private Integer isShow;
    private Integer ticketType;
    private String tikcetName;
    private Integer ticketStatus;// 1:'长期有效卡券',2:'未开始卡券',3:'已开始卡券(未结束)',4:'已结束卡券',5:'限时有效卡券',6:'兑换已截至'
    private Integer status;
    private Integer shopId;
    private String pName;
    private Integer catogryId;
    private Integer leveId;
    private Integer isPutaway;

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public String getTikcetName() {
        return tikcetName;
    }

    public void setTikcetName(String tikcetName) {
        this.tikcetName = tikcetName;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Integer getCatogryId() {
        return catogryId;
    }

    public Integer getIsPutaway() {
        return isPutaway;
    }

    public void setIsPutaway(Integer isPutaway) {
        this.isPutaway = isPutaway;
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

}
