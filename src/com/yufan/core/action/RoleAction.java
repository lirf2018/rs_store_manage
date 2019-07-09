package com.yufan.core.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbRole;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 角色管理
 * @作者 lirongfan
 * @时间 2016年4月25日 下午1:37:28
 */
@Scope("prototype")
@Service("roleAction")
@Namespace("/role")
@ParentPackage("common")
public class RoleAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private TbRole role = new TbRole();
    private String handleType;//add or update
    private String role_id;//角色标识

    /**
     * 查询条件
     */
    private String roleName;
    private String state;

    /**
     * 加载角色列表
     *
     * @return
     */
    @Action(value = "loadrolelist")
    public void loadrolelist() {
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
            role.setRoleName(roleName);
            if (null != state && !"".equals(state)) {
                role.setStatus(Integer.parseInt(state));
            }
            page = findInfoDao.loadrolelist(role, page);
//			handdleRole();
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handdleRole() {
        try {
            List<Map<String, Object>> list_role_type = findInfoDao.listParamMap("member_type");
            if (null != list_role_type && list_role_type.size() > 0 && page != null && null != page.getList() && page.getList().size() > 0) {
                List<Object> list = new ArrayList<Object>();
                for (Object object : page.getList()) {
                    Object[] obj = (Object[]) object;
                    //得到倒数2位role_type
                    String l_value_2 = obj[obj.length - 2].toString();
                    //得到倒数1位param_value
                    String param_value_ = "0";
                    for (int i = 0; i < list_role_type.size(); i++) {
                        String param_key = list_role_type.get(i).get("param_key").toString();
                        String param_value = list_role_type.get(i).get("param_value").toString();
                        if (l_value_2.equals(param_key)) {
                            param_value_ = param_value;
                        }
                    }
                    obj[obj.length - 1] = param_value_;
                    list.add(obj);
                }
                page.setList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加或者修改角色
     */
    @Action(value = "addOrUpdateRole")
    public void addOrUpdateRole() {
        initData();
        try {

            int id = 0;
            if (null != role.getRoleId()) {
                id = role.getRoleId();
            }

            if (findInfoDao.checkRoleCode(id, role.getRoleCode())) {
                response.getWriter().write(write_Json_result("false", "角色编码已存在!"));
                return;
            }
            if (findInfoDao.checkRoleName(id, role.getRoleName())) {
                response.getWriter().write(write_Json_result("false", "角色名称已存在!"));
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {//增加角色
                role.setCreatetime(new Timestamp(System.currentTimeMillis()));
                role.setCreateman(user.getLoginName());
                role.setRoleParentid(0);
                role.setStatus(1);
                boolean flag = saveInfoDao.addOrUpdateRole(role);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                }
                return;
            } else {//修改
                TbRole role_ = findInfoDao.loadRoleInfo(role.getRoleId());
                role.setCreateman(role_.getCreateman());
                role.setCreatetime(role_.getCreatetime());
                role.setRoleParentid(0);
                role.setStatus(1);
                role.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                role.setLastalterman(user.getLoginName());
                boolean flag = saveInfoDao.addOrUpdateRole(role);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除或者启用角色(逻辑删除)
     */
    @Action(value = "delOrUpdateRole")
    public void delOrUpdateRole() {
        initData();
        try {
            String roleid = request.getParameter("roleid");
            String state = request.getParameter("status");
            boolean flag = saveInfoDao.delOrUpdateRole(roleid, state);
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

    @Action(value = "qureyParam_role")
    public void qureyParam() {
        try {
            initData();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list = findInfoDao.listParamMap("member_type");
            response.getWriter().write(write_Json_result("msg", list));
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

    public TbRole getRole() {
        return role;
    }

    public void setRole(TbRole role) {
        this.role = role;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
