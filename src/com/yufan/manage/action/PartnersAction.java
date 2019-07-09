package com.yufan.manage.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.*;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.ImageAction;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbPartners;
import com.yufan.pojo.TbShop;
import com.yufan.vo.PartnersCondition;

/**
 * @功能名称 商家管理
 * @作者 lirongfan
 * @时间 2016年9月8日 上午9:29:23
 */
@Scope("prototype")
@Service("partnersAction")
@Namespace("/manage")
@ParentPackage("common")
public class PartnersAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer id;// 标识
    private String op;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbPartners partners = new TbPartners();
    private List<Map<String, Object>> listShop = new ArrayList<Map<String, Object>>();

    private String photoPath;
    private File file;

    /**
     * 加载商家列表
     */
    @Action(value = "loadPartnersListPage")
    public void loadPartnersListPage() {
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

            PartnersCondition p = new PartnersCondition();
            String pname = request.getParameter("pname");
            if (null != pname && !"".equals(pname) && !"null".equals(pname)) {
                p.setPartnersName(pname);
            }
            String pcode = request.getParameter("pcode");
            if (null != pcode && !"".equals(pcode) && !"null".equals(pcode)) {
                p.setPartnersCode(pcode);
            }
            String sname = request.getParameter("sname");
            if (null != sname && !"".equals(sname) && !"null".equals(sname)) {
                p.setShopName(sname);
            }
            String status = request.getParameter("status");
            if (null != status && !"".equals(status)) {
                p.setStatus(Integer.parseInt(status));
            }

            page = findInfoDao.loadPartnersPage(page, p);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改页面
     *
     * @return
     */
    @Action(value = "toAddorUpdatePartnersPage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-partners.jsp")})
    public String toAddorUpdatePartnersPage() {
        initData();
        try {
            TbShop shop = new TbShop();
            shop.setStatus(1);
            //查询商品规则
            List<Map<String, Object>> listRelRule = new ArrayList<Map<String, Object>>();
            photoPath = RsConstants.phone_url + "null.jpg";
            listShop = findInfoDao.listShopMap(shop);
            if ("update".equals(op)) {
                partners = findInfoDao.loadTbPartnersByIdInfo(id);
                if (partners.getPartnersImg() != null && !"".equals(partners.getPartnersImg())) {
                    photoPath = RsConstants.phone_url + partners.getPartnersImg();
                }
                //查询商品规则
                listRelRule = findInfoDao.listRelRule(0, partners.getId());
            } else {
                //SecretKey如果没绑定账号,则由管理员生成,绑定后,可以自己后端生成
//				String uuid = UUIDGenerator.getUUID();
//				partners.setSecretKey(uuid);
                partners.setPartnersSort(0);
            }
            request.setAttribute("listRelRule", listRelRule);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 增加或者修改商家
     */
    @Action(value = "addOrUpdatePartners")
    public void addOrUpdatePartners() {
        initData();
        try {
            boolean flag = false;
            //验证商家编码是否已经存在
            flag = findInfoDao.checkPartnersCode(partners.getId(), partners.getPartnersCode());
            if (flag) {
                response.getWriter().write(write_Json_result("false", "商家编码已存在"));
                return;
            }
            //验证商家账号是否已经存在
            flag = findInfoDao.checkPartnersAccount(partners.getId(), partners.getAccount());
            if (flag) {
                response.getWriter().write(write_Json_result("false", "账号已存在"));
                return;
            }

            //一个商家只能关联一个店铺,一个店铺也只能关联一个商家
            if (null != partners.getShopId() && 0 != partners.getShopId()) {
                boolean f = findInfoDao.checkPartnersTicket(partners.getShopId(), partners.getId());
                if (f) {
                    response.getWriter().write(write_Json_result("false", "增加失败:所关联的店铺已被关联其它商家"));
                    return;
                }
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(op)) {// 增加
                partners.setCreatetime(new Timestamp(System.currentTimeMillis()));
                partners.setCreateman(user.getLoginName());

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        partners.setPartnersImg(imgpath);
                    }
                }
                String passwd = partners.getPasswd();
                partners.setPasswdShow(passwd);
                partners.setPasswd(MD5.enCodeStandard(passwd));

                flag = saveInfoDao.saveOrUpdateObj(partners);
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParters(id);
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败(" + reusltMsg + ")"));
                }
            } else {// 修改
                TbPartners p = findInfoDao.loadTbPartnersByIdInfo(partners.getId());
                partners.setCreateman(p.getCreateman());
                partners.setCreatetime(p.getCreatetime());
                partners.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                partners.setLastalterman(user.getLoginName());

                String passwd = partners.getPasswd();//得到页面上的密码
                if (!MD5.enCodeStandard(p.getPasswdShow()).equals(passwd)) {
                    partners.setPasswdShow(passwd);
                    partners.setPasswd(MD5.enCodeStandard(passwd));
                } else {
                    partners.setPasswdShow(p.getPasswdShow());
                }

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        partners.setPartnersImg(imgpath);
                        ImageUtil.getInstance().removePathImg(p.getPartnersImg(), request);
                    }
                } else {
                    partners.setPartnersImg(p.getPartnersImg());
                }

                flag = saveInfoDao.saveOrUpdateObj(partners);
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParters(id);
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败(" + reusltMsg + ")"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除商家
     */
    @Action(value = "delPartnersStatus")
    public void delPartnersStatus() {
        initData();
        try {
            String status = request.getParameter("status");

            boolean flag = saveInfoDao.updatePartnersStatus(id, Integer.parseInt(status));
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParters(id);
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功(" + reusltMsg + ")"));
                }
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败(" + reusltMsg + ")"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除商家与店铺关联
     */
    @Action(value = "delPartnersShopRel")
    public void delPartnersShopRel() {
        try {
            initData();
            partners = findInfoDao.loadTbPartnersByIdInfo(id);
            partners.setShopId(0);
            boolean flag = false;
            flag = saveInfoDao.saveOrUpdateObj(partners);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParters(id);
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "解除成功(" + reusltMsg + ")"));
            } else {
                response.getWriter().write(write_Json_result("false", "解除失败(" + reusltMsg + ")"));
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public TbPartners getPartners() {
        return partners;
    }

    public void setPartners(TbPartners partners) {
        this.partners = partners;
    }

    public List<Map<String, Object>> getListShop() {
        return listShop;
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

    public void setListShop(List<Map<String, Object>> listShop) {
        this.listShop = listShop;
    }
}
