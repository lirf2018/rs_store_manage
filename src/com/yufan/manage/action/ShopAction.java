package com.yufan.manage.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbShopPartnersRel;
import com.yufan.util.RefreshCacheUtil;
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
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbShop;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.ShopCondition;

/**
 * 功能名称: 店铺管理
 * 开发人: lirf
 * 开发时间: 2016下午10:15:03 其它说明：
 */
@Scope("prototype")
@Service("shopAction")
@Namespace("/manage")
@ParentPackage("common")
public class ShopAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String status;// 状态

    private String photoPath;

    private String handleType = "update";// add增加 update

    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbShop shop = new TbShop();
    private String shop_id;

    private List<Map<String, Object>> list_admin;
    private List<Map<String, Object>> listImg;//图片列表
    private String imgPath;
    //查询条件
    private String shop_name;
    private String user_name;

    /**
     * 加载店铺列表
     *
     * @return
     */
    @Action(value = "loadshoplist")
    public void loadshoplist() {
        System.out.println("=======rolelist===========");
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

            ShopCondition s = new ShopCondition();

            if (null != shop_name && !"".equals(shop_name)) {
                s.setShopName(shop_name);
            }
            if (null != user_name && !"".equals(user_name)) {
                s.setUserName(user_name);
            }
            if (null != status && !"".equals(status)) {
                s.setStatus(Integer.parseInt(status));
                ;
            }
            String isOutShop = request.getParameter("isOutShop");
            if (null != isOutShop && !"".equals(isOutShop)) {
                s.setIsOutShop(Integer.parseInt(isOutShop));
            }

            page = findInfoDao.loadShopListPage(page, s);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 跳到增加或者修改店铺页面
     *
     * @return
     */
    @Action(value = "toAddorUpdateShopPage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-shop.jsp")})
    public String toAddorUpdateShopPage() {
        initData();
        //查询用户
        list_admin = findInfoDao.listAdminMap(1);
        List<Map<String, Object>> listShopPartnerRel = findInfoDao.listShopPartnerRel(1);//商家列表
        request.setAttribute("listShopPartnerRel", listShopPartnerRel);
        photoPath = RsConstants.phone_url + "null.jpg";
        if ("update".equals(handleType)) {
            String path = RsConstants.phone_url;
            shop = findInfoDao.loadTbShopInfo(Integer.parseInt(shop_id));
            if (null != shop.getShopLogo() && !"".equals(shop)) {
                imgPath = path + shop.getShopLogo();
            }

            listImg = findInfoDao.listImgsByReleteIdMap(Integer.parseInt(shop_id), 2);
            List<Map<String, Object>> outListImg = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < listImg.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map = listImg.get(i);
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
            request.setAttribute("listImgSize", listImg.size());
        } else {
            shop.setShopMoney(0.00);
            shop.setDeposit(0.00);
            shop.setDepositTime(new Timestamp(System.currentTimeMillis()));
            request.setAttribute("listImgSize", 0);
        }
        return "success";
    }

    /**
     * 增加或者修改店铺
     */
    @Action(value = "addOrUpdateshop")
    public void addOrUpdateshop() {
        initData();
        try {
            int id = 0;
            if (null != shop.getShopId()) {
                id = shop.getShopId();
            }

            String img_strs[] = request.getParameterValues("img_str");//介绍图片
            String pxs[] = request.getParameterValues("px");//排序

            //图片表
            List<TbImg> imgList = new ArrayList<TbImg>();
            long time[] = {900000, 700000, 400000, 100000};
            if (null != img_strs && img_strs.length > 0) {
                for (int i = 0; i < img_strs.length; i++) {
                    String imgUrl = img_strs[i];
                    if (null != imgUrl && !"".equals(imgUrl)) {
                        TbImg img = new TbImg();
                        long tim = time[0];
                        if (null != pxs) {
                            for (int j = 0; j < pxs.length; j++) {
                                if (pxs[j].equals(imgUrl)) {
                                    tim = time[j];
                                }
                            }
                        }
                        img.setCreatetime(new Timestamp(System.currentTimeMillis() - tim));
                        img.setImgClassyfi(2);
                        img.setImgType(1);
                        img.setImgUrl(imgUrl);
                        imgList.add(img);
                    }
                }
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {
                //增加店铺
                shop.setCreateman(user.getLoginName());
                shop.setCreatetime(new Timestamp(System.currentTimeMillis()));
                shop.setShopMoney(0.00);
                shop.setDeposit(0.00);
                shop.setDepositTime(new Timestamp(System.currentTimeMillis()));
                shop.setEnterStartTime(new Timestamp(System.currentTimeMillis()));
                boolean flag = saveInfoDao.saveOrUpdateObj(shop);
                saveInfoDao.saveImg(imgList, shop.getShopId());
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshShop(shop.getShopId());
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败(" + reusltMsg + ")"));
                }
            } else {
                //修改
                TbShop shop_ = findInfoDao.loadTbShopInfo(shop.getShopId());
                shop.setLastalterman(user.getLoginName());
                shop.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                shop.setCreateman(shop_.getCreateman());
                shop.setCreatetime(shop_.getCreatetime());
                shop.setShopMoney(shop_.getShopMoney());
                shop.setEnterStartTime(shop_.getEnterStartTime());

                boolean flag = saveInfoDao.saveOrUpdateObj(shop);
                //删除商品图片表
                saveInfoDao.deleteImgBuRelateId(shop.getShopId(), 2);
                saveInfoDao.saveImg(imgList, shop.getShopId());
                //删除店铺与商家关联
                saveInfoDao.delShopPartnersRel(shop.getShopId());
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshShop(shop.getShopId());
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "更新失败(" + reusltMsg + ")"));
                }
            }
            //处理店铺与商家关联
            String[] partnersArry = request.getParameterValues("checkSub");
            if (null != partnersArry && partnersArry.length > 0) {
                for (int i = 0; i < partnersArry.length; i++) {
                    String partnersId = partnersArry[i];
                    TbShopPartnersRel r = new TbShopPartnersRel();
                    r.setPartnersId(Integer.parseInt(partnersId));
                    r.setShopId(shop.getShopId());
                    r.setCreatetime(new Timestamp(new Date().getTime()));
                    r.setCreateman(user.getLoginName());
                    saveInfoDao.saveOrUpdateObj(r);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除店铺
     */
    @Action(value = "delShopStatus")
    public void delShopStatus() {
        initData();
        try {
            String state = request.getParameter("status");
            boolean flag = saveInfoDao.delStatusShop(shop_id, state);

            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshShop(Integer.parseInt(shop_id));
            String reusltMsg = reuslt.getString("msg");

            if (flag) {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功(" + reusltMsg + ")"));
                }
            } else {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除失败(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用失败(" + reusltMsg + ")"));
                }
            }
        } catch (Exception e) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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

    public TbShop getShop() {
        return shop;
    }

    public void setShop(TbShop shop) {
        this.shop = shop;
    }

    public List<Map<String, Object>> getList_admin() {
        return list_admin;
    }

    public void setList_admin(List<Map<String, Object>> list_admin) {
        this.list_admin = list_admin;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public List<Map<String, Object>> getListImg() {
        return listImg;
    }

    public void setListImg(List<Map<String, Object>> listImg) {
        this.listImg = listImg;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
