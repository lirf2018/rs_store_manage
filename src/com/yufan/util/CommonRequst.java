package com.yufan.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 创建人: lirf
 * 创建时间:  2017-12-28 16:40
 * 功能介绍:
 */
public class CommonRequst {
    private final static Logger LOG = Logger.getLogger(CommonRequst.class);
    private final static String URL = ConfigProperty.getConfigValue("inf_url");

    /**
     * 直接连接方式
     *
     * @param obj
     * @param req_type
     */
    public static ResultBean executeNew(Object obj, String req_type) {
        JSONObject httpJson = new JSONObject();
        //系统参数
        String sid = "test";
        String appsecret = "2b65a10ed4154187f82880cfd841c09f";
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String sign = getSign(sid, appsecret, timestamp, obj);
        httpJson.put("req_type", req_type);
        httpJson.put("sid", sid);
        httpJson.put("timestamp", timestamp);
        httpJson.put("sign", sign);
        httpJson.put("data", obj);
        LOG.info("请求地址：" + URL);
        LOG.info("请求参数：" + httpJson.toString());
        Long b = System.currentTimeMillis() / 1000;
        String result = RequestMethod.httpPost(URL, httpJson.toString());
        LOG.info("返回参数：" + result);
        Long e = System.currentTimeMillis() / 1000;
        LOG.info("接口用时：" + (e - b));
        if (null != result) {
            return JSONObject.toJavaObject(JSONObject.parseObject(result), ResultBean.class);
        }
        return null;
    }

    public static String getSign(String sid, String appsecret, String timestamp, Object obj) {

        MyMap map = new MyMap();
        //系统参数
        map.put("sid", sid);
        map.put("appsecret", appsecret);
        map.put("timestamp", timestamp);
        JSONObject json = JSONObject.parseObject(String.valueOf(obj));
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            //只取data第一层数据
            if (null != v && !(v instanceof JSONArray) && v.toString().indexOf("{") == -1 && v.toString().indexOf("[") == -1) {
                map.put(k.toString(), v);
            }
        }
        String sign = MD5.enCodeStandard(HelpCommon.getSign(map) + appsecret);
        return sign;
    }
}
