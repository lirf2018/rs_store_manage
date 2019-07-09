package com.yufan.manage.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbExchangeJurisdiction;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbItemprops;
import com.yufan.pojo.TbPartners;
import com.yufan.pojo.TbPropsValue;
import com.yufan.pojo.TbShop;
import com.yufan.pojo.TbTicket;
import com.yufan.pojo.TbTicketAttribute;
import com.yufan.util.DatetimeUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.TicketCondition;

/**
 * @功能名称 卡券管理
 * @作者 lirongfan
 * @时间 2016年8月11日 上午11:02:45
 */
@Scope("prototype")
@Controller("ticketAction")
@Namespace("/manage")
@ParentPackage("common")
public class TicketAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private int id;//标识
    private String op;//add or update
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private List<Map<String, Object>> listShops;//店铺列表
    private List<Map<String, Object>> listImg;//卡券图片列表
    private List<Map<String, Object>> listClassfy;//分类

    private TbTicket ticket = new TbTicket();
    private String imgPath;
    private List<Map<String, Object>> listPartners;
    private Integer catogryId;
    private Integer leveId;
    private List<Map<String, Object>> listClassfyCatogry;

    /**
     * 加载卡券模板列表
     */
    @Action(value = "loadTicketDataList")
    public void loadTicketDataList() {
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
            TicketCondition t = new TicketCondition();
            String tikcetName = request.getParameter("tikcetName");
            if (null != tikcetName && !"".equals(tikcetName.trim())) {
                t.setTikcetName(tikcetName);
            }
            String isShow = request.getParameter("isShow");
            if (!"".equals(isShow) && !"-1".equals(isShow)) {
                t.setIsShow(Integer.parseInt(isShow));
            }
            String ticketType = request.getParameter("ticketType");
            if (!"".equals(ticketType)) {
                t.setTicketType(Integer.parseInt(ticketType));
            }
            String ticketStatus = request.getParameter("ticketStatus");
            if (!"".equals(ticketStatus)) {
                t.setTicketStatus(Integer.parseInt(ticketStatus));
            }
            String status = request.getParameter("status");
            if (!"".equals(status)) {
                t.setStatus(Integer.parseInt(status));
            }
            String pName = request.getParameter("pName");
            if (null != pName && !"".equals(pName.trim())) {
                t.setpName(pName);
            }
            if (null != catogryId && 0 != catogryId) {
                t.setCatogryId(catogryId);
            }
            if (null != leveId && 0 != leveId) {
                t.setLeveId(leveId);
            }
            String isPutaway = request.getParameter("isPutaway");
            if (null != isPutaway && !"".equals(isPutaway)) {
                t.setIsPutaway(Integer.parseInt(isPutaway));
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            t.setShopId(user.getShopId());

            page = findInfoDao.loadticketPage(page, t);
            page.setExtendsObj(user.getShopId());
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到增加或者修改卡券页面
     *
     * @return
     */
    @Action(value = "toAddTicketPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-ticket.jsp")})
    public String toAddTicketPage() {
        initData();
        try {
//查询商品分类
            listClassfy = findInfoDao.listTbCategoryMap(1, null);
            //查询店铺列表
            TbShop shop = new TbShop();
            shop.setStatus(1);
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
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
            //查询商品规则
            List<Map<String, Object>> listRelRule = new ArrayList<Map<String, Object>>();

            //查询一级分类
            listClassfyCatogry = findInfoDao.listClassfyMap(null);
            listPartners = findInfoDao.listPartnersMap(partners);
            if ("update".equals(op)) {
                String path = RsConstants.phone_url;
                ticket = findInfoDao.loadTicketInfo(id);
                if (null != ticket.getTicketImg() && !"".equals(ticket.getTicketImg().trim())) {
                    imgPath = path + ticket.getTicketImg();
                }
                listImg = findInfoDao.listImgsByReleteIdMap(id, 1);
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

                //查询一级分类
                List<Map<String, Object>> listMap = findInfoDao.listClassfyMap(ticket.getClassifyId());
                if (null != listMap && listMap.size() > 0) {
                    leveId = Integer.parseInt(String.valueOf(listMap.get(0).get("level_id")));
                    //查询类目
                    listClassfy = findInfoDao.listTbCategoryMap(1, leveId);
                }
                //查询商品规则
                listRelRule = findInfoDao.listRelRule(2, ticket.getTikcetId());
            } else {
                request.setAttribute("listImgSize", 0);
                ticket.setStartTime(new Date());
                ticket.setEndTime(new Date());
                ticket.setTicketNum(1);
                ticket.setWeight(0);
                ticket.setLimitNum(0);
                ticket.setOutDate(30);
                ticket.setLimitBeginTime(new Date());
                String time = DatetimeUtil.getDate_lastOrNext("yyyy-MM-dd", DatetimeUtil.getNow("yyyy-MM-dd"), 30);
                ticket.setAppointDate(DatetimeUtil.convertStrToDate(time, "yyyy-MM-dd"));
            }
            request.setAttribute("listRelRule", listRelRule);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 根据分类查询属性和属性值
     */
    @Action(value = "searchTicketClassifyInfosById")
    public void searchTicketClassifyInfosById() {
        try {
            initData();
            String ticketId = request.getParameter("tikcetId");
            //根据卡券标识查询卡券已勾选的属性值
            Map<String, Object> ticketAttributeMap = new HashMap<String, Object>();
            if (null != ticketId && !"".equals(ticketId)) {
                ticketAttributeMap = findInfoDao.listTbTicketAttributeMap(Integer.parseInt(ticketId));
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
                        if (null != ticketAttributeMap && "ok".equals(String.valueOf(ticketAttributeMap.get(valueId)))) {
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
            response.getWriter().write(write_Json_result("ok", obj));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 增加或者修改卡券
     */
    @SuppressWarnings("unused")
    @Action(value = "addOrUpdateTicket")
    public void addOrUpdateTicket() {
        try {
            initData();

            if (null == ticket.getWeight()) {
                ticket.setWeight(0);
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            //获取选择的非销售属性值
            String selected[] = request.getParameterValues("selected");
            String checkbox[] = request.getParameterValues("checkbox");


            String img_strs[] = request.getParameterValues("img_str");//卡券介绍图片
            String pxs[] = request.getParameterValues("px");//卡券排序

            //卡券属性表
            List<TbTicketAttribute> attributeList = new ArrayList<TbTicketAttribute>();
            //卡券图片表
            List<TbImg> imgList = new ArrayList<TbImg>();
            //用来保存卡券属性值key=属性+属性值
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
                        img.setImgClassyfi(1);
                        img.setImgType(1);
                        img.setImgUrl(imgUrl);
                        imgList.add(img);
                    }
                }
            }

            //map处理卡券重复的属性值
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String keys = entry.getKey();
                String key[] = keys.split(";");
                TbTicketAttribute attrbute = new TbTicketAttribute();
                attrbute.setCreatetime(new Timestamp(System.currentTimeMillis()));
                attrbute.setPropId(Integer.parseInt(key[0]));
                attrbute.setValueId(Integer.parseInt(key[1]));
                attributeList.add(attrbute);
            }

            if ("update".equals(op)) {

                TbTicket ticket_ = findInfoDao.loadTicketInfo(ticket.getTikcetId());
                //删除卡券属性表
                saveInfoDao.deleteTicketAttributeByTicketId(ticket.getTikcetId());
                //删除卡券图片表
                saveInfoDao.deleteImgBuRelateId(ticket.getTikcetId(), 1);

                ticket.setCreateman(ticket_.getCreateman());
                ticket.setCreatetime(ticket_.getCreatetime());
                //删除对应商家兑换权限(如果商家对应配置有关联的店铺 那么自动增加兑换权限)
                saveInfoDao.delExchangeId(null, ticket_.getTikcetId(), null);
//				if(ticket_.getPartnersId()!=ticket.getPartnersId()){
//					saveInfoDao.delExchangeRelByTP(ticket.getTikcetId(), ticket_.getPartnersId());
//				}
            } else {
                ticket.setCreateman(user.getLoginName());
                ticket.setCreatetime(new Timestamp(System.currentTimeMillis()));
            }
            ticket.setLastalterman(user.getLoginName());
            ticket.setLastaltertime(new Timestamp(System.currentTimeMillis()));

            //卡券表
            if (null == ticket.getAreaId()) {
                ticket.setAreaId(0);
            }
            boolean flag = saveInfoDao.saveOrUpdateObj(ticket);

            if (flag && ticket.getTikcetId() != null && ticket.getTikcetId() > 0) {
                //卡券属性表
                saveInfoDao.saveTicketAttrbute(attributeList, ticket);
                //卡券图片表
                saveInfoDao.saveImg(imgList, ticket.getTikcetId());

            }

            try {
                //增加对应商家兑换权限(如果商家对应配置有关联的店铺 那么自动增加兑换权限)
                TbExchangeJurisdiction e = new TbExchangeJurisdiction();
                e.setCreateman(user.getLoginName());
                e.setCreatetime(new Timestamp(System.currentTimeMillis()));
                e.setStartTime(new Timestamp(System.currentTimeMillis()));
                e.setValidDate(1);
                e.setTikcetId(ticket.getTikcetId());
                e.setPartnersId(ticket.getPartnersId());
                saveInfoDao.saveObject(e);

            } catch (Exception e) {

            }

            JSONObject obj = new JSONObject();
            String flags = "false";
            String msg = "操作失败";
            if (flag) {
                if ("update".equals(op)) {
                    flags = "ok";
                    msg = "更新成功";
                } else {
                    flags = "ok";
                    msg = "新增成功";
                }
            }
            response.getWriter().write(write_Json_result(flags, msg));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 逻辑删除卡券
     */
    @Action(value = "deleteTicketStatus")
    public void deleteTicketStatus() {
        initData();
        try {

            //如果是店员,不允许删除---店铺成员类型 0:管理员1:店长2店员shop_menber_type
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer shopMenberType = user.getShopMenberType();
            if (null == shopMenberType || shopMenberType == 2) {
                response.getWriter().write(write_Json_result("false", "操作失败,店员不能删除商品"));
                return;
            }

            String status = request.getParameter("status");
            ticket.setStatus(Integer.parseInt(status));
            ticket.setTikcetId(id);
            boolean flag = saveInfoDao.updateTicketStatus(ticket);
            if (flag && "0".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else if (flag && "1".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "启用成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新商品上架状态
     */
    @Action(value = "updateTicketPutaway")
    public void updateTicketPutaway() {
        initData();
        try {
            Integer isPutaway = Integer.parseInt(request.getParameter("isPutaway"));
            ticket = new TbTicket();
            ticket.setIsPutaway(isPutaway);
            ticket.setTikcetId(id);
            boolean flag = saveInfoDao.updateTicketPutaway(ticket);
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "修改成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "修改失败"));
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

    public TbTicket getTicket() {
        return ticket;
    }

    public void setTicket(TbTicket ticket) {
        this.ticket = ticket;
    }

    public List<Map<String, Object>> getListShops() {
        return listShops;
    }

    public void setListShops(List<Map<String, Object>> listShops) {
        this.listShops = listShops;
    }

    public List<Map<String, Object>> getListImg() {
        return listImg;
    }

    public void setListImg(List<Map<String, Object>> listImg) {
        this.listImg = listImg;
    }

    public List<Map<String, Object>> getListClassfy() {
        return listClassfy;
    }


    public void setListClassfy(List<Map<String, Object>> listClassfy) {
        this.listClassfy = listClassfy;
    }


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public List<Map<String, Object>> getListPartners() {
        return listPartners;
    }


    public void setListPartners(List<Map<String, Object>> listPartners) {
        this.listPartners = listPartners;
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


    public List<Map<String, Object>> getListClassfyCatogry() {
        return listClassfyCatogry;
    }


    public void setListClassfyCatogry(List<Map<String, Object>> listClassfyCatogry) {
        this.listClassfyCatogry = listClassfyCatogry;
    }

}
