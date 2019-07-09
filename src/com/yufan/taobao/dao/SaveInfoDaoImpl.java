package com.yufan.taobao.dao;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.dao.CommonDAO;
import com.yufan.taobao.bean.ModelBean;
import com.yufan.taobao.dao.SaveInfoDao;

/**
 * @功能名称 数据增加修改删除 taobao页面管管理dao实现层
 * @作者 lirongfan
 * @时间 2015年11月30日 下午2:25:09
 */
@Scope("prototype")
@Service("saveInfoDao_taobao")
public class SaveInfoDaoImpl implements SaveInfoDao {
    @Resource(name = "commonDao")
    private CommonDAO commonDao;

    @Override
    public boolean delStatus(String id, String state) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_taobao_page_config set state=" + state + " where id=" + id);
        commonDao.executeBySql(sql.toString());
        return true;
    }

    @Override
    public boolean saveOrUpdate(ModelBean bean) {
        commonDao.saveOrUpdate(bean);
        return true;
    }
}
