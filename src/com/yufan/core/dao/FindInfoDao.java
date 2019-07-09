package com.yufan.core.dao;

import java.util.List;
import java.util.Map;

import com.yufan.pojo.*;
import com.yufan.common.bean.PageUtil;
import com.yufan.vo.ActivityCondition;
import com.yufan.vo.AdminCondition;
import com.yufan.vo.AdminRoleCondition;
import com.yufan.vo.BannelCondition;
import com.yufan.vo.CatogryLeve1Condition;
import com.yufan.vo.ClassfyCondition;
import com.yufan.vo.GoodsCondition;
import com.yufan.vo.OrderCondition;
import com.yufan.vo.PartnersCondition;
import com.yufan.vo.RuleCondition;
import com.yufan.vo.ShopCondition;
import com.yufan.vo.TicketCondition;


/**
 * @功能名称 数据查询公共dao接口
 * @作者 lirongfan
 * @时间 2015年10月29日 下午2:34:37
 */
public interface FindInfoDao {
    // **************************微信菜单管理**************************

    /**
     * 加载微信菜单列表
     *
     * @param menu_parent
     * @param state
     * @param key_code
     * @return
     */
    public List<Map<String, Object>> listWeixinMenuMap(String menu_parent, String state, String key_code);

    /**
     * 查询菜单by id
     *
     * @param id
     * @return
     */
    public TbWeixinMenu loadTbWeixinMenuInfo(int id);

    /**
     * 查询是否存在有效的access_token
     *
     * @param combineCode :AppID+AppSecret的组合
     * @return
     */
    public String getAccessTokenByCode(String combineCode);

    // **************************登录**************************

    /**
     * 查询登录用户信息
     *
     * @param loginName
     * @return
     */
    public TbAdmin loadTbAdminInfo(String loginName);

    public List<Map<String, Object>> loadTbAdminList(String loginName);

    /**
     * 根据用户角色查询对应用户角色菜单
     *
     * @param admin_id
     * @return
     */
    public List<Map<String, Object>> listMenusMap(int admin_id);

    // **************************菜单管理**************************

    /**
     * 查询主菜单或者子菜单
     *
     * @param menuType
     * @param function
     * @return
     */
    public PageUtil loadParentMenusOrSubMenus(TbFunctions function, PageUtil page, String menuType);

    /**
     * 查询菜单
     *
     * @param function_id
     * @return
     */
    public TbFunctions loadFunctionById(int function_id);

    /**
     * 查询主菜单列表
     *
     * @param status 状态
     * @return
     */
    public List<Map<String, Object>> listParentMenusMap(int status);

    /**
     * 检测名称是否存在
     *
     * @param menuId
     * @param name
     * @return
     */
    public boolean checkName(int menuId, String name, String menuType);

    /**
     * 检测编码是否存在
     *
     * @param menuId
     * @param code
     * @return
     */
    public boolean checkcode(int menuId, String code, String menuType);

    /**
     * 加载功能列表
     *
     * @param role_id
     */
    public List<Map<String, Object>> listFunctionsMap(String role_id);

    // **************************角色管理**************************

    /**
     * 加载角色列表
     *
     * @param role
     * @param page
     * @return
     */
    public PageUtil loadrolelist(TbRole role, PageUtil page);

    /**
     * 检测角色编码是否存在
     *
     * @param code
     * @return
     */
    public boolean checkRoleCode(int role_id, String code);

    /**
     * 检测角色编码是否名称
     *
     * @param name
     * @return
     */
    public boolean checkRoleName(int role_id, String name);

    /**
     * 查询角色
     *
     * @param role_id
     * @return
     */
    public TbRole loadRoleInfo(int role_id);

    /**
     * 查询角色列表
     *
     * @param role_status 状态
     * @return
     */
    public List<Map<String, Object>> listRoleMap(String role_status);

    // **************************参数管理**************************

    /**
     * 根据参数code查询参数列表
     *
     * @param param_code
     * @return
     */
    public List<Map<String, Object>> listParamMap(String param_code);

