package com.yufan.core.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbUserRole;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.AdminRoleCondition;

/**
 * 功能名称: 用户角色管理
 * 开发人: lirf
 * 开发时间: 2016下午8:26:10
 * 其它说明：
 */
@Scope("prototype")
@Service("adminRoleAction")
@Namespace("/adminrole")
@ParentPackage("common")
public class AdminRoleAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private TbUserRole admin_role = new TbUserRole();
    private String handleType;//add or update
    private String admin_role_id;//用户角色标识
    private String admin_id;//
    private String admin_ids;//用户标识(多个用,隔开)

    private List<Map<String, Object>> listRole = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listAdmin = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listShop = new ArrayList<Map<String, Object>>();

    /**
     * 查询条件
     */
    private String user_name;//用户名称
    private String login_name;//登录名称
    private String role_id;//角色标识

    /**
     * 跳转到管理员角色页面
     *
     * @return
     */
    @Action(value = "toAddAdminRolePage", results = {
            @Result(name = "success", location = "/jsp/sys/addorupdate-adminrole.jsp")})
    public String toAddAdminRolePage() {
        initData();
        listRole = findInfoDao.listRoleMap("1");
        System.out.println(JSONObject.toJSON(listRole));
        return "success";
    }

    /**
     * 增加用户角色的时候加载的用户列表
     */
    @Action(value = "addadminRole_adminlist")
    public void addadminRole_adminlist() {
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

            AdminRoleCondition bean = new AdminRoleCondition();
            bean.setRoleId(role_id);
            bean.setUserName(user_name);
            bean.setLoginName(login_name);

            page = findInfoDao.addAdminRoleAdminList(page, bean);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 加载用户角色列表
     *
     * @return
     */
    @Action(value = "loadadminrolelist")
    public void loadadminrolelist() {
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

            AdminRoleCondition bean = new AdminRoleCondition();
            bean.setRoleId(role_id);
            bean.setUserName(user_name);
            bean.setLoginName(login_name);

            page = findInfoDao.listAdminRole(page, bean);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 增加或者修改用户角色
     */
    @Action(value = "addOrUpdateAdminRole")
    public void addOrUpdateAdminRole() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {//增加用户角色
                String admin_id[] = admin_ids.split(",");
                boolean flag = false;
                int r = Integer.parseInt(role_id);
                for (int i = 0; i < admin_id.length; i++) {
                    admin_role = new TbUserRole();
                    admin_role.setCreateman(user.getLoginName());
                    admin_role.setCreatetime(new Timestamp(i + System.currentTimeMillis()));
                    admin_role.setAdminId(Integer.parseInt(admin_id[i]));
                    admin_role.setRoleId(r);
                    flag = saveInfoDao.addOrUpdateAdminRole(admin_role);
                }

                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
                return;
            } else {//修改
                admin_role.setRoleId(Integer.parseInt(role_id));
                admin_role.setAdminId(Integer.parseInt(admin_id));
                admin_role.setCreateman(user.getLoginName());
                admin_role.setCreatetime(new Timestamp(System.currentTimeMillis()));
                boolean flag = saveInfoDao.addOrUpdateAdminRole(admin_role);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除或者启用用户角色(物理删除)
     */
    @Action(value = "deleteAdminRole")
    public void deleteAdminRole() {
        initData();
        try {
            boolean flag = saveInfoDao.deleteAdminRole(admin_role_id);
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载角色列表
     */
    @Action(value = "getListRoleAdminRole")
    public void getListRoleAdminRole() {
        try {
            initData();
            listRole = findInfoDao.listRoleMap(null);
            response.getWriter().write(write_Json_result("listRole", listRole));
            System.out.println(write_Json_result("listRole", listRole));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载用户列表
     */
//	public void listAdmin(){
//		try {
//			initData();
//			String shop_id=request.getParameter("shop_id");
//			listAdmin=sysTemAdminService.listAdmin(null,shop_id,login_name,user_name);
//			response.getWriter().write(write_Json_result("listAdmin", listAdmin));
//			System.out.println(write_Json_result("listAdmin", listAdmin));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * 加载店铺列表
     */
    @Action(value = "listShopAdminRole")
    public void listShopAdminRole() {
        try {
            initData();
            listShop = findInfoDao.listShopMap(null);
            response.getWriter().write(write_Json_result("listShop", listShop));
            System.out.println(write_Json_result("listShop", listShop));
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


    public TbUserRole getAdmin_role() {
        return admin_role;
    }


    public void setAdmin_role(TbUserRole admin_role) {
        this.admin_role = admin_role;
    }


    public String getHandleType() {
        return handleType;
    }


    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }


    public String getAdmin_role_id() {
        return admin_role_id;
    }


    public void setAdmin_role_id(String admin_role_id) {
        this.admin_role_id = admin_role_id;
    }


    public String getAdmin_id() {
        return admin_id;
    }


    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }


    public String getAdmin_ids() {
        return admin_ids;
    }


    public void setAdmin_ids(String admin_ids) {
        this.admin_ids = admin_ids;
    }


    public List<Map<String, Object>> getListRole() {
        return listRole;
    }


    public void setListRole(List<Map<String, Object>> listRole) {
        this.listRole = listRole;
    }


    public List<Map<String, Object>> getListAdmin() {
        return listAdmin;
    }


    public void setListAdmin(List<Map<String, Object>> listAdmin) {
        this.listAdmin = listAdmin;
    }


    public List<Map<String, Object>> getListShop() {
        return listShop;
    }


    public void setListShop(List<Map<String, Object>> listShop) {
        this.listShop = listShop;
    }


    public String getUser_name() {
        return user_name;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getLogin_name() {
        return login_name;
    }


    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }


    public String getRole_id() {
        return role_id;
    }


    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
