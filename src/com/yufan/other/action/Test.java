package com.yufan.other.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yufan.util.RequestMethod;
import com.yufan.weixin.bean.WeiXinBean;
import com.yufan.weixin.bean.WeiXinSubBean;

/**
 * 功能名称:
 * 开发人: lirf
 * 开发时间: 2016下午6:29:43
 * 其它说明：
 */
public class Test {
    private static String access_token = "GxCnyFRCA7Gju12TFi3u-UNp5eFuiW1iXJ6-ohO-c_QZv1CCWs31Sx3eJ-j5xG-jQ_--uk0btRBc29iD2qmra7SpVCAWSxJusJaorkaWFV9QI7E1H7FVYtEyLfHNRaw4HXYgAEAYOP";

    public static void main(String[] args) {
//		selectMenu();//{"errcode":46003,"errmsg":"menu no exist hint: [OPfk60633vr20]"}
//		selectMenu2();//{"errcode":46003,"errmsg":"menu no exist hint: [OPfk60633vr20]"}
//		addMenu();//{"errcode":0,"errmsg":"ok"}
//		delMenu();//{"errcode":0,"errmsg":"ok"}
        String areaIds = String.valueOf("150000000000-150100000000-150101000000-");
        System.out.println(areaIds.substring(0, areaIds.length() - 1));
        areaIds = areaIds.replace("-", "','");
    }

    public static void addMenu() {
        List<Map<String, Object>> button = new ArrayList<Map<String, Object>>();
        //click类 view类  无子菜单类
        Map<String, Object> map_click = new HashMap<String, Object>();
        map_click.put("type", "click");
        map_click.put("name", "今日歌曲");
        map_click.put("key", "V1001_TODAY_MUSIC");
        //view类   有子菜单类
        Map<String, Object> map_view = new HashMap<String, Object>();
        map_view.put("name", "菜单");

        List<Map<String, Object>> sub_button = new ArrayList<Map<String, Object>>();
        Map<String, Object> sub_button_map_click1 = new HashMap<String, Object>();
        sub_button_map_click1.put("type", "view");
        sub_button_map_click1.put("name", "搜索");
        sub_button_map_click1.put("url", "http://www.soso.com/");
        Map<String, Object> sub_button_map_click2 = new HashMap<String, Object>();
        sub_button_map_click2.put("type", "view");
        sub_button_map_click2.put("name", "视频");
        sub_button_map_click2.put("url", "http://www.baidu.com/");

        Map<String, Object> sub_button_map_click3 = new HashMap<String, Object>();
        sub_button_map_click3.put("type", "click");
        sub_button_map_click3.put("name", "视频1");
        sub_button_map_click3.put("key", "V1001_TODAY");


        //view类sub_button 添加 n个 sub_button_map_click
        sub_button.add(sub_button_map_click1);
//		sub_button.add(sub_button_map_click2);
//		sub_button.add(sub_button_map_click3);
        //view类map_view添加子button
        map_view.put("sub_button", sub_button);
        //button要添加(click类map_click)和(view类map_view);
//		button.add(map_click);
        button.add(map_view);

        JSONObject json = new JSONObject();
        json.put("button", button);
        System.out.println(json);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token;
        String reslut = RequestMethod.httpPost(url, json.toString());
        System.out.println(reslut);
    }

    public static void selectMenu() {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + access_token;
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);
        JSONObject json_menu = JSONObject.parseObject(result);
        String menu = json_menu.getString("menu");
        if (null != menu) {
            JSONObject json_button = JSONObject.parseObject(menu);
            String button = json_button.getString("button");
            if (null != button) {
                JSONArray ja_button = JSONArray.parseArray(button);
                if (ja_button.size() > 0) {
                    handdleResult(ja_button);
                }
            }
        }
    }

    public static void selectMenu2() {
        String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=" + access_token;
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);
    }

    public static void delMenu() {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + access_token;
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);
    }

    /**
     * 解析微信菜单
     *
     * @param ja_button
     */
    public static void handdleResult(JSONArray ja_button) {
        //{"menu":{"button":[{"type":"click","name":"今日歌曲","key":"V1001_TODAY_MUSIC","sub_button":[]},{"name":"菜单","sub_button":[{"type":"view","name":"搜索","url":"http:\/\/www.soso.com\/","sub_button":[]},{"type":"view","name":"视频","url":"http:\/\/www.baidu.com\/","sub_button":[]}]}]}}
        List<WeiXinBean> list_weixin_bean = new ArrayList<WeiXinBean>();
        for (int i = 0; i < ja_button.size(); i++) {
            JSONObject obj = ja_button.getJSONObject(i);
            JSONArray ja = obj.getJSONArray("sub_button");
            if (null != ja && ja.size() > 0) {
                WeiXinBean weiXinBean = new WeiXinBean();
                weiXinBean = JSONObject.toJavaObject(obj, WeiXinBean.class);
                System.out.println("--");
            } else {
                WeiXinSubBean weiXinSubBean = new WeiXinSubBean();
                weiXinSubBean = JSONObject.toJavaObject(obj, WeiXinSubBean.class);
                System.out.println("--");
            }
        }
    }
}