    /**
     * 加载参数列表
     *
     * @param page
     * @param param
     * @return
     */
    public PageUtil loadParamList(PageUtil page, TbParam param);

    /**
     * 查询参数
     *
     * @param id
     * @return
     */
    public TbParam loadTbParamInfo(int id);

    /**
     * 根据参数查询参数类型列表
     *
     * @return
     */
    public List<Map<String, Object>> listParamGroupMap();

    // **************************管理员管理**************************

    /**
     * 加载管理员列表
     *
     * @param page
     * @param bean
     * @return
     */
    public PageUtil listAdmin(PageUtil page, AdminCondition bean);

    /**
     * 加载管理员信息
     *
     * @param admin_id
     * @return
     */
    public TbAdmin loadAdminInfo(int admin_id);

    /**
     * 检测登录名称是否已经存在
     *
     * @param admin_id
     * @param login_name
     * @return
     */
    public boolean checkLoginName(int admin_id, String login_name);

    /**
     * 查询管理员列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> listAdminMap(int status);


    // **************************用户角色管理**************************

    /**
     * 加载管理员角色列表
     *
     * @param page
     * @param bean
     * @return
     */
    public PageUtil listAdminRole(PageUtil page, AdminRoleCondition bean);

    /**
     * 检测是否已经存在用户角色关系
     *
     * @param admin_role_id
     * @param admin_id
     * @param role_id
     * @return
     */
    public boolean checkAdminRole(String admin_role_id, String admin_id, String role_id);

    /**
     * 增加管理员角色的时候加载的用户列表
     *
     * @param page
     * @param bean
     * @return
     */
    public PageUtil addAdminRoleAdminList(PageUtil page, AdminRoleCondition bean);

    // **************************渠道管理**************************

    /**
     * 查询渠道列表
     *
     * @param page
     * @param channel
     * @return
     */
    public PageUtil listChannel(PageUtil page, TbChannel channel);

    /**
     * 检测渠道名称
     *
     * @param name
     * @return
     */
    public boolean checkChannelName(String channelid, String name);

    /**
     * 检测渠道编码
     *
     * @param code
     * @return
     */
    public boolean checkChannelCode(String channelid, String code);

    /**
     * 查询渠道信息
     *
     * @param channel_id
     * @return
     */
    public TbChannel loadChannelInfo(int channel_id);

    /**
     * 查询渠道列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> listChannelMap(String status);

    // **************************类目管理**************************

    /**
     * 查询类目列表
     *
     * @param leveId
     * @return
     */
    public List<Map<String, Object>> listTbCategoryMap(Integer status, Integer leveId);

    /**
     * 查询类目信息
     *
     * @param category_id
     * @return
     */
    public TbCategory loadTbCategoryInfo(int category_id);

    /**
     * 查询类目属性列表
     *
     * @param itemprops
     * @return
     */
    public List<Map<String, Object>> listTbItempropsMap(TbItemprops itemprops);

    /**
     * 查询类目属性信息
     *
     * @param propId
     * @return
     */
    public TbItemprops loadTbItempropsInfo(int propId);

    /**
     * 查询类目属性值列表
     *
     * @param propsValue
     * @return
     */
    public List<Map<String, Object>> listTbPropsValueMap(TbPropsValue propsValue);

    /**
     * 查询类目属性值
     *
     * @param value_id
     * @return
     */
    public TbPropsValue loadTbPropsValueInfo(int value_id);

    /**
     * 查询类目属性值
     *
     * @param item
     * @return
     */
    public PageUtil loadItempropsListPage(PageUtil page, ClassfyCondition item);

    /**
     * 查询类目属性值
     *
     * @param value
     * @return
     */
    public PageUtil loadPropsValueListPage(PageUtil page, ClassfyCondition value);

    /**
     * 查询类目编码是否存在
     *
     * @param code
     * @param id
     * @return
     */
    public boolean checkClassifyCodeIsExsit(String code, Integer id);

    /**
     * 查询类目属性编码是否存在
     *
     * @param code
     * @param id
     * @return
     */
    public boolean checkPropCodeIsExsit(String code, Integer id);

    public List<Map<String, Object>> listTbCategoryByIds(String ids);

