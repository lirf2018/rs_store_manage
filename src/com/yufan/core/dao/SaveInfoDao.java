package com.yufan.core.dao;

import java.util.List;

import com.yufan.pojo.*;


/**
 * @功能名称 数据操作增加修改删除 dao 接口
 * @作者 lirongfan
 * @时间 2015年10月29日 下午2:40:14
 */
public interface SaveInfoDao {

    //**************************微信菜单管理***********************

    /**
     * 保存微信菜单
     *
     * @param weixin
     * @return
     */
    public int saveWeixinMenu(TbWeixinMenu weixin);

    /**
     * 逻辑删除微信菜单
     *
     * @param state
     * @param menu_id
     * @param appId_secret
     * @return
     */
    public boolean delWeixinMenu(String state, String menu_id, String appId_secret);

    /**
     * 保存accessToken
     *
     * @param accessToken
     */
    public void saveWeixinAccessToken(TbWeixinAccessToken accessToken);
    //**************************用户登录***********************

    /**
     * 更新登录时间
     */
    public void updateLoinTime(int id);

    //**************************菜单/功能管理***********************

    /**
     * 保存或者更新菜单
     *
     * @param function
     * @return
     */
    public boolean addorupdateMenu(TbFunctions function);

    /**
     * 启用或者删除菜单(逻辑删除)
     *
     * @param function
     * @return
     */
    public boolean deleteOrUpdateMenu(TbFunctions function);

    //**************************角色管理***********************

    /**
     * 增加或者修改角色
     *
     * @param role
     */
    public boolean addOrUpdateRole(TbRole role);

    /**
     * 逻辑删除角色
     *
     * @param role_id
     * @param status
     */
    public boolean delOrUpdateRole(String role_id, String status);

    // **************************参数管理**************************

    /**
     * 增加或者修改参数
     *
     * @param param
     * @return
     */
    public boolean addOrUpdateParam(TbParam param);

    /**
     * 逻辑删除参数
     *
     * @param id
     * @param state
     * @return
     */
    public boolean delParamState(String id, String state);

    // **************************角色功能管理**************************

    /**
     * 删除相应角色的功能权限
     *
     * @param role_id
     */
    public boolean delRoleFunction(String role_id);

    /**
     * 增加或者更新角色功能
     *
     * @param roleFunction
     * @return
     */
    public boolean addRoleFunction(TbRoleFunction roleFunction);

    // **************************管理员管理**************************

    /**
     * 增加管理员
     *
     * @param admin
     * @return
     */
    public boolean addAdmin(TbAdmin admin);

    /**
     * 删除或启用用户
     *
     * @param admin_id
     * @param status
     * @return
     */
    public boolean deleteOrUseAdmin(String admin_id, String status);

    // **************************管理员角色管理**************************

    /**
     * 增加或者更新用户角色
     *
     * @param admin_role
     * @return
     */
    public boolean addOrUpdateAdminRole(TbUserRole admin_role);

    /**
     * 删除用户角色
     *
     * @param admin_role_id
     * @return
     */
    public boolean deleteAdminRole(String admin_role_id);

    // **************************渠道管理**************************

    /**
     * 增加或者修改渠道
     *
     * @param channel
     * @param channel
     * @return
     */
    public boolean addOrUpdateChannel(TbChannel channel);


    /**
     * 删除或者启用渠道(逻辑删除)
     *
     * @param channelid
     * @param status
     * @return
     */
    public boolean deleteOrupdateChannel(String channelid, String status);

    // **************************分类管理**************************

    /**
     * 删除或者启用分类
     *
     * @param category_id
     * @param status
     */
    public boolean delorupdatecategory(String category_id, String status);

    /**
     * 删除或者启用分类属性
     *
     * @param itemprops_id
     * @param status
     */
    public boolean delorupdateitemprops(String itemprops_id, String status);

    /**
     * 保存或者更新分类
     *
     * @param obj
     * @return
     */
    public boolean saveOrUpdateObj(Object obj);

    public int saveOrUpdateItemprops(TbItemprops itemprops);


