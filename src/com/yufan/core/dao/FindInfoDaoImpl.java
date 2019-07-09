package com.yufan.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yufan.pojo.*;
import com.yufan.vo.OrderCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.dao.CommonDAO;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.ActivityCondition;
import com.yufan.vo.AdminCondition;
import com.yufan.vo.AdminRoleCondition;
import com.yufan.vo.BannelCondition;
import com.yufan.vo.CatogryLeve1Condition;
import com.yufan.vo.ClassfyCondition;
import com.yufan.vo.GoodsCondition;
import com.yufan.vo.PartnersCondition;
import com.yufan.vo.RuleCondition;
import com.yufan.vo.ShopCondition;
import com.yufan.vo.TicketCondition;

/**
 * @功能名称 数据查询公共dao接口实现
 * @作者 lirongfan
 * @时间 2015年10月29日 下午2:34:59
 */
@Scope("prototype")
@Service("findInfoDao")
public class FindInfoDaoImpl implements FindInfoDao {
    @Resource(name = "commonDao")
    private CommonDAO commonDao;
    private final String path = RsConstants.phone_url;

    /**
     * 加载微信菜单列表
     */
    @Override
    public List<Map<String, Object>> listWeixinMenuMap(String menu_parent, String status, String key_code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT w.menu_id,w.menu_name,w.menu_type,w.menu_parent,w.menu_index, ");
        sql.append(" if((w.menu_foward is null or w.menu_foward='') and p.param_value1='view','http://',w.menu_foward) as menu_foward, ");
        sql.append(" w.menu_foward,w.status,w.remark,p.param_value1,w.menu_key from tb_weixin_menu w  ");
        sql.append(" LEFT JOIN tb_param p on (w.menu_type=p.param_value and p.param_code='weixin_button_type' and p.is_make_sure=1) where 1=1  ");
        if (null != menu_parent && !"".equals(menu_parent) && !"null".equals(menu_parent)) {
            sql.append(" and w.menu_parent=" + menu_parent + " ");
        }
        if (null != status && !"".equals(status) && !"null".equals(status)) {
            sql.append(" and w.status=" + status + " ");
        }
        sql.append(" and w.key_code='" + key_code + "' ");
        sql.append("  ORDER BY w.menu_index,w.createtime desc ");
        if ("0".equals(menu_parent)) {
            sql.append("  limit 0,3 ");
        } else {
            sql.append("  limit 0,5 ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询菜单by id
     *
     * @param id
     * @return
     */
    @Override
    public TbWeixinMenu loadTbWeixinMenuInfo(int id) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from TbWeixinMenu where 1=1 ");
        hql.append(" and id=" + id + " ");
        return (TbWeixinMenu) commonDao.loadObj(hql.toString());
    }


    /**
     * 查询是否存在有效的access_token
     *
     * @param combineCode :AppID+";"+AppSecret的组合
     * @return
     */
    public String getAccessTokenByCode(String combineCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT access_token from tb_weixin_access_token where combine_code=? and DATE_FORMAT(NOW(),'%Y-%m-%d %T')<DATE_FORMAT(expiry_date,'%Y-%m-%d %T') and status = 1 ");
        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString(), combineCode);
        if (list != null && list.size() == 1) {
            String accessToen = String.valueOf(list.get(0).get("access_token"));
            return accessToen;
        }
        return null;
    }

    /**
     * 查询登录用户信息
     *
     * @param loginName
     * @return
     */
    @Override
    public TbAdmin loadTbAdminInfo(String loginName) {
        String hql = " from TbAdmin where loginName='" + loginName + "' and status=1 ";

        if (null == checkUserRole(loginName)) {
            return null;
        }
        return (TbAdmin) commonDao.loadObj(hql);
    }

    public List<Map<String, Object>> loadTbAdminList(String loginName) {
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT u.admin_id,u.login_name,u.login_password,u.user_name,u.birthday,u.sex,if(u.sex=0,'女','男') as sex_name,u.nick_name,u.idcard,u.phone,u.email,u.qq, ");
        sql.append("  DATE_FORMAT(u.member_begintime,'%Y-%m-%d') as member_begintime,DATE_FORMAT(u.member_endtime,'%Y-%m-%d') as member_endtime,  ");
        sql.append("  u.shop_id,u.shop_menber_type,u.status,u.createtime,u.remark,r.role_id,r.role_name,r.`status` role_status,s.shop_name,u.is_make_sure, ");
        sql.append("  ifnull(p.id,0) as partners_id,s.is_out_shop ");
        sql.append("  from tb_admin u LEFT JOIN tb_user_role ur on ur.admin_id=u.admin_id LEFT JOIN tb_role r on r.role_id=ur.role_id  ");
        sql.append("  LEFT JOIN tb_shop s on s.shop_id=u.shop_id  ");
        sql.append("  LEFT JOIN tb_partners p on p.shop_id=s.shop_id and p.`status`=1 ");
        sql.append("  where 1=1 and login_name=? ");
        return commonDao.getBySql2(sql.toString(), loginName);
    }

    /**
     * 查询用户角色是否正常
     *
     * @return
     */
    private String checkUserRole(String login_name) {
//		sad
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.role_id,r.role_code ");
        sql.append(" from tb_role r JOIN tb_param p on p.param_code='member_type'  and r.role_type=p.param_key  ");
        sql.append(" JOIN tb_user_role ur on ur.role_id = r.role_id JOIN tb_admin a on a.admin_id=ur.admin_id ");
        sql.append(" where 1=1 and p.is_make_sure=1 and p.`status`=1 and r.`status`=1 and a.login_name=? and a.`status`=1 ");

        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString(), login_name);
        if (null != list && list.size() > 0) {
            return "1";
        }
        System.out.println("---->查询用户角色出现问题");
        return null;
    }

