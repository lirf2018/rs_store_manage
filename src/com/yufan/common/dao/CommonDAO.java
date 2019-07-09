/**
 *
 */
package com.yufan.common.dao;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.yufan.common.bean.Page;
import com.yufan.common.bean.PageBean;
import com.yufan.common.bean.PageInfo;
import com.yufan.common.bean.Pagination;
import com.yufan.common.bean.PageUtil;


@SuppressWarnings("rawtypes")
public interface CommonDAO<T, PK extends Serializable> {
    public void exceHql(String hql) throws HibernateException;

    public void delObj(String hql) throws HibernateException;

    public void deleteObj(Object obj) throws HibernateException;

    public void deleteAll(Collection con) throws HibernateException;

    public void deleteAll(Class clazz) throws HibernateException;

    public void deleteAll(String hql, Object[] params) throws HibernateException;

    public Object loadObj(Class className, Serializable id) throws HibernateException;

    public Object loadObj(final String hql);

    public Object loadObjByKey(Class clazz, String keyName, Object keyValue) throws HibernateException;

    public List getObjBySql(final String sql);

    public List getListObj(Class className) throws HibernateException;

    public List getListObj(String hql) throws HibernateException;

    public void createObj(Object obj) throws HibernateException;


    public void saveOrUpdateList(final List<? extends Object> list);

    public List findByNamedQuery(String queryName) throws HibernateException;

    public List findByNameQuery(String queryName, Object[] params) throws HibernateException;

    public List find(String queryString, Object[] params) throws HibernateException;

    public void updateObj(Object obj) throws HibernateException;

    public void updateSql(String sql) throws HibernateException;

    public void saveOrUpdate(Object obj) throws HibernateException;

    public int getCountNumBySql(String sql) throws Exception;

    public List divPageBySql(final String countSql, final String listSql, final Pagination page);

    public List divPage(final DetachedCriteria dc, final Pagination page, final Session session);

    public List divPage(final String countSql, final Query dc, final Pagination page);

    public int getSqlCountNum(String sql);

    //hql分页
    public List divPage(final String countHql, final String listHql, final Pagination page);

    //hql分页（防注入）
    public List divPage(final String countHql, final String listHql, final Pagination page, final Map<Serializable, Serializable> map);

    public int createList(final List<Object> list);

    public PageBean hqlProcess(PageBean pageBean);

    public List<Object[]> getSQLQuery(String sqlQuery);

    public PageBean SQLProcess(PageBean pageBean);

    public void processSQL(String insertSql, String insertSql2, String updateSql, String deleteSql);

    public void callProcedure(String procName, long singleValue);

    public void hqlPageInfo(PageInfo page);

    public void sqlPageInfo(PageInfo page);

    public Class<T> getEntityClass();

    public void flushSession();

    public void clearSession();

    public void delete(PK id);

    public void delete(T entity);

    public long getSequence(String seqName);

    public boolean exists(PK id);

    public T get(PK id);

    public <T2> T2 get(Class T2, PK id);

    public T get(Map<String, Object> values);

    public List<T> getList(Map<String, Object> values);

    public List<T> getAll();

    public void saveOrUpdateObj(T entity);

    public void merge(T entity);

    public void update(T entity);

    public int Insert(String Sql);

    public int executeByHql(final String queryString, final Map<String, ?> values);

    public int executeByHql(final String queryString, final Object... values);

    public int executeBySql(final String queryString, final Map<String, ?> values);

    public int executeBySql(final String queryString, final Object... values);

    public T queryUniqueByHql(final String queryString, final Object... values);

    public T queryUniqueByHql(final String queryString, final Map<String, ?> values);

    public T queryUniqueBySql(final boolean transformer, final String queryString, final Map<String, ?> values);

    public T queryUniqueBySql(final boolean transformer, final String queryString, final Object... values);

    public T queryUniqueBySql(String queryString, Map<String, ?> values);

    public T queryUniqueBySql(String queryString, Object... values);

    public Object queryObjectBySql(final String queryString, final Map<String, ?> values);

    public Object queryObjectBySql(final String queryString, final Object... values);

    public Long queryUniqueNumberByHql(final String queryString, final Map<String, ?> values);

    public Long queryUniqueNumberByHql(final String queryString, final Object... values);

    public Long queryUniqueNumberBySql(final String queryString, final Map<String, ?> values);

    public BigDecimal queryUniqueBigDecimalBySql(final String queryString, final Map<String, ?> values);

    public Long queryUniqueNumberBySql(final String queryString, final Object... values);