    // **************************店铺管理**************************

    /**
     * 店铺管理
     *
     * @param page
     * @return
     */
    public PageUtil loadShopListPage(PageUtil page, ShopCondition shop);

    /**
     * 查询店铺
     *
     * @param shop_id
     * @return
     */
    public TbShop loadTbShopInfo(int shop_id);

    /**
     * 查询店铺列表
     *
     * @param shop
     * @return
     */
    public List<Map<String, Object>> listShopMap(TbShop shop);

    /**
     * 查询店铺关联的商家
     *
     * @param partnerStatus
     * @return
     */
    public List<Map<String, Object>> listShopPartnerRel(Integer partnerStatus);

    // **************************活动专题管理**************************

    /**
     * 活动专题管理
     *
     * @param page
     * @param activity
     * @return
     */
    public PageUtil loadActivityPage(PageUtil page, ActivityCondition activity);

    /**
     * 查询活动
     *
     * @param activity_id
     * @return
     */
    public TbActivity loadTbActivityInfo(int activity_id);

    // **************************bannel管理**************************

    /**
     * bannel管理
     *
     * @param page
     * @param bannel
     * @return
     */
    public PageUtil loadTbBannelPage(PageUtil page, BannelCondition bannel);

    /**
     * 查询bannel
     *
     * @param bannel_id
     * @return
     */
    public TbBannel loadTbBannelInfo(int bannel_id);

    // **************************配送地址管理**************************

    /**
     * 查询配送地址列表
     *
     * @param page
     * @param distribution
     * @return
     */
    public PageUtil loadTBDistributionPage(PageUtil page, TbDistributionAddr distribution);

    /**
     * 查询配送地址
     *
     * @param id
     * @return
     */
    public TbDistributionAddr loadTbDistributionAddrInfo(int id);


    /**
     * 查询全国地址详细
     *
     * @param page
     * @return
     */
    public PageUtil loadAddrDetailPage(PageUtil page, TbRegion region);

    /**
     * 全国地址管理
     *
     * @param page
     * @param region
     * @return
     */
    public PageUtil loadAddrManagePage(PageUtil page, TbRegion region);

    public TbRegion loadTbRegion(String regionCode);

    public TbRegion loadTbRegion(int regionId);

    public boolean checkRegionCode(Integer id, String regionCode);

    public List<Map<String, Object>> loadTbRegionListMap(String regionCode, String regionName);


    /**
     * 查询全国地区
     *
     * @param parentId 行政区划代码
     * @return
     */
    public List<Map<String, Object>> loadAddrListMap(String parentId, String regionName);

    public TbUserAddr loadTbUserAddrById(int id);

    // **************************规则管理**************************

    /**
     * 查询规则列表
     *
     * @param page
     * @param rulec
     * @return
     */
    public PageUtil loadTbRulePage(PageUtil page, RuleCondition rulec);

    /**
     * 查询规则
     *
     * @param rule_id
     * @return
     */
    public TbRule loadTbRuleByIdInfo(int rule_id);

    /**
     * 查询规则列表
     *
     * @param rule
     * @return
     */
    public List<Map<String, Object>> listRuleMap(TbRule rule);

    // **************************商品管理**************************

    /**
     * 查询商品sku列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadTbGoodsSkuPage(PageUtil page, GoodsCondition goods);

    public PageUtil loadTbGoodsSkuPage2(PageUtil page, GoodsCondition goods);

    /**
     * 查询商品sku
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> loadGoodsSkuList(Integer goodsId);

    /**
     * 根据商品标识查询商品已勾选的属性值
     *
     * @param goodsId
     * @return
     */
    public Map<String, Object> listTbGoodsAttributeMap(Integer goodsId);

    /**
     * 查询商品sku
     *
     * @param goodsId
     * @return
     */
    public List<TbGoodsSku> listGoodsSkuByGoodsId(Integer goodsId);

