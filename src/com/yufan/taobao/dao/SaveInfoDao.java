package com.yufan.taobao.dao;

import com.yufan.taobao.bean.ModelBean;

/**
 * @功能名称 数据增加修改删除 taobao页面管管理dao层
 * @作者 lirongfan
 * @时间 2015年11月30日 下午2:25:02
 */
public interface SaveInfoDao {

    /**
     * 逻辑删除
     *
     * @return
     */
    public boolean delStatus(String id, String state);

    /**
     * 增加或者修改
     *
     * @param bean
     * @return
     */
    public boolean saveOrUpdate(ModelBean bean);

}