    /**
     * 根据用户角色查询对应用户角色菜单
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> listMenusMap(int admin_id) {

        return handlMenu(getMenus(admin_id, "0"), admin_id);
    }

    /**
     * 查询菜单
     *
     * @param admin_id
     * @param function_parentid
     * @return
     */
    private List<Map<String, Object>> getMenus(int admin_id, String function_parentid) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT f.function_id,f.function_code,f.function_name,f.function_parentid,f.function_type,f.sort,f.function_action from tb_functions f  ");
        sql.append(" JOIN tb_role_function rf on rf.function_id = f.function_id JOIN tb_user_role ur on ur.role_id = rf.role_id ");
        sql.append(" where f.`status`=1 and ur.admin_id = ? and f.function_parentid = ? order BY sort desc,function_parentid ");
        list = commonDao.getBySql2(sql.toString(), admin_id, function_parentid);
        return list;
    }

    /**
     * 菜单处理
     *
     * @return
     */
    private List<Map<String, Object>> handlMenu(List<Map<String, Object>> listParentMenus, Integer admin_id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listParentMenus.size(); i++) {
            list.add(listParentMenus.get(i));
            String menuId = listParentMenus.get(i).get("function_id").toString();
            List<Map<String, Object>> listSubMenus = new ArrayList<Map<String, Object>>();
            listSubMenus = getMenus(admin_id, menuId);//查询二级菜单
            for (int j = 0; j < listSubMenus.size(); j++) {
                list.add(listSubMenus.get(j));
            }
        }
        return list;
    }

    /**
     * 查询主菜单或者子菜单
     *
     * @param menuType
     * @return
     */
    @Override
    public PageUtil loadParentMenusOrSubMenus(TbFunctions function, PageUtil page, String menuType) {
        StringBuffer sql = new StringBuffer();
        if ("parentMenu".equals(menuType)) {//查询主菜单
            sql.append("SELECT function_id,function_code,function_name,sort,function_action,status,DATE_FORMAT(createtime,'%Y-%m-%d %T') createtime,remark from tb_functions where function_parentid=0 order BY sort desc");
        } else if ("subMenu".equals(menuType)) {//查询子菜单
            sql.append("SELECT function_id,function_code,function_name,sort,function_action,status,DATE_FORMAT(createtime,'%Y-%m-%d %T') createtime,remark from tb_functions where 1=1  ");
            if (function.getFunctionParentid() != 0) {
                sql.append(" and function_parentid=" + function.getFunctionParentid() + " ");
            }
            if (null != function.getFunctionName() && !"".equals(function.getFunctionName()) & !"null".equals(function.getFunctionName())) {
                sql.append(" and  function_name like '%" + function.getFunctionName().trim() + "%' ");
            }
            if (null != function.getFunctionCode() && !"".equals(function.getFunctionCode())) {
                sql.append(" and function_code like '%" + function.getFunctionCode().trim() + "%' ");
            }
            if (null != function.getStatus()) {
                sql.append(" and status=" + function.getStatus() + " ");
            }
            sql.append("  order BY sort desc ");
        }
        PageUtil list = commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
        return list;
    }

    /**
     * 查询菜单
     *
     * @param function_id
     * @return
     */
    @Override
    public TbFunctions loadFunctionById(int function_id) {
        String hql = " from TbFunctions where functionId= " + function_id;
        return (TbFunctions) commonDao.loadObj(hql);
    }

    /**
     * 查询主菜单列表
     *
     * @param status 状态
     * @return
     */
    @Override
    public List<Map<String, Object>> listParentMenusMap(int status) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT function_id,function_name from tb_functions WHERE function_parentid=0 and status=?");
        list = commonDao.getBySql2(sql.toString(), status);
        return list;
    }

    /**
     * 检测名称是否存在
     *
     * @param menuId
     * @param name
     * @return
     */
    @Override
    public boolean checkName(int menuId, String name, String menuType) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT function_id from tb_functions where 1=1 ");
        if (0 != menuId) {
            sql.append(" and function_id!=?");
        }
        //检测名称
        sql.append(" and function_name=? ");
        if ("parentMenu".equals(menuType)) {
            sql.append(" and function_type!=1 ");
        } else {
            sql.append(" and function_type!=0 ");
        }
        if (0 != menuId) {
            list = commonDao.getBySql2(sql.toString(), menuId, name);
        } else {
            list = commonDao.getBySql2(sql.toString(), name);
        }

        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检测编码是否存在
     *
     * @param menuId
     * @param code
     * @return
     */
    @Override
    public boolean checkcode(int menuId, String code, String menuType) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT function_id from tb_functions where 1=1 ");
        if (0 != menuId) {
            sql.append(" and function_id !=?");
        }
        //检测编码
        sql.append(" and function_code=? ");
        if ("parentMenu".equals(menuType)) {
            sql.append(" and function_type!=1 ");
        } else {
            sql.append(" and function_type!=0 ");
        }

        if (0 != menuId) {
            list = commonDao.getBySql2(sql.toString(), menuId, code);
        } else {
            list = commonDao.getBySql2(sql.toString(), code);
        }

        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 加载角色列表
     *
     * @param role
     * @param page
     * @return
     */
    @Override
    public PageUtil loadrolelist(TbRole role, PageUtil page) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.role_id,r.role_code,r.role_name,r.sort,r.role_parentid,r.status,date_format(r.createtime,'%Y-%m-%d %T') createtime,r.createman ");
        sql.append(" from tb_role r where 1=1 ");
        if (null != role.getRoleName() && !"".equals(role.getRoleName())) {
            sql.append(" and r.role_name like '%" + role.getRoleName() + "%' ");
        }
        if (null != role.getStatus()) {
            sql.append(" and r.status=" + role.getStatus() + " ");
        }
        sql.append(" ORDER BY r.sort desc,r.createtime desc  ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 检测角色编码
     */
    @Override
    public boolean checkRoleCode(int role_id, String code) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT role_id FROM tb_role where role_code=? ");
        if (0 != role_id && !"".equals(role_id) && !"0".equals(role_id)) {
            sql.append(" and  role_id!=? ");
        }
        if (0 != role_id && !"".equals(role_id) && !"0".equals(role_id)) {
            list = commonDao.getBySql2(sql.toString(), code, role_id);
        } else {
            list = commonDao.getBySql2(sql.toString(), code);
        }
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检测角色名称
     */
    @Override
    public boolean checkRoleName(int role_id, String name) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT role_id FROM tb_role where role_name=? ");
        if (0 != role_id && !"".equals(role_id) && !"0".equals(role_id)) {
            sql.append(" and  role_id!=? ");
        }

        if (0 != role_id && !"".equals(role_id) && !"0".equals(role_id)) {
            list = commonDao.getBySql2(sql.toString(), name, role_id);
        } else {
            list = commonDao.getBySql2(sql.toString(), name);
        }

        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据参数code查询参数列表
     *
     * @param param_code
     * @return
     */
    @Override
    public List<Map<String, Object>> listParamMap(String param_code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_id,param_name,param_type,param_code,param_key,param_value,param_value1,param_value2 from tb_param where param_code=? and status=1 and is_make_sure=1 ");
        return commonDao.getBySql2(sql.toString(), param_code);
    }

    /**
     * 查询角色
     *
     * @param role_id
     * @return
     */
    @Override
    public TbRole loadRoleInfo(int role_id) {
        String hql = " from TbRole where roleId= " + role_id;
        return (TbRole) commonDao.loadObj(hql);
    }

    /**
     * 加载参数列表
     *
     * @param page
     * @param param
     * @return
     */
    @Override
    public PageUtil loadParamList(PageUtil page, TbParam param) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_id,param_name,param_type,param_code,param_key,param_value,param_value1,param_value2,`status`,remark,DATE_FORMAT(createtime,'%Y-%m-%d %T') createtime,createman,is_make_sure from tb_param where 1=1 ");
        if (null != param.getParamId() && 0 != param.getParamId()) {
            sql.append(" and param_id=" + param.getParamId() + "  ");
        }
        if (null != param.getStatus()) {
            sql.append(" and status=" + param.getStatus() + " ");
        }
        if (null != param.getParamCode() && !"".equals(param.getParamCode())) {
            sql.append(" and param_code like '%" + param.getParamCode() + "%' ");
        }
        if (null != param.getParamName() && !"".equals(param.getParamName())) {
            sql.append(" and param_name like '%" + param.getParamName() + "%' ");
        }
        if (null != param.getIsMakeSure()) {
            sql.append(" and is_make_sure=" + param.getIsMakeSure() + " ");
        }
        sql.append(" ORDER BY createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询参数
     *
     * @param id
     * @return
     */
    @Override
    public TbParam loadTbParamInfo(int id) {
        String hql = " from TbParam where paramId= " + id;
        return (TbParam) commonDao.loadObj(hql);
    }

    /**
     * 根据参数查询参数类型列表
     *
     * @return
     */
    public List<Map<String, Object>> listParamGroupMap() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_code,param_name from tb_param  GROUP BY param_code ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询角色列表
     *
     * @param role_status 状态
     * @return
     */
    @Override
    public List<Map<String, Object>> listRoleMap(String role_status) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT role_id,role_name from tb_role where 1=1 ");
        if ("1".equals(role_status) || "0".equals(role_status)) {
            sql.append(" and status=" + role_status);
        }
        list = commonDao.getBySql2(sql.toString());
        return list;
    }

    /**
     * 加载功能列表
     */
    @Override
    public List<Map<String, Object>> listFunctionsMap(String role_id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//用于存储处理后的结果
        List<Map<String, Object>> list_ = new ArrayList<Map<String, Object>>();//存在根据主功能标识查询得到的结果
        List<String> listId = new ArrayList<String>();
        listId = getListFunctionParentId();
        //查询所有有效的父级功能下子功能
        for (int i = 0; i < listId.size(); i++) {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT f.function_id,f.function_name,f.function_type,IFNULL(rf.role_id,0) role_id from tb_functions f  ");
            sql.append(" LEFT JOIN (SELECT function_id,role_id from tb_role_function where role_id=" + role_id + ") rf on rf.function_id=f.function_id  ");
            sql.append(" LEFT JOIN tb_role r on r.role_id=rf.role_id where f.function_parentid=" + listId.get(i) + " or f.function_id=" + listId.get(i) + " and f.status=1 ORDER BY f.function_type,f.sort desc");
            list_ = commonDao.getBySql2(sql.toString());
            if (null != list_ && list_.size() > 0) {
                for (int j = 0; j < list_.size(); j++) {
                    list.add(list_.get(j));
                    //System.out.println(JSONObject.toJSONString(list_.get(j)));
                }
            }
        }
        return list;
    }

    /**
     * 查询所有有效的父级功能标识
     */
    public List<String> getListFunctionParentId() {
        List<Map<String, Object>> list_ = new ArrayList<Map<String, Object>>();
        List<String> listId = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT function_id from tb_functions where status=1 and function_type=0 ");
        list_ = commonDao.getBySql2(sql.toString());
        if (null != list_ && list_.size() > 0) {
            for (int i = 0; i < list_.size(); i++) {
                listId.add(String.valueOf(list_.get(i).get("function_id")));
            }
        }
        return listId;
    }

    /**
     * 加载管理员列表
     *
     * @param page
     * @param bean
     * @return
     */
    @Override
    public PageUtil listAdmin(PageUtil page, AdminCondition bean) {
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT u.admin_id,u.login_name,u.login_password,u.user_name,u.birthday,u.sex,if(u.sex=0,'女','男') as sex_name,u.nick_name,u.idcard,u.phone,u.email,u.qq, ");
        sql.append("  if(u.photo is null,CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',u.photo)) as photo ,  ");
        sql.append("  DATE_FORMAT(u.member_begintime,'%Y-%m-%d') as member_begintime,DATE_FORMAT(u.member_endtime,'%Y-%m-%d') as member_endtime,  ");
        sql.append("  u.shop_id,u.shop_menber_type,u.status,u.createtime,u.remark,r.role_id,r.role_name,s.shop_name,u.is_make_sure ");
        sql.append("  from tb_admin u LEFT JOIN tb_user_role ur on ur.admin_id=u.admin_id LEFT JOIN tb_role r on r.role_id=ur.role_id  ");
        sql.append("  LEFT JOIN tb_shop s on s.shop_id=u.shop_id  ");
        sql.append(" where 1=1 ");
        if (null != bean) {
//			if(!"null".equals(bean.getAdminId())&&null!=bean.getAdminId()&&0!=bean.getAdminId()){
//				sql.append(" and u.admin_id= "+admin.getAdminId());
//			}
            if (!"null".equals(bean.getLoginName()) && null != bean.getLoginName() && !"".equals(bean.getLoginName())) {
                sql.append(" and u.login_name like '%" + bean.getLoginName().trim() + "%' ");
            }
            if (!"null".equals(bean.getUserName()) && null != bean.getUserName() && !"".equals(bean.getUserName())) {
                sql.append(" and u.user_name like '%" + bean.getUserName().trim() + "%' ");
            }
            if (!"null".equals(bean.getShopName()) && null != bean.getShopName() && !"".equals(bean.getShopName())) {
                sql.append(" and s.shop_name like '%" + bean.getShopName().trim() + "%' ");//
            }
            if (!"null".equals(bean.getPhone()) && null != bean.getPhone() && !"".equals(bean.getPhone())) {
                sql.append(" and u.phone like '%" + bean.getPhone().trim() + "%' ");
            }
            if (!"null".equals(bean.getIdcard()) && null != bean.getIdcard() && !"".equals(bean.getIdcard())) {
                sql.append(" and u.idcard like '%" + bean.getIdcard().trim() + "%' ");
            }
            if (!"null".equals(bean.getRoleId()) && null != bean.getRoleId() && !"".equals(bean.getRoleId()) && !"0".equals(bean.getRoleId())) {
                sql.append(" and r.role_id=" + bean.getRoleId() + " ");
            }
            if (!"null".equals(bean.getStatus()) && null != bean.getStatus() && !"".equals(bean.getStatus())) {
                sql.append(" and u.status=" + bean.getStatus() + " ");
            }
            if (null != bean.getIsMakeSure()) {
                sql.append(" and u.is_make_sure=" + bean.getIsMakeSure() + " ");
            }
        }
        sql.append(" ORDER BY u.createtime desc  ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 加载管理员信息
     *
     * @param admin_id
     * @return
     */
    @Override
    public TbAdmin loadAdminInfo(int admin_id) {
        String hql = " from TbAdmin where adminId=" + admin_id + " ";
        return (TbAdmin) commonDao.loadObj(hql);
    }

    /**
     * 检测登录名称是否已经存在
     *
     * @param admin_id
     * @param login_name
     * @return
     */
    @Override
    public boolean checkLoginName(int admin_id, String login_name) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT admin_id from tb_admin where  login_name=? ");
        if (0 != admin_id && !"null".equals(admin_id) && !"".equals(admin_id) && !"0".equals(admin_id)) {
            sql.append("  and admin_id!=? ");
        }

        List<Map<String, Object>> list = null;
        if (0 != admin_id && !"null".equals(admin_id) && !"".equals(admin_id)) {
            list = commonDao.getBySql2(sql.toString(), login_name, admin_id);
        } else {
            list = commonDao.getBySql2(sql.toString(), login_name);
        }
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 加载管理员角色列表
     */
    @Override
    public PageUtil listAdminRole(PageUtil page, AdminRoleCondition bean) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ur.id,a.login_name,a.user_name,r.role_name,DATE_FORMAT(ur.createtime,'%Y-%m-%d %T') createtime,ur.role_id,a.admin_id ");
        sql.append(" from tb_user_role ur JOIN tb_admin a on a.admin_id=ur.admin_id JOIN tb_role r on r.role_id=ur.role_id  ");
        sql.append(" where 1=1 ");
        if (null != bean) {
            if (null != bean.getUserName() && !"null".equals(bean.getUserName()) && !"".equals(bean.getUserName())) {
                sql.append(" and a.user_name like '%" + bean.getUserName().trim() + "%' ");
            }
            if (null != bean.getLoginName() && !"null".equals(bean.getLoginName()) && !"".equals(bean.getLoginName())) {
                sql.append(" and a.login_name like '%" + bean.getLoginName().trim() + "%' ");
            }
            if (null != bean.getRoleId() && !"null".equals(bean.getRoleId()) && !"".equals(bean.getRoleId()) && !"0".equals(bean.getRoleId())) {
                sql.append(" and ur.role_id=" + bean.getRoleId().trim() + " ");
            }
        }
        sql.append(" ORDER BY ur.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 检测是否已经存在用户角色关系
     */
    @Override
    public boolean checkAdminRole(String admin_role_id, String admin_id, String role_id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT role_id from tb_user_role where role_id=" + role_id + " and admin_id=" + admin_id + " ");
        if (null != admin_role_id && !"null".equals(admin_role_id) && !"".equals(admin_role_id)) {
            sql.append(" and id != " + admin_role_id);
        }
        if (commonDao.getBySql2(sql.toString()).size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 增加管理员角色的时候加载的用户列表
     *
     * @param page
     * @return
     */
    @Override
    public PageUtil addAdminRoleAdminList(PageUtil page, AdminRoleCondition bean) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.admin_id,a.login_name,a.user_name,r.role_name,DATE_FORMAT(ur.createtime,'%Y-%m-%d %T') createtime,ur.role_id ");
        sql.append(" from tb_admin a LEFT JOIN tb_user_role ur on a.admin_id=ur.admin_id LEFT JOIN tb_role r on r.role_id=ur.role_id ");
        sql.append(" where 1=1 ");
        if (null != bean) {
            if (null != bean.getUserName() && !"null".equals(bean.getUserName()) && !"".equals(bean.getUserName())) {
                sql.append(" and a.user_name like '%" + bean.getUserName().trim() + "%' ");
            }
            if (null != bean.getLoginName() && !"null".equals(bean.getLoginName()) && !"".equals(bean.getLoginName())) {
                sql.append(" and a.login_name like '%" + bean.getLoginName().trim() + "%' ");
            }
            if (null != bean.getRoleId() && !"null".equals(bean.getRoleId()) && !"".equals(bean.getRoleId()) && !"0".equals(bean.getRoleId())) {
                sql.append(" and ur.role_id=" + bean.getRoleId().trim() + " ");
            }
        }
        sql.append(" ORDER BY a.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询渠道列表
     *
     * @param page
     * @param channel
     * @return
     */
    @Override
    public PageUtil listChannel(PageUtil page, TbChannel channel) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT channel_id,channel_name,channel_code,DATE_FORMAT(createtime,'%Y-%m-%d %T') createtime,status from tb_channel where 1=1 ");
        if (null != channel.getChannelName() && !"".equals(channel.getChannelName())) {
            sql.append(" and channel_name like '%" + channel.getChannelName() + "%' ");
        }
        if (null != channel.getStatus()) {
            sql.append(" and status=" + channel.getStatus() + " ");
        }
        sql.append(" ORDER BY createtime  desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 检测渠道名称
     *
     * @return
     */
    @Override
    public boolean checkChannelName(String channelid, String name) {
        boolean flag = false;
        StringBuffer sql = new StringBuffer("SELECT channel_name from tb_channel where channel_name='" + name + "' ");
        if (null != channelid && !"null".equals(channelid) && !"".equals(channelid)) {
            sql.append("  and channel_id !=" + channelid);
        }
        if (commonDao.getBySql2(sql.toString()).size() > 0) {
            return true;
        }
        return flag;
    }

    /**
     * 检测渠道编码
     *
     * @return
     */
    @Override
    public boolean checkChannelCode(String channelid, String code) {
        boolean flag = false;
        StringBuffer sql = new StringBuffer("SELECT channel_id from tb_channel where channel_code='" + code + "' ");
        if (null != channelid && !"null".equals(channelid) && !"".equals(channelid)) {
            sql.append(" and channel_id !=" + channelid);
        }
        if (commonDao.getBySql2(sql.toString()).size() > 0) {
            return true;
        }
        return flag;
    }

    /**
     * 查询渠道信息
     *
     * @param channel_id
     * @return
     */
    @Override
    public TbChannel loadChannelInfo(int channel_id) {
        String hql = " from TbChannel where channelId=" + channel_id + " ";
        return (TbChannel) commonDao.loadObj(hql);
    }

    /**
     * 查询渠道列表
     *
     * @param status
     * @return
     */
    @Override
    public List<Map<String, Object>> listChannelMap(String status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT channel_id,channel_name from tb_channel where 1=1 ");
        if (null != status && !"".equals(status)) {
            sql.append(" and status=" + status + " ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询类目列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> listTbCategoryMap(Integer status, Integer leveId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_name,ca.short as category_short,ca.oute_id, ");
        sql.append(" if(ca.category_img is null,CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',ca.category_img)) as category_img , ");
        sql.append(" ca.category_code,ca.is_show,ca.status,DATE_FORMAT(ca.createtime,'%Y-%m-%d %T') createtime,CONCAT(ca.category_name,'(',ca.category_code,')') as category_name_code ");
        sql.append(" from tb_category ca  LEFT JOIN tb_classyfy_catogry_rel crel on crel.category_id=ca.category_id ");
        sql.append("  where 1=1 ");
        if (null != status) {
            sql.append(" and ca.status=" + status + " ");
        }
        if (null != leveId && 0 != leveId) {
            sql.append(" and crel.level_id=" + leveId + " ");
        }
        sql.append(" ORDER BY short desc,createtime desc ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询类目信息
     *
     * @return
     */
    @Override
    public TbCategory loadTbCategoryInfo(int category_id) {
        String hql = " from TbCategory where categoryId=" + category_id + " ";
        return (TbCategory) commonDao.loadObj(hql);
    }

    /**
     * 查询类目属性列表
     *
     * @param itemprops
     * @return
     */
    @Override
    public List<Map<String, Object>> listTbItempropsMap(TbItemprops itemprops) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT item.prop_id,item.prop_name,item.category_id,item.oute_id,item.is_sales,item.show_view,item.prop_img,item.prop_code,item.is_show, ");
        sql.append(" item.sort,item.`status`,item.remark,DATE_FORMAT(item.createtime,'%Y-%m-%d %T') createtime,ca.category_name ");
        sql.append(" FROM tb_itemprops item JOIN tb_category ca on ca.category_id=item.category_id where 1=1 ");
        if (null != itemprops) {
            if (null != itemprops.getCategoryId()) {
                sql.append(" and item.category_id=" + itemprops.getCategoryId() + " ");
            }
            if (null != itemprops.getStatus()) {
                sql.append(" and item.status=" + itemprops.getStatus() + " ");
            }
        }
        sql.append(" ORDER BY item.sort desc,item.createtime desc ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询类目属性值列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> listTbPropsValueMap(TbPropsValue propsValue) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT pv.value_id,pv.prop_id,pv.value_name,pv.category_id,pv.oute_id,pv.`value`,pv.short,pv.short as short_,DATE_FORMAT(pv.createtime,'%Y-%m-%d %T') createtime, ");
        sql.append(" pv.`status`,pv.remark,it.prop_name,cat.category_name ");
        sql.append(" from tb_props_value pv LEFT JOIN tb_itemprops it on it.prop_id=pv.prop_id ");
        sql.append(" LEFT JOIN tb_category cat on cat.category_id=pv.category_id where 1=1 and pv.status=1 ");
        if (null != propsValue) {
            if (propsValue.getPropId() != null && 0 != propsValue.getPropId()) {
                sql.append(" and  pv.prop_id=" + propsValue.getPropId() + " ");
            }
            if (propsValue.getCategoryId() != null && propsValue.getCategoryId() != 0) {
                sql.append(" and pv.category_id=" + propsValue.getCategoryId());
            }
            if (null != propsValue.getStatus()) {
                sql.append(" and pv.status=" + propsValue.getStatus());
            }
        }
        sql.append(" ORDER BY pv.short desc,pv.createtime ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询类目属性信息
     *
     * @param propId
     * @return
     */
    @Override
    public TbItemprops loadTbItempropsInfo(int propId) {
        String hql = " from TbItemprops where propId=" + propId + " ";
        return (TbItemprops) commonDao.loadObj(hql);
    }

    /**
     * 查询类目属性值
     *
     * @param item
     * @return
     */
    @Override
    public PageUtil loadItempropsListPage(PageUtil page, ClassfyCondition item) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT it.prop_id,it.prop_name,it.category_id,it.oute_id,it.is_sales,it.show_view,it.prop_code,it.is_show, ");
        sql.append(" it.sort,it.`status`,DATE_FORMAT(it.createtime,'%Y-%m-%d %T') createtime ,cat.category_name,le.level_name ");
        sql.append(" from tb_itemprops it LEFT JOIN tb_category cat on cat.category_id=it.category_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=it.category_id LEFT JOIN tb_catogry_level1 le on le.level_id=rel.level_id ");
        sql.append(" where 1=1 ");
        if (null != item) {
            if (item.getCatogryId() != null && item.getCatogryId() != 0) {
                sql.append(" and it.category_id=" + item.getCatogryId() + " ");
            }
            if (null != item.getPropName() && !"".equals(item.getPropName())) {
                sql.append(" and it.prop_name like '%" + item.getPropName().trim() + "%' ");
            }
            if (null != item.getStatus()) {
                sql.append(" and it.`status`=" + item.getStatus() + " ");
            }
            if (null != item.getPropCode() && !"".equals(item.getPropCode())) {
                sql.append(" and it.prop_code like '%" + item.getPropCode().trim() + "%' ");
            }
            if (null != item.getLeveId() && 0 != item.getLeveId()) {
                sql.append(" and rel.level_id=" + item.getLeveId() + " ");
            }
        }
        sql.append(" ORDER BY it.category_id desc ,it.sort desc ,it.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询类目属性值
     *
     * @return
     */
    @Override
    public PageUtil loadPropsValueListPage(PageUtil page, ClassfyCondition value) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT pv.value_id,pv.prop_id,pv.value_name,pv.oute_id,pv.`value`,pv.short as sort,pv.`status`, ");
        sql.append(" DATE_FORMAT(pv.createtime,'%Y-%m-%d') createtime,item.prop_name, ");
        sql.append(" if(pv.value_img is null or pv.value_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',pv.value_img)) as value_img,  ");
        sql.append(" ca.category_name,le.level_name ");
        sql.append(" from tb_props_value pv LEFT JOIN tb_itemprops item on item.prop_id=pv.prop_id ");
        sql.append(" JOIN tb_category ca on ca.category_id=pv.category_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=pv.category_id LEFT JOIN tb_catogry_level1 le on le.level_id=rel.level_id ");
        sql.append(" where 1=1 ");
        if (null != value) {
            if (null != value.getValueName() && !"".equals(value.getValueName())) {
                sql.append(" and pv.value_name like '%" + value.getValueName().trim() + "%' ");
            }
            if (null != value.getStatus()) {
                sql.append(" and pv.status=" + value.getStatus() + " ");
            }
            if (null != value.getPropId() && 0 != value.getPropId()) {
                sql.append(" and pv.prop_id=" + value.getPropId() + " ");
            }
            if (null != value.getCatogryId() && 0 != value.getCatogryId()) {
                sql.append(" and pv.category_id=" + value.getCatogryId() + " ");
            }
            if (null != value.getLeveId() && 0 != value.getLeveId()) {
                sql.append(" and rel.level_id=" + value.getLeveId() + " ");
            }
        }
        sql.append(" ORDER BY pv.prop_id desc,pv.short desc,pv.createtime desc ");

        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询类目属性值
     *
     * @param value_id
     * @return
     */
    @Override
    public TbPropsValue loadTbPropsValueInfo(int value_id) {
        String hql = " from TbPropsValue where valueId=" + value_id + " ";
        return (TbPropsValue) commonDao.loadObj(hql);
    }

    /**
     * 查询类目编码是否存在
     *
     * @param code
     * @param id
     * @return
     */
    public boolean checkClassifyCodeIsExsit(String code, Integer id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT category_code from tb_category where category_code=? ");
        if (null != id && 0 != id) {
            sql.append(" and category_id !=? ");
            list = commonDao.getBySql2(sql.toString(), code, id);
        } else {
            list = commonDao.getBySql2(sql.toString(), code);
        }

        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询类目属性编码是否存在
     *
     * @param code
     * @param id
     * @return
     */
    public boolean checkPropCodeIsExsit(String code, Integer id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT prop_code from tb_itemprops where prop_code=? ");
        if (null != id && 0 != id) {
            sql.append(" and prop_id !=? ");
            list = commonDao.getBySql2(sql.toString(), code, id);
        } else {
            list = commonDao.getBySql2(sql.toString(), code);
        }

        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    public List<Map<String, Object>> listTbCategoryByIds(String ids) {
        String sql = " SELECT category_id,category_name from tb_category where `status`=1 and category_id in (" + ids + ") ";
        return commonDao.getBySql2(sql);
    }

    /**
     * 店铺管理页面
     *
     * @param page
     * @return
     */
    @Override
    public PageUtil loadShopListPage(PageUtil page, ShopCondition shop) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT s.shop_id,s.shop_name,s.shop_tel1,CONCAT(s.deposit,'') as deposit,DATE_FORMAT(s.deposit_time,'%Y-%m-%d %T') as deposit_time, ");
        sql.append(" CONCAT(s.shop_money,'') as shop_money,s.`status`,DATE_FORMAT(s.enter_end_time,'%Y-%m-%d') enter_end_time,DATE_FORMAT(s.createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" if(s.shop_logo is null or s.shop_logo='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',s.shop_logo)) as shop_logo,s.weight,  ");
        sql.append(" s.is_out_shop ");
        sql.append(" from tb_shop s ");
        sql.append(" where 1=1 ");
        if (null != shop) {
            if (null != shop.getShopName() && !"".equals(shop.getShopName())) {
                sql.append(" and s.shop_name like '%" + shop.getShopName() + "%' ");
            }
            if (null != shop.getUserName() && !"".equals(shop.getUserName())) {
                sql.append(" and a.user_name like '%" + shop.getUserName() + "%' ");
            }
            if (null != shop.getStatus()) {
                sql.append(" and s.`status`=" + shop.getStatus() + " ");
            }
            if (null != shop.getIsOutShop()) {
                sql.append(" and s.is_out_shop=" + shop.getIsOutShop() + " ");
            }
        }
        sql.append(" ORDER BY s.weight desc,s.createtime desc ");

        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询店铺
     *
     * @param shop_id
     * @return
     */
    @Override
    public TbShop loadTbShopInfo(int shop_id) {
        String hql = " from TbShop where shopId=" + shop_id + " ";
        return (TbShop) commonDao.loadObj(hql);

    }

    /**
     * 查询店铺列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> listShopMap(TbShop shop) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select shop_id,shop_name from  tb_shop where 1=1 ");
        if (null != shop) {
            if (null != shop.getStatus()) {
                sql.append(" and status=" + shop.getStatus() + " ");
            }
            if (null != shop.getShopId()) {
                sql.append(" and shop_id=" + shop.getShopId() + " ");
            }
            if (null != shop.getShopName()) {
                sql.append(" and shop_name like '%" + shop.getShopName() + "%' ");
            }
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询店铺关联的商家
     *
     * @param partnerStatus
     * @return
     */
    public List<Map<String, Object>> listShopPartnerRel(Integer partnerStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT p.id,partners_name,rel.id as rel_id,s.shop_id ");
        sql.append("  from tb_partners p LEFT JOIN tb_shop_partners_rel rel on rel.partners_id=p.id ");
        sql.append(" LEFT JOIN tb_shop s on s.shop_id=rel.shop_id where p.status=1 order by partners_sort desc ");
        return commonDao.getBySql2(sql.toString());
    }

    @Override
    public List<Map<String, Object>> listAdminMap(int status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT admin_id,user_name from tb_admin where status=? ");
        return commonDao.getBySql2(sql.toString(), status);
    }

    /**
     * 活动专题管理
     *
     * @param page
     * @param activity
     * @return
     */
    @Override
    public PageUtil loadActivityPage(PageUtil page, ActivityCondition activity) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.activity_id,a.activity_title, ");
        sql.append(" if(a.activity_img is null,CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',a.activity_img)) as activity_img , ");
        sql.append(" DATE_FORMAT(a.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(a.end_time,'%Y-%m-%d %T') as end_time, ");
        sql.append(" a.`status`,DATE_FORMAT(a.createtime,'%Y-%m-%d %T') as createtime,a.valid_date,a.weight ");
        sql.append(" from tb_activity a LEFT JOIN tb_partners s on s.id=a.partners_id ");
        sql.append(" where 1=1 ");
        if (null != activity) {
            if (activity.getChannelId() != null) {
                sql.append(" and a.channel_id=" + activity.getChannelId() + " ");
            }
            if (null != activity.getActivityTitle() && !"".equals(activity.getActivityTitle()) && !"null".equals(activity.getActivityTitle())) {
                sql.append(" and a.activity_title like '%" + activity.getActivityTitle() + "%' ");
            }
            if (null != activity.getStatus()) {
                sql.append(" and a.status=" + activity.getStatus() + " ");
            }
            if (null != activity.getPartnersName() && !"".equals(activity.getPartnersName()) && !"null".equals(activity.getPartnersName())) {
                sql.append(" and p.partners_name like '%" + activity.getPartnersName() + "%' ");
            }
            // 查询实际状态 0:不查1:长期有效2:未开始3:已开始(未结束)4:已结束的活动5:限时有效
            if (null != activity.getObj()) {
                if (activity.getObj() == 1) {
                    sql.append(" and a.valid_date=1 ");
                }
                if (activity.getObj() == 2) {
                    sql.append(" and DATE_FORMAT(a.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
                }
                if (activity.getObj() == 3) {
                    sql.append(" and ((DATE_FORMAT(a.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(a.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or ( a.valid_date=1 and DATE_FORMAT(a.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T'))) ");
                }
                if (activity.getObj() == 4) {
                    sql.append(" and DATE_FORMAT(a.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
                }
                if (activity.getObj() == 5) {
                    sql.append(" and a.valid_date=0 ");
                }

            }
        }
        sql.append(" ORDER BY a.weight desc,a.createtime desc ");
        PageUtil list = commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
        return list;
    }

    /**
     * 查询活动专题
     *
     * @param activity_id
     * @return
     */
    @Override
    public TbActivity loadTbActivityInfo(int activity_id) {
        String hql = " from TbActivity where activityId=" + activity_id + " ";
        return (TbActivity) commonDao.loadObj(hql);
    }

    /**
     * bannel管理
     *
     * @param page
     * @param bannel
     * @return
     */
    @Override
    public PageUtil loadTbBannelPage(PageUtil page, BannelCondition bannel) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT b.bannel_id,b.bannel_name, ");
        sql.append(" if(b.bannel_img is null or b.bannel_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',b.bannel_img)) as bannel_img , ");
        sql.append(" DATE_FORMAT(b.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(b.end_time,'%Y-%m-%d %T') as end_time, ");
        sql.append(" b.`status`,DATE_FORMAT(b.createtime,'%Y-%m-%d %T') as createtime,p.partners_name,b.valid_date,b.weight, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d %T')>DATE_FORMAT(b.end_time,'%Y-%m-%d %T'),0,1) as expire ");
        sql.append(" from tb_bannel b LEFT JOIN tb_partners p on p.id=b.partners_id ");
        sql.append(" where 1=1 ");
        if (null != bannel) {
            if (bannel.getChannelId() != null) {
                sql.append(" and b.channel_id=" + bannel.getChannelId() + " ");
            }
            if (null != bannel.getBannelName() && !"".equals(bannel.getBannelName()) && !"null".equals(bannel.getBannelName())) {
                sql.append(" and b.bannel_name like '%" + bannel.getBannelName().trim() + "%' ");
            }
            if (null != bannel.getStatus()) {
                sql.append(" and b.status=" + bannel.getStatus() + " ");
            }
            if (null != bannel.getPartnersName() && !"".equals(bannel.getPartnersName()) && !"null".equals(bannel.getPartnersName())) {
                sql.append(" and p.partners_name like '%" + bannel.getPartnersName().trim() + "%' ");
            }
            //查询实际状态 0:不查1:长期有效2:未开始3:已开始(未结束)4:已结束的活动5:限时有效
            if (null != bannel.getObj()) {
                if (bannel.getObj() == 1) {
                    sql.append(" and b.valid_date=1 ");
                }
                if (bannel.getObj() == 2) {
                    sql.append(" and DATE_FORMAT(b.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
                }
                if (bannel.getObj() == 3) {
                    sql.append(" and ((DATE_FORMAT(b.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(b.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(b.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and b.valid_date=1)) ");
                }
                if (bannel.getObj() == 4) {
                    sql.append(" and DATE_FORMAT(b.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
                }
                if (bannel.getObj() == 5) {
                    sql.append(" and b.valid_date=0 ");
                }
            }
        }
        sql.append(" ORDER BY b.weight desc,b.createtime desc ");
        PageUtil list = commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
        return list;
    }

    /**
     * 查询bannel
     *
     * @param bannel_id
     * @return
     */
    @Override
    public TbBannel loadTbBannelInfo(int bannel_id) {
        String hql = " from TbBannel where bannelId=" + bannel_id + " ";
        return (TbBannel) commonDao.loadObj(hql);
    }

    /**
     * 查询配送地址列表
     *
     * @param page
     * @param distribution
     * @return
     */
    public PageUtil loadTBDistributionPage(PageUtil page, TbDistributionAddr distribution) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,detail_addr,responsible_man,responsible_phone,CONCAT(freight,'') freight,sort_char, addr_type,addr_prefix,`status`,addr_short, ");
        sql.append(" if(addr_type=4,'自取','配送') as addr_type_name,addr_name ");
        sql.append("  from tb_distribution_addr ");
        sql.append(" where 1=1 ");
        if (null != distribution) {
            if (null != distribution.getAddrPrefix() && !"".equals(distribution.getAddrPrefix()) && !"null".equals(distribution.getAddrPrefix())) {
                sql.append(" and addr_prefix='" + distribution.getAddrPrefix() + "' ");
            }
            if (null != distribution.getDetailAddr() && !"".equals(distribution.getDetailAddr()) && !"null".equals(distribution.getDetailAddr())) {
                sql.append(" and detail_addr like '%" + distribution.getDetailAddr() + "%' ");
            }
            if (null != distribution.getResponsibleMan() && !"".equals(distribution.getResponsibleMan()) && !"null".equals(distribution.getResponsibleMan())) {
                sql.append(" and (responsible_man like '%" + distribution.getResponsibleMan() + "%' or responsible_phone like '%" + distribution.getResponsibleMan() + "%') ");
            }
            if (null != distribution.getStatus()) {
                sql.append(" and status=" + distribution.getStatus() + "  ");
            }
            if (null != distribution.getAddrType() && -1 != distribution.getAddrType()) {
                sql.append(" and addr_type=" + distribution.getAddrType() + " ");
            }
        }
        sql.append(" ORDER BY sort_char,addr_short desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询配送地址
     *
     * @param id
     * @return
     */
    public TbDistributionAddr loadTbDistributionAddrInfo(int id) {
        String hql = " from TbDistributionAddr where id=" + id + " ";
        return (TbDistributionAddr) commonDao.loadObj(hql);
    }


    /**
     * 查询全国地址管理
     *
     * @param page
     * @return
     */
    public PageUtil loadAddrManagePage(PageUtil page, TbRegion region) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en, ");
        sql.append(" region_type,createman,DATE_FORMAT(createtime,'%Y-%m-%d %T') as creattime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,`status`,remark,freight ");
        sql.append(" from tb_region where 1=1  ");
        String condtionSql = loadAddrManagePageCondition(region);
        sql.append(condtionSql);
        sql.append(" ORDER BY region_order desc,region_id desc ");
        StringBuffer sqlCount = new StringBuffer();
        sqlCount.append(" select count(*) from tb_region where 1=1 ");
        sqlCount.append(condtionSql);

        return commonDao.queryListSQLPageByField(sql.toString(), sqlCount.toString(), page.getCurrentPage(), page.getPageSize());
    }

    private String loadAddrManagePageCondition(TbRegion region) {
        StringBuffer sql = new StringBuffer();

        if (region.getStatus() != null) {
            sql.append(" and `status`=" + region.getStatus() + " ");
        }
        if (StringUtils.isNotEmpty(region.getRegionCode())) {
            sql.append(" and region_code='" + region.getRegionCode().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(region.getParentId())) {
            sql.append(" and parent_id='" + region.getParentId().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(region.getRegionName())) {
            sql.append(" and region_name like '%" + region.getRegionName().trim() + "%' ");
        }
        if (!"-1".equals(region.getRegionType()) && null != region.getRegionType()) {
            sql.append(" and `region_type`=" + region.getRegionType() + " ");
        }
        if (!"-1".equals(region.getRegionLevel()) && null != region.getRegionLevel()) {
            sql.append(" and `region_level`=" + region.getRegionLevel() + " ");
        }


        return sql.toString();
    }

    /**
     * 全国地址管理详细
     *
     * @param page
     * @param region
     * @return
     */
    public PageUtil loadAddrDetailPage(PageUtil page, TbRegion region) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en, ");
        sql.append(" region_type,createman,DATE_FORMAT(createtime,'%Y-%m-%d %T') as creattime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,`status`,remark,freight ");
        sql.append(" from tb_region where 1=1 and region_level=1 ");
        if (region.getStatus() != null) {
            sql.append(" and `status`=" + region.getStatus() + " ");
        }
        if (StringUtils.isNotEmpty(region.getRegionCode())) {
            sql.append(" and left(region_code,2)='" + region.getRegionCode().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(region.getRegionName())) {
            sql.append(" and left(region_code,2) in (SELECT DISTINCT left(region_code,2) from tb_region where region_name like '%" + region.getRegionName().trim() + "%') ");
        }
        if (!"-1".equals(region.getRegionType()) && null != region.getRegionType()) {
            sql.append(" and `region_type`=" + region.getRegionType() + " ");
        }
        sql.append(" ORDER BY region_order desc,region_id desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    public TbRegion loadTbRegion(String regionCode) {
        String hql = " from TbRegion where regionCode='" + regionCode + "' ";
        return (TbRegion) commonDao.loadObj(hql);
    }

    public TbRegion loadTbRegion(int id) {
        String hql = " from TbRegion where regionId=" + id + " ";
        return (TbRegion) commonDao.loadObj(hql);
    }

    public boolean checkRegionCode(Integer id, String regionCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code from tb_region where region_code='" + regionCode.trim() + "' ");
        if (null != id) {
            sql.append(" and region_id !=" + id + " ");
        }
        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString());
        if (null != list && list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询全国地区
     *
     * @param parentId 行政区划代码
     * @return
     */
    public List<Map<String, Object>> loadAddrListMap(String parentId, String regionName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en, ");
        sql.append(" region_type,createman,DATE_FORMAT(createtime,'%Y-%m-%d %T') as creattime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,`status`,remark,freight ");
        sql.append(" from tb_region where   1=1 ");
        sql.append(" and parent_id='" + parentId + "' ");
        sql.append(" ORDER BY region_order desc,region_id desc ");
        return commonDao.getBySql2(sql.toString());
    }

    public List<Map<String, Object>> loadTbRegionListMap(String regionCode, String regionName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en, ");
        sql.append(" region_type,createman,DATE_FORMAT(createtime,'%Y-%m-%d %T') as creattime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,`status`,remark,freight ");
        sql.append(" from tb_region where   1=1 ");
        if (StringUtils.isNotEmpty(regionCode)) {
            sql.append(" and region_code='" + regionCode.trim() + "' ");
        }
        if (StringUtils.isNotEmpty(regionName)) {
            sql.append(" and region_name like '%" + regionName.trim() + "%' ");
        }
        if (StringUtils.isEmpty(regionCode) && StringUtils.isEmpty(regionName)) {
            sql.append(" limit 100 ");
        }

        return commonDao.getBySql2(sql.toString());
    }

    public TbUserAddr loadTbUserAddrById(int id) {
        String hql = " from TbUserAddr where id=? ";
        return (TbUserAddr) commonDao.queryUniqueByHql(hql, id);
    }

    /**
     * 查询规则列表
     *
     * @param page
     * @return
     */
    public PageUtil loadTbRulePage(PageUtil page, RuleCondition rulec) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.rule_id,r.rule_name,r.rule_code,r.rule_type,r.rule_value,r.rule_belong,r.rule_desc,DATE_FORMAT(r.createtime,'%Y-%m-%d %T') createtime, ");
        sql.append(" if(r.rule_type=0,'店铺规则',if(r.rule_type=2,'卡券规则','商品规则')) as rule_type_name,ifnull(p.partners_name,'通用') as partners_name,r.`status`,r.is_make_sure ");
        sql.append(" from tb_rule r LEFT JOIN tb_partners p on p.id=r.rule_belong ");
        sql.append(" where 1=1 ");
        if (null != rulec) {
            if (rulec.getStatus() != null) {
                sql.append(" and r.status=" + rulec.getStatus() + " ");
            }
            if (null != rulec.getRuleName() && !"".equals(rulec.getRuleName()) && !"null".equals(rulec.getRuleName())) {
                sql.append(" and r.rule_name like '%" + rulec.getRuleName() + "%' ");
            }
            if ("0".equals(rulec.getShopName()) || "通用".equals(rulec.getShopName())) {
                sql.append(" and r.rule_belong=0 ");
            } else {
                if (null != rulec.getShopName() && !"".equals(rulec.getShopName()) && !"null".equals(rulec.getShopName())) {
                    sql.append(" and p.partners_name like '%" + rulec.getShopName() + "%' ");
                }
            }
            if (rulec.getRuleType() != null) {
                sql.append(" and r.rule_type=" + rulec.getRuleType() + " ");
            }
            if (rulec.getRuleCode() != null) {
                sql.append(" and r.rule_code='" + rulec.getRuleCode() + "' ");
            }
            if (null != rulec.getIsMakeSure()) {
                sql.append(" and r.is_make_sure=" + rulec.getIsMakeSure() + " ");
            }
        }
        sql.append(" ORDER BY r.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询规则
     *
     * @param rule_id
     * @return
     */
    public TbRule loadTbRuleByIdInfo(int rule_id) {
        String hql = " from TbRule where ruleId=" + rule_id + " ";
        return (TbRule) commonDao.loadObj(hql);
    }

    /**
     * 查询卡券列表
     *
     * @param ticket
     * @return
     */
    public List<Map<String, Object>> listTicketsMap(TbTicket ticket) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tikcet_id,tikcet_name from tb_ticket where 1=1 ");
        if (null != ticket) {
            if (ticket.getStatus() != null) {
                sql.append(" and `status`=" + ticket.getStatus() + " ");
            }
            if (null != ticket.getTicketType()) {
                sql.append(" and ticket_type=" + ticket.getTicketType() + " ");
            }
            if (null != ticket.getShopId()) {
                sql.append(" and shop_id= " + ticket.getShopId() + " ");
            }
            if (null != ticket.getTikcetName()) {
                sql.append(" and tikcet_name like '%" + ticket.getTikcetName() + "%' ");
            }
        }
        sql.append(" and tikcet_id not in (SELECT rel_id from tb_rule_rel r where r.rel_id_type=2) ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 根据商品标识查询商品已勾选的属性值
     *
     * @param goodsId
     * @return
     */
    public Map<String, Object> listTbGoodsAttributeMap(Integer goodsId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("  from TbGoodsAttribute where goodsId=" + goodsId + " ");
        List<TbGoodsAttribute> list = new ArrayList<TbGoodsAttribute>();
        list = commonDao.queryListByHql(sql.toString());
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getValueId() + "", "ok");
            }
        }
        return map;
    }

    /**
     * 查询商品信息
     *
     * @param goodsId
     * @return
     */
    public TbGoods loadTbGoodsInfo(int goodsId) {
        String hql = " from TbGoods where goodsId=" + goodsId + " ";
        return (TbGoods) commonDao.loadObj(hql);
    }

    /**
     * @param goodsId
     * @param code
     * @return
     */
    public TbGoods checkGoodsCode(Integer goodsId, String code) {
        String hql = " from TbGoods where goodsCode='" + code + "' ";
        if (null != goodsId) {
            hql += " and goodsId!=" + goodsId + " ";
        }
        return (TbGoods) commonDao.loadObj(hql);
    }

    /**
     * 查询商品列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadTbGoodsSkuPage(PageUtil page, GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,if(sku.sku_name is null,g.goods_name,CONCAT(g.goods_name,'(',sku.sku_name,')')) as goods_name,g.title, ");
        sql.append(" CONCAT(if(sku.true_money is null,g.true_money,sku.true_money),'') as true_money,CONCAT(if(sku.now_money is null,g.now_money,sku.now_money),'') as now_money,g.intro,g.shop_id, ");
        sql.append(" if(sku.sku_img is null,if(g.goods_img is null or g.goods_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',g.goods_img)),CONCAT('" + path + "',sku.sku_img)) as goods_img , ");
        sql.append(" g.is_yuding,g.get_way,g.is_invoice, g.is_putaway,g.property, g.weight,g.classify_id,g.area_id,g.valid_date, ");
        sql.append(" DATE_FORMAT(g.start_time,'%Y-%m-%d') start_time,DATE_FORMAT(g.end_time,'%Y-%m-%d') end_time,  g.is_single,g.is_return, ");
        sql.append(" g.goods_code,g.goods_unit,if(sku.sku_num is null,g.goods_num,sku.sku_num) as goods_num,g.ticket_id,g.goods_type,s.shop_name,p.param_value as goods_type_name, ");
        sql.append(" DATE_FORMAT(g.createtime,'%Y-%m-%d %T') createtime,g.`status`, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d'),'0','1') is_end, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d'),'0','1') is_begin,pa.partners_name ");
        sql.append("  ,IFNULL(sku.sku_id,'0') as sku_id,ca.category_name,leve.level_name,CONCAT(if(is_single=1,g.purchase_price,sku.purchase_price),'') as purchase_price, ");
        sql.append(" g.is_time_goods,CONCAT(g.deposit_money,'') as deposit_money ");
        sql.append(" from tb_goods g LEFT JOIN tb_shop s on s.shop_id=g.shop_id LEFT JOIN tb_param p on p.param_key=g.goods_type and p.param_code='goods_type' and p.is_make_sure=1 ");
        sql.append(" LEFT JOIN tb_partners pa on pa.id=g.partners_id ");
        sql.append(" LEFT JOIN tb_goods_sku sku on sku.goods_id=g.goods_id ");
        sql.append(" LEFT JOIN tb_category ca on ca.category_id=g.classify_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=g.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sql.append(" where 1=1 ");
        String conditionSql = loadTbGoodsSkuPageCondition(goods);
        sql.append(conditionSql);
        sql.append(" ORDER BY g.weight desc,g.createtime desc ");

        StringBuffer sqlCount = new StringBuffer();
        sqlCount.append(" select count(*) ");
        sqlCount.append(" from tb_goods g LEFT JOIN tb_shop s on s.shop_id=g.shop_id LEFT JOIN tb_param p on p.param_key=g.goods_type and p.param_code='goods_type' and p.is_make_sure=1 ");
        sqlCount.append(" LEFT JOIN tb_partners pa on pa.id=g.partners_id ");
        sqlCount.append(" LEFT JOIN tb_goods_sku sku on sku.goods_id=g.goods_id ");
        sqlCount.append(" LEFT JOIN tb_category ca on ca.category_id=g.classify_id ");
        sqlCount.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=g.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sqlCount.append(" where 1=1 ");
        sqlCount.append(conditionSql);

        return commonDao.queryListMapSQLPageByField(sql.toString(), sqlCount.toString(), page.getCurrentPage(), page.getPageSize());
    }

    private String loadTbGoodsSkuPageCondition(GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        if (null != goods) {
            if (null != goods.getGoodsId()) {
                sql.append(" and g.goods_id=" + goods.getGoodsId() + " ");
            }
            if (goods.getGoodsName() != null) {
                sql.append(" and  g.goods_name like '%" + goods.getGoodsName() + "%'");
            }
            if (goods.getGoodsProperty() != null) {
                sql.append(" and g.property=" + goods.getGoodsProperty() + " ");
            }
            if (goods.getGoodsType() != null) {
                sql.append(" and g.goods_type=" + goods.getGoodsType() + "  ");
            }
            if (goods.getIsPutaway() != null) {
                sql.append(" and g.is_putaway=" + goods.getIsPutaway() + " ");
            }
            if (goods.getStatus() != null) {
                sql.append(" and g.status=" + goods.getStatus() + "  ");
            }
            if (null != goods.getpName()) {
                sql.append(" and pa.partners_name like '%" + goods.getpName() + "%' ");
            }
            if (null != goods.getIsSingle()) {
                sql.append(" and g.is_single=" + goods.getIsSingle() + "  ");
            }
            if (null != goods.getIsTimeGoods()) {
                sql.append(" and g.is_time_goods=" + goods.getIsTimeGoods() + "  ");
            }
            //商品结合状态
            if (null != goods.getGoodsType()) {
                if (goods.getGoodsStatus() == 1) {//长期有效商品
                    sql.append(" and g.valid_date=1  ");
                }
                if (goods.getGoodsStatus() == 2) {//未开始商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 3) {//已开始商品(未结束)
                    sql.append(" and ((DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(g.end_time,'%Y-%m-%d')) or (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and g.valid_date=1)) ");
                }
                if (goods.getGoodsStatus() == 4) {//已结束商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 5) {//限时有效商品
                    sql.append(" and g.valid_date=0 ");
                }
            }

            if (null != goods.getCatogryId() && 0 != goods.getCatogryId()) {
                sql.append(" and g.classify_id=" + goods.getCatogryId() + " ");
            }
            if (null != goods.getLeveId() && 0 != goods.getLeveId()) {
                sql.append(" and rel.level_id=" + goods.getLeveId() + " ");
            }
        }
        if (null != goods && null != goods.getShopId() && 0 != goods.getShopId()) {
            sql.append(" and g.shop_id=" + goods.getShopId() + " ");
        }
        return sql.toString();
    }

    /**
     * 查询商品sku
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> loadGoodsSkuList(Integer goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sku_id,sku_name,CONCAT(true_money,'') as true_money,CONCAT(now_money,'') as now_money,sku_code,prop_code,sku_num, ");
        sql.append(" CONCAT(purchase_price,'') as purchase_price,if(sku_img is null or sku_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',sku_img)) as sku_img ");
        sql.append(" from tb_goods_sku where goods_id=? ");
        return commonDao.getBySql2(sql.toString(), goodsId);
    }

    /**
     * 查询商品列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadTbGoodsSkuPage2(PageUtil page, GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,g.goods_name,g.title,CONCAT(g.true_money,'') as true_money,CONCAT(g.now_money,'') as now_money,g.intro,g.shop_id, ");
        sql.append(" if(g.goods_img is null or g.goods_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',g.goods_img)) as goods_img , ");
        sql.append(" g.is_yuding,g.get_way, g.is_invoice, g.is_putaway,g.property, g.weight,g.classify_id,g.area_id,g.valid_date, ");
        sql.append(" DATE_FORMAT(g.start_time,'%Y-%m-%d') start_time,DATE_FORMAT(g.end_time,'%Y-%m-%d') end_time,  g.is_single,g.is_return, ");
        sql.append(" g.goods_code,g.goods_unit,g.goods_num,g.ticket_id,g.goods_type,s.shop_name,p.param_value as goods_type_name, ");
        sql.append(" DATE_FORMAT(g.createtime,'%Y-%m-%d %T') createtime,g.`status`, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d'),'0','1') is_end, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d'),'0','1') is_begin,pa.partners_name, ");
        sql.append(" ca.category_name,leve.level_name,CONCAT(g.purchase_price) as purchase_price, ");
        sql.append(" g.is_time_goods,CONCAT(g.deposit_money,'') as deposit_money ");
        sql.append(" from tb_goods g LEFT JOIN tb_shop s on s.shop_id=g.shop_id LEFT JOIN tb_param p on p.param_key=g.goods_type and p.param_code='goods_type' and p.is_make_sure=1 ");
        sql.append(" LEFT JOIN tb_partners pa on pa.id=g.partners_id ");
        sql.append(" LEFT JOIN tb_category ca on ca.category_id=g.classify_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=g.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sql.append(" where 1=1 ");

        String conditionSql = loadTbGoodsSkuPage2Condition(goods);
        sql.append(conditionSql);
        sql.append(" ORDER BY g.weight desc,g.createtime desc ");

        StringBuffer sqlCount = new StringBuffer();
        sqlCount.append(" select count(*) from tb_goods g LEFT JOIN tb_shop s on s.shop_id=g.shop_id LEFT JOIN tb_param p on p.param_key=g.goods_type and p.param_code='goods_type' and p.is_make_sure=1 ");
        sqlCount.append(" LEFT JOIN tb_partners pa on pa.id=g.partners_id ");
        sqlCount.append(" LEFT JOIN tb_category ca on ca.category_id=g.classify_id ");
        sqlCount.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=g.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sqlCount.append(" where 1=1 ");

        return commonDao.queryListMapSQLPageByField(sql.toString(), sqlCount.toString(), page.getCurrentPage(), page.getPageSize());
    }

    private String loadTbGoodsSkuPage2Condition(GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        if (null != goods) {
            if (null != goods.getGoodsId()) {
                sql.append(" and g.goods_id=" + goods.getGoodsId() + " ");
            }
            if (goods.getGoodsName() != null) {
                sql.append(" and  g.goods_name like '%" + goods.getGoodsName() + "%'");
            }
            if (goods.getGoodsProperty() != null) {
                sql.append(" and g.property=" + goods.getGoodsProperty() + " ");
            }
            if (goods.getGoodsType() != null) {
                sql.append(" and g.goods_type=" + goods.getGoodsType() + "  ");
            }
            if (goods.getIsPutaway() != null) {
                sql.append(" and g.is_putaway=" + goods.getIsPutaway() + " ");
            }
            if (goods.getStatus() != null) {
                sql.append(" and g.status=" + goods.getStatus() + "  ");
            }
            if (null != goods.getpName()) {
                sql.append(" and pa.partners_name like '%" + goods.getpName() + "%' ");
            }
            if (null != goods.getIsSingle()) {
                sql.append(" and g.is_single=" + goods.getIsSingle() + "  ");
            }
            if (null != goods.getIsTimeGoods()) {
                sql.append(" and g.is_time_goods=" + goods.getIsTimeGoods() + "  ");
            }
            //商品结合状态
            if (null != goods.getGoodsType()) {
                if (goods.getGoodsStatus() == 1) {//长期有效商品
                    sql.append(" and g.valid_date=1  ");
                }
                if (goods.getGoodsStatus() == 2) {//未开始商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 3) {//已开始商品(未结束)
                    sql.append(" and ((DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(g.end_time,'%Y-%m-%d')) or (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and g.valid_date=1)) ");
                }
                if (goods.getGoodsStatus() == 4) {//已结束商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 5) {//限时有效商品
                    sql.append(" and g.valid_date=0 ");
                }
            }

            if (null != goods.getCatogryId() && 0 != goods.getCatogryId()) {
                sql.append(" and g.classify_id=" + goods.getCatogryId() + " ");
            }
            if (null != goods.getLeveId() && 0 != goods.getLeveId()) {
                sql.append(" and rel.level_id=" + goods.getLeveId() + " ");
            }
        }
        if (goods.getGetWay() != null) {
            sql.append(" and g.get_way=").append(goods.getGetWay()).append(" ");
        }
        if (null != goods && null != goods.getShopId() && 0 != goods.getShopId()) {
            sql.append(" and g.shop_id=" + goods.getShopId() + " ");
        }
        return sql.toString();

    }


    /**
     * 查询商品sku
     *
     * @param goodsId
     * @return
     */
    public List<TbGoodsSku> listGoodsSkuByGoodsId(Integer goodsId) {
        List<TbGoodsSku> listSku = new ArrayList<TbGoodsSku>();
        StringBuffer sql = new StringBuffer();
        sql.append(" from TbGoodsSku where goodsId=" + goodsId + " ");
        listSku = commonDao.queryListByHql(sql.toString());
        return listSku;
    }

    /**
     * 查询商品图片
     *
     * @param relateId
     * @param imgClassyfi 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public List<Map<String, Object>> listImgsByReleteIdMap(Integer relateId, Integer imgClassyfi) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT img_id,img_url from tb_img where relate_id=? and img_classyfi=? ORDER BY createtime ");
        return commonDao.getBySql2(sql.toString(), relateId, imgClassyfi);
    }

    public List<Map<String, Object>> listImgsByReleteIdMap2(Integer relateId, Integer imgClassyfi, Integer orderType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT img_id,img_url from tb_img where relate_id=? and img_classyfi=? and img_type=? ORDER BY createtime ");
        return commonDao.getBySql2(sql.toString(), relateId, imgClassyfi, orderType);
    }

    public List<Map<String, Object>> listImgsByReleteIdMap3(Integer relateId, Integer imgClassyfi, Integer orderType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT img_id,img_url from tb_img where relate_id=? and img_classyfi=? and img_type=? ORDER BY img_sort desc ");
        return commonDao.getBySql2(sql.toString(), relateId, imgClassyfi, orderType);
    }

    /**
     * 查询卡券信息
     *
     * @param tikcetId
     * @return
     */
    public TbTicket loadTicketInfo(Integer tikcetId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from TbTicket where tikcetId=" + tikcetId + " ");
        return (TbTicket) commonDao.loadObj(hql.toString());
    }

    /**
     * 根据卡券标识查询卡券已勾选的属性值
     *
     * @return
     */
    public Map<String, Object> listTbTicketAttributeMap(Integer ticketId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("  from TbTicketAttribute where ticketId=" + ticketId + " ");
        List<TbTicketAttribute> list = new ArrayList<TbTicketAttribute>();
        list = commonDao.queryListByHql(sql.toString());
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getValueId() + "", "ok");
            }
        }
        return map;
    }

    /**
     * 查询卡券列表
     *
     * @param page
     * @param ticket
     * @return
     */
    public PageUtil loadticketPage(PageUtil page, TicketCondition ticket) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT t.tikcet_id,t.tikcet_name, ");
        sql.append(" if(t.ticket_img is null or t.ticket_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',t.ticket_img)) as ticket_img , ");
        sql.append(" DATE_FORMAT(t.start_time,'%Y-%m-%d') as start_time,DATE_FORMAT(t.end_time,'%Y-%m-%d') end_time, ");
        sql.append(" t.ticket_num,t.weight,t.ticket_type,CONCAT(t.ticket_value,'') as ticket_value,t.is_show,t.out_date, ");
        sql.append(" t.`status`,DATE_FORMAT(t.createtime,'%Y-%m-%d %T') as createtime,t.valid_date,p.partners_name,s.shop_name,ca.category_name,leve.level_name,t.is_putaway ");
        sql.append(" from tb_ticket t LEFT JOIN tb_partners p on p.id=t.partners_id ");
        sql.append(" left join tb_shop s on s.shop_id=t.shop_id ");
        sql.append(" LEFT JOIN tb_category ca on ca.category_id=t.classify_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=t.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sql.append(" where 1=1 ");

        if (null != ticket) {
            if (null != ticket.getTikcetName()) {
                sql.append(" and  t.tikcet_name like '%" + ticket.getTikcetName() + "%'");
            }
            if (null != ticket.getTicketType()) {
                sql.append(" and t.ticket_type=" + ticket.getTicketType() + " ");
            }
            if (null != ticket.getStatus()) {
                sql.append(" and t.status=" + ticket.getStatus() + " ");
            }
            if (null != ticket.getIsShow()) {
                sql.append(" and t.is_show=" + ticket.getIsShow() + " ");
            }
            if (ticket.getpName() != null) {
                sql.append(" and p.partners_name like '%" + ticket.getpName() + "%'");
            }
            if (null != ticket.getTicketStatus()) {
                //1:'长期有效卡券',2:'未开始卡券',3:'已开始卡券(未结束)',4:'已结束卡券',5:'限时有效卡券',6:'兑换已截至'
                if (ticket.getTicketStatus() == 1) {//1:'长期有效卡券''
                    sql.append(" and t.valid_date=1 ");
                }
                if (ticket.getTicketStatus() == 2) {//2:'未开始卡券'
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(t.start_time,'%Y-%m-%d') ");
                }
                if (ticket.getTicketStatus() == 3) {//,3:'已开始卡券(未结束)'
                    sql.append(" and ((DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(t.end_time,'%Y-%m-%d') and DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(t.start_time,'%Y-%m-%d')) or (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(t.start_time,'%Y-%m-%d') and t.valid_date=1)) ");
                }
                if (ticket.getTicketStatus() == 4) {//4:'已结束卡券'
                    sql.append(" DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(t.end_time,'%Y-%m-%d') ");
                }
                if (ticket.getTicketStatus() == 5) {//5:'限时有效卡券'
                    sql.append(" and t.valid_date=0 ");
                }
                if (ticket.getTicketStatus() == 6) {//6:'兑换已截至'
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d %T')>DATE_FORMAT(t.out_of_time,'%Y-%m-%d %T') ");
                }
            }
            if (null != ticket.getIsPutaway()) {
                sql.append(" and t.is_putaway=" + ticket.getIsPutaway() + " ");
            }
        }
        if (0 != ticket.getShopId()) {
            sql.append(" and t.shop_id=" + ticket.getShopId() + " ");
        }

        if (null != ticket.getCatogryId() && 0 != ticket.getCatogryId()) {
            sql.append(" and t.classify_id=" + ticket.getCatogryId() + " ");
        }
        if (null != ticket.getLeveId() && 0 != ticket.getLeveId()) {
            sql.append(" and rel.level_id=" + ticket.getLeveId() + " ");
        }

        sql.append(" ORDER BY t.weight desc,t.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }


    /**
     * 查询规则列表
     *
     * @return
     */
    public List<Map<String, Object>> listRuleMap(TbRule rule) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT rule_id,rule_name from tb_rule where 1=1 ");
        if (null != rule) {
            if (rule.getStatus() != null) {
                sql.append(" and status=" + rule.getStatus() + " ");
            }
            if (null != rule.getRuleName()) {
                sql.append(" and rule_name like '%" + rule.getRuleName() + "%' ");
            }
            if (rule.getRuleBelong() == null) {
                sql.append(" and rule_belong=0 ");
            }
            if (null != rule.getRuleBelong()) {
                sql.append(" and (rule_belong=0 or rule_belong=" + rule.getRuleBelong() + ") ");
            }
            if (null != rule.getRuleType()) {
                sql.append(" and rule_type=" + rule.getRuleType() + " ");
            }
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商品列表
     *
     * @param goods
     * @return
     */
    public List<Map<String, Object>> listGoodsMap(TbGoods goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT goods_name,goods_id from tb_goods where 1=1 ");
        if (null != goods) {
            if (null != goods.getStatus()) {
                sql.append(" and status=" + goods.getStatus() + "  ");
            }
            if (null != goods.getGoodsName()) {
                sql.append(" and goods_name like '%" + goods.getGoodsName() + "%' ");
            }
            if (null != goods.getShopId()) {
                sql.append(" and shop_id =" + goods.getShopId() + " ");
            }
        }
        sql.append(" and goods_id  not in (SELECT rel_id from tb_rule_rel where rel_id_type=1) ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商家/商品/卡券规则列表
     *
     * @param relId
     * @return type 关联标识类型 0:商家规则1商品规则2卡券规则
     */
    public List<Map<String, Object>> listRelRule(Integer type, Integer relId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT r.rule_name from tb_rule_rel rel JOIN tb_rule r on r.rule_id=rel.rule_id where rel.rel_id_type=? and rel.is_make_sure=1 and rel.rel_id=? ");
        return commonDao.getBySql2(sql.toString(), type, relId);
    }


    /**
     * 查询卡券规则列表
     *
     * @param page
     * @param ruleName
     * @param ticketName
     * @return
     */
    public PageUtil loadDataTicketRuleRle(PageUtil page, String ruleName, String ticketName, Integer timeLimit, Integer shopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,t.tikcet_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,re.is_make_sure ");
        sql.append(" from tb_rule_rel re JOIN tb_ticket t on t.tikcet_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=2 ");
        if (null != shopId && shopId != 0) {
            sql.append(" and t.shop_id=" + shopId + " ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (ticketName != null && !"".equals(ticketName)) {
            sql.append(" and t.tikcet_name like '%" + ticketName + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and re.valid_date=1)) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询卡券规则列表（新）
     *
     * @param page
     * @param ruleName
     * @param ticketName
     * @return
     */
    public PageUtil loadTicletRulePage(PageUtil page, String ruleName, String ticketName, Integer timeLimit, Integer userShopId, String ticketShopName, String partnersName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,t.tikcet_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date, ");
        sql.append(" re.is_make_sure,s.shop_name,p.partners_name ");
        sql.append(" from tb_rule_rel re JOIN tb_ticket t on t.tikcet_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=t.shop_id JOIN tb_partners p on p.id=t.partners_id ");
        sql.append(" where 1=1 and re.rel_id_type=2 ");
        if (null != userShopId && userShopId != 0) {
            sql.append(" and t.shop_id=" + userShopId + " ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (ticketName != null && !"".equals(ticketName)) {
            sql.append(" and t.tikcet_name like '%" + ticketName + "%' ");
        }
        if (null != ticketShopName && !"".equals(ticketShopName)) {
            sql.append(" and s.shop_name like '%" + ticketShopName.trim() + "%' ");
        }
        if (null != partnersName && !"".equals(partnersName)) {
            sql.append(" and p.partners_name like '%" + partnersName.trim() + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and re.valid_date=1)) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询商品规则列表
     *
     * @param page
     * @param ruleName
     * @param goodsName
     * @return
     */
    public PageUtil loadDataGoodsRuleRle(PageUtil page, String ruleName, String goodsName, Integer timeLimit, Integer shopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,g.goods_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,re.is_make_sure ");
        sql.append(" from tb_rule_rel re JOIN tb_goods g on g.goods_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=1 ");
        if (null != shopId && shopId != 0) {
            sql.append(" and g.shop_id=" + shopId + " ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (goodsName != null && !"".equals(goodsName)) {
            sql.append(" and g.goods_name like '%" + goodsName + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and re.valid_date=1)) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询商品规则列表（新）
     *
     * @param page
     * @param ruleName
     * @param goodsName
     * @return
     */
    public PageUtil loadGoodsRulePage(PageUtil page, String ruleName, String goodsName, Integer timeLimit, Integer userShopId, String goodsShopName, String partnersName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,g.goods_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date, ");
        sql.append(" re.is_make_sure,s.shop_name,p.partners_name ");
        sql.append(" from tb_rule_rel re JOIN tb_goods g on g.goods_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id JOIN tb_partners p on p.id=g.partners_id ");
        sql.append(" where 1=1 and re.rel_id_type=1 ");
        if (null != userShopId && userShopId != 0) {
            sql.append(" and g.shop_id=" + userShopId + " ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (goodsName != null && !"".equals(goodsName)) {
            sql.append(" and g.goods_name like '%" + goodsName + "%' ");
        }
        if (null != goodsShopName && !"".equals(goodsShopName)) {
            sql.append(" and s.shop_name like '%" + goodsShopName.trim() + "%' ");
        }
        if (null != partnersName && !"".equals(partnersName)) {
            sql.append(" and p.partners_name like '%" + partnersName.trim() + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and re.valid_date=1)) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询商家规则列表
     *
     * @param page
     * @param ruleName
     * @param shopName
     * @return
     */
    public PageUtil loadDataPartnersRuleRle(PageUtil page, String ruleName, String shopName, Integer timeLimit, Integer partnerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,p.partners_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,re.is_make_sure ");
        sql.append(" from tb_rule_rel re JOIN tb_partners p on p.id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=0 ");
        if (null != partnerId && 0 != partnerId) {
            sql.append(" and p.id=" + partnerId + " ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (shopName != null && !"".equals(shopName)) {
            sql.append(" and s.shop_name like '%" + shopName + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (re.valid_date=1 and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T'))) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询商家规则列表(新)
     *
     * @param page
     * @param ruleName
     * @param partnerName
     * @return
     */
    public PageUtil loadPartnersRulePage(PageUtil page, String ruleName, String partnerName, Integer timeLimit, Integer userShopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,p.partners_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,re.is_make_sure ");
        sql.append(" from tb_rule_rel re JOIN tb_partners p on p.id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=0 ");
        if (null != userShopId && 0 != userShopId) {
            sql.append(" AND re.rel_id IN (  SELECT id from tb_partners p where p.shop_id=" + userShopId + " or p.id in (SELECT partners_id from tb_shop_partners_rel where shop_id=" + userShopId + ")) ");
        }
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and ru.rule_name like '%" + ruleName + "%' ");
        }
        if (null != partnerName && !"".equals(partnerName)) {
            sql.append(" and p.partners_name like '%" + partnerName.trim() + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (re.valid_date=1 and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T'))) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询全部数据列表
     *
     * @param page
     * @param ruleName
     * @param typeId
     * @return
     */
    public PageUtil loadDataAllRuleRle(PageUtil page, String ruleName, String relName, Integer typeId, Integer timeLimit, Integer shopId, Integer partnerId, Integer isMakeSure) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT re.id,re.rel_name,re.rule_name,re.start_time,re.end_time,re.createtime,re.valid_date,re.type_id,re.is_make_sure from ( ");
        sql.append(" (SELECT re.id,t.tikcet_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,'3' as type_id,t.shop_id,re.is_make_sure  ");
        sql.append(" from tb_rule_rel re JOIN tb_ticket t on t.tikcet_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=2 ");
        if (null != shopId && shopId != 0) {
            sql.append(" and t.shop_id=" + shopId + " ");
        }
        if (null != isMakeSure) {
            sql.append(" and re.is_make_sure=0 ");
        }
        sql.append(" ) ");
        sql.append(" UNION  ");
        sql.append(" (SELECT re.id,g.goods_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,'2' as type_id,g.shop_id,re.is_make_sure  ");
        sql.append(" from tb_rule_rel re JOIN tb_goods g on g.goods_id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id ");
        sql.append(" where 1=1 and re.rel_id_type=1 ");
        if (null != shopId && shopId != 0) {
            sql.append(" and g.shop_id=" + shopId + " ");
        }
        if (null != isMakeSure) {
            sql.append(" and re.is_make_sure=0 ");
        }
        sql.append(" ) ");
        sql.append(" UNION ");
        sql.append(" (SELECT re.id,p.partners_name as rel_name,ru.rule_name,DATE_FORMAT(re.start_time,'%Y-%m-%d %T') as start_time, ");
        sql.append(" DATE_FORMAT(re.end_time,'%Y-%m-%d %T') as end_time,DATE_FORMAT(re.createtime,'%Y-%m-%d %T') as createtime,re.valid_date,'1' as type_id,p.id,re.is_make_sure ");
        sql.append(" from tb_rule_rel re JOIN tb_partners p on p.id=re.rel_id JOIN tb_rule ru on ru.rule_id=re.rule_id  ");
        sql.append(" where 1=1 and re.rel_id_type=0 ");
        if (null != partnerId && partnerId != 0) {
            sql.append(" and p.id=" + partnerId + " ");
        }
        if (null != isMakeSure) {
            sql.append(" and re.is_make_sure=0 ");
        }
        sql.append(" ) ");
        sql.append(" ) re where 1=1 ");
        if (ruleName != null && !"".equals(ruleName)) {
            sql.append(" and re.rule_name like '%" + ruleName + "%' ");
        }
        if (relName != null && !"".equals(relName)) {
            sql.append(" and re.rel_name like '%" + relName + "%' ");
        }
        if (null != timeLimit && -1 != timeLimit) {//1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'
            if (timeLimit == 1) {
                sql.append(" and re.valid_date=1 ");
            }
            if (timeLimit == 2) {
                sql.append(" and DATE_FORMAT(re.start_time,'%Y-%m-%d %T')>DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 3) {
                sql.append(" and ((DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T')) or (DATE_FORMAT(re.start_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and re.valid_date=1)) ");
            }
            if (timeLimit == 4) {
                sql.append(" and DATE_FORMAT(re.end_time,'%Y-%m-%d %T')<DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            }
            if (timeLimit == 5) {
                sql.append(" and re.valid_date=0 ");
            }
        }
        if (null != typeId && -1 != typeId) {
            sql.append(" and re.type_id=" + typeId + " ");
        }
        sql.append(" ORDER BY re.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询卡券是否已经存在规则关联(卡券是多(规则)对一(卡券))
     *
     * @param relId
     * @return
     */
    public boolean checkTicketRelIsExsit(int relId) {
        String sql = " SELECT id from tb_rule_rel where rel_id=? and rel_id_type=2 ";
        List<Map<String, Object>> list = commonDao.getBySql2(sql, relId);
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询是否已经存在规则关联
     *
     * @param ruleId
     * @param relId
     * @param typeId
     * @return
     */
    public boolean checkRelIsExsit(int ruleId, int relId, int typeId) {
        String sql = " SELECT id from tb_rule_rel where rel_id=? and rel_id_type=? and rule_id=? ";
        List<Map<String, Object>> list = commonDao.getBySql2(sql, relId, typeId, ruleId);
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询卡券列表
     *
     * @param userShopId
     * @param ticketName
     * @return
     */
    public List<Map<String, Object>> listTicketRulerel(Integer userShopId, String ticketName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tikcet_id,tikcet_name from tb_ticket where status=1 and shop_id=" + userShopId + " ");
        if (null != ticketName && !"".equals(ticketName.trim())) {
            sql.append(" and tikcet_name like '%" + ticketName.trim() + "%' ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询卡券规则列表
     *
     * @param ticketId
     * @return
     */
    public List<Map<String, Object>> listTicketRuelRel(Integer ticketId, String ruleName, Integer userShopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT rule_id,rule_name  from tb_rule r   ");
        sql.append(" where r.rule_type=2 and r.`status`=1 and (rule_belong=0 or rule_belong in  ");
        sql.append(" (SELECT id from tb_partners p where p.shop_id=" + userShopId + " or p.id in (SELECT partners_id from tb_shop_partners_rel where shop_id=" + userShopId + ")))  ");
        if (null != ticketId && 0 != ticketId) {
            sql.append(" and r.rule_id not in (SELECT rule_id from tb_rule_rel rel where rel.rel_id_type=2 and rel.rel_id=" + ticketId + ")  ");
        }
        if (null != ruleName && !"".equals(ruleName.trim())) {
            sql.append(" and r.rule_name like '%" + ruleName.trim() + "%'  ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商品列表
     *
     * @param userShopId
     * @param goodsName
     * @return
     */
    public List<Map<String, Object>> listGoodsRulerel(Integer userShopId, String goodsName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT goods_id,goods_name from tb_goods where status=1 and shop_id=" + userShopId + " ");
        if (null != goodsName && !"".equals(goodsName.trim())) {
            sql.append(" and goods_name like '%" + goodsName.trim() + "%' ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商品规则列表
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> listGoodsRuelRel(Integer goodsId, String ruleName, Integer userShopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT rule_id,rule_name  from tb_rule r   ");
        sql.append(" where r.rule_type=1 and r.`status`=1 and (rule_belong=0 or rule_belong in  ");
        sql.append(" (SELECT id from tb_partners p where p.shop_id=" + userShopId + " or p.id in (SELECT partners_id from tb_shop_partners_rel where shop_id=" + userShopId + ")))  ");
        if (null != goodsId && 0 != goodsId) {
            sql.append(" and r.rule_id not in (SELECT rule_id from tb_rule_rel rel where rel.rel_id_type=1 and rel.rel_id=" + goodsId + ")  ");
        }
        if (null != ruleName && !"".equals(ruleName.trim())) {
            sql.append(" and r.rule_name like '%" + ruleName.trim() + "%'  ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商家列表
     *
     * @param userShopId
     * @param partnersName
     * @return
     */
    public List<Map<String, Object>> listPartnersRulerel(Integer userShopId, String partnersName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,partners_name from tb_partners where 1=1 and status=1  ");
        sql.append(" and (shop_id=" + userShopId + " or id in (SELECT partners_id from tb_shop_partners_rel where shop_id=" + userShopId + ")) ");
        if (null != partnersName && !"".equals(partnersName.trim())) {
            sql.append(" and partners_name like '%" + partnersName.trim() + "%' ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商家规则
     *
     * @param partnersId
     * @return
     */
    public List<Map<String, Object>> listPartnreesRuelRel(Integer partnersId, String ruleName, Integer userShopId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT rule_id,rule_name  from tb_rule r   ");
        sql.append(" where r.rule_type=0 and r.`status`=1 and (rule_belong=0 or rule_belong in  ");
        sql.append(" (SELECT id from tb_partners p where p.shop_id=" + userShopId + " or p.id in (SELECT partners_id from tb_shop_partners_rel where shop_id=" + userShopId + ")))  ");
        if (null != partnersId && 0 != partnersId) {
            sql.append(" and r.rule_id not in (SELECT rule_id from tb_rule_rel rel where rel.rel_id_type=0 and rel.rel_id=" + partnersId + ")  ");
        }
        if (null != ruleName && !"".equals(ruleName.trim())) {
            sql.append(" and r.rule_name like '%" + ruleName.trim() + "%'  ");
        }
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 分页查询商家列表
     *
     * @param page
     * @param partners
     * @return
     */
    public PageUtil loadPartnersPage(PageUtil page, PartnersCondition partners) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.id,p.partners_code,p.partners_name,DATE_FORMAT(p.createtime,'%Y-%m-%d %T') as createtime,s.shop_name,p.`status`, ");
        sql.append(" p.partners_sort,p.first_name_code,p.account, ");
        sql.append(" if(p.partners_img is null or p.partners_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',p.partners_img)) as img  ");
        sql.append(" from tb_partners p LEFT JOIN tb_shop s on s.shop_id=p.shop_id where 1=1 ");

        if (null != partners) {
            if (null != partners.getPartnersCode()) {
                sql.append(" and p.partners_code like '%" + partners.getPartnersCode() + "%'  ");
            }
            if (null != partners.getShopName()) {
                sql.append(" and s.shop_name like '%" + partners.getShopName() + "%' ");
            }
            if (null != partners.getPartnersName()) {
                sql.append(" and p.partners_name like '%" + partners.getPartnersName() + "%' ");
            }
            if (null != partners.getStatus()) {
                sql.append(" and p.status=" + partners.getStatus() + " ");
            }
        }
        sql.append(" ORDER BY p.partners_sort desc,p.createtime DESC ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询商家
     *
     * @param id
     * @return
     */
    public TbPartners loadTbPartnersByIdInfo(Integer id) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from TbPartners where id=" + id + " ");
        return (TbPartners) commonDao.loadObj(hql.toString());

    }

    public TbPartners loadTbPartnersByShopIdInfo(Integer shopId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from TbPartners where shopId=" + shopId + " ");
        return (TbPartners) commonDao.loadObj(hql.toString());

    }

    /**
     * 检查合用商编码是否已经存在
     *
     * @param id
     * @param code
     * @return
     */
    public boolean checkPartnersCode(Integer id, String code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id from tb_partners where partners_code='" + code + "' ");
        if (null != id && id != 0) {
            sql.append(" and id !=" + id + " ");
        }
        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString());
        if (list != null && list.size() == 0) {
            return false;
        }

        return true;
    }

    /**
     * 查询商家列表
     *
     * @param partners
     * @return
     */
    public List<Map<String, Object>> listPartnersMap(TbPartners partners) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,partners_name from tb_partners where 1=1 ");
        if (null != partners) {
            if (null != partners.getStatus()) {
                sql.append(" and status=" + partners.getStatus() + "  ");
            }
            if (partners.getShopId() != null) {
                sql.append(" and shop_id=" + partners.getShopId() + "  ");
            }
            if (null != partners.getPartnersName()) {
                sql.append(" and partners_name like '%" + partners.getPartnersName() + "%' ");
            }
        }
        sql.append("  and id not in (SELECT rel_id from tb_rule_rel where rel_id_type=0) ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 检查店铺是否已经关联有商家
     *
     * @param shopId
     * @param partnersId
     * @return
     */
    public boolean checkPartnersTicket(Integer shopId, Integer partnersId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id from tb_partners where 1=1 ");
        if (shopId != null && 0 != shopId) {
            sql.append(" and shop_id=" + shopId + " ");
        }
        if (partnersId != null && 0 != partnersId) {
            sql.append(" and id!=" + partnersId + " ");
        }
        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString());
        if (list != null && list.size() == 0) {
            return false;
        }

        return true;
    }

    /**
     * 检查商家是否已经关联有商家
     *
     * @param partnersId
     * @return
     */
    public boolean checkPartnersAccount(Integer partnersId, String account) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id from tb_partners where account='" + account + "' ");
        if (null != partnersId && partnersId != 0) {
            sql.append(" and id !=" + partnersId + " ");
        }
        List<Map<String, Object>> list = commonDao.getBySql2(sql.toString());
        if (list != null && list.size() == 0) {
            return false;
        }

        return true;
    }

    /**
     * 查询兑换权限列表
     *
     * @param page
     * @param pName      商家名称
     * @param ticketName 卡券名称
     * @return
     */
    public PageUtil loadExchangeJurisdictionPage(PageUtil page, String pName, String ticketName, String shopName, String isMakeSure) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT e.id,t.tikcet_name,p.partners_name ,DATE_FORMAT(e.createtime,'%Y-%m-%d %T') as createtime,p.id as p_id,t.tikcet_id,s.shop_name, ");
        sql.append(" e.valid_date,DATE_FORMAT(e.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(e.end_time,'%Y-%m-%d %T') as end_time,e.is_make_sure ");
        sql.append("   from tb_ticket t  LEFT JOIN tb_exchange_jurisdiction e on e.tikcet_id=t.tikcet_id LEFT JOIN tb_partners p on p.id=e.partners_id  ");
        sql.append(" LEFT JOIN tb_shop s on s.shop_id=t.shop_id ");
        sql.append(" where 1=1 and t.`status`=1  ");
        if (null != pName && !"".equals(pName.trim()) && !"null".equals(pName)) {
            sql.append(" and p.partners_name like '%" + pName + "%' ");
        }
        if (null != ticketName && !"".equals(ticketName.trim()) && !"null".equals(ticketName)) {
            sql.append(" and t.tikcet_name like '%" + ticketName + "%' ");
        }
        if (null != shopName && !"".equals(shopName.trim()) && !"null".equals(shopName)) {
            sql.append(" and s.shop_name like '%" + shopName + "%' ");
        }
        if (null != isMakeSure && !"".equals(isMakeSure) && !"null".equals(isMakeSure)) {
            sql.append("  and e.is_make_sure=" + isMakeSure + " ");
        }
        sql.append(" ORDER BY e.createtime desc,t.tikcet_id desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }


    /**
     * 根据ticketId查询没有兑换权限的商家
     *
     * @param ticketId
     * @return
     */
    public List<Map<String, Object>> listTicketPartnersByTicketId(Integer ticketId, String pName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,partners_name from tb_partners where `status`=1 ");
        if (null != pName && !"".equals(pName) && !"null".equals(pName)) {
            sql.append(" and partners_name like '%" + pName + "%' ");
        }
        if (null != ticketId && 0 != ticketId) {
            sql.append(" and  id not in (SELECT partners_id from tb_exchange_jurisdiction where tikcet_id=" + ticketId + ") ");
        }
        return commonDao.getBySql2(sql.toString());

    }

    /**
     * 查询一级分类列表
     *
     * @param page
     * @param leve
     * @return
     */
    public PageUtil loadCatogryLeve1ListPage(PageUtil page, CatogryLeve1Condition leve) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.level_id,ca.level_code,ca.level_name,ca.level_sort,ca.`status`,CONCAT(c.category_name,'(',c.category_code,')') as category_name,DATE_FORMAT(ca.createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" if(ca.level_img is null,CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',ca.level_img)) as level_img ");
        sql.append(" from tb_catogry_level1 ca ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel crl on crl.level_id=ca.level_id LEFT JOIN tb_category c on c.category_id=crl.category_id ");
        sql.append(" where 1=1 ");
        if (null != leve) {
            if (null != leve.getLevelName() && !"null".equals(leve.getLevelName()) && !"".equals(leve.getLevelName())) {
                sql.append(" and ca.level_name like '%" + leve.getLevelName().trim() + "%' ");
            }
            if (null != leve.getLevelCode() && !"null".equals(leve.getLevelCode()) && !"".equals(leve.getLevelCode())) {
                sql.append(" and ca.level_code like '%" + leve.getLevelCode().trim() + "%' ");
            }
            if (null != leve.getCategoryId() && 0 != leve.getCategoryId()) {
                sql.append(" and crl.category_id=" + leve.getCategoryId() + " ");
            }
            if (null != leve.getCategoryCode() && !"".equals(leve.getCategoryCode())) {
                sql.append(" and c.category_code='" + leve.getCategoryCode().trim() + "' ");
            }
        }
        sql.append(" ORDER BY ca.level_sort desc ,ca.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询一级分类列表
     *
     * @param page
     * @param leve
     * @return
     */
    public PageUtil loadCatogryLeve1ListPage2(PageUtil page, CatogryLeve1Condition leve) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level1.level_id,level1.level_name,level1.level_code,level1.level_sort,DATE_FORMAT(level1.createtime,'%Y-%m-%d %T') as createtime,level1.`status`, ");
        sql.append(" if(level1.level_img is null,CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',level1.level_img)) as level1_img ");
        sql.append(" from tb_catogry_level1 level1  where 1=1 ");
        if (null != leve.getLevelName() && !"".equals(leve.getLevelName().trim())) {
            sql.append(" and level1.level_name like '%" + leve.getLevelName().trim() + "%' ");
        }
        if (null != leve.getLevelCode() && !"".equals(leve.getLevelCode().trim())) {
            sql.append(" and level1.level_code ='" + leve.getLevelCode().trim() + "' ");
        }
        if (null != leve.getStatus()) {
            sql.append(" and level1.`status`=" + leve.getStatus() + " ");
        }
        if (null != leve.getCategoryId() && 0 != leve.getCategoryId()) {
            sql.append(" and level1.level_id in (SELECT rel.level_id from tb_category ca JOIN tb_classyfy_catogry_rel rel on rel.category_id=ca.category_id where ca.category_id=" + leve.getCategoryId() + ") ");
        }
        if (null != leve.getCategoryCode() && !"".equals(leve.getCategoryCode().trim())) {
            sql.append(" and level1.level_id in (SELECT rel.level_id from tb_category ca JOIN tb_classyfy_catogry_rel rel on rel.category_id=ca.category_id where ca.category_name like '%" + leve.getCategoryCode().trim() + "%' or ca.category_code like '%" + leve.getCategoryCode().trim() + "%') ");
        }
        sql.append(" ORDER BY level1.level_sort desc,level1.createtime desc ");
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    public List<Map<String, Object>> listTbCatogryLevel1() {
        String sql = " SELECT level_id,level_name from tb_catogry_level1 where `status`=1 ";
        return commonDao.getBySql2(sql);
    }


    /**
     * 查询一级菜单
     *
     * @return
     */
    public TbCatogryLevel1 loadCatogryLeve1Info(int levelId) {
        String hql = " from TbCatogryLevel1 where levelId=" + levelId + " ";
        return (TbCatogryLevel1) commonDao.loadObj(hql);
    }

    /**
     * 检测分类编码是否存在
     *
     * @param leveId
     * @param code
     * @return
     */
    public boolean checkCatogryLeveCodeIsExist(String leveId, String code) {

        String hql = " from TbCatogryLevel1 where 1=1 ";
        hql = hql + " and levelCode='" + code + "' ";
        if (null != leveId && !"".equals(leveId) && !"null".equals(leveId) && !"0".equals(leveId)) {
            hql = hql + " and levelId !=" + Integer.parseInt(leveId) + " ";
        }
        TbCatogryLevel1 leve = (TbCatogryLevel1) commonDao.loadObj(hql);
        if (leve == null) {
            return false;
        }

        return true;
    }

    /**
     * 查询分类类目关系
     *
     * @return
     */
    public List<Map<String, Object>> listClassfyCatogryMap(String keyWord, Integer leveId) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_name,ca.category_code,CONCAT(ca.category_name,'(',ca.category_code,')') as category_name_code,carel.level_id ");
        sql.append(" from tb_category ca LEFT JOIN tb_classyfy_catogry_rel carel on ca.category_id=carel.category_id where ca.`status`=1 ");
        if (null != keyWord && !"".equals(keyWord) && !"null".equals(keyWord)) {
            sql.append(" and (ca.category_name like '%" + keyWord.trim() + "%' or ca.category_code like '%" + keyWord.trim() + "%') ");
        }
        if (null != leveId && 0 != leveId) {
            sql.append(" and carel.level_id=" + leveId + " ");
        }
        sql.append(" ORDER BY ca.short desc ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 加载一级分类
     *
     * @return
     */
    public List<Map<String, Object>> listClassfyMap(Integer catogryId) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT l.level_id,l.level_name ");
        if (null != catogryId && 0 != catogryId) {
            sql.append(" ,rel.category_id ");
        }
        sql.append(" from tb_catogry_level1 l ");
        if (null != catogryId && 0 != catogryId) {
            sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.level_id=l.level_id ");
        }
        sql.append(" where 1=1 and l.status=1  ");
        if (null != catogryId && 0 != catogryId) {
            sql.append(" and rel.category_id=" + catogryId + " ");
        }
        sql.append(" order by l.level_sort desc ");
        return commonDao.getBySql2(sql.toString());
    }


    /**
     * 根据一级分类标识查询对应的类目
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> listClassfyCatogroyByClassfyId(Integer categoryId, String categroyName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_name,ca.category_code,CONCAT(ca.category_name,'(',ca.category_code,')') as category_name_code,carel.level_id,ca.short ");
        sql.append(" from tb_category ca  JOIN tb_classyfy_catogry_rel carel on ca.category_id=carel.category_id where ca.`status`=1 ");
        if (null != categoryId && 0 != categoryId) {
            sql.append(" and carel.level_id=" + categoryId + " ");
        }
        if (StringUtils.isNotEmpty(categroyName)) {
            sql.append(" and ca.category_name like '%" + categroyName.trim() + "%' ");
        }
        sql.append(" ORDER BY ca.short desc ");
        return commonDao.getBySql2(sql.toString());
    }

    /**
     * 查询商品sku列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadlistGoodsSKU(PageUtil page, GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sku.sku_id,sku.goods_id,g.goods_name,sku.sku_name,CONCAT(sku.true_money,'') as true_money,CONCAT(sku.now_money,'') as now_money,sku.sku_code,sku.prop_code,sku.sku_num, ");
        sql.append(" if(sku.sku_img is null or sku.sku_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',sku.sku_img)) as sku_img,CONCAT(sku.purchase_price,'') as purchasePrice  ");
        sql.append(" from tb_goods_sku sku JOIN tb_goods g on g.goods_id=sku.goods_id  ");
        if (null != goods.getShopId() && 0 != goods.getShopId()) {
            sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id and s.shop_id=" + goods.getShopId() + " ");
        }
        sql.append(" where 1=1 ");
        if (null != goods) {
            if (null != goods.getSkuName() && !"".equals(goods.getSkuName().trim()) && !"null".equals(goods.getSkuName())) {
                sql.append(" and sku.sku_name like '%" + goods.getSkuName().trim() + "%' ");
            }
            if (null != goods.getGoodsName() && !"".equals(goods.getGoodsName().trim()) && !"null".equals(goods.getGoodsName())) {
                sql.append(" and g.goods_name like '%" + goods.getGoodsName().trim() + "%' ");
            }
            if (null != goods.getPropCode() && !"".equals(goods.getPropCode().trim()) && !"null".equals(goods.getPropCode())) {
                sql.append(" and sku.prop_code like '%" + goods.getPropCode().trim() + "%' ");
            }
            if (goods.getGoodsId() != null) {
                sql.append(" and sku.goods_id=").append(goods.getGoodsId()).append(" ");
            }
        }
        sql.append(" ORDER BY sku.goods_id desc ");

        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查商品sku信息
     *
     * @param skuId
     * @return
     */
    public TbGoodsSku loadGoodsSKUInfo(int skuId) {
        String hql = " from TbGoodsSku where skuId=" + skuId + " ";
        return (TbGoodsSku) commonDao.loadObj(hql);
    }

    /**
     * 查询商品库存
     *
     * @param goodsId
     * @return
     */
    public Integer searchGoodsStoreById(Integer goodsId) {
        try {
            String sql = " SELECT SUM(sku_num) as goods_store from tb_goods_sku where goods_id=? ";
            List<Map<String, Object>> list = commonDao.getBySql2(sql, goodsId);
            Integer sum = 0;
            if (list != null && list.size() > 0) {
                sum = Integer.parseInt(list.get(0).get("goods_store").toString());
            }
            return sum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询修改密钥是否正确
     *
     * @param updateKey
     * @return
     */
    public TbSecretkey loadTbSecretkey(String updateKey) {
        String hql = " from TbSecretkey where updateKey='" + updateKey + "' and status=1 ";
        return (TbSecretkey) commonDao.loadObj(hql);
    }

    /**
     * 查询商品列表
     *
     * @param page
     * @param goods
     * @return
     */
    public PageUtil loadTbGoodsPage(PageUtil page, GoodsCondition goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,g.goods_name as goods_name,g.title, g.true_money as true_money,g.now_money as now_money,g.intro,g.shop_id, ");
        sql.append(" if(g.goods_img is null or g.goods_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',g.goods_img)) as goods_img , ");
        sql.append(" g.is_yuding,g.get_way, g.is_invoice, g.is_putaway,g.property, g.weight,g.classify_id,g.area_id,g.valid_date, ");
        sql.append(" DATE_FORMAT(g.start_time,'%Y-%m-%d') start_time,DATE_FORMAT(g.end_time,'%Y-%m-%d') end_time,   g.is_single,g.is_return, ");
        sql.append(" g.goods_code,g.goods_unit,g.goods_num as goods_num,g.ticket_id,g.goods_type,s.shop_name,p.param_value as goods_type_name, ");
        sql.append(" DATE_FORMAT(g.createtime,'%Y-%m-%d %T') createtime,g.`status`, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d'),'0','1') is_end, ");
        sql.append(" if(DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d'),'0','1') is_begin,pa.partners_name ");
        sql.append(" ,'0' as sku_id,ca.category_name,leve.level_name,g.purchase_price as purchase_price ");
        sql.append(" from tb_goods g LEFT JOIN tb_shop s on s.shop_id=g.shop_id LEFT JOIN tb_param p on p.param_key=g.goods_type and p.param_code='goods_type' and p.is_make_sure=1 ");
        sql.append(" LEFT JOIN tb_partners pa on pa.id=g.partners_id ");
        sql.append(" LEFT JOIN tb_category ca on ca.category_id=g.classify_id ");
        sql.append(" LEFT JOIN tb_classyfy_catogry_rel rel on rel.category_id=g.classify_id LEFT JOIN tb_catogry_level1 leve on leve.level_id=rel.level_id ");
        sql.append(" where 1=1 ");
        if (null != goods) {
            if (goods.getGoodsName() != null) {
                sql.append(" and  g.goods_name like '%" + goods.getGoodsName() + "%'");
            }
            if (goods.getGoodsProperty() != null) {
                sql.append(" and g.property=" + goods.getGoodsProperty() + " ");
            }
            if (goods.getGoodsType() != null) {
                sql.append(" and g.goods_type=" + goods.getGoodsType() + "  ");
            }
            if (goods.getIsPutaway() != null) {
                sql.append(" and g.is_putaway=" + goods.getIsPutaway() + " ");
            }
            if (goods.getStatus() != null) {
                sql.append(" and g.status=" + goods.getStatus() + "  ");
            }
            if (null != goods.getpName()) {
                sql.append(" and pa.partners_name like '%" + goods.getpName() + "%' ");
            }
            //商品结合状态
            if (null != goods.getGoodsType()) {
                if (goods.getGoodsStatus() == 1) {//长期有效商品
                    sql.append(" and g.valid_date=1  ");
                }
                if (goods.getGoodsStatus() == 2) {//未开始商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')<DATE_FORMAT(g.start_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 3) {//已开始商品(未结束)
                    sql.append(" and ((DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(g.end_time,'%Y-%m-%d')) or (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and g.valid_date=1)) ");
                }
                if (goods.getGoodsStatus() == 4) {//已结束商品
                    sql.append(" and DATE_FORMAT(NOW(),'%Y-%m-%d')>DATE_FORMAT(g.end_time,'%Y-%m-%d') ");
                }
                if (goods.getGoodsStatus() == 5) {//限时有效商品
                    sql.append(" and g.valid_date=0 ");
                }
            }

            if (null != goods.getCatogryId() && 0 != goods.getCatogryId()) {
                sql.append(" and g.classify_id=" + goods.getCatogryId() + " ");
            }
            if (null != goods.getLeveId() && 0 != goods.getLeveId()) {
                sql.append(" and rel.level_id=" + goods.getLeveId() + " ");
            }
        }
        if (null != goods && null != goods.getShopId() && 0 != goods.getShopId()) {
            sql.append(" and g.shop_id=" + goods.getShopId() + " ");
        }
        sql.append(" GROUP BY g.goods_id ORDER BY g.weight desc,g.createtime desc ");
        return commonDao.queryListMapSQLPageByField(sql.toString(), null, page.getCurrentPage(), page.getPageSize());
    }


    /**
     * 查询抢购商品列表
     *
     * @param page
     * @param goodsName
     * @param partnersName
     * @param status
     * @param isMakeSure
     */
    public PageUtil loadTimeGoodsPage(PageUtil page, String goodsName, String partnersName, Integer status, Integer userShopId, Integer isMakeSure) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tg.id,DATE_FORMAT(tg.begin_time,'%Y-%m-%d %T') as begin_time,DATE_FORMAT(tg.end_time,'%Y-%m-%d %T') as end_time, ");
        sql.append(" CONCAT(tg.time_price,'') as time_price,tg.goods_store,tg.limit_num,tg.time_way,tg.weight,tg.is_make_sure,DATE_FORMAT(tg.createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" tg.status,g.goods_name,if(g.goods_img is null or g.goods_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',g.goods_img)) as goods_img ,p.partners_name,s.shop_name ");
        sql.append(" ,tg.goods_id ");
        sql.append(" from tb_time_goods tg JOIN tb_goods g on g.goods_id=tg.goods_id ");
        sql.append(" JOIN tb_partners p on p.id=g.partners_id ");
        sql.append(" JOIN tb_shop s on s.shop_id=g.shop_id ");
        sql.append(" where 1=1 ");
        if (null != userShopId && 0 != userShopId) {
            sql.append(" and s.shop_id=" + userShopId + " ");
        }
        if (null != partnersName && !"".equals(partnersName.trim())) {
            sql.append(" and p.partners_name like '%" + partnersName.trim() + "%' ");
        }
        if (null != goodsName && !"".equals(goodsName.trim())) {
            sql.append(" and g.goods_name like '%" + goodsName.trim() + "%' ");
        }
        if (null != isMakeSure && -1 != isMakeSure) {
            sql.append(" and tg.is_make_sure=" + isMakeSure + " ");
        }
        if (status == 1) {
            sql.append(" and tg.`status`=1 ");
            sql.append(" and DATE_FORMAT(tg.begin_time,'%Y-%m-%d %T')<=DATE_FORMAT(NOW(),'%Y-%m-%d %T') and DATE_FORMAT(tg.end_time,'%Y-%m-%d %T')>=DATE_FORMAT(NOW(),'%Y-%m-%d %T') ");
            sql.append(" and g.`status`=1 and g.is_putaway=2 ");
            sql.append(" and ( (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and g.valid_date=1) OR ");
            sql.append(" (DATE_FORMAT(NOW(),'%Y-%m-%d')>=DATE_FORMAT(g.start_time,'%Y-%m-%d') and DATE_FORMAT(NOW(),'%Y-%m-%d')<=DATE_FORMAT(g.end_time,'%Y-%m-%d')) ) ");
            sql.append(" ORDER BY  tg.weight, tg.createtime desc ");
        } else {
            sql.append(" and tg.status=0 ");
        }
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    /**
     * 查询抢购商品
     *
     * @param goodsId
     * @return
     */
    public TbTimeGoods loadTbTimeGoodsBuyGoodsId(Integer goodsId) {
        String hql = " from TbTimeGoods where goodsId=" + goodsId + " and  status=1 ";
        return (TbTimeGoods) commonDao.loadObj(hql);
    }

    /**
     * 查询订单
     */
    public PageUtil loadOrderPageList(PageUtil page, OrderCondition orderCondition) {
        String sql = loadOrderPageListSql(orderCondition);

        StringBuffer sqlCount = new StringBuffer();
        sqlCount.append(" SELECT count(*) from tb_order o ");
        sqlCount.append(" LEFT JOIN tb_param pstatus on pstatus.param_key=o.order_status and pstatus.param_code='order_status' ");
        sqlCount.append(" LEFT JOIN tb_param pbusinestype on pbusinestype.param_key=o.business_type and pbusinestype.param_code='business_type' ");
        sqlCount.append(" LEFT JOIN tb_param adpaytype on adpaytype.param_key=o.advance_pay_way and adpaytype.param_code='pay_way' ");
        sqlCount.append("  LEFT JOIN tb_param paytype on paytype.param_key=o.pay_way and paytype.param_code='pay_way' ");
        sqlCount.append(" LEFT JOIN tb_param ppostway on ppostway.param_key=o.post_way and ppostway.param_code='post_way'");
        sqlCount.append("  where 1=1  ");
        sqlCount.append(loadOrderPageListSqlCondition(orderCondition));

        return commonDao.queryListMapSQLPageByField(sql, sqlCount.toString(), page.getCurrentPage(), page.getPageSize());
    }

    public TbOrder loadTbOrderById(int orderid) {
        String hql = " from TbOrder where orderId=? ";
        return (TbOrder) commonDao.queryUniqueByHql(hql, orderid);
    }

    public TbOrderDetail loadTbOrderDetailByDetailId(int detailId) {
        String hql = " from TbOrderDetail where detailId=? ";
        return (TbOrderDetail) commonDao.queryUniqueByHql(hql, detailId);
    }

    public List<Map<String, Object>> loadOrderListMap(OrderCondition orderCondition) {
        String sql = loadOrderPageListSql(orderCondition);
        return commonDao.getBySql2(sql);
    }

    private String loadOrderPageListSql(OrderCondition orderCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT o.order_id,o.user_id,o.order_no,o.order_from,o.goods_count,CONCAT(o.order_price,'') as order_price, ");
        sql.append(" CONCAT(o.real_price,'') as real_price,CONCAT(o.advance_price,'') as advance_price,CONCAT(o.needpay_price,'') as needpay_price,o.user_name, ");
        sql.append(" o.user_phone,o.user_addr,o.user_addr_id,o.advance_pay_way,o.advance_pay_code,DATE_FORMAT(o.advance_pay_time,'%Y-%m-%d %T') as advance_pay_time, ");
        sql.append(" o.pay_way,o.pay_code,DATE_FORMAT(o.pay_time,'%Y-%m-%d %T') as pay_time, ");
        sql.append(" o.trade_channel,CONCAT(o.post_price,'') as post_price,o.post_way,o.post_name,o.post_no,o.user_remark,o.service_remark, ");
        sql.append(" o.order_status,DATE_FORMAT(o.order_time,'%Y-%m-%d %T') as order_time,DATE_FORMAT(o.post_time,'%Y-%m-%d %T') as post_time, ");
        sql.append(" o.business_type,o.discounts_id,CONCAT(o.discounts_price,'') as discounts_price,o.discounts_remark,o.trade_no, ");
        sql.append(" CONCAT(o.refund_price,'') as refund_price,o.refund_remark,DATE_FORMAT(o.createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" DATE_FORMAT(o.lastaltertime,'%Y-%m-%d %T') as lastaltertime,o.lastalterman,o.remark,o.partners_id,o.partners_name,o.ticket_json, ");
        sql.append("  pstatus.param_value as order_status_name,pbusinestype.param_value as business_type_name,ppostway.param_value post_way_name, ");
        sql.append(" adpaytype.param_value as advance_pay_way_name,paytype.param_value as pay_way_name,   ");
        sql.append(" o.post_man,o.post_phone ");
        sql.append(" from tb_order o ");
        sql.append(" LEFT JOIN tb_param pstatus on pstatus.param_key=o.order_status and pstatus.param_code='order_status' ");
        sql.append(" LEFT JOIN tb_param pbusinestype on pbusinestype.param_key=o.business_type and pbusinestype.param_code='business_type' ");
        sql.append(" LEFT JOIN tb_param adpaytype on adpaytype.param_key=o.advance_pay_way and adpaytype.param_code='pay_way' ");
        sql.append("  LEFT JOIN tb_param paytype on paytype.param_key=o.pay_way and paytype.param_code='pay_way' ");
        sql.append(" LEFT JOIN tb_param ppostway on ppostway.param_key=o.post_way and ppostway.param_code='post_way' where 1=1 ");
        sql.append(loadOrderPageListSqlCondition(orderCondition));
        sql.append(" ORDER BY o.order_time desc ");
        return sql.toString();
    }

    private String loadOrderPageListSqlCondition(OrderCondition orderCondition) {
        StringBuffer sql = new StringBuffer();
        if (null != orderCondition.getOrderId()) {
            sql.append(" and o.order_id=" + orderCondition.getOrderId() + " ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getOrderNo())) {
            sql.append(" and o.order_no='" + orderCondition.getOrderNo() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getUserName())) {
            sql.append(" and o.user_name='" + orderCondition.getUserName() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getPhone())) {
            sql.append(" and o.user_phone='" + orderCondition.getPhone() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getBusinessType())) {
            sql.append(" and o.business_type='" + orderCondition.getBusinessType() + "' ");
        }
        if (null != orderCondition.getOrderStatus()) {
            sql.append(" and o.order_status=" + orderCondition.getOrderStatus() + " ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getPartnersName())) {
            sql.append(" and o.partners_name like '&" + orderCondition.getPartnersName() + "&' ");

        }
        if (StringUtils.isNotEmpty(orderCondition.getBeginTime())) {
            sql.append(" and DATE_FORMAT(o.order_time,'%Y-%m-%d')>='" + orderCondition.getBeginTime() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getEndTime())) {
            sql.append(" and DATE_FORMAT(o.order_time,'%Y-%m-%d')<='" + orderCondition.getEndTime() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getAdPayType())) {
            sql.append(" and o.advance_pay_way='" + orderCondition.getAdPayType() + "' ");
        }
        if (StringUtils.isNotEmpty(orderCondition.getGoodsName()) || null != orderCondition.getGoodsId()) {
            sql.append(" and o.order_id in (SELECT detail.order_id from tb_order_detail detail where 1=1 ");
            if (StringUtils.isNotEmpty(orderCondition.getGoodsName())) {
                sql.append(" and detail.goods_name like '%" + orderCondition.getGoodsName() + "%' ");
            }
            if (null != orderCondition.getGoodsId()) {
                sql.append(" and detail.goods_id=" + orderCondition.getGoodsId() + " ");
            }
            sql.append(" ) ");
        }
        if (null != orderCondition.getGetWay()) {
            sql.append(" and post_way=").append(orderCondition.getGetWay()).append(" ");
        }
        return sql.toString();
    }

    public List<Map<String, Object>> loadOrderDetailListByOrderId(int orderId) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT d.detail_id,d.order_id,d.goods_id,d.goods_name,d.goods_spec,d.goods_spec_name, ");
        sql.append("  d.goods_count,CONCAT(d.sale_money,'') as sale_money,CONCAT(d.goods_true_money,'') as goods_true_money,CONCAT(d.goods_purchase_price,'') as goods_purchase_price,  ");
        sql.append(" CONCAT(d.time_price,'') as time_price,CONCAT(d.deposit_price,'') as deposit_price,d.shop_id,d.shop_name,d.partners_id, ");
        sql.append(" d.partners_name,d.get_addr_id,d.get_addr_name,DATE_FORMAT(d.get_time,'%Y-%m-%d') as get_time,d.back_addr_id, ");
        sql.append(" d.back_addr_name,DATE_FORMAT(d.back_time,'%Y-%m-%d') as back_time,d.detail_status,d.is_ticket,d.ticket_json, ");
        sql.append(" DATE_FORMAT(d.createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(d.lastaltertime,'%Y-%m-%d %T') as lastaltertime,d.lastalterman,d.status,d.remark, ");
        sql.append(" d.goods_img, ");
        sql.append(" d.cart_id,DATE_FORMAT(d.get_goods_date,'%Y-%m-%d') as get_goods_date,d.time_goods_id,  ");
        sql.append(" p.param_value as detail_status_name, ");
        sql.append(" if(d.is_ticket=0,'否','是') as is_ticket_name,if(d.time_goods_id=0,'否','是') as is_time_goods_name, ");
        sql.append(" tp.detail_id as tp_detail_id ");
        sql.append(" from tb_order_detail d ");
        sql.append(" LEFT JOIN tb_param p on p.param_code='order_detail_status' and p.param_key=d.detail_status ");
        sql.append(" LEFT JOIN (SELECT DISTINCT detail_id from  tb_order_detail_property ) tp on tp.detail_id=d.detail_id ");
        sql.append(" where d.order_id=" + orderId + " ");
        return commonDao.getBySql2(sql.toString());
    }

    public List<Map<String, Object>> loadDetailPropBuyDetailId(int detailId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.property_id,p.property_key,property_value from tb_order_detail_property p ");
        sql.append(" where detail_id=" + detailId + " ");

        return commonDao.getBySql2(sql.toString());
    }

    public List<Map<String, Object>> loadDetailPropBuyOrderId(int orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.property_id,p.property_key,property_value,p.detail_id,p.order_id from tb_order_detail_property p ");
        sql.append(" where order_id=" + orderId + " ");

        return commonDao.getBySql2(sql.toString());
    }

    public List<Map<String, Object>> listStatusRecord(String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT orc.id,orc.order_no,orc.order_status,DATE_FORMAT(orc.lastaltertime,'%Y-%m-%d %T') lastaltertime,orc.lastalterman,orc.remark,p.param_value as status_name ");
        sql.append(" from tb_order_status_recode orc ");
        sql.append(" LEFT JOIN tb_param p on  p.param_key=orc.order_status and p.param_code='order_status' ");
        sql.append(" where orc.order_no='" + orderNo + "' ");
        sql.append(" ORDER BY orc.id desc ");
        return commonDao.getBySql2(sql.toString());
    }

    public PageUtil loadMainNemuPageList(PageUtil page, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,menu_name,menu_sort,`status`,leve1_ids,category_ids,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" if(menu_img is null or menu_img='',CONCAT('" + path + "','null.jpg'),CONCAT('" + path + "',menu_img)) as menu_img,menu_url from tb_main_menu");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(status) && !"-1".equals(status)) {
            sql.append(" and status=" + status + " ");
        }
        sql.append("  ORDER BY menu_sort desc  ");

        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    public TbMainMenu loadTbMainMenu(int id) {
        String hql = " from TbMainMenu where id=? ";
        return (TbMainMenu) commonDao.queryUniqueByHql(hql, id);
    }

    public PageUtil loadImgRecordPage(PageUtil page, Integer status, String imgPathName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,img_path,table_name,img_status,DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time,DATE_FORMAT(update_time,'%Y-%m-%d %T') as update_time,CONCAT('≈',ROUND(img_size/1024,2),'KB') as img_size from tb_img_upload_name_record ");
        sql.append(" where 1=1 ");
        if (null != status && status != -1) {
            sql.append(" and img_status=" + status + " ");
        }
        if (StringUtils.isNotEmpty(imgPathName)) {
            sql.append(" and img_path like '%" + imgPathName + "%' ");
        }
        return commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
    }

    public TbImgUploadNameRecord loadTbImgUploadNameRecord(int id) {
        String hql = " from TbImgUploadNameRecord where id=? ";
        return (TbImgUploadNameRecord) commonDao.queryUniqueByHql(hql, id);
    }

    public TbImgUploadNameRecord loadTbImgUploadNameRecord(String ImgPath) {
        String hql = " from TbImgUploadNameRecord where imgPath=? ";
        return (TbImgUploadNameRecord) commonDao.queryUniqueByHql(hql, ImgPath);
    }


    public TbActivity loadTbActivityByImgPath(String ImgPath) {
        String hql = " from TbActivity where activityImg=? ";
        return (TbActivity) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbBannel loadTbBannelByImgPath(String ImgPath) {
        String hql = " from TbBannel where bannelImg=? ";
        return (TbBannel) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbCategory loadTbCategoryByImgPath(String ImgPath) {
        String hql = " from TbCategory where categoryImg=? ";
        return (TbCategory) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbCatogryLevel1 loadTbCatogryLevel1ByImgPath(String ImgPath) {
        String hql = " from TbCatogryLevel1 where levelImg=? ";
        return (TbCatogryLevel1) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbComment loadTbCommentByImgPath(String ImgPath) {
        String hql = " from TbComment where img1=? or img2=? or img3=? or img4=? or img5=? or img6=? ";
        return (TbComment) commonDao.queryUniqueByHql(hql, ImgPath, ImgPath, ImgPath, ImgPath, ImgPath, ImgPath);
    }

    public TbGoods loadTbGoodsImgPath(String ImgPath) {
        String hql = " from TbGoods where goodsImg=? ";
        return (TbGoods) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbGoodsSku loadTbGoodsSkuImgPath(String ImgPath) {
        String hql = " from TbGoodsSku where skuImg=? ";
        return (TbGoodsSku) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbImg loadTbImgImgPath(String ImgPath) {
        String hql = " from TbImg where imgUrl=? ";
        return (TbImg) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbItemprops loadTbItempropsImgPath(String ImgPath) {
        String hql = " from TbItemprops where propImg=? ";
        return (TbItemprops) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbMainMenu loadTbMainMenuImgPath(String ImgPath) {
        String hql = " from TbMainMenu where menuImg=? ";
        return (TbMainMenu) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbOrderCart loadTbOrderCartImgPath(String ImgPath) {
        String hql = " from TbOrderCart where goodsImg=? ";
        return (TbOrderCart) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbOrderDetail loadTbOrderDetailImgPath(String ImgPath) {
        String hql = " from TbOrderDetail where goodsImg=? ";
        return (TbOrderDetail) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbPartners loadTbPartnersImgPath(String ImgPath) {
        String hql = " from TbPartners where partnersImg=? ";
        return (TbPartners) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbShop loadTbShopImgPath(String ImgPath) {
        String hql = " from TbShop where shopLogo=? ";
        return (TbShop) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbTicket loadTbTicketImgPath(String ImgPath) {
        String hql = " from TbTicket where ticketImg=? ";
        return (TbTicket) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbTicketDownQr loadTbTicketDownQrImgPath(String ImgPath) {
        String hql = " from TbTicketDownQr where qrImg=? ";
        return (TbTicketDownQr) commonDao.queryUniqueByHql(hql, ImgPath);
    }

    public TbUserInfo loadTbUserInfoImgPath(String ImgPath) {
        String hql = " from TbUserInfo where userImg=? ";
        return (TbUserInfo) commonDao.queryUniqueByHql(hql, ImgPath);
    }


    /**
     * 查询wap用户列表
     *
     * @param page
     * @param userInfo
     * @return
     */
    public PageUtil loadWapUserPage(PageUtil page, TbUserInfo userInfo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT u.user_id,u.login_name,u.nick_name,u.user_email,u.email_valite,u.user_mobile,u.mobile_valite,u.log_count,u.user_state,  ");
        sql.append(" DATE_FORMAT(u.createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(u.lastlogintime,'%Y-%m-%d %T') as lastlogintime  from tb_user_info  u  where 1=1 ");
        String condition = loadWapUserPageCondition(page, userInfo);
        sql.append(condition);
        sql.append("  order by u.lastlogintime desc,u.createtime desc ");
        //查询总数
        StringBuffer sqlCount = new StringBuffer(" select count(*) from tb_user_info where 1=1  ");
        sqlCount.append(condition);

        return commonDao.queryListMapSQLPageByField(sql.toString(), sqlCount.toString(), page.getCurrentPage(), page.getPageSize());
    }

    private String loadWapUserPageCondition(PageUtil page, TbUserInfo userInfo) {
        StringBuffer sql = new StringBuffer();
        if (userInfo.getUserId() != null) {
            sql.append(" and u.user_id=" + userInfo.getUserId() + " ");
        }

        Map<String, Object> paramMap = page.getParamMap();
        if (null != paramMap) {
            String uid = paramMap.get("uid") == null ? "" : paramMap.get("uid").toString();
            if (StringUtils.isNotEmpty(uid)) {
                sql.append(" and u.user_id in (SELECT user_id from tb_user_sns where uid like '%" + uid + "%') ");
            }
        }
        if (StringUtils.isNotEmpty(userInfo.getNickName())) {
            sql.append(" and u.nick_name  like '%" + userInfo.getNickName().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(userInfo.getLoginName())) {
            sql.append(" and u.login_name like '%" + userInfo.getLoginName().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(userInfo.getUserEmail())) {
            sql.append(" and u.user_email   like '%" + userInfo.getUserEmail().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(userInfo.getUserMobile())) {
            sql.append(" and u.user_mobile  like '%" + userInfo.getUserMobile().trim() + "%' ");
        }
        if (userInfo.getUserState() != null) {
            sql.append(" and u.user_state=" + userInfo.getUserState() + " ");
        }
        return sql.toString();

    }

    /**
     * 查询wap用户绑定
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> loadWapUserSnsListMap(int userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  sns_id,user_id,sns_type,uid,openkey,sns_name,sns_account,sns_img,is_use_img,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(update_time,'%Y-%m-%d %T') as update_time,`status` from tb_user_sns  ");
        sql.append(" where user_id=" + userId + " ");
        sql.append(" ORDER BY update_time DESC,createtime desc ");
        return commonDao.getBySql2(sql.toString());
    }


    public PageUtil loadComplainPage(PageUtil page, TbComplain complain) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,user_id,information,contents,`status`,is_read,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime from tb_complain ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(complain.getInformation())) {
            sql.append(" and information like '%" + complain.getInformation().trim() + "%' ");
        }
        if (null != complain.getIsRead()) {
            sql.append(" and is_read=" + complain.getIsRead() + " ");
        }
        if (null != complain.getStatus()) {
            sql.append(" and status=" + complain.getStatus() + " ");
        }
        sql.append(" ORDER BY createtime desc ");
        return commonDao.queryListMapSQLPageByField(sql.toString(), "", page.getCurrentPage(), page.getPageSize());
    }

    public TbComplain loadTbComplainById(int id) {
        String hql = " from TbComplain where id=? ";
        return (TbComplain) commonDao.queryUniqueByHql(hql, id);
    }
}