    /**
     * 查询商品图片
     *
     * @param relateId
     * @param imgClassyfi 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public List<Map<String, Object>> listImgsByReleteIdMap(Integer relateId, Integer imgClassyfi);

    public List<Map<String, Object>> listImgsByReleteIdMap2(Integer relateId, Integer imgClassyfi, Integer orderType);

    public List<Map<String, Object>> listImgsByReleteIdMap3(Integer relateId, Integer imgClassyfi, Integer orderType);

    /**
     * 查询商品信息
     *
     * @param goodsId
     * @return
     */
    public TbGoods loadTbGoodsInfo(int goodsId);

    /**
     * @param goodsId
     * @param code
     * @return
     */
    public TbGoods checkGoodsCode(Integer goodsId, String code);


    /**
     * 查询商品列表
     *
     * @param goods
     * @return
     */
    public List<Map<String, Object>> listGoodsMap(TbGoods goods);

    /**
     * 查询商家/商品/卡券规则列表
     *
     * @param relId
     * @return type 关联标识类型 0:商家规则1商品规则2卡券规则
     */
    public List<Map<String, Object>> listRelRule(Integer type, Integer relId);
    // **************************卡券管理**************************

    /**
     * 查询卡券列表
     *
     * @param page
     * @param ticket
     * @return
     */
    public PageUtil loadticketPage(PageUtil page, TicketCondition ticket);

    /**
     * 查询卡券列表
     *
     * @param ticket
     * @return
     */
    public List<Map<String, Object>> listTicketsMap(TbTicket ticket);

    /**
     * 查询卡券
     *
     * @param ticketId
     * @return
     */
    public TbTicket loadTicketInfo(Integer ticketId);

    /**
     * 根据卡券标识查询卡券已勾选的属性值
     *
     * @param ticketId
     * @return
     */
    public Map<String, Object> listTbTicketAttributeMap(Integer ticketId);

    //*************************规则关联管理*********************

    /**
     * 查询卡券规则页面列表
     *
     * @param page
     * @param ruleName
     * @param ticketName
     * @return
     */
    public PageUtil loadDataTicketRuleRle(PageUtil page, String ruleName, String ticketName, Integer timeLimit, Integer shopId);

    public PageUtil loadTicletRulePage(PageUtil page, String ruleName, String ticketName, Integer timeLimit, Integer userShopId, String ticketShopName, String partnersName);

    /**
     * 查询商品规则列表
     *
     * @param page
     * @param ruleName
     * @param goodsName
     * @param shopId
     * @return
     */
    public PageUtil loadDataGoodsRuleRle(PageUtil page, String ruleName, String goodsName, Integer timeLimit, Integer shopId);

    public PageUtil loadGoodsRulePage(PageUtil page, String ruleName, String goodsName, Integer timeLimit, Integer userShopId, String goodsShopName, String partnersName);

    /**
     * 查询商家规则列表
     *
     * @param page
     * @param ruleName
     * @param shopName
     * @param partnerId
     * @return
     */
    public PageUtil loadDataPartnersRuleRle(PageUtil page, String ruleName, String shopName, Integer timeLimit, Integer partnerId);

    public PageUtil loadPartnersRulePage(PageUtil page, String ruleName, String partnerName, Integer timeLimit, Integer userShopId);

    /**
     * 查询全部数据列表
     *
     * @param page
     * @param ruleName
     * @param relName
     * @param typeId
     * @param shopId
     * @return
     */
    public PageUtil loadDataAllRuleRle(PageUtil page, String ruleName, String relName, Integer typeId, Integer timeLimit, Integer shopId, Integer partnerId, Integer isMakeSure);

    /**
     * 查询卡券是否已经存在规则关联(卡券是多(规则)对一(卡券))
     *
     * @param relId
     * @return
     */
    public boolean checkTicketRelIsExsit(int relId);

    /**
     * 查询是否已经存在规则关联
     *
     * @param ruleId
     * @param relId
     * @param typeId
     * @return
     */
    public boolean checkRelIsExsit(int ruleId, int relId, int typeId);

    /**
     * 查询卡券列表
     *
     * @param userShopId
     * @param ticketName
     * @return
     */
    public List<Map<String, Object>> listTicketRulerel(Integer userShopId, String ticketName);

