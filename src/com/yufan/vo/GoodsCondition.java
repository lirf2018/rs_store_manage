package com.yufan.vo;

/**
 * 功能名称: 商品查询条件 开发人: lirf 开发时间: 2016下午1:26:38 其它说明：
 */
public class GoodsCondition {


    private Integer goodsId;
    private String goodsName;
    private Integer goodsType;
    private Integer goodsProperty;
    private Integer isPutaway;
    private Integer goodsStatus;//1:'长期有效商品',2:'未开始商品',3:'已开始商品(未结束)',4:'已结束商品',5:'限时有效商品
    private Integer status;
    private Integer shopId;
    private String pName;
    private String skuName;
    private String propCode;
    private Integer catogryId;
    private Integer leveId;
    private Integer isSingle;
    private Integer isTimeGoods;
    private Integer getWay;

    public Integer getIsTimeGoods() {
        return isTimeGoods;
    }

    public void setIsTimeGoods(Integer isTimeGoods) {
        this.isTimeGoods = isTimeGoods;
    }

    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getGoodsProperty() {
        return goodsProperty;
    }

    public void setGoodsProperty(Integer goodsProperty) {
        this.goodsProperty = goodsProperty;
    }

    public Integer getIsPutaway() {
        return isPutaway;
    }

    public void setIsPutaway(Integer isPutaway) {
        this.isPutaway = isPutaway;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPropCode() {
        return propCode;
    }

    public void setPropCode(String propCode) {
        this.propCode = propCode;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGetWay() {
        return getWay;
    }

    public void setGetWay(Integer getWay) {
        this.getWay = getWay;
    }
}