    public BigDecimal queryUniqueBigDecimalBySql(final String queryString, final Object... values);

    public List<T> queryListByHql(final String queryString, final Map<String, ?> values);

    public List<T> queryListByHql(final String queryString, final Object... values);

    public List<T> queryListBySql(final boolean transformer, final String queryString, final Map<String, ?> values);

    public List<T> queryListBySql(final boolean transformer, final String queryString, final Object... values);

    public List<T> queryListBySql(final boolean transformer, final String queryString, final Class transFormerClass, final Object... values);

    public List queryObjectListBySql(final boolean transformer, final String queryString, final Object... values);

    public List queryObjectListBySql(final String queryString, final Map<String, ?> map);

    public List<BigDecimal[]> queryListBigDecimalBySql(final String queryString, final Object... values);

    public List<BigDecimal[]> queryListBigDecimalBySql(final String queryString, final int currentPage, final int pageSize, final Object... values);

    public List<BigDecimal[]> queryListBigDecimalBySql(final String queryString, final Map<String, Object> values);

    public List<BigDecimal[]> queryListBigDecimalBySql(final String queryString, final int currentPage,
                                                       final int pageSize, final Map<String, Object> values);

    public List<Object[]> queryListObjectBySql(final String queryString, final int currentPage,
                                               final int pageSize, final Map<String, Object> values);

    public List<Object[]> queryListObjectBySql(final String queryString, final Map<String, Object> values);

    public List<T> queryListBySql(String queryString, Map<String, ?> values);

    public List<T> queryListBySql(String queryString, Object... values);

    public <T2> List<T2> queryListBySqlWithTranCalss(final String queryString, final Class<T2> tranClass, final Object... values);

    public <T2> List<T2> queryListBySqlWithTranCalss(final String queryString, final Class<T2> tranClass, final Map<String, ?> values);

    public List queryObjectListBySql(String queryString, Object... values);

    public List<T> queryPageListByHql(final String queryString, Page<T> page, final Map<String, ?> values);

    public List<T> queryPageListByHql(final String queryString, Page<T> page, final Object... values);

    public List<T> queryPageListByHql(final String queryString, final int currentPage, final int pageSize, final Map<String, ?> values);

    public List<T> queryPageListByHql(final String queryString,
                                      final int currentPage, final int pageSize, final Object... values);

    public <T2> List<T2> queryPageListBySql(final boolean transformer, final String queryString, final Class<T2> dto, final int currentPage,
                                            final int pageSize, final Object... values);

    public <T2> List<T2> queryPageListBySql(final boolean transformer, final String queryString, final Class<T2> dto, final int currentPage,
                                            final int pageSize, final Map<String, ?> values);

    public <T2> List<T2> queryListBySql(final boolean transformer,
                                        final String queryString, final Class<T2> dto, final Map<String, ?> values);

    public List<T> queryPageListBySql(final boolean transformer, final String queryString, final int currentPage, final int pageSize, final Object... values);

    public List<T> queryPageListBySql(String queryString, int currentPage,
                                      int pageSize, Map<String, ?> values);

    public List<T> queryPageListBySql(boolean transformer, String queryString, int currentPage, int pageSize,
                                      Map<String, ?> values);

    public List<T> queryPageListBySql(String queryString, int currentPage, int pageSize, Object... values);

    public List<T> queryPageListBySql(String queryString, Page<T> page, Map<String, ?> values);

    public List<T> queryPageListBySql(String queryString, Page<T> page, Object... values);

    public List queryResultToMap(final String sql, final Map<String, ?> values);

    public Map<String, String> queryResultToMapByString(final String sql, final Object... values);

    public Map<Long, String> queryResultToMap(final String sql, final Object... values);

    public Map<Long, Long> queryResultToMapByLong(final String sql, final Object... values);

    public void setNamedParams(StringBuffer sql, Map<String, ?> values);

    public List<Map<String, Object>> getBySql2(String sql, Object... values);

    /**
     * 查询分页
     *
     * @param sql
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageUtil queryListSQLPageByField(final String sql, int currentPage, int pageSize);

    public PageUtil queryListSQLPageByField(final String sql, String sqlCount, int currentPage, int pageSize);

    public PageUtil queryListMapSQLPageByField(final String sql, String sqlCount, int currentPage, int pageSize, Object... values);

    public Session get_Session();

    public int maxId(String sql);

    //返回sql结果集
    public ResultSet selecAllBySQL(String sql);

    //更新数据
    public void updateBySQL(String sql);
    //public List getObjBySql2(final String sql);


}
