package com.yufan.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

/**
 * action的基类
 * Author: Administrator
 * CreateDate :2014年12月12日
 */
public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = -4232190044539760500L;
    public HttpServletRequest request = ServletActionContext.getRequest();
    public HttpServletResponse response = ServletActionContext.getResponse();
    private Logger log = Logger.getLogger(this.getClass());


    private String msg;
    private String jsonStr;
    private String ver;
    private String did;
    private String dType;

    /**
     * 对入参进行初步解析
     * Author: Administrator
     * CreateDate :2014年12月12日
     */
    public void parseXML(String inXml) {
        JSONObject jsonObj = JSONObject.parseObject(inXml);
        try {
            ver = jsonObj.get("ver").toString();
            did = jsonObj.get("did").toString();
            dType = jsonObj.get("dtype").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute() throws Exception {
        // TODO Auto-generated method stub
        return super.execute();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    /**
     * @return the ver
     */
    public String getVer() {
        return ver;
    }

    /**
     * @param ver the ver to set
     */
    public void setVer(String ver) {
        this.ver = ver;
    }

    /**
     * @return the did
     */
    public String getDid() {
        return did;
    }

    /**
     * @param did the did to set
     */
    public void setDid(String did) {
        this.did = did;
    }

    /**
     * @return the dType
     */
    public String getDType() {
        return dType;
    }

    /**
     * @param type the dType to set
     */
    public void setDType(String type) {
        dType = type;
    }
}
