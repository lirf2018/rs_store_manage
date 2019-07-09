package com.yufan.weixintest.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yufan.weixintest.util.RequestUtil;

/**
 * @功能名称 微信自定义菜单管理
 * @作者 lirongfan
 * @时间 2015年12月11日 下午6:50:04
 */
public class WeixinMenuAction extends WeixinBaseDev {

    private String access_token = "7VffM8Q43ot5oLVarO2X9PriL1xMVmQqAaMDid8N1rYgHaPTof7WyfeZ3Ui17IkLhgQhTM5dWM8cZEoDUsHIcTvDdyWMS7BrQilYAhiadF0Sr3fYvC_TBqlLzah9SoLEFNYbAAAIQB";


    /**
     * 增加或修改微信一级菜单
     */
    public void addWeixinMenu() {
        String menu_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token;//+getAccess_token();
        System.out.println("-------> access_token=" + access_token);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //一级菜单
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "商城");//菜单名称
        list.add(map);


        JSONObject obj = new JSONObject();
        obj.put("button", list);

        //子菜单1
        List<Map<String, Object>> sub_list = new ArrayList<Map<String, Object>>();
        Map<String, Object> sub_map = new HashMap<String, Object>();
        sub_map.put("type", "view");
        sub_map.put("name", "商场子");
        sub_map.put("url", "http://192.168.0.172:8089/weixingtest/index/indexAction");
        sub_list.add(sub_map);

        map.put("sub_button", sub_list);

        System.out.println(obj);
        //{"button":[{"name":"商城","sub_button":[{"name":"商场","type":"view","url":"http://192.168.0.172:8089/weixingtest/index/indexAction"}]}]}
        String result = RequestUtil.httpPost(menu_url, obj.toString());

        System.out.println(result);

    }

    /**
     * 删除菜单
     */
    public void delWeixingMenu() {
        String menu_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + access_token;

        String result = RequestUtil.sendGetWX1(menu_url);

        System.out.println(result);
    }

    /**
     * 查询菜单
     */
    public void selectWeixingMenu() {
        String menu_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + access_token;

        String result = RequestUtil.sendGetWX1(menu_url);

        System.out.println(result);
    }


    public static void main(String[] args) {
        WeixinMenuAction w = new WeixinMenuAction();
        w.selectWeixingMenu();
    }
}