    /**
     * 查询卡券规则列表
     *
     * @param ticketId
     * @return
     */
    public List<Map<String, Object>> listTicketRuelRel(Integer ticketId, String ruleName, Integer userShopId);


    /**
     * 查询商品列表
     *
     * @param userShopId
     * @param goodsName
     * @return
     */
    public List<Map<String, Object>> listGoodsRulerel(Integer userShopId, String goodsName);

    /**
     * 查询商品规则列表
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> listGoodsRuelRel(Integer goodsId, String ruleName, Integer userShopId);

    /**
     * 查询商家列表
     *
     * @param userShopId
     * @param partnersName
     * @return
     */
    public List<Map<String, Object>> listPartnersRulerel(Integer userShopId, String partnersName);

    /**
     * 查询商家规则列表
     *
     * @param paertnersId
     * @return
     */
    public List<Map<String, Object>> listPartnreesRuelRel(Integer paertnersId, String ruleName, Integer userShopId);

    //**********************************商家管理*******************************

    /**
     * 分页查询商家列表
     *
     * @param page
     * @param partners
     * @return
     */
    public PageUtil loadPartnersPage(PageUtil page, PartnersCondition partners);

    /**
     * 查询商家
     *
     * @param id
     * @return
     */
    public TbPartners loadTbPartnersByIdInfo(Integer id);

    public TbPartners loadTbPartnersByShopIdInfo(Integer shopId);

    /**
     * 检查商家编码是否已经存在
     *
     * @param id
     * @param code
     * @return
     */
    public boolean checkPartnersCode(Integer id, String code);

    /**
     * 查询商家列表
     *
     * @param partners
     * @return
     */
    public List<Map<String, Object>> listPartnersMap(TbPartners partners);

    /**
     * 检查商家是否已经关联有店铺
     *
     * @param shopId
     * @param partnersId
     * @return
     */
    public boolean checkPartnersTicket(Integer shopId, Integer partnersId);

    /**
     * 检查商家是否已经关联有商家
     *
     * @param account
     * @param partnersId
     * @return
     */
    public boolean checkPartnersAccount(Integer partnersId, String account);

    //**********************************兑换权限管理*******************************

    /**
     * 查询兑换权限列表
     *
     * @param page
     * @param pName      商家名称
     * @param ticketName 卡券名称
     * @return
     */
    public PageUtil loadExchangeJurisdictionPage(PageUtil page, String pName, String ticketName, String shopName, String isMakeSure);

    /**
     * 根据ticketId查询对应兑换权限的商家
     *
     * @param ticketId
     * @return
     */
    public List<Map<String, Object>> listTicketPartnersByTicketId(Integer ticketId, String pName);


    //************************************************************************

    /**
     * 查询一级分类列表
     *
     * @param page
     * @param leve
     * @return
     */
    public PageUtil loadCatogryLeve1ListPage(PageUtil page, CatogryLeve1Condition leve);

    public PageUtil loadCatogryLeve1ListPage2(PageUtil page, CatogryLeve1Condition leve);

    public List<Map<String, Object>> listTbCatogryLevel1();

    /**
     * 查询一级菜单
     *
     * @param levelId
     * @return
     */
    public TbCatogryLevel1 loadCatogryLeve1Info(int levelId);

    /**
     * 检测分类编码是否存在
     *
     * @param leveId
     * @param code
     * @return
     */
    public boolean checkCatogryLeveCodeIsExist(String leveId, String code);

    /**
     * 查询分类类目关系
     *
     * @return
     */
    public List<Map<String, Object>> listClassfyCatogryMap(String keyWord, Integer leveId);

    /**
     * 加载一级分类
     *
     * @return
     */
    public List<Map<String, Object>> listClassfyMap(Integer catogryId);

    /**
     * 根据一级分类标识查询对应的类目
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> listClassfyCatogroyByClassfyId(Integer categoryId, String categroyName);

    //***************************************商品sku管理

    /**
     * 查询商品sku列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadlistGoodsSKU(PageUtil page, GoodsCondition goods);

    /**
     * 查商品sku信息
     *
     * @param skuId
     * @return
     */
    public TbGoodsSku loadGoodsSKUInfo(int skuId);

