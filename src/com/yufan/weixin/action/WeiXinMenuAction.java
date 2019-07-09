package com.yufan.weixin.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbWeixinAccessToken;
import com.yufan.pojo.TbWeixinMenu;
import com.yufan.util.ConfigProperty;
import com.yufan.util.DatetimeUtil;
import com.yufan.util.ParamHelp;
import com.yufan.util.RequestMethod;
import com.yufan.util.WeixinBaseDev;
import com.yufan.weixin.bean.WeiXinBean;
import com.yufan.weixin.bean.WeiXinInfoBean;
import com.yufan.weixin.bean.WeiXinSubBean;
import com.yufan.weixin.bean.WeixinResp;

/**
 * @功能名称 微信菜单管理
 * @作者 lirongfan
 * @时间 2016年2月17日 下午1:52:13
 */
@Scope("prototype")
@Controller("weiXinMenuAction")
@Namespace("/weixin")
@ParentPackage("common")
public class WeiXinMenuAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private com.yufan.taobao.dao.FindInfoDao findInfoDao_taobao;

    private String id;
    private String menu_parent;
    private String menu_name;
    private String menu_foward;//跳转地址
    private String menu_type;//菜单类型
    private String handleType;//操作类型 add update
    private String menu_key;//clcik类按钮

    private TbWeixinMenu weixin = new TbWeixinMenu();

    //菜单列表
    private List<Map<String, Object>> list_menu = new ArrayList<Map<String, Object>>();

    //菜单类型列表
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    private String access_token = "";//微信

    private String appId_secret = "";

    private String msg = "";


    /**
     * 加载微信渠道
     */
    @Action(value = "loadWeixinChannel")
    public void loadWeixinChannel() {
        initData();
        try {
            ParamHelp wxInfoHelp = new ParamHelp();
            list = wxInfoHelp.getWeixinChannelList(findInfoDao_taobao);
            response.getWriter().write(write_Json_result("ok", list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询菜单
     */
    @Action(value = "loadWeiXinMenu")
    public void listWeixinMenuMap() {
        try {
            initData();
            list_menu = findInfoDao.listWeixinMenuMap(menu_parent, "1", appId_secret);

            response.getWriter().write(write_Json_result("ok", list_menu));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步微信菜单
     */
    @Action(value = "loadMenuFronWeiXin")
    public void loadMenuFronWeiXin() {
        try {
            initData();
            //删除数据库中的菜单
            saveInfoDao.delWeixinMenu("0", null, appId_secret);

            WeiXinInfoBean wxBean = new WeiXinInfoBean();
            String[] appId_secrets = appId_secret.split(";");
            wxBean.setAppID(appId_secrets[0]);
            wxBean.setAppsecret(appId_secrets[1]);
            //查询微信菜单并更新到数据库中
            boolean flag = selectWeixinMenus(wxBean);
            if (!flag) {
                response.getWriter().write(write_Json_result("false", msg));
                return;
            }
            response.getWriter().write(write_Json_result("ok", "微信菜单拉取成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询微信菜单
     */
    public boolean selectWeixinMenus(WeiXinInfoBean wxBean) {
        try {
            accessToken(wxBean);// 获取access_token
            String weixin_menu_select = new ConfigProperty().getConfigValue("weixin_menu_select");
            String url = weixin_menu_select + access_token;
            String result = RequestMethod.sendGetWX1(url);
            System.out.println("---->查询微信菜单" + result);
            JSONObject json_menu = JSONObject.parseObject(result);
            String menu = json_menu.getString("menu");
            if (null != menu) {
                JSONObject json_button = JSONObject.parseObject(menu);
                String button = json_button.getString("button");
                if (null != button) {
                    JSONArray ja_button = JSONArray.parseArray(button);
                    if (ja_button.size() > 0) {
                        handdleMenuResult(ja_button);
                    }
                }
            } else {
                WeixinResp resp = new WeixinResp();
                result = resp.getMap().get(json_menu.getInteger("errcode")) + "";
                msg = "获取失败-->" + result;
                return false;
            }
            //{"errcode":48001,"errmsg":"api unauthorized hint: [3L5hHa0515rsz3!]"}
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 解析微信菜单并更新到数据库中
     *
     * @param ja_button
     */
    public void handdleMenuResult(JSONArray ja_button) {
        //{"menu":{"button":[{"type":"click","name":"今日歌曲","key":"V1001_TODAY_MUSIC","sub_button":[]},{"name":"菜单","sub_button":[{"type":"view","name":"搜索","url":"http:\/\/www.soso.com\/","sub_button":[]},{"type":"view","name":"视频","url":"http:\/\/www.baidu.com\/","sub_button":[]}]}]}}
//		List<WeiXinBean> list_weixin_bean=new ArrayList<WeiXinBean>();
        //查询微信按钮类型
        ParamHelp wxInfoHelp = new ParamHelp();
        list = wxInfoHelp.getWeiXinButtonType(findInfoDao_taobao);
        Map<String, String> map_btn_type = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            String param_value = String.valueOf(list.get(i).get("param_value"));//按钮类型数值
            String param_value1 = String.valueOf(list.get(i).get("param_value1"));//按钮类型编码
            if (param_value != null && !"null".equals(param_value) && !"".equals(param_value) && param_value1 != null && !"null".equals(param_value1) && !"".equals(param_value1)) {
                map_btn_type.put(param_value1, param_value);
            }
        }
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        int index = 1;
        int indexS = 1;
        for (int i = 0; i < ja_button.size(); i++) {
            JSONObject obj = ja_button.getJSONObject(i);
            JSONArray ja = obj.getJSONArray("sub_button");
            if (null != ja && ja.size() > 0) {//  有子菜单一级菜单
                WeiXinBean weiXinBean = new WeiXinBean();
                weiXinBean = JSONObject.toJavaObject(obj, WeiXinBean.class);

                //判断是否有子菜单
                if (weiXinBean.getSub_button() != null && weiXinBean.getSub_button().size() > 0) {//有子菜单
                    TbWeixinMenu wx = new TbWeixinMenu();
                    wx.setMenuParent(0);
                    wx.setMenuIndex(index);
                    wx.setMenuName(weiXinBean.getName());
                    wx.setKeyCode(appId_secret);
                    wx.setMenuType(0);
//					wx.setMenuKey();
//					wx.setMenuFoward();
                    wx.setStatus(1);
                    wx.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    wx.setCreateman(user.getAdminId());
                    wx.setLastalterman(user.getAdminId());
                    wx.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                    int menu_PId = saveInfoDao.saveWeixinMenu(wx);
                    for (int j = 0; j < weiXinBean.getSub_button().size(); j++) {
                        WeiXinSubBean weiXinSubBean = new WeiXinSubBean();
                        weiXinSubBean = weiXinBean.getSub_button().get(j);

                        TbWeixinMenu wx_ = new TbWeixinMenu();
                        wx_.setMenuParent(menu_PId);
                        wx_.setMenuIndex(indexS);
                        wx_.setMenuName(weiXinSubBean.getName());
                        wx_.setKeyCode(appId_secret);
                        String menu_y = map_btn_type.get(weiXinSubBean.getType());
                        if (null == menu_y) {
                            menu_y = "0";
                        }
                        wx_.setMenuType(Integer.parseInt(menu_y));
                        wx_.setMenuKey(weiXinSubBean.getKey());
                        wx_.setMenuFoward(weiXinSubBean.getUrl());
                        wx_.setStatus(1);
                        wx_.setCreatetime(new Timestamp(System.currentTimeMillis()));
                        wx_.setCreateman(user.getAdminId());
                        wx_.setLastalterman(user.getAdminId());
                        wx_.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                        saveInfoDao.saveWeixinMenu(wx_);
                        indexS = indexS + 1;
                    }
                }
            } else {//   没有子菜单一级菜单
                WeiXinSubBean weiXinSubBean = new WeiXinSubBean();
                weiXinSubBean = JSONObject.toJavaObject(obj, WeiXinSubBean.class);

                TbWeixinMenu wx = new TbWeixinMenu();
                wx.setMenuParent(0);
                wx.setMenuIndex(index);
                wx.setMenuName(weiXinSubBean.getName());
                wx.setKeyCode(appId_secret);
                String menu_y = map_btn_type.get(weiXinSubBean.getType());
                if (null == menu_y) {
                    menu_y = "0";
                }
                wx.setMenuType(Integer.parseInt(menu_y));
                wx.setMenuKey(weiXinSubBean.getKey());
                wx.setMenuFoward(weiXinSubBean.getUrl());
                wx.setStatus(1);
                wx.setCreatetime(new Timestamp(System.currentTimeMillis()));
                wx.setCreateman(user.getAdminId());
                wx.setLastalterman(user.getAdminId());
                wx.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                saveInfoDao.saveWeixinMenu(wx);
            }
            index = index + 1;
        }
    }

    /**
     * 获取access_token
     */
    public void accessToken(WeiXinInfoBean wxBean) {
        //查询是否存在有效的access_token
        String code = wxBean.getAppID() + ";" + wxBean.getAppsecret();
        String dbAccessToken = findInfoDao.getAccessTokenByCode(code);
        access_token = dbAccessToken;
        if (null == dbAccessToken || "".equals(dbAccessToken) || "null".equals(dbAccessToken)) {
            String passTime = DatetimeUtil.addMinuteTime(DatetimeUtil.getNow(), 110, "yyyy-MM-dd HH:mm:ss");
            //获取 access_token
            WeixinBaseDev wxd = new WeixinBaseDev(wxBean.getAppID(), wxBean.getAppsecret());
            access_token = wxd.getAccess_token();
            //保存数据到db中
            TbWeixinAccessToken accessToken = new TbWeixinAccessToken();
            accessToken.setAccessToken(access_token);
            accessToken.setExpiryDate(new Timestamp(DatetimeUtil.StringToDate(passTime, "yyyy-MM-dd HH:mm:ss").getTime()));
            accessToken.setCreateDate(new Timestamp(System.currentTimeMillis()));
            accessToken.setStatus(1);
            accessToken.setCombineCode(code);
            saveInfoDao.saveWeixinAccessToken(accessToken);
        }

    }


    /**
     * 增加修改菜单
     */
    @Action(value = "addWeixinMenu")
    public void addWeixinMenu() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");

            weixin.setMenuParent(Integer.parseInt(menu_parent));
            weixin.setMenuName(menu_name);

            weixin.setKeyCode(appId_secret);

            weixin.setMenuType(Integer.parseInt(menu_type));

            weixin.setMenuKey(menu_key);
            weixin.setMenuFoward(menu_foward);

            if ("add".equals(handleType)) {
                //处理菜单排序
                list_menu = findInfoDao.listWeixinMenuMap(menu_parent, "1", appId_secret);
                weixin.setMenuIndex(list_menu.size() + 1);
                if (null == menu_key || "".equals(menu_key) || "null".equals(menu_key)) {
                    menu_key = String.valueOf(System.currentTimeMillis());
                }
                weixin.setMenuKey(menu_key);
                weixin.setStatus(1);
                weixin.setCreatetime(new Timestamp(System.currentTimeMillis()));
                weixin.setCreateman(user.getAdminId());
                weixin.setLastalterman(user.getAdminId());
                weixin.setLastaltertime(new Timestamp(System.currentTimeMillis()));
            } else {
                TbWeixinMenu w = new TbWeixinMenu();
                w = findInfoDao.loadTbWeixinMenuInfo(Integer.parseInt(id));
                weixin.setStatus(1);
                weixin.setCreatetime(w.getCreatetime());
                weixin.setCreateman(w.getCreateman());
                weixin.setLastalterman(user.getAdminId());
                weixin.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                weixin.setMenuParent(w.getMenuParent());
                weixin.setMenuIndex(w.getMenuIndex());
                weixin.setMenuId(Integer.parseInt(id));
            }
            saveInfoDao.saveWeixinMenu(weixin);
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除菜单
     */
    @Action(value = "delWeixinMenu")
    public void delWeixinMenu() {
        initData();
        try {
            saveInfoDao.delWeixinMenu("0", id, null);
            response.getWriter().write(write_Json_result("ok", "删除成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载菜单类型
     */
    @Action(value = "loadWeixinButtonType")
    public void loadWeixinButtonType() {
        initData();
        try {
            ParamHelp wxInfoHelp = new ParamHelp();
            list = wxInfoHelp.getWeiXinButtonType(findInfoDao_taobao);
            response.getWriter().write(write_Json_result("ok", list));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存排序
     */
    @Action(value = "saveIndex")
    public void saveIndex() {
        initData();
        try {
            String[] ids = request.getParameterValues("i_id");
            String[] indexs = request.getParameterValues("index");
            for (int i = 0; i < ids.length; i++) {
                TbWeixinMenu w = new TbWeixinMenu();
                w = findInfoDao.loadTbWeixinMenuInfo(Integer.parseInt(ids[i]));
                w.setMenuIndex(Integer.parseInt(indexs[i].trim()));
                saveInfoDao.saveWeixinMenu(w);
            }
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //保存并发布微信菜单
    @Action(value = "saveAndReleaseWeixinMenu")
    public void saveAndReleaseWeixinMenu() {
        initData();
        try {
            if (null == appId_secret || "".equals(appId_secret) || "null".equals(appId_secret)) {
                response.getWriter().write(write_Json_result("false", "微信渠道信息不存在"));
                return;
            }

            WeiXinInfoBean wxBean = new WeiXinInfoBean();
            String[] appId_secrets = appId_secret.split(";");
            wxBean.setAppID(appId_secrets[0]);
            wxBean.setAppsecret(appId_secrets[1]);
            accessToken(wxBean);// 获取access_token

            //查询微信按钮类型
            ParamHelp wxInfoHelp = new ParamHelp();
            list = wxInfoHelp.getWeiXinButtonType(findInfoDao_taobao);
            Map<String, String> map_btn_type = new HashMap<String, String>();
            for (int i = 0; i < list.size(); i++) {
                String param_value = String.valueOf(list.get(i).get("param_value"));//按钮类型数值
                String param_value1 = String.valueOf(list.get(i).get("param_value1"));//按钮类型编码
                if (param_value != null && !"null".equals(param_value) && !"".equals(param_value) && param_value1 != null && !"null".equals(param_value1) && !"".equals(param_value1)) {
                    map_btn_type.put(param_value, param_value1);
                }
            }

            List<Map<String, Object>> button = new ArrayList<Map<String, Object>>();

            //获取一级菜单
            list_menu = findInfoDao.listWeixinMenuMap("0", "1", appId_secret);
            if (null != list_menu && list_menu.size() > 0) {

                for (int i = 0; i < list_menu.size(); i++) {
                    String menu_id = String.valueOf(list_menu.get(i).get("menu_id"));
                    //查询子类菜单
                    List<Map<String, Object>> list_menu_sub = new ArrayList<Map<String, Object>>();
                    list_menu_sub = findInfoDao.listWeixinMenuMap(menu_id, "1", appId_secret);
                    if (null != list_menu_sub && list_menu_sub.size() > 0) {//有子菜单
                        button.add(handdleHaveSub(list_menu.get(i), list_menu_sub, map_btn_type));
                    } else {//没有子菜单
                        button.add(handdleNoSub(list_menu.get(i), map_btn_type));
                    }
                }
            } else {
                if (delFromWeiXinMenu()) {
                    response.getWriter().write(write_Json_result("ok", "发布成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "发布失败"));
                }
                return;
            }

            JSONObject json = new JSONObject();
            json.put("button", button);
            System.out.println(json);
            String weixin_menu_add = new ConfigProperty().getConfigValue("weixin_menu_add");
            String url = weixin_menu_add + access_token;
            String reslut = RequestMethod.httpPost(url, json.toString());
            System.out.println("reslut-->" + reslut);
            //{"errcode":0,"errmsg":"ok"}
            JSONObject json_result = JSONObject.parseObject(reslut);
            String errcode = json_result.getString("errcode");
            if (null != errcode && "0".equals(errcode)) {
                response.getWriter().write(write_Json_result("ok", "发布成功"));
                return;
            }
            response.getWriter().write(write_Json_result("false", "发布失败reslut-->" + reslut));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理没有子菜单的按钮
     *
     * @param map          一级菜单信息
     * @param map_btn_type 按钮类型
     * @return
     */
    public Map<String, Object> handdleNoSub(Map<String, Object> map, Map<String, String> map_btn_type) {
        //click类 view类  无子菜单类
        Map<String, Object> map_click = new HashMap<String, Object>();
        String menu_type = String.valueOf(map_btn_type.get(map.get("menu_type").toString()));
        map_click.put("type", menu_type);
        map_click.put("name", map.get("menu_name"));
        if ("view".equals(menu_type)) {
            map_click.put("url", map.get("menu_foward"));
        } else {
            map_click.put("key", map.get("menu_key"));
        }
        return map_click;
    }

    /**
     * 处理有子菜单的按钮
     *
     * @param map          一级菜单信息
     * @param datas_sub    子菜单列表
     * @param map_btn_type 按钮类型
     * @return
     */
    public Map<String, Object> handdleHaveSub(Map<String, Object> map, List<Map<String, Object>> datas_sub, Map<String, String> map_btn_type) {
        Map<String, Object> map_view = new HashMap<String, Object>();
        map_view.put("name", map.get("menu_name"));

        List<Map<String, Object>> sub_button = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < datas_sub.size(); i++) {
            Map<String, Object> sub_button_map_click = new HashMap<String, Object>();
            String menu_type = String.valueOf(map_btn_type.get(datas_sub.get(i).get("menu_type").toString()));
            sub_button_map_click.put("type", menu_type);
            sub_button_map_click.put("name", datas_sub.get(i).get("menu_name"));
            if ("view".equals(menu_type)) {
                sub_button_map_click.put("url", datas_sub.get(i).get("menu_foward"));
            } else {
                sub_button_map_click.put("key", datas_sub.get(i).get("menu_key"));
            }
            sub_button.add(sub_button_map_click);
        }
        map_view.put("sub_button", sub_button);
        return map_view;
    }

    /**
     * 删除已发布的微信按钮
     */
    public boolean delFromWeiXinMenu() {
        try {
            String weixin_menu_delete = new ConfigProperty().getConfigValue("weixin_menu_delete");
            String url = weixin_menu_delete + access_token;
            String result = RequestMethod.sendGetWX1(url);
            System.out.println("---->删除已发布的微信按钮" + result);
            //{"errcode":0,"errmsg":"ok"}
            JSONObject json_result = JSONObject.parseObject(result);
            String errcode = json_result.getString("errcode");
            if (null != errcode && "0".equals(errcode)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public FindInfoDao getfindInfoDao() {
        return findInfoDao;
    }


    public void setfindInfoDao(FindInfoDao findInfoDao) {
        this.findInfoDao = findInfoDao;
    }


    public SaveInfoDao getsaveInfoDao() {
        return saveInfoDao;
    }


    public void setsaveInfoDao(SaveInfoDao saveInfoDao) {
        this.saveInfoDao = saveInfoDao;
    }

    public String getMenu_parent() {
        return menu_parent;
    }


    public TbWeixinMenu getWeixin() {
        return weixin;
    }

    public void setWeixin(TbWeixinMenu weixin) {
        this.weixin = weixin;
    }

    public void setMenu_parent(String menu_parent) {
        this.menu_parent = menu_parent;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_foward() {
        return menu_foward;
    }

    public void setMenu_foward(String menu_foward) {
        this.menu_foward = menu_foward;
    }

    public com.yufan.taobao.dao.FindInfoDao getFindInfoDao_taobao() {
        return findInfoDao_taobao;
    }

    public void setFindInfoDao_taobao(com.yufan.taobao.dao.FindInfoDao findInfoDao_taobao) {
        this.findInfoDao_taobao = findInfoDao_taobao;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId_secret() {
        return appId_secret;
    }

    public void setAppId_secret(String appId_secret) {
        this.appId_secret = appId_secret;
    }

    public String getMenu_key() {
        return menu_key;
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

    public List<Map<String, Object>> getList_menu() {
        return list_menu;
    }

    public void setList_menu(List<Map<String, Object>> list_menu) {
        this.list_menu = list_menu;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMenu_key(String menu_key) {
        this.menu_key = menu_key;
    }

}
