package com.yufan.core.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.yufan.pojo.TbRoleFunction;

/**
 * 功能名称: 角色功能管理 开发人: lirf 开发时间: 2016下午12:16:17 其它说明：
 */
@Scope("prototype")
@Service("roleFunctionAction")
@Namespace("/rolefunction")
@ParentPackage("common")
public class RoleFunctionAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;


    private String role_id;
    private String function_ids;// 功能id用,组合

    private List<Map<String, Object>> listRoles = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listFunctions = new ArrayList<Map<String, Object>>();

    /**
     * 加载角色列表
     */
    @Action(value = "getListRole_")
    public void getListRole_() {
        try {
            initData();
            listRoles = findInfoDao.listRoleMap("1");
            response.getWriter().write(write_Json_result("listRole", listRoles));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载功能列表
     */
    @Action(value = "getListFunctions_")
    public void getListFunctions_() {
        try {
            initData();
            if (null == role_id || "null".equals(role_id) || "".equals(role_id)) {
                role_id = "0";
            }
            listFunctions = findInfoDao.listFunctionsMap(role_id);
            response.getWriter().write(write_Json_result("listFunctions", listFunctions));
            // System.out.println(JSONObject.toJSONString(listFunctions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给角色授权
     */
    @Action(value = "addOrupdateRoleFunction")
    public void addOrupdateRoleFunction() {
        try {
            initData();
            String[] function_id = function_ids.split(",");
            // 删除相应角色的功能权限
            boolean f = saveInfoDao.delRoleFunction(role_id);
            if ("".equals(function_ids) && f) {
                response.getWriter().write(write_Json_result("ok", "授权成功"));
                return;
            }
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            for (int i = 0; i < function_id.length; i++) {
                TbRoleFunction role_function = new TbRoleFunction();
                role_function.setCreatetime(new Timestamp(System.currentTimeMillis() + i));
                role_function.setCreateman(user.getLoginName());
                role_function.setRoleId(Integer.parseInt(role_id));
                role_function.setFunctionId(Integer.parseInt(function_id[i]));
                flag = saveInfoDao.addRoleFunction(role_function);
            }
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "授权成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "授权失败"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除相应角色的功能权限
     */
    public void delRoleFunction() {
        saveInfoDao.delRoleFunction(role_id);
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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getFunction_ids() {
        return function_ids;
    }

    public void setFunction_ids(String function_ids) {
        this.function_ids = function_ids;
    }

    public List<Map<String, Object>> getListRoles() {
        return listRoles;
    }

    public void setListRoles(List<Map<String, Object>> listRoles) {
        this.listRoles = listRoles;
    }

    public List<Map<String, Object>> getListFunctions() {
        return listFunctions;
    }

    public void setListFunctions(List<Map<String, Object>> listFunctions) {
        this.listFunctions = listFunctions;
    }
}
