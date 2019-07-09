package com.yufan.core.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.util.DatetimeUtil;
import com.yufan.util.MD5;
import com.yufan.util.MyLog;

/**
 * @功能名称 用户登录
 * @作者 lirongfan
 * @时间 2016年4月22日 下午3:50:18
 */
@Scope("prototype")
@Service("loginAction")
@Namespace("/login")
@ParentPackage("common")
public class LoginAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;
    private String login_name;// 登录名称
    private String login_pass;// 登录密码
    private String checkcode;// 验证码


    /**
     * 登录验证
     */
    @Action(value = "userLogin")
    public void userLogin() {
        try {
            initData();

            String validCode = (String) request.getSession().getAttribute("valideCode");//验证码
            if (this.checkcode != null && !this.checkcode.equalsIgnoreCase(validCode)) {
                response.getWriter().write(write_Json_result("false", "验证码不正确"));
                return;
            }

            //查询用户角色是否出现问题
//			TbAdmin admin=new TbAdmin();
            List<Map<String, Object>> userl = findInfoDao.loadTbAdminList(login_name);
            if (userl == null || userl.size() != 1) {
                response.getWriter().write(write_Json_result("false", "账号异常或者不存在，请联系管理员处理"));
                return;
            }
            Map<String, Object> map = userl.get(0);
            //验证用户状态
            String status = map.get("status").toString();
            //--0无效1有效2冻结(管理员)3删除(管理员)
            if ("0".equals(status)) {
                response.getWriter().write(write_Json_result("false", "账号无效，请联系管理员处理"));
                return;
            } else if ("2".equals(status)) {
                response.getWriter().write(write_Json_result("false", "账号已冻结，请联系管理员处理"));
                return;
            } else if ("3".equals(status)) {
                response.getWriter().write(write_Json_result("false", "账号已删除，请联系管理员处理"));
                return;
            }

            //如果是第三方账号,是否关联了商家(第三方账号必须关联商家)
            Integer shopId = Integer.parseInt(String.valueOf(map.get("shop_id")));
            if (shopId != 0) {
                String isOutShop = map.get("is_out_shop").toString();
                String pId = map.get("is_out_shop").toString();//商家标识
                if ("2".equals(isOutShop) && "0".equals(pId)) {//第三方店铺
                    LOG.info("------>第三方店铺账号必须关联商家");
                    response.getWriter().write(write_Json_result("false", "账号异常，请联系管理员处理"));
                    return;
                }
            }

            //是否验证
            String isMakeSure = map.get("is_make_sure").toString();
            if (StringUtils.isEmpty(isMakeSure) || "0".equals(isMakeSure)) {
                response.getWriter().write(write_Json_result("false", "账号未验证，请联系管理员处理"));
                return;
            }

            //验证用户角色是否正确
            String roleStatus = String.valueOf(map.get("role_status"));
            if (!"1".equals(roleStatus)) {
                MyLog.getLogger().info("---->验证用户角色失败,loginName=" + login_name);
                response.getWriter().write(write_Json_result("false", "账号异常，请联系管理员处理"));
                return;
            }
            //验证用户是否过期
            String memberEndtime = map.get("member_endtime").toString();
            String nowDate = DatetimeUtil.getNow("yyyy-MM-dd");
            if (!"admin".equals(login_name) && DatetimeUtil.compareDate(nowDate, memberEndtime) > 0) {
                response.getWriter().write(write_Json_result("false", "账号已过期，请联系管理员处理"));
                return;
            }
            //验证用户密码
            String passwd = String.valueOf(map.get("login_password"));
            String nowPasswd = MD5.enCodeStandard(login_pass + login_name);
            if (!passwd.equals(nowPasswd)) {
                response.getWriter().write(write_Json_result("false", "账号或密码有误，请确认后重新登录"));
                return;
            }

            Integer adminId = Integer.parseInt(map.get("admin_id").toString());
            //更新登录时间
            saveInfoDao.updateLoinTime(adminId);
            //设置登录人信息
            TbAdmin admin = new TbAdmin();
            admin.setAdminId(adminId);
            admin.setUserName(String.valueOf(map.get("user_name")));
            admin.setLoginName(login_name);
            admin.setNickName(String.valueOf(map.get("nick_name")));
            admin.setStatus(Integer.parseInt(status));
            admin.setShopId(shopId);
            admin.setShopMenberType(Integer.parseInt(String.valueOf(map.get("shop_menber_type"))));
            admin.setLoginPassword(nowPasswd);
            request.getSession().setAttribute("user", admin);
            response.getWriter().write(write_Json_result("ok", "ok"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Action(value = "exitWeb")
    public String exitWeb() {
        initData();
        try {
            Enumeration em = request.getSession().getAttributeNames();
            while (em.hasMoreElements()) {
                request.getSession().removeAttribute(String.valueOf(em.nextElement()));
            }
//		return "success";
            String path = request.getSession().getServletContext().getRealPath("/");
            response.sendRedirect("/rs_store_manage/");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载登录菜单
     */
    @Action(value = "loadMenus")
    public void loadMenus() {
        List<Map<String, Object>> listMenu = new ArrayList<Map<String, Object>>();
        try {
            initData();

            TbAdmin admin = new TbAdmin();
            admin = (TbAdmin) request.getSession().getAttribute("user");
            if (null == admin) {
                response.getWriter().write(write_Json_result("false", null));
            } else {
                //根据用户角色得到菜单
                listMenu = findInfoDao.listMenusMap(admin.getAdminId());
                response.getWriter().write(write_Json_result("ok", listMenu));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到登录人信息
     */
    @Action(value = "getLoginInfo")
    public void getLoginInfo() {
        try {
            initData();
            TbAdmin user = new TbAdmin();
            user = (TbAdmin) request.getSession().getAttribute("user");
            TbAdmin admin = new TbAdmin();
            admin.setLoginName(user.getLoginName());
            admin.setUserName(user.getUserName());
            admin.setLoginPassword(user.getLoginPassword());
            admin.setLastlogintime(user.getLastlogintime());
            response.getWriter().write(write_Json_result("ok", admin));
        } catch (IOException e) {
            e.printStackTrace();
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


    public String getLogin_name() {
        return login_name;
    }


    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }


    public String getLogin_pass() {
        return login_pass;
    }


    public void setLogin_pass(String login_pass) {
        this.login_pass = login_pass;
    }


    public String getCheckcode() {
        return checkcode;
    }


    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

}
