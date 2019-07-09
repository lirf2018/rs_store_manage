package com.yufan.util;

import com.yufan.taobao.dao.FindInfoDao;
import com.yufan.weixin.bean.WeiXinInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @功能名称 查询配置参数工具信息
 * @作者 lirongfan
 * @时间 2016年2月19日 上午10:33:52
 */
public class ParamHelp {

    //
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    /**
     * 查询微信参数
     *
     * @return
     */
    public WeiXinInfoBean getWeixinParam(FindInfoDao findInfoDao) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("state", "1");
        map.put("param_code", "weixin_code");

        WeiXinInfoBean bean = new WeiXinInfoBean();
        //得到微信信息 如 appID appsecret
        list = findInfoDao.getSysParam(map);
        for (int i = 0; i < list.size(); i++) {
            String param_key = String.valueOf(list.get(i).get("param_key"));
            String param_value = String.valueOf(list.get(i).get("param_value"));
            if ("appID".equals(param_key)) {
//				bean.setAppID(param_value);
            } else if ("appsecret".equals(param_key)) {
//				bean.setAppsecret(param_value);
            } else if ("jsapi_ticket_url".equals(param_key)) {
                bean.setJsapi_ticket_url(param_value);
            } else if ("scan_url".equals(param_key)) {
                bean.setScan_url(param_value);
            }
        }
        return bean;
    }

    /**
     * 查询微信渠道列表
     *
     * @return
     */
    public List<Map<String, Object>> getWeixinChannelList(FindInfoDao findInfoDao) {
        //得到微信信息 如 appID appsecret
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "1");
        map.put("param_code", "weixin_channel");
        list = findInfoDao.getSysParam(map);
        return list;
    }

    /**
     * 查询微信按钮类型列表
     *
     * @return
     */
    public List<Map<String, Object>> getWeiXinButtonType(FindInfoDao findInfoDao) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "1");
        map.put("param_code", "weixin_button_type");
        //得到微信微信按钮类型
        list = findInfoDao.getSysParam(map);
        return list;
    }
}