    /**
     * 删除分类属性为prop_ids的集合的属性值
     *
     * @param prop_ids
     * @return
     */
    public boolean delPropsValue(String prop_ids);

    /**
     * 逻辑删除同一个属性下的属性值
     *
     * @param value_ids
     * @param propId
     * @return
     */
    public boolean delPropsValueByValueIdsStatus(String value_ids, Integer propId);

    /**
     * 删除属性值
     *
     * @param propsValue_id
     * @param status
     * @return
     */
    public boolean delStatusPropsValue(String propsValue_id, String status);

    // **************************店铺管理**************************

    /**
     * 删除店铺
     *
     * @param shop_id
     * @param status
     * @return
     */
    public boolean delStatusShop(String shop_id, String status);

    /**
     * 删除店铺与商家关联
     *
     * @param shopId
     * @return
     */
    public boolean delShopPartnersRel(Integer shopId);

    // **************************活动专题管理**************************

    /**
     * 修改活动专题状态
     *
     * @param activity_id
     * @param status
     * @return
     */
    public boolean upadteTbActivitStatus(String activity_id, String status);

    // **************************bannel管理**************************

    /**
     * 修改bannel状态
     *
     * @param bannel_id
     * @param status
     * @return
     */
    public boolean upadteTbBannelStatus(String bannel_id, String status);

    // **************************配送地址管理**************************


    /**
     * 修改配送地址状态
     *
     * @param status
     * @param id
     * @return
     */
    public boolean updateTbDistributionAddrStatus(String status, String id);

    /**
     * 修改全国地址状态
     *
     * @param status
     * @param id
     * @return
     */
    public boolean updateTbRegionStatus(int id, int status);


    /**
     * 删除用户地址
     */
    public void updateUserAddrStatus(int addrType, String addrIds);

    public void updateUserAddrStatus1(int addrType, String addrIds);

    // **************************规则管理**************************

    /**
     * 修改规则状态
     *
     * @param status
     * @param rule_id
     * @return
     */
    public boolean updateTbRuleStatus(String status, String rule_id);


    // **************************商品管理**************************

    /**
     * 保存商品
     *
     * @param goods
     * @return
     */
    public boolean saveGoods(TbGoods goods);

    /**
     * 保存商品属性
     *
     * @param attributeList
     * @param goods
     * @return
     */
    public boolean saveGoodsAttrbute(List<TbGoodsAttribute> attributeList, TbGoods goods);

    /**
     * 保存商品sku
     *
     * @param skuList
     * @param goods
     * @return
     */
    public boolean saveGoodsSku(List<TbGoodsSku> skuList, TbGoods goods);

    /**
     * 保存商品图片
     *
     * @param imgList
     * @param relateId
     * @return
     */
    public boolean saveImg(List<TbImg> imgList, Integer relateId);

    /**
     * 更新商品状态
     *
     * @param goods
     * @return
     */
    public boolean updateGoodsStatus(TbGoods goods);

    /**
     * 更新上架状态
     *
     * @param goods
     * @return
     */
    public boolean updateGoodsIsPutaway(TbGoods goods);

    /**
     * 删除商品SKU
     *
     * @param goodsId
     * @return
     */
    public boolean deleteGoodsSkuByGoodsId(Integer goodsId, Integer skuId);

    /**
     * 删除商品属性
     *
     * @param goodsId
     * @return
     */
    public boolean deleteGoodsAttributeByGoodsId(Integer goodsId, Integer valueId);

    /**
     * 删除商品图片
     *
     * @param relateId
     * @param imgClassyfi 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public boolean deleteImgBuRelateId(Integer relateId, Integer imgClassyfi);

    // **************************卡券管理**************************

    /**
     * 修改卡券状态
     *
     * @param ticket
     * @return
     */
    public boolean updateTicketStatus(TbTicket ticket);

    /**
     * 修改上架状态
     *
     * @param ticket
     * @return
     */
    public boolean updateTicketPutaway(TbTicket ticket);

    /**
     * 删除卡券属性
     *
     * @param ticketId
     * @return
     */
    public boolean deleteTicketAttributeByTicketId(Integer ticketId);

