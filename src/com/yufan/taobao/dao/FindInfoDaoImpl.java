package com.yufan.taobao.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.dao.CommonDAO;
import com.yufan.taobao.bean.ModelBean;
import com.yufan.common.bean.PageUtil;

/**
 * @功能名称 数据查询 taobao页面管管理dao实现层
 * @作者 lirongfan
 * @时间 2015年11月30日 下午2:25:28
 */
@Scope("prototype")
@Service("findInfoDao_taobao")
public class FindInfoDaoImpl implements FindInfoDao {
    @Resource(name = "commonDao")
    private CommonDAO commonDao;

    @Override
    public PageUtil loadTaobaoModelList(ModelBean bean, PageUtil page) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,page_name,b_contents,a_contents,img,to_page,sort,remark,img_num from tb_taobao_page_config where 1=1 ");
        sql.append(" and state=1 ");

        if (null != bean.getPageName() && !"null".equals(bean.getPageName()) && !"".equals(bean.getPageName())) {
            sql.append(" and page_name like '%" + bean.getPageName() + "%' ");
        }

        if (null != bean.getImgNum()) {
            sql.append(" and img_num =" + bean.getImgNum() + " ");
        }

        sql.append(" ORDER BY sort,create_time desc ");
        PageUtil list = commonDao.queryListSQLPageByField(sql.toString(), page.getCurrentPage(), page.getPageSize());
        return list;
    }

    @Override
    public ModelBean getModelBeanById(int id) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from ModelBean where id=" + id + " ");
        return (ModelBean) commonDao.loadObj(hql.toString());
    }

    /**
     * 查询淘宝主页功能资源权限
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getTaoBaoPower() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_value from tb_param where param_code='taobao_source' ");
        list = commonDao.getBySql2(sql.toString());
        return list;
    }

    /**
     * 查询配置的数字字典参数
     *
     * @param code
     * @return
     */
    @Override
    public List<Map<String, Object>> getSysParam(Map<String, String> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_id,param_name,param_type,param_code,param_key,param_value,param_value1,remark from tb_param where 1=1 ");
        String param_code = map.get("param_code");
        String param_value = map.get("param_value");
        String status = map.get("status");

        if (null != param_code && !"".equals(param_code) && !"null".equals(param_code)) {
            sql.append(" and param_code='" + param_code + "' ");
        }
        if (null != param_value && !"".equals(param_value) && !"null".equals(param_value)) {
            sql.append(" and param_value='" + param_value + "' ");
        }
        if (null != status && !"".equals(status) && !"null".equals(status)) {
            sql.append(" and status=" + status + " ");
        }
        list = commonDao.getBySql2(sql.toString());
        return list;
    }
}
