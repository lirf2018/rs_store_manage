package com.yufan.util;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 创建人: lirf
 * 创建时间:  2017-12-29 11:37
 * 功能介绍:
 */
public class ResultBean {

    @JSONField(name = "resp_code")
    private Integer respCode;

    @JSONField(name = "resp_desc")
    private String respDesc;

    @JSONField(name = "data")
    private JSONObject data;

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