    /**
     * 保存卡券属性
     *
     * @param attributeList
     * @param ticket
     * @return
     */
    public boolean saveTicketAttrbute(List<TbTicketAttribute> attributeList, TbTicket ticket);

    // **************************规则关联管理**************************

    /**
     * 删除规则关联
     *
     * @param id
     * @return
     */
    public boolean delRuleRel(int id);

    public boolean delRuleRuleId(int id);

    /**
     * 保存数据
     *
     * @param obj
     * @return
     */
    public boolean saveObject(Object obj);

    //***************************商家管理****************************

    /**
     * 修改商家状态
     *
     * @param id
     * @param status
     * @return
     */
    public boolean updatePartnersStatus(Integer id, Integer status);

    //***************************兑换权限管理****************************

    /**
     * 删除兑换权限关联
     *
     * @param relId
     * @return
     */
    public boolean delExchangeId(Integer relId, Integer ticketId, Integer partnersId);

    //***********************一级分类***********************

    /**
     * 修改一级菜单状态
     *
     * @param leveId
     * @param status
     * @return
     */
    public boolean updateCatogryLeve1Status(String leveId, String status);

    /**
     * 删除分类类目关系
     *
     * @param leveId
     */
    public void deleteClassfyCatogryRel(Integer leveId);

    //***********************其它***********************

    /**
     * 更新通用表排序
     *
     * @param tableName
     * @param sort
     */
    public void updateSortByTableName(String tableName, Integer sort);

    /**
     * 更新菜单排序
     *
     * @param tableName
     * @param parentId
     * @param sort
     */
    public void updateSortMenuByTableName(String tableName, Integer parentId, Integer sort);

    //***********************确认管理***********************

    /**
     * 参数确认
     *
     * @param ids
     */
    public void updateParamConfirm(String ids);

    /**
     * 用户确认
     *
     * @param ids
     */
    public void updateUserConfirm(String ids);

    /**
     * 卡券上架确认
     *
     * @param ids
     */
    public void updateTicketConfirm(String ids);

    /**
     * 商品上架确认
     *
     * @param ids
     */
    public void updateGoodsConfirm(String ids);

    /**
     * 规则确认
     *
     * @param ids
     */
    public void updateRuleConfirm(String ids);

    /**
     * 规则关联确认
     *
     * @param ids
     */
    public void updateRuleRelConfirm(String ids);

    /**
     * 兑换确认
     *
     * @param ids
     */
    public void updateExchangeConfirm(String ids);

    /**
     * 商品排序确认
     *
     * @param strs
     */
    public void updateGoodssortConfirm(String strs);

    /**
     * 卡券排序确认
     *
     * @param strs
     */
    public void updateTicketsortConfirm(String strs);

    /**
     * 抢购商品排序
     *
     * @param sortStr
     */
    public void updatetimeGoodsSortConfirm(String sortStr);

    /**
     * 抢购商品确认
     *
     * @param ids
     */
    public void updatetimeGoodsConfirm(String ids);

    // **************************抢购管理**************************

    /**
     * 修改抢购商品状态为0和 修改商品抢购状态为0
     *
     * @param goodsId
     */
    public void updateTimeGoodsStatus(Integer goodsId, String loginName);

    /**
     * 更新商品为抢购商品
     *
     * @param goodsId
     */
    public void updateGoodsTimeGoods(Integer goodsId, Integer isTimeGoods);

    // **************************订单管理**************************

    /**
     * 修改购物车商品
     *
     * @param goodsId
     */
    public void updateOrderCart(int goodsId, int status);

    public void updateOrderCart(String goodsIds, int status);

    // **************************首页菜单管理**************************
    public boolean updateMainMenuStatus(int id, int status);

    public boolean updateMainMenuSort(int id, int menuSort);

    // **************************图片管理**************************
    public void updateTbImgUploadNameRecord(int id, int status, String tableName);

    public void updateTbImgUploadNameRecord(int id, String tableName);

    public void updateTbImgUploadNameRecord(int id, int status);

    public void delTbImgUploadNameRecord(int id);

    // **************************wap用户管理**************************

    public boolean updateWapUserStatus(int userId, int status);

}