    /**
     * 查询商品库存
     *
     * @param goodsId
     * @return
     */
    public Integer searchGoodsStoreById(Integer goodsId);

    //***************************************确认管理

    /**
     * 查询修改密钥是否正确
     *
     * @param updateKey
     * @return
     */
    public TbSecretkey loadTbSecretkey(String updateKey);

    /**
     * 查询商品列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadTbGoodsPage(PageUtil page, GoodsCondition goods);

    //***************************************抢购管理

    /**
     * 查询抢购商品列表
     *
     * @param page
     * @param goodsName
     * @param partnersName
     * @param status
     * @param isMakeSure
     */
    public PageUtil loadTimeGoodsPage(PageUtil page, String goodsName, String partnersName, Integer status, Integer userShopId, Integer isMakeSure);

    /**
     * 查询抢购商品
     *
     * @param goodsId
     * @return
     */
    public TbTimeGoods loadTbTimeGoodsBuyGoodsId(Integer goodsId);

    //***************************************订单管理

    /**
     * 查询订单
     */
    public PageUtil loadOrderPageList(PageUtil page, OrderCondition orderCondition);

    public List<Map<String, Object>> loadOrderListMap(OrderCondition orderCondition);

    public TbOrder loadTbOrderById(int orderid);

    public List<Map<String, Object>> loadOrderDetailListByOrderId(int orderId);

    public TbOrderDetail loadTbOrderDetailByDetailId(int detailId);

    public List<Map<String, Object>> loadDetailPropBuyDetailId(int detailId);

    public List<Map<String, Object>> loadDetailPropBuyOrderId(int orderId);

    public List<Map<String, Object>> listStatusRecord(String orderNo);

    //***************************************首页菜单管理
    public PageUtil loadMainNemuPageList(PageUtil page, String status);

    public TbMainMenu loadTbMainMenu(int id);

    //***************************************图片上传地址管理
    public PageUtil loadImgRecordPage(PageUtil page, Integer status, String imgPathName);

    public TbImgUploadNameRecord loadTbImgUploadNameRecord(int id);

    public TbImgUploadNameRecord loadTbImgUploadNameRecord(String ImgPath);

    public TbActivity loadTbActivityByImgPath(String ImgPath);

    public TbBannel loadTbBannelByImgPath(String ImgPath);

    public TbCategory loadTbCategoryByImgPath(String ImgPath);

    public TbCatogryLevel1 loadTbCatogryLevel1ByImgPath(String ImgPath);

    public TbComment loadTbCommentByImgPath(String ImgPath);

    public TbGoods loadTbGoodsImgPath(String ImgPath);

    public TbGoodsSku loadTbGoodsSkuImgPath(String ImgPath);

    public TbImg loadTbImgImgPath(String ImgPath);

    public TbItemprops loadTbItempropsImgPath(String ImgPath);

    public TbMainMenu loadTbMainMenuImgPath(String ImgPath);

    public TbOrderCart loadTbOrderCartImgPath(String ImgPath);

    public TbOrderDetail loadTbOrderDetailImgPath(String ImgPath);

    public TbPartners loadTbPartnersImgPath(String ImgPath);

    public TbShop loadTbShopImgPath(String ImgPath);

    public TbTicket loadTbTicketImgPath(String ImgPath);

    public TbTicketDownQr loadTbTicketDownQrImgPath(String ImgPath);

    public TbUserInfo loadTbUserInfoImgPath(String ImgPath);


    //***********************************************wap用户管理********************************************

    /**
     * 查询wap用户列表
     *
     * @param page
     * @param userInfo
     * @return
     */
    public PageUtil loadWapUserPage(PageUtil page, TbUserInfo userInfo);//查询wap用户列表

    /**
     * 查询wap用户绑定
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> loadWapUserSnsListMap(int userId);//查询wap用户绑定


    //***********************************************投诉建议管理********************************************
    public PageUtil loadComplainPage(PageUtil page, TbComplain complain);

    public TbComplain loadTbComplainById(int id);


}
