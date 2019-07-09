package com.yufan.taobao.dao;

import java.util.List;
import java.util.Map;

import com.yufan.taobao.bean.ModelBean;
import com.yufan.common.bean.PageUtil;

/**
 * @功能名称 数据查询 taobao页面管管理dao层
 * @作者 lirongfan
 * @时间 2015年11月30日 下午2:25:21
 */
public interface FindInfoDao {

    /**
     * 加载列表
     *
     * @param bean
     * @param page
     * @return
     */
    public PageUtil loadTaobaoModelList(ModelBean bean, PageUtil page);

    /**
     * @param id
     * @return
     */
    public ModelBean getModelBeanById(int id);

    /**
     * 查询淘宝主页功能资源权限
     *
     * @return
     */
    public List<Map<String, Object>> getTaoBaoPower();

    /**
     * 查询配置的数字字典参数
     *
     * @param code
     * @return
     */
    public List<Map<String, Object>> getSysParam(Map<String, String> map);
}
