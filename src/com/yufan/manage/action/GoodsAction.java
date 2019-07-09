package com.yufan.manage.action;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yufan.common.bean.PageUtil;
import com.yufan.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.ImageAction;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbGoodsAttribute;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbItemprops;
import com.yufan.pojo.TbPartners;
import com.yufan.pojo.TbPropsValue;
import com.yufan.pojo.TbShop;
import com.yufan.pojo.TbTicket;
import com.yufan.vo.GoodsCondition;

/**
 * @功能名称 商品管理
 * @作者 lirongfan
 * @时间 2016年7月18日 上午9:57:19
 */
@Scope("prototype")
@Controller("goodsAction")
@Namespace("/manage")
@ParentPackage("common")
public class GoodsAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private int id;//标识
    private String op;//add or update
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private List<Map<String, Object>> listGoodsType;//商品类型
    private List<Map<String, Object>> listClassfy;//商品分类
    private List<Map<String, Object>> listDJTickets;//商品券列表
    private List<Map<String, Object>> listShops;//店铺列表
    private List<TbGoodsSku> listGoodsSku;//商品sku列表
    private List<Map<String, Object>> listImg;//商品图片列表
    private List<Map<String, Object>> listPartners;

    private TbGoods goods = new TbGoods();
    private String imgPath;

    private String photoPath;
    private File file;

    private TbGoodsSku sku = new TbGoodsSku();

    private Integer catogryId;
    private Integer leveId;
    private List<Map<String, Object>> listClassfyCatogry;

    /**
     * 加载商品列表(根据店铺,只能查登录人所属于的店铺的商品)
     */
    @Action(value = "loadGoodsDataList")
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
            String isSingle = request.getParameter("isSingle");
            if (null != isSingle && !"".equals(isSingle) && !"-1".equals(isSingle)) {
                goodsc.setIsSingle(Integer.parseInt(isSingle));
            }
            String isTimeGoods = request.getParameter("isTimeGoods");
            if (null != isTimeGoods && !"".equals(isTimeGoods) && !"-1".equals(isTimeGoods)) {
                goodsc.setIsTimeGoods(Integer.parseInt(isTimeGoods));
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            goodsc.setShopId(user.getShopId());

            if (null != catogryId && 0 != catogryId) {
                goodsc.setCatogryId(catogryId);
            }
            if (null != leveId && 0 != leveId) {
                goodsc.setLeveId(leveId);
            }
            String goodsId = request.getParameter("goodsId");
            if (null != goodsId && !"".equals(goodsId.trim())) {
                goodsc.setGoodsId(Integer.parseInt(goodsId.trim()));
            }
            page = findInfoDao.loadTbGoodsSkuPage(page, goodsc);
            page.setExtendsObj(user.getShopId());
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action(value = "loadGoodsDataList2")
    public void loadGoodsDataList2() {
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
            if (StringUtils.isNotEmpty(goods_name)) {
                goodsc.setGoodsName(goods_name);
            }
            String goods_type = request.getParameter("goods_type");
            if (StringUtils.isNotEmpty(goods_type) && !"-1".equals(goods_type)) {
                goodsc.setGoodsType(Integer.parseInt(goods_type));
            }
            String goods_property = request.getParameter("goods_property");
            if (StringUtils.isNotEmpty(goods_property)) {
                goodsc.setGoodsProperty(Integer.parseInt(goods_property));
            }
            String is_putaway = request.getParameter("is_putaway");
            if (StringUtils.isNotEmpty(is_putaway)) {
                goodsc.setIsPutaway(Integer.parseInt(is_putaway));
            }
            String goods_status = request.getParameter("goods_status");
            if (StringUtils.isNotEmpty(goods_status)) {
                goodsc.setGoodsStatus(Integer.parseInt(goods_status));
            }
            String status = request.getParameter("status");
            if (StringUtils.isNotEmpty(status)) {
                goodsc.setStatus(Integer.parseInt(status));
            }
            String pName = request.getParameter("pName");
            if (StringUtils.isNotEmpty(pName)) {
                goodsc.setpName(pName.trim());
            }
            String isSingle = request.getParameter("isSingle");
            if (null != isSingle && !"".equals(isSingle) && !"-1".equals(isSingle)) {
                goodsc.setIsSingle(Integer.parseInt(isSingle));
            }
            String isTimeGoods = request.getParameter("isTimeGoods");
            if (null != isTimeGoods && !"".equals(isTimeGoods) && !"-1".equals(isTimeGoods)) {
                goodsc.setIsTimeGoods(Integer.parseInt(isTimeGoods));
            }
            String getWay = request.getParameter("getWay");
            if (StringUtils.isNotEmpty(getWay)) {
                goodsc.setGetWay(Integer.parseInt(getWay));
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            goodsc.setShopId(user.getShopId());

            if (null != catogryId && 0 != catogryId) {
                goodsc.setCatogryId(catogryId);
            }
            if (null != leveId && 0 != leveId) {
                goodsc.setLeveId(leveId);
            }
            String goodsId = request.getParameter("goodsId");
            if (null != goodsId && !"".equals(goodsId.trim())) {
                goodsc.setGoodsId(Integer.parseInt(goodsId.trim()));
            }
            page = findInfoDao.loadTbGoodsSkuPage2(page, goodsc);
            page.setExtendsObj(user.getShopId());
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询商品sku列表
     */
    @Action(value = "loadGoodsSkuList")
    public void loadGoodsSkuList() {
        initData();
        try {
            Integer goodsId = Integer.parseInt(request.getParameter("goods_id"));
            List<Map<String, Object>> listSku = findInfoDao.loadGoodsSkuList(goodsId);
            response.getWriter().write(write_Json_result("msg", listSku));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改商品页面
     *
     * @return
     */
    @Action(value = "toAddgoodsPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-goods.jsp")})
    public String toAddGoodsPage() {
        initData();
        goods = new TbGoods();
        //查询商品分类
        listClassfy = findInfoDao.listTbCategoryMap(1, null);
        //查询商品类别
        listGoodsType = findInfoDao.listParamMap("goods_type");
        //查询卡券列表
        TbTicket ticket = new TbTicket();
        ticket.setStatus(1);
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        ticket.setShopId(user.getShopId());
        listDJTickets = findInfoDao.listTicketsMap(ticket);
        //查询店铺列表
        TbShop shop = new TbShop();
        shop.setStatus(1);
        TbAdmin admin = findInfoDao.loadAdminInfo(user.getAdminId());
        shop.setShopId(admin.getShopId());
        listShops = findInfoDao.listShopMap(shop);
        if (listShops.size() == 0) {
            listShops = null;
        }
        TbPartners partners = new TbPartners();
        partners.setStatus(1);
        //第三方店铺只能增加自己的商家
        shop = findInfoDao.loadTbShopInfo(shop.getShopId());
        if (null != shop && null != shop.getIsOutShop() && shop.getIsOutShop() != 1) {
            partners.setShopId(admin.getShopId());
        }
        listPartners = findInfoDao.listPartnersMap(partners);
        goods.setLimitNum(0);
        //查询一级分类
        listClassfyCatogry = findInfoDao.listClassfyMap(null);
        //查询商品规则
        List<Map<String, Object>> listRelRule = new ArrayList<Map<String, Object>>();
        String path = RsConstants.phone_url;
        if ("update".equals(op)) {
            goods = findInfoDao.loadTbGoodsInfo(id);
            if (null != goods.getGoodsImg() && !"".equals(goods.getGoodsImg().trim())) {
                imgPath = path + goods.getGoodsImg();
            }
            if (goods.getLimitBeginTime() == null) {
                goods.setLimitBeginTime(new Timestamp(System.currentTimeMillis()));
            }
            List<Map<String, Object>> listImgInfo = findInfoDao.listImgsByReleteIdMap3(id, 0, 2);//商品介绍
            request.setAttribute("listImgInfo", listImgInfo);
            request.setAttribute("urlPath", path);
            listImg = findInfoDao.listImgsByReleteIdMap3(id, 0, 1);//商品bannel
            List<Map<String, Object>> outListImg = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < listImg.size(); i++) {
                Map<String, Object> map = listImg.get(i);

                if (listImg.size() == 4) {
                    map.put("index", i + 1);
                } else {
                    map.put("index", i + 2);
                }
                map.put("i", i + 1);
                map.put("imgPath", path + map.get("img_url"));
                outListImg.add(map);
            }
            listImg.clear();
            listImg = outListImg;

            //查询一级分类
            List<Map<String, Object>> listMap = findInfoDao.listClassfyMap(goods.getClassifyId());
            if (null != listMap && listMap.size() > 0) {
//				leveId = Integer.parseInt(String.valueOf(listMap.get(0).get("level_id")));
                //查询类目
                listClassfy = findInfoDao.listTbCategoryMap(1, leveId);
            }
            request.setAttribute("listImgSize", listImg.size());

            //查询商品规则
            listRelRule = findInfoDao.listRelRule(1, goods.getGoodsId());
            request.setAttribute("getWayCheck" + goods.getGetWay(), "checked");
        } else {
            goods.setStartTime(new Timestamp(System.currentTimeMillis()));
            goods.setEndTime(new Timestamp(System.currentTimeMillis()));
            goods.setWeight(0);
            goods.setDepositMoney(new BigDecimal("0"));
            goods.setGoodsNum(0);
            goods.setLimitNum(0);
            goods.setLimitBeginTime(new Timestamp(System.currentTimeMillis()));
            request.setAttribute("listImgSize", 0);
            request.setAttribute("getWayCheck4", "checked");
        }

        request.setAttribute("listRelRule", listRelRule);
        return "success";
    }

    /**
     * 根据分类查询属性和属性值
     */
    @Action(value = "searClassifyInfosById")
    public void searClassifyInfosById() {
        try {
            initData();
            String goodsId = request.getParameter("goodsId");
            //根据商品标识查询商品已勾选的属性值
            Map<String, Object> goodsAttributeMap = new HashMap<String, Object>();
            if (null != goodsId && !"".equals(goodsId)) {
                goodsAttributeMap = findInfoDao.listTbGoodsAttributeMap(Integer.parseInt(goodsId));
                listGoodsSku = findInfoDao.listGoodsSkuByGoodsId(Integer.parseInt(goodsId));
            }
            Integer categoryId = Integer.parseInt(request.getParameter("classifyId"));
            //查询属性列表
            TbItemprops itemprops = new TbItemprops();
            itemprops.setCategoryId(categoryId);
            itemprops.setStatus(1);
            List<Map<String, Object>> listItemprops = findInfoDao.listTbItempropsMap(itemprops);
            //查询属性值列表
            TbPropsValue propsValue = new TbPropsValue();
            propsValue.setCategoryId(categoryId);
            propsValue.setStatus(1);
            List<Map<String, Object>> listPropsValue = findInfoDao.listTbPropsValueMap(propsValue);
            JSONObject obj = new JSONObject();
            //数据处理
            List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < listItemprops.size(); i++) {
                Map<String, Object> itemMap = listItemprops.get(i);
                String propId = itemMap.get("prop_id").toString();
                //用于保存属性值列表
                List<Map<String, Object>> dPropValue = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < listPropsValue.size(); j++) {
                    Map<String, Object> prosValue = listPropsValue.get(j);
                    String propId_ = prosValue.get("prop_id").toString();
                    String valueId = prosValue.get("value_id").toString();
                    if (propId_.equals(propId)) {
                        if (null != goodsAttributeMap && "ok".equals(String.valueOf(goodsAttributeMap.get(valueId)))) {
                            prosValue.put("isChecked", "1");
                        } else {
                            prosValue.put("isChecked", "0");
                        }
                        dPropValue.add(prosValue);
                    }
                }
                itemMap.put("propvalues", dPropValue);
                outList.add(itemMap);
            }
            obj.put("propValue", outList);
            //查询商品sku
            obj.put("listGoodsSku", listGoodsSku);
            response.getWriter().write(write_Json_result("ok", obj));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 增加或者修改商品
     */
    @SuppressWarnings("unused")
    @Action(value = "addOrUpdateGoods")
    public void addOrUpdateGoods() {
        try {
            initData();
            //检查编码是否已经存在
            if (null != goods.getGoodsCode() && !"".equals(goods.getGoodsCode().trim())) {
                TbGoods g = findInfoDao.checkGoodsCode(goods.getGoodsId(), goods.getGoodsCode());
                if (null != g) {
                    response.getWriter().write(write_Json_result("false", "编码已存在"));
                    return;
                }
            }
            if (null == goods.getWeight()) {
                goods.setWeight(0);
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            //获取选择的非销售属性值
            String selected[] = request.getParameterValues("selected");
            String checkbox[] = request.getParameterValues("checkbox");
            //获取销售属性值
            String propIds = request.getParameter("propIds");
            String ids[] = request.getParameterValues("ids");
            String names[] = request.getParameterValues("names");
            String trueMoney[] = request.getParameterValues("trueMoney");//原价
            String goodsSkuPrice[] = request.getParameterValues("goodsSkuPrice");//现价
            String goodsSkuKc[] = request.getParameterValues("goodsSkuKc");
            String goodsSkuCode[] = request.getParameterValues("goodsSkuCode");
            String purchasePrice[] = request.getParameterValues("purchasePrice");//进货价

            //商品属性表
            List<TbGoodsAttribute> attributeList = new ArrayList<TbGoodsAttribute>();
            //商品sku表
            List<TbGoodsSku> skuList = new ArrayList<TbGoodsSku>();
            //商品图片表
            List<TbImg> imgList = new ArrayList<TbImg>();
            //用来保存商品属性值key=属性+属性值
            Map<String, String> map = new HashMap<String, String>();
            //获取选择的非销售属性值
            if (null != selected && selected.length > 0) {
                for (int i = 0; i < selected.length; i++) {
                    map.put(selected[i], selected[i]);
                }
            }
            if (null != checkbox && checkbox.length > 0) {
                for (int i = 0; i < checkbox.length; i++) {
                    map.put(checkbox[i], checkbox[i]);
                }
            }
            String img_strs[] = request.getParameterValues("imgName1");//商品bannel图片
            //处理商品图片
            if (null != img_strs && img_strs.length > 0) {
                for (int i = 0; i < img_strs.length; i++) {
                    String imgUrl = img_strs[i];
                    if (imgUrl != null && !"".equals(imgUrl)) {
                        TbImg img = new TbImg();
                        img.setCreatetime(new Timestamp(System.currentTimeMillis()));
                        img.setImgClassyfi(0);
                        img.setImgType(1);
                        img.setImgUrl(imgUrl);
                        img.setImgSort(img_strs.length - i);
                        imgList.add(img);
                    }
                }
            }

            String img_strsInfo[] = request.getParameterValues("imgName2");//商品info图片
            //处理商品图片
            if (null != img_strsInfo && img_strsInfo.length > 0) {
                for (int i = 0; i < img_strsInfo.length; i++) {
                    String imgUrl = img_strsInfo[i];
                    if (imgUrl != null && !"".equals(imgUrl)) {
                        TbImg img = new TbImg();
                        img.setCreatetime(new Timestamp(System.currentTimeMillis()));
                        img.setImgClassyfi(0);
                        img.setImgType(2);
                        img.setImgUrl(imgUrl);
                        img.setImgSort(img_strsInfo.length - i);
                        imgList.add(img);
                    }
                }
            }
            goods.setIsSingle(1);
            //获取销售属性值 商品sku
            if (null != propIds) {
                String propId[] = propIds.split(";");
                goods.setIsSingle(0);
                for (int i = 0; i < ids.length; i++) {
                    String idsStr = ids[i];//属性值
                    String namesStr = names[i];//属性值对应中文名称
                    String trueMoneyStr = trueMoney[i];
                    String goodsSkuPriceStr = goodsSkuPrice[i];
                    String goodsSkuKcStr = goodsSkuKc[i];
                    String goodsSkuCodeStr = goodsSkuCode[i];
                    String purchasePriceStr = purchasePrice[i];
                    //商品属性值
                    String idsStr_[] = idsStr.split(";");
                    for (int j = 0; j < idsStr_.length; j++) {
                        String key = propId[j] + ";" + idsStr_[j];
                        map.put(key, key);
                    }

                    //商品sku
                    TbGoodsSku sku = new TbGoodsSku();
                    sku.setSkuName(namesStr);
                    sku.setTrueMoney(new BigDecimal(trueMoneyStr));
                    sku.setNowMoney(new BigDecimal(goodsSkuPriceStr));
                    sku.setSkuCode(goodsSkuCodeStr);
                    sku.setPropCode(idsStr);
                    sku.setPurchasePrice(new BigDecimal(purchasePriceStr));
                    sku.setSkuNum(Integer.parseInt(goodsSkuKcStr));
                    sku.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    skuList.add(sku);
                }
            }

            //map处理商品重复的属性值
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String keys = entry.getKey();
                String key[] = keys.split(";");
                TbGoodsAttribute attrbute = new TbGoodsAttribute();
                attrbute.setCreatetime(new Timestamp(System.currentTimeMillis()));
                attrbute.setPropId(Integer.parseInt(key[0]));
                attrbute.setValueId(Integer.parseInt(key[1]));
                attributeList.add(attrbute);
            }

            if ("update".equals(op)) {
                TbGoods goods_ = findInfoDao.loadTbGoodsInfo(goods.getGoodsId());
                goods.setWeight(goods_.getWeight());
                //删除商品属性表
                saveInfoDao.deleteGoodsAttributeByGoodsId(goods.getGoodsId(), null);
                //删除商品sku表
                saveInfoDao.deleteGoodsSkuByGoodsId(goods.getGoodsId(), null);
                //删除商品图片表
                saveInfoDao.deleteImgBuRelateId(goods.getGoodsId(), 0);
                goods.setCreateman(goods_.getCreateman());
                goods.setCreatetime(goods_.getCreatetime());
                goods.setIsTimeGoods(goods_.getIsTimeGoods());
                //修改购物车商品状态
                saveInfoDao.updateOrderCart(goods.getGoodsId(), 2);
            } else {
                goods.setCreateman(user.getLoginName());
                goods.setCreatetime(new Timestamp(System.currentTimeMillis()));
                goods.setIsTimeGoods(0);
            }
            goods.setLastalterman(user.getLoginName());
            goods.setLastaltertime(new Timestamp(System.currentTimeMillis()));

            //商品表
            if (null == goods.getAreaId()) {
                goods.setAreaId(0);
            }
            if (goods.getIsSingle() == 0) {
                goods.setAdvancePrice(new BigDecimal(0));
                goods.setPurchasePrice(new BigDecimal(0));
            }
            boolean flag = saveInfoDao.saveGoods(goods);
            if (flag && goods.getGoodsId() != null && goods.getGoodsId() > 0) {
                //商品属性表
                saveInfoDao.saveGoodsAttrbute(attributeList, goods);
                //商品sku表
                saveInfoDao.saveGoodsSku(skuList, goods);
                //商品图片表
                saveInfoDao.saveImg(imgList, goods.getGoodsId());
            }

            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(goods.getGoodsId());
            String reusltMsg = reuslt.getString("msg");

            JSONObject obj = new JSONObject();
            String flags = "false";
            String msg = "操作失败";
            if (flag) {
                if ("update".equals(op)) {
                    flags = "ok";
                    msg = "更新成功(" + reusltMsg + ")";
                } else {
                    flags = "ok";
                    msg = "新增成功(" + reusltMsg + ")";
                }
            }
            response.getWriter().write(write_Json_result(flags, msg));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 逻辑删除商品
     */
    @Action(value = "deleteGoodsStatus")
    public void deleteGoodsStatus() {
        initData();
        try {

            //如果是店员,不允许删除---店铺成员类型 0:管理员1:店长2店员shop_menber_type
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer shopMenberType = user.getShopMenberType();
            if (null == shopMenberType || shopMenberType == 2) {
                response.getWriter().write(write_Json_result("false", "操作失败,店员不能删除商品"));
                return;
            }
            goods = new TbGoods();
            String status = request.getParameter("status");
            goods.setStatus(Integer.parseInt(status));
            goods.setGoodsId(id);
            boolean flag = saveInfoDao.updateGoodsStatus(goods);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(id);
            String reusltMsg = reuslt.getString("msg");
            if (flag && "0".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "删除成功(" + reusltMsg + ")"));
            } else if (flag && "1".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "启用成功(" + reusltMsg + ")"));
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败(" + reusltMsg + ")"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新商品上架状态
     */
    @Action(value = "updateGoodsIsPutaway")
    public void updateGoodsIsPutaway() {
        initData();
        try {
            goods = new TbGoods();
            Integer isPutaway = Integer.parseInt(request.getParameter("isPutaway"));
            goods.setGoodsId(id);
            goods.setIsPutaway(isPutaway);
            boolean flag = saveInfoDao.updateGoodsIsPutaway(goods);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(goods.getGoodsId());
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
            } else {
                response.getWriter().write(write_Json_result("false", "修改失败(" + reusltMsg + ")"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //--------------------------------------------------------商品sku管理------------------------------------

    /**
     * 加载商品sku列表
     *
     * @return
     */
    @Action(value = "loadGoodsSKUDataList")
    public void loadGoodsSKUDataList() {
        System.out.println("=======loadlistGoodsSKU===========");
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
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            GoodsCondition skuCondition = new GoodsCondition();
            String goodsId = request.getParameter("goodsId");
            if (StringUtils.isNotEmpty(goodsId)) {
                skuCondition.setGoodsId(Integer.parseInt(goodsId.trim()));
            }
            skuCondition.setGoodsName(request.getParameter("goodsName"));
            skuCondition.setSkuName(request.getParameter("skuName"));
            skuCondition.setPropCode(request.getParameter("propCode"));
            skuCondition.setShopId(user.getShopId());
            page = findInfoDao.loadlistGoodsSKU(page, skuCondition);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 跳转到增加或者修改商品sku页面（只做修改不做增加）
     *
     * @return
     */
    @Action(value = "toAddorUpdateGoodsSKUPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-goodsku.jsp")})
    public String toAddorUpdateGoodsSKUPage() {
        initData();
        try {

            photoPath = RsConstants.phone_url + "null.jpg";
            sku = findInfoDao.loadGoodsSKUInfo(Integer.parseInt(request.getParameter("skuId")));
            if (sku.getSkuImg() != null && !"".equals(sku.getSkuImg())) {
                photoPath = RsConstants.phone_url + sku.getSkuImg();
            }
            goods = findInfoDao.loadTbGoodsInfo(sku.getGoodsId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改商品sku
     */
    @Action(value = "addOrUpdateGoodsSKU")
    public void addOrUpdateGoodsSKU() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (null == user.getShopId() || user.getShopId() == 0) {
                response.getWriter().write(write_Json_result("false", "操作失败,管理员不允许修改商品sku"));
                return;
            }
            boolean flag = false;
            TbGoodsSku sku_ = findInfoDao.loadGoodsSKUInfo(sku.getSkuId());
            if (file != null) {
                String imgpath = new ImageAction().uploadFileToLocalhost(file);
                if (null != imgpath && !"".equals(imgpath)) {
                    sku_.setSkuImg(imgpath);
                    ImageUtil.getInstance().removePathImg(sku_.getSkuImg(), request);
                }
            } else {
                sku_.setSkuImg(sku_.getSkuImg());
            }
            sku_.setSkuCode(sku.getSkuCode());
            sku_.setTrueMoney(sku.getTrueMoney());
            sku_.setNowMoney(sku.getNowMoney());
            sku_.setSkuNum(sku.getSkuNum());
            sku_.setPurchasePrice(sku.getPurchasePrice());
            flag = saveInfoDao.saveOrUpdateObj(sku_);
            if (flag) {
                MyLog.getLogger().info("--sku保存成功-");
            }
            //保存商品库存
            Integer goodsId = sku_.getGoodsId();
            TbGoods goods = findInfoDao.loadTbGoodsInfo(goodsId);
            //计算库存
            Integer store = findInfoDao.searchGoodsStoreById(goodsId);
            goods.setGoodsNum(store);
            goods.setLastalterman(user.getLoginName());
            goods.setLastaltertime(new Timestamp(System.currentTimeMillis()));
            //修改商品sku,则商品需要重新上架确认
            goods.setIsPutaway(1);
            flag = saveInfoDao.saveOrUpdateObj(goods);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshGoods(goods.getGoodsId());
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
            } else {
                response.getWriter().write(write_Json_result("false", "修改失败(" + reusltMsg + ")"));
            }
            //修改购物车商品状态
            saveInfoDao.updateOrderCart(goods.getGoodsId(), 2);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除商品sku(该方法不用)
     */
//	@Action(value="deleteGoodsSKUStatus")
//	public void deleteGoodsSKUStatus(){
//		initData();
//		try {
//			
//			//如果是店员,不允许删除---店铺成员类型 0:管理员1:店长2店员shop_menber_type
//			TbAdmin user=(TbAdmin) request.getSession().getAttribute("user");
//			Integer shopMenberType = user.getShopMenberType();
//			if(null==shopMenberType||shopMenberType==2){
//				response.getWriter().write(write_Json_result("false", "操作失败,店员不能删除商品"));
//				return;
//			}
//			
//			Integer goodsId = Integer.parseInt(request.getParameter("goodsId"));
//			Integer skuId = Integer.parseInt(request.getParameter("skuId"));
//			sku = findInfoDao.loadGoodsSKUInfo(skuId);
//			
//			boolean flag=saveInfoDao.deleteGoodsSkuByGoodsId(goodsId, skuId);
//			if(flag){
//				//修改商品库存
//				TbGoods goods = findInfoDao.loadTbGoodsInfo(goodsId);
//				//计算库存
//				Integer store = findInfoDao.searchGoodsStoreById(goodsId);
//				goods.setGoodsNum(store);
//				flag = saveInfoDao.saveOrUpdateObj(goods);
//				if(flag){
//					MyLog.getLogger().info("---删除商品sku,修改商品库存成功");
//				}
//				//删除商品属性值
//				String propCode = sku.getPropCode();
//				String propCodes[] = propCode.split(";");
//				if(propCodes.length>0){
//					for (int i = 0; i < propCodes.length; i++) {
//						String valueId = propCodes[i];
//						if(null!=valueId&&!"".equals(valueId)){
//							saveInfoDao.deleteGoodsAttributeByGoodsId(goodsId, Integer.parseInt(valueId));
//						}
//					}
//				}
//				response.getWriter().write(write_Json_result("ok", "删除成功"));
//			}else{
//				response.getWriter().write(write_Json_result("false", "删除失败"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//

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


    public int getId() {
        return id;
    }


    public void setId(int id) {
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


    public List<Map<String, Object>> getListGoodsType() {
        return listGoodsType;
    }


    public void setListGoodsType(List<Map<String, Object>> listGoodsType) {
        this.listGoodsType = listGoodsType;
    }


    public List<Map<String, Object>> getListClassfy() {
        return listClassfy;
    }


    public void setListClassfy(List<Map<String, Object>> listClassfy) {
        this.listClassfy = listClassfy;
    }


    public TbGoods getGoods() {
        return goods;
    }


    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }


    public TbGoodsSku getSku() {
        return sku;
    }


    public void setSku(TbGoodsSku sku) {
        this.sku = sku;
    }


    public List<Map<String, Object>> getListDJTickets() {
        return listDJTickets;
    }


    public void setListDJTickets(List<Map<String, Object>> listDJTickets) {
        this.listDJTickets = listDJTickets;
    }

    public List<Map<String, Object>> getListShops() {
        return listShops;
    }

    public void setListShops(List<Map<String, Object>> listShops) {
        this.listShops = listShops;
    }

    public List<Map<String, Object>> getListPartners() {
        return listPartners;
    }


    public void setListPartners(List<Map<String, Object>> listPartners) {
        this.listPartners = listPartners;
    }


    public List<Map<String, Object>> getListImg() {
        return listImg;
    }


    public String getPhotoPath() {
        return photoPath;
    }


    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public File getFile() {
        return file;
    }


    public void setFile(File file) {
        this.file = file;
    }


    public void setListImg(List<Map<String, Object>> listImg) {
        this.listImg = listImg;
    }


    public List<TbGoodsSku> getListGoodsSku() {
        return listGoodsSku;
    }


    public void setListGoodsSku(List<TbGoodsSku> listGoodsSku) {
        this.listGoodsSku = listGoodsSku;
    }


    public List<Map<String, Object>> getListClassfyCatogry() {
        return listClassfyCatogry;
    }


    public void setListClassfyCatogry(List<Map<String, Object>> listClassfyCatogry) {
        this.listClassfyCatogry = listClassfyCatogry;
    }


    public String getImgPath() {
        return imgPath;
    }


    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public static void main(String[] args) {
        String a[] = {"a", "b"};
        String b[] = {"c"};
        String c[] = {"e", "f", "g"};
        String d[] = {"h", "i"};
        List<String[]> list = new ArrayList<String[]>();
        list.add(a);
        list.add(b);
//		list.add(c);
//		list.add(d);
        GoodsAction goodss = new GoodsAction();
//		goodss.comArrayN(list);
//		switch (list.size()) {
//		case 1:
//			goodss.comArray1(list);
//			break;
//		case 2:
//			goodss.comArray2(list);
//			break;
//		case 3:
//			goodss.comArray3(list);
//			break;
//		case 4:
//			goodss.comArray4(list);
//			break;
//
//		default:
//			break;
//		}


        Map<String, String> map = new HashMap<String, String>();
        map.put("aa", "fds");
        map.put("vf", "fds");
        map.put("dsa", "fds");
        map.put("rew", "fds");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    /**
     * n层    System.out.println("--->结束");
     *
     * @param list
     */
    private String str = "";

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

    List<String[]> listsub = new ArrayList<String[]>();

    public void comArrayN(List<String[]> list) {

        for (int i = 0; i < list.size(); i++) {
            String strs[] = list.get(i);
            for (int j = 0; j < strs.length; j++) {
                String value = strs[j];
                str = str + value;
                if (i == list.size() - 1) {//得到最后一层值
                    System.out.println("--->" + str);
                    str = "";
                    listsub.clear();
                } else {
                    for (int k = i + 1; k < strs.length; k++) {
                        listsub.add(list.get(k));
                    }
                    comArrayN(listsub);
                }

            }
        }


    }


    /**
     * 一层
     *
     * @param list
     */
    public void comArray1(List<String[]> list) {
        //笨方法
        String[] a0 = list.get(0);

        for (int i = 0; i < a0.length; i++) {
            System.out.println(a0[i]);
        }
    }

    /**
     * 二层
     *
     * @param list
     */
    public void comArray2(List<String[]> list) {
        //笨方法
        String[] a0 = list.get(0);
        String[] a1 = list.get(1);

        for (int i = 0; i < a0.length; i++) {
            for (int j = 0; j < a1.length; j++) {
                System.out.println(a0[i] + a1[j]);
            }
        }
    }

    /**
     * 三层
     *
     * @param list
     */
    public void comArray3(List<String[]> list) {
        //笨方法
        String[] a0 = list.get(0);
        String[] a1 = list.get(1);
        String[] a2 = list.get(2);

        for (int i = 0; i < a0.length; i++) {
            for (int j = 0; j < a1.length; j++) {
                for (int k = 0; k < a2.length; k++) {
                    System.out.println(a0[i] + a1[j] + a2[k]);
                }
            }
        }
    }

    /**
     * 4层
     *
     * @param list
     */
    public void comArray4(List<String[]> list) {
        //笨方法
        String[] a0 = list.get(0);
        String[] a1 = list.get(1);
        String[] a2 = list.get(2);
        String[] a3 = list.get(3);

        for (int i = 0; i < a0.length; i++) {
            for (int j = 0; j < a1.length; j++) {
                for (int k = 0; k < a2.length; k++) {
                    for (int l = 0; l < a3.length; l++) {
                        System.out.println(a0[i] + a1[j] + a2[k] + a3[l]);
                    }
                }
            }
        }
    }
}
