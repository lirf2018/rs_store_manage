package com.yufan.core.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yufan.pojo.TbUserInfo;
import org.apache.commons.lang3.StringUtils;
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
import com.yufan.pojo.TbShop;
import com.yufan.util.MD5;
import com.yufan.util.NumberUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.AdminCondition;

/**
 * 功能名称: 用户管理
 * 开发人: lirf
 * 开发时间: 2016下午12:52:58
 * 其它说明：
 */
@Scope("prototype")
@Service("userAction")
@Namespace("/user")
@ParentPackage("common")
public class UserAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private TbAdmin admin = new TbAdmin();
    private String handleType;//add or update scan
    private String admin_id;//用户标识

    private String photoPath;

    private File file;

    private List<Map<String, Object>> listShop = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listRole = new ArrayList<Map<String, Object>>();


    /**
     * 查询条件
     */
    private String login_name;
    private String user_name;
    private String shop_id;
    private String shop_name;
    private String phone;
    private String idcard;
    private String role_id;
    private String status;

    /**
     * 加载管理员列表
     *
     * @return
     */
    @Action(value = "loadadminlist")
    public void loadadminlist() {
//		MyLog.getLogger().info("---->查询");
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


            AdminCondition bean = new AdminCondition();
            bean.setLoginName(login_name);
            bean.setUserName(user_name);
            bean.setShopId(shop_id);
            bean.setShopName(shop_name);
            bean.setPhone(phone);
            bean.setIdcard(idcard);
            bean.setRoleId(role_id);
            bean.setStatus(status);

            String isMakeSure = request.getParameter("isMakeSure");
            if (null != isMakeSure) {
                bean.setIsMakeSure(Integer.parseInt(isMakeSure));
            }

            page = findInfoDao.listAdmin(page, bean);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 跳转到增加或者修改管理员页面
     *
     * @return
     */
    @Action(value = "toAddAdminPage",
            results = {@Result(name = "success", location = "/jsp/sys/addorupdate-admin.jsp")})
    public String toAddAdminPage() {
        initData();
        TbShop shop = new TbShop();
        shop.setStatus(1);
        listShop = findInfoDao.listShopMap(shop);
//		listRole=findInfoDao.listRoleMap("1");
        photoPath = RsConstants.phone_url + "null.jpg";
        if ("add".equals(handleType)) {
//			admin.setStatus(1);
        } else if ("update".equals(handleType)) {
            admin = findInfoDao.loadAdminInfo(Integer.parseInt(admin_id));
            if (null != admin.getPhoto() && !"".equals(admin.getPhoto())) {
                photoPath = RsConstants.phone_url + admin.getPhoto();
            }
        }
        return "success";
    }

    /**
     * 增加或者修改管理员
     */
    @Action(value = "addOrUpdateadmin")
    public void addOrUpdateadmin() {
        initData();
        try {
            int id = 0;
            if (null != admin.getAdminId()) {
                id = admin.getAdminId();
            }
            if (findInfoDao.checkLoginName(id, admin.getLoginName())) {
                response.getWriter().write(write_Json_result("false", "登录名称已存在!"));
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {//增加管理员
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        admin.setPhoto(imgpath);
                    }
                }
                admin.setCreatetime(new Timestamp(System.currentTimeMillis()));
                admin.setCreateman(user.getLoginName());
                String passwd = admin.getLoginPassword();
                if (null == passwd || "".equals(passwd)) {
                    passwd = NumberUtil.genRandomNum(6);
                }
                admin.setPasswordHide(passwd);
                admin.setLoginPassword(MD5.enCodeStandard(passwd + admin.getLoginName()));
                boolean flag = saveInfoDao.addAdmin(admin);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
                return;
            } else {//修改
                TbAdmin admin_ = findInfoDao.loadAdminInfo(admin.getAdminId());
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        admin.setPhoto(imgpath);
                    }
                } else {
                    admin.setPhoto(admin_.getPhoto());
                }
                admin.setLastalterman(user.getLoginName());
                admin.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                admin.setCreatetime(admin_.getCreatetime());
                admin.setCreateman(admin_.getCreateman());
                String passw = admin.getLoginPassword();//得到页面的密码
                if (admin_.getLoginPassword().equals(passw)) {
                    admin.setLoginPassword(admin_.getLoginPassword());
                    admin.setPasswordHide(admin_.getPasswordHide());
                } else {
                    admin.setLoginPassword(MD5.enCodeStandard(passw + admin_.getLoginName()));
                    admin.setPasswordHide(passw);
                }
                admin.setWxOpenid(admin_.getWxOpenid());
                admin.setIsMakeSure(0);
                boolean flag = saveInfoDao.addAdmin(admin);

                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "更新失败"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改个人密码
     */
    @Action(value = "updatePassword")
    public void updatePassword() {
        initData();
        TbAdmin user_ = (TbAdmin) request.getSession().getAttribute("user");
        TbAdmin admin = findInfoDao.loadAdminInfo(user_.getAdminId());
        String oldPass = request.getParameter("opassword");
        try {
            if (!MD5.enCodeStandard(oldPass + admin.getLoginName()).equals(admin.getLoginPassword())) {
                response.getWriter().write(write_Json_result("false", "原密码不正确"));
                return;
            }
            String newPass = request.getParameter("npassword");
            admin.setPasswordHide(newPass);
            admin.setLoginPassword(MD5.enCodeStandard(newPass + admin.getLoginName()));
            admin.setIsMakeSure(0);
            boolean flag = saveInfoDao.addAdmin(admin);
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
     * 删除或者启用管理员(逻辑删除)
     */
    @Action(value = "delOrUpdateAdmin")
    public void delOrUpdateAdmin() {
        initData();
        try {
            String state = request.getParameter("status");
            String admin_id = request.getParameter("admin_id");
            boolean flag = saveInfoDao.deleteOrUseAdmin(admin_id, state);
            if (flag) {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除失败"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用失败"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载角色列表
     */
    @Action(value = "getListRole")
    public void getListRole_() {
        try {
            initData();
            listRole = findInfoDao.listRoleMap("1");
            response.getWriter().write(write_Json_result("msg", listRole));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //****************************************************wap用户管理*******************************************

    /**
     * 查询wap用户
     */
    @Action(value = "loadWapUserlist")
    public void loadWapUserlist() {
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

            TbUserInfo userInfo = new TbUserInfo();
            String loginName = request.getParameter("loginName");
            userInfo.setLoginName(loginName);
            String nickName = request.getParameter("nickName");
            userInfo.setNickName(nickName);
            String userEmail = request.getParameter("userEmail");
            userInfo.setUserEmail(userEmail);
            String userMobile = request.getParameter("userMobile");
            userInfo.setUserMobile(userMobile);
            String userId = request.getParameter("userId");
            if (StringUtils.isNotEmpty(userId)) {
                userInfo.setUserId(Integer.parseInt(userId.trim()));
            }
            String userStatus = request.getParameter("userStatus");
            if (StringUtils.isNotEmpty(userStatus)) {
                userInfo.setUserState(Integer.parseInt(userStatus));
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String uid = request.getParameter("uid");
            if (StringUtils.isNotEmpty(uid)) {
                paramMap.put("uid", uid.trim());
            }
            page.setParamMap(paramMap);
            page = findInfoDao.loadWapUserPage(page, userInfo);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户绑定信息列表
     */
    @Action(value = "loadWapUserSnslistMap")
    public void loadWapUserSnslistMap() {
        try {
            initData();
            int userId = Integer.parseInt(request.getParameter("userId"));
            List<Map<String, Object>> list = findInfoDao.loadWapUserSnsListMap(userId);
            response.getWriter().write(write_Json_result("msg", list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改wap用户状态
     */
    @Action(value = "updateWapStatus")
    public void updateWapStatus() {
        initData();
        try {
            int status = Integer.parseInt(request.getParameter("status"));
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean flag = saveInfoDao.updateWapUserStatus(userId, status);
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "操作成功"));
            } else {
                response.getWriter().write(write_Json_result("ok", "操作失败"));
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

    public TbAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(TbAdmin admin) {
        this.admin = admin;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
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

    public List<Map<String, Object>> getListShop() {
        return listShop;
    }

    public void setListShop(List<Map<String, Object>> listShop) {
        this.listShop = listShop;
    }

    public List<Map<String, Object>> getListRole() {
        return listRole;
    }

    public void setListRole(List<Map<String, Object>> listRole) {
        this.listRole = listRole;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void main(String[] args) {
        System.out.println(MD5.enCodeStandard("123456lirf"));
    }
}
