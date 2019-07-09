package com.yufan.vo;

/**
 * @功能名称 店铺列表查询条件
 * @作者 lirongfan
 * @时间 2016年9月8日 下午1:59:18
 */
public class ShopCondition {

    private String shopName;
    private String userName;
    private Integer isOutShop;
    private Integer status;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsOutShop() {
        return isOutShop;
    }

    public void setIsOutShop(Integer isOutShop) {
        this.isOutShop = isOutShop;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
