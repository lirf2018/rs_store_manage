package com.yufan.vo;

/**
 * @功能名称
 * @作者 lirongfan
 * @时间 2016年8月22日 上午11:42:13
 */
public class ActivityCondition {

    private String activityTitle;
    private Integer channelId;
    private String partnersName;
    private Integer obj;// 查询实际状态 0:不查1:长期有效2:未开始3:已开始(未结束)4:已结束的活动5:限时有效
    private Integer status;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public String getPartnersName() {
        return partnersName;
    }

    public void setPartnersName(String partnersName) {
        this.partnersName = partnersName;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
