package com.yufan.common.dao.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.ActionContext;
import com.yufan.common.bean.Page;
import com.yufan.common.bean.PageBean;
import com.yufan.common.bean.PageInfo;
import com.yufan.common.bean.Pagination;
import com.yufan.common.bean.PropertyFilter;
import com.yufan.common.bean.ReflectionUtils;
import com.yufan.common.dao.CommonDAO;
import com.yufan.util.ConfigProperty;
import com.yufan.common.bean.PageUtil;
import org.springframework.util.StringUtils;

@SuppressWarnings({"unused", "unchecked", "deprecation", "rawtypes"})
public class CommonDAOImpl<T, PK extends Serializable> extends HibernateDaoSupport implements CommonDAO<T, PK> {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private SessionFactory sessionFactory;

    protected Class<T> entityClass;

    public CommonDAOImpl() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    public CommonDAOImpl(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setEntityClass(final Class entityClass) {
        this.entityClass = entityClass;
    }

    public void exceHql(String hql) throws HibernateException {
        Session session = getSessionObj();

        Transaction tx = session.beginTransaction();
        String hqlInsert = hql;
        int createdEntities = session.createQuery(hqlInsert).executeUpdate();
        tx.commit();
    }

    public List<Map<String, Object>> getBySql2(String sql, Object... values) {
        Session session = getSessionObj();
        try {
            SQLQuery query = session.createSQLQuery(sql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    query.setParameter(i, values[i]);
                }
            }
            query.setResultTransformer(new ResultTransformer() { // alues=值
                // columns=列名
                public Object transformTuple(Object[] values, String[] columns) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int i = 0; i < columns.length; i++) {
                        map.put(columns[i], values[i] == null ? "" : values[i]);
                    }
                    return map;
                }

                public List transformList(List columns) {
                    return columns;
                }
            });

            return query.list();
        } catch (HibernateException he) {
            log.error("通过SQL查询 " + sql + " 数据失败", he);
            return null;
        } finally {
        }
    }

    /*
     * @SuppressWarnings("unchecked") public List<Map<String object="">>
     * getObjBySql2(final String sql) { return (List<Map<String object="">>)
     * dao.getHibertemplate().execute(new HibernateCallback() {
     *
     * @Override public List<Map<string object="">> doInHibernate(Session
     * session) throws HibernateException, SQLException { SQLQuery query =
     * session.createSQLQuery(sql); query.setResultTransformer( new
     * ResultTransformer() { private static final long serialVersionUID =
     * 7178852496845637376L;
     *
     * @Override public Object transformTuple(Object[] values, String[] aliases)
     * { if(ObjectUtils.isEmpty(aliases)) { return null; } Map<String object="">
     * rowMap = new StringKeyCaseInsensitiveMap<String object="">();
     *
     * for(int i = 0 ; i < aliases.length; i ++) { rowMap.put(aliases[i],
     * values[i]); } return rowMap; }
     *
     * @SuppressWarnings("rawtypes")
     *
     * @Override public List transformList(List collection) { return collection;
     * } }); return Collections.unmodifiableList(query.list()); } }); }
     */

    /*
     * public List getObjBySql2(final String sql) { Session session =
     * getSessionObj(); try { List list = new ArrayList(); Query query =
     * session.createSQLQuery(sql); list = query.list(); return list; } catch
     * (HibernateException he) { log.error("通过SQL查询 " + sql + " 数据失败", he);
     * return null; } finally { ; } }
     */
    public List getObjBySql(final String sql) {
        Session session = getSessionObj();
        try {
            List list = new ArrayList();
            Query query = session.createSQLQuery(sql);
            list = query.list();
            return list;
        } catch (HibernateException he) {
            log.error("通过SQL查询 " + sql + " 数据失败", he);
            return null;
        } finally {
        }
    }

    public void delObj(final String hql) throws HibernateException {
        this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws SQLException,
                    HibernateException {
                Query query = session.createQuery(hql);
                query.executeUpdate();
                return null;
            }
        });
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteObj(Object obj) throws HibernateException {
        try {
            getHibernateTemplate().delete(obj);
            String fromIpAddr = (String) ActionContext.getContext()
                    .getSession().get("fromIpAddr");

        } catch (DataAccessException e) {
            log.error("从数据库删除 " + obj.getClass().getName() + " 实例失败", e);
            throw new HibernateException("从数据库删除 " + obj.getClass().getName()
                    + " 实例失败", e);
        }
    }

    /**
     * 从数据库删除�?有对应于�?个业务对象的记录
     *
     * @param Collection 指定类型的业务对�?
     * @throws HibernateException 当删除记录失败时抛出异常
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAll(Collection con) throws HibernateException {
        try {
            getHibernateTemplate().deleteAll(con);
        } catch (DataAccessException e) {
            log.error("从数据库删除的所有记录失�?", e);
        }
    }

    public void deleteAll(String hql, Object[] params)
            throws HibernateException {
        try {
            List temp = find(hql, params);
            this.getHibernateTemplate().deleteAll(temp);

        } catch (DataAccessException e) {
            throw new HibernateException("从数据库删除 " + hql + " 的所有记录失�?", e);
        }
    }

    /**
     * 从数据库删除�?有对应于�?个业务对象的记录
     *
     * @param clazz 指定类型的业务对�?
     * @throws HibernateException 当删除记录失败时抛出异常
     */
    public void deleteAll(Class clazz) throws HibernateException {
        try {
            List result = getHibernateTemplate().loadAll(clazz);
            getHibernateTemplate().deleteAll(result);
        } catch (DataAccessException e) {
            log.error("从数据库删除 " + clazz.getName() + " 的所有记录失�?", e);
            throw new HibernateException("从数据库删除 " + clazz.getName()
                    + " 的所有记录失�?", e);
        }
    }

    public Object loadObj(Class className, Serializable id)
            throws HibernateException {
        Object obj = getHibernateTemplate().get(className, id);
        if (!Hibernate.isInitialized(obj)) {
            Hibernate.initialize(obj);
        }
        return obj;
    }

    public Object loadObj(final String hql) {
        return (Object) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {
                return session.createQuery(hql).setMaxResults(1).uniqueResult();
            }
        });

    }

    public Object getMaxObj(final String hql) {
        try {
            return (Object) getHibernateTemplate().execute(
                    new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                throws HibernateException {
                            return session.createQuery(hql).uniqueResult();
                        }
                    });
        } catch (DataAccessException e) {
            throw new HibernateException(e);
        }

    }

    public Object loadObjByKey(Class clazz, String keyName, Object keyValue)
            throws HibernateException {
        Object obj = null;
        try {
            List result = getHibernateTemplate().find(
                    "from " + clazz.getName() + " where " + keyName + " = ?",
                    keyValue);
            if (result != null && result.size() > 0) {
                obj = result.get(0);
            }
        } catch (DataAccessException e) {
            log.error(
                    "加载 " + keyName + " �? " + keyValue + " �? "
                            + clazz.getName() + " 实例失败", e);
            throw new HibernateException("加载 " + keyName + " �? " + keyValue
                    + " �? " + clazz.getName() + " 实例失败", e);
        }
        return obj;
    }

    public List getListObj(Class className) throws HibernateException {
        List list = null;
        try {

            list = getHibernateTemplate().loadAll(className);
        } catch (DataAccessException e) {
            log.error("加载�?�? " + className.getName() + " 实例时失�?", e);
            throw new HibernateException("加载�?�? " + className.getName()
                    + " 实例时失�?", e);
        }
        return list;
    }

    public List getListObj(String hql) throws HibernateException {
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (DataAccessException e) {
            log.error("执行查询 " + hql + " 失败", e);
            throw new HibernateException("执行查询 " + hql + " 失败", e);
        }
        this.getSession().clear();

        return list;
    }

    /**
     * 向数据库添加�?条对应于�?个业务对象实例的记录
     *
     * @param entity 业务对象实例
     * @throws DaoException 当添加记录失败时抛出异常
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createObj(Object obj) throws HibernateException {
        try {
            getHibernateTemplate().save(obj);
        } catch (DataAccessException e) {
            log.error("保存 " + obj.getClass().getName() + " 实例到数据库失败", e);
            throw new HibernateException("保存 " + obj.getClass().getName()
                    + " 实例到数据库失败", e);
        }
    }

    public int createList(final List<Object> list) {
        Integer result = (Integer) super.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session) {
                        int rows = 0;

                        for (Object t : list) {
                            session.save(t);
                            rows++;
                            if (rows % 10000 == 0) {
                                session.flush();
                                session.clear();
                            }
                        }
                        return new Integer(rows);
                    }
                });
        return result.intValue();
    }

    public void saveOrUpdateList(final List<? extends Object> list) {
        super.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                int rows = 0;
                for (Object t : list) {
                    session.saveOrUpdate(t);
                    rows++;
                    if (rows % 10000 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
                return new Integer(rows);
            }
        });
    }

    public List findByNamedQuery(String queryName) throws HibernateException {
        try {
            return getHibernateTemplate().findByNamedQuery(queryName);
        } catch (DataAccessException e) {
            log.error("执行命名�? " + queryName + " 的查询失�?");
            throw new HibernateException("执行命名�? " + queryName + " 的查询失�?");
        }
    }

    public List findByNameQuery(String queryName, Object[] params)
            throws HibernateException {
        try {
            return getHibernateTemplate().findByNamedQuery(queryName, params);
        } catch (DataAccessException e) {
            StringBuffer paramString = new StringBuffer("");
            for (int i = 0; i < params.length; i++) {
                paramString.append(params[i]);
                paramString.append(" ");
            }
            log.error("执行参数�? " + paramString + "命名�? " + queryName + " 的查询失�?");
            throw new HibernateException("执行参数�? " + paramString + "命名�? "
                    + queryName + " 的查询失�?");
        }
    }

    public List find(String queryString, Object[] params)
            throws HibernateException {
        try {
            return getHibernateTemplate().find(queryString, params);
        } catch (DataAccessException e) {
            StringBuffer paramString = new StringBuffer("");
            for (int i = 0; i < params.length; i++) {
                paramString.append(params[i]);
                paramString.append(" ");
            }
            log.error("执行参数�? " + paramString + "的查�? " + queryString + " 失败",
                    e);
            throw new HibernateException("执行参数�? " + paramString + "的查�? "
                    + queryString + " 失败", e);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateObj(Object obj) throws HibernateException {
        try {
            getHibernateTemplate().update(obj);
            String fromIpAddr = (String) ActionContext.getContext()
                    .getSession().get("fromIpAddr");
        } catch (DataAccessException e) {
            log.error("更新 " + obj.getClass().getName() + " 实例到数据库失败", e);
            throw new HibernateException("更新 " + obj.getClass().getName()
                    + " 实例到数据库失败", e);
        }
    }

    public void updateSql(String sql) throws HibernateException {
        Session session = this.getSession();
        try {
            session.connection().createStatement().executeUpdate(sql);
        } catch (Exception ex) {
            log.error("更新 " + sql + " 实例到数据库失败", ex);
        } finally {
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Object obj) throws HibernateException {
        getHibernateTemplate().saveOrUpdate(obj);

    }

    public int getCountNumBySql(String hql) throws Exception {
        int count = 0;
        Object countt = (Long) getHibernateTemplate().find(hql).listIterator()
                .next();
        return (countt == null) ? Integer.parseInt("0") : Integer
                .parseInt(countt.toString());
    }

    /**
     * 分页查询依据Criterial
     */
    public List divPage(final DetachedCriteria dc, final Pagination page,
                        final Session session) {
        Criteria ca = dc.getExecutableCriteria(session);
        try {
            page.setTotalNum(((Integer) ca
                    .setProjection(Projections.rowCount()).uniqueResult())
                    .intValue());
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        page.turnpage();
        ca.setProjection(null);
        ca.setMaxResults(page.getPageSize());
        ca.setFirstResult(page.getStartIndex());
        try {
            return ca.list();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("分页" + ca.getClass().getName(), ex);
        }
        return new ArrayList();
    }

    /**
     * 分页查询依据Criterial
     */
    public List divPage(final String countSql, final Query dc,
                        final Pagination page) {
        try {
            page.setTotalNum(this.getCountNumBySql(countSql));
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        page.turnpage();
        dc.setMaxResults(page.getPageSize());
        dc.setFirstResult(page.getStartIndex());
        try {
            return dc.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList();
    }

    public List divPage(final String countHql, final String listHql, final Pagination page) {
        try {
            page.setTotalNum(this.getCountNumBySql(countHql));
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        page.turnpage();
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {
                Query query = session.createQuery(listHql);
                query.setFirstResult(page.getStartIndex());
                query.setMaxResults(page.getPageSize());
                return query.list();
            }
        });
    }

    /**
     * 分页重载（防注入）
     *
     * @param countHql
     * @param listHql
     * @param page
     * @return
     */
    public List divPage(final String countHql, final String listHql,
                        final Pagination page, final Map<Serializable, Serializable> map) {
        try {
            page.setTotalNum(this.getTotalNumBySql(countHql, map));
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        page.turnpage();
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException {
                Query query = session.createQuery(listHql);
                query.setFirstResult(page.getStartIndex());
                query.setMaxResults(page.getPageSize());
                for (Serializable key : map.keySet()) {
                    query.setParameter((String) key, "%" + map.get(key) + "%");
                }
                return query.list();
            }
        });
    }

    private int getTotalNumBySql(String countHql,
                                 final Map<Serializable, Serializable> map) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(countHql);
        for (Serializable key : map.keySet()) {
            query.setParameter((String) key, map.get(key));
        }
        List lsit = query.list();
        if (lsit != null) {
            return Integer.parseInt(lsit.get(0).toString());
        }
        return 0;
    }

    /**
     * jsp(Sql分页)
     *
     * @param countSql
     * @param listSql
     * @param page
     * @return
     */
    public List divPageBySql(final String countSql, final String listSql,
                             final Pagination page) {
        Session session = getSessionObj();
        try {
            page.setTotalNum(this.getSqlCountNum(countSql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.turnpage();
        List list = new ArrayList();
        try {
            Query query = session.createSQLQuery(listSql);
            query.setFirstResult(page.getStartIndex());
            query.setMaxResults(page.getPageSize());
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return list;
    }

    public Session getSessionObj() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    // 采用getCurrentSession()创建的session在commit或rollback时会自动关闭，而采用openSession()，创建的session必须手动关闭
    public Session getSessionObj1() throws HibernateException {
//		return getHibernateTemplate().getSessionFactory().getCurrentSession();
        return super.getSessionFactory().getCurrentSession();
    }

    public String replaceS(String field) {
        String result = field.replaceAll("'", "''");
        return result;
    }

    /**
     * 分页
     *
     * @param pageBean
     * @return
     */
    public PageBean hqlProcess(PageBean pageBean) {
        Session session = getSessionObj();
        int fromIndex;
        String fromHql = "";
        fromIndex = pageBean.getHql().indexOf("from ");
        fromHql = pageBean.getHql().substring(fromIndex);

        /**
         * �?查查询语句中有无排序字段
         */
        if (pageBean.getOrderBy() != null && pageBean.getOrderBy().length() > 0) {
            String tmpHql = pageBean.getHql();
            if (pageBean.getHql().indexOf("order by ") == -1) {
                tmpHql += " order by " + pageBean.getOrderBy();
            } else {
                tmpHql += ", " + pageBean.getOrderBy();
            }
            pageBean.setHql(tmpHql);
        }

        if (pageBean.getTotalProperty() == 0) {// 计算总数
            String countHql = "select count(*) " + fromHql;
            Query countQuery = session.createQuery(countHql);
            Object totalRow = countQuery.list().iterator().next();
            pageBean.setTotalProperty((totalRow == null) ? Integer
                    .parseInt("0") : Integer.parseInt(totalRow.toString()));
        }

        if (pageBean.getTotalProperty() > 0) {// 查询记录信息
            Query query = session.createQuery(pageBean.getHql());
            query.setFirstResult(pageBean.getStart());
            query.setMaxResults(pageBean.getPageSize());
            List resultList = query.list();
            pageBean.setRoot(resultList);

        }
        return pageBean;
    }

    public List<Object[]> getSQLQuery(String sqlQuery) {
        List<Object[]> list = new ArrayList<Object[]>();
        Session session = this.getSessionObj();
        Query query = session.createSQLQuery(sqlQuery);
        list = query.list();
        return list;
    }

    /**
     * 纯sql查询总条�?
     *
     * @param sql
     * @return
     */
    public int getSqlCountNum(String sql) {
        Session session = getSessionObj();
        Query countQuery = session.createSQLQuery(sql);
        Object[] totalRow = (Object[]) countQuery.list().iterator().next();
        return totalRow == null ? 0 : totalRow.length;
    }

    /**
     * 处理纯SQL 查询分页
     *
     * @param pageBean
     * @return
     */
    public PageBean SQLProcess(PageBean pageBean) {
        Session session = getSessionObj();
        int fromIndex;
        String fromHql = "";
        fromIndex = pageBean.getHql().indexOf("from ");

        fromHql = pageBean.getHql().substring(fromIndex);
        if (pageBean.getTotalProperty() == 0) {// 计算总数
            String countHql = "select count(*) " + fromHql;
            Query countQuery = session.createSQLQuery(countHql);
            Object totalRow = countQuery.list().iterator().next();
            pageBean.setTotalProperty(totalRow == null ? Integer.parseInt("0")
                    : Integer.parseInt(totalRow.toString()));
        }

        if (pageBean.getTotalProperty() > 0) {// 查询记录信息
            Query query = session.createSQLQuery(pageBean.getHql());
            query.setFirstResult(pageBean.getStart());
            query.setMaxResults(pageBean.getPageSize());
            List resultList = query.list();
            pageBean.setRoot(resultList);

        }
        return pageBean;
    }

    /**
     * @param insertSql
     * @param updateSql
     * @param deleteSql
     */
    public void processSQL(String insertSql, String insertSql2,
                           String updateSql, String deleteSql) {
        Session session = this.getSessionObj();
        Transaction ts = session.beginTransaction();
        if (!"".equals(insertSql) && null != insertSql) {
            int i = session.createSQLQuery(insertSql).executeUpdate();
        }
        if (!"".equals(insertSql2) && null != insertSql2) {
            int j = session.createSQLQuery(insertSql2).executeUpdate();
        }
        if (!"".equals(updateSql) && null != updateSql) {
            int k = session.createSQLQuery(updateSql).executeUpdate();
        }
        if (!"".equals(deleteSql) && null != deleteSql) {
            int l = session.createSQLQuery(deleteSql).executeUpdate();
        }
        ts.commit();
    }

    /**
     * 调用 只有1个输入参数的存储过程
     */
    public void callProcedure(String procName, long singleValue) {
        Session session = this.getSessionObj();
        Connection con = session.connection();
        String callProc = "{call " + procName + "(?)}";
        CallableStatement callStmt = null;
        try {
            callStmt = con.prepareCall(callProc);
            callStmt.setLong(1, singleValue);
            callStmt.execute();
        } catch (SQLException e) {
            System.out.println("执行存储过程'" + procName + "'失败");
            e.printStackTrace();
        } finally {
            try {
                if (callStmt != null) {
                    callStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sqlPageInfo(PageInfo page) {
        Session session = getSessionObj();
        int fromIndex;
        String fromHql = "";
        fromIndex = page.getHql().indexOf("from ");
        fromHql = page.getHql().substring(fromIndex);

        if (page.getOrderby() != null && page.getOrderby().length() > 0) {
            String tmpHql = page.getHql();
            if (page.getHql().indexOf("order by ") == -1) {
                tmpHql += " order by " + page.getOrderby();
            } else {
                tmpHql += ", " + page.getOrderby();
            }
            page.setHql(tmpHql);
        }

        if (page.getRecordSum() == 0) {// 计算总数
            String countHql = "select count(*) " + fromHql;
            Query countQuery = session.createSQLQuery(countHql);
            // Query countQuery = session.createQuery(countHql);
            Object totalRow = countQuery.list().iterator().next();
            page.setRecordSum(totalRow == null ? Integer.parseInt("0")
                    : Integer.parseInt(totalRow.toString()));
        }

        account(page);

        if (page.getRecordSum() > 0) {// 查询记录信息
            Query query = session.createSQLQuery(page.getHql());
            query.setFirstResult((page.getCurrePage() - 1) * page.getPageSize());
            query.setMaxResults(page.getPageSize());
            List resultList = query.list();
            page.setResultList(resultList);
            ServletActionContext.getRequest().setAttribute("pi", page);
        }
    }

    public void hqlPageInfo(PageInfo page) {
        Session session = getSessionObj();
        int fromIndex;
        String fromHql = "";
        fromIndex = page.getHql().indexOf("from ");
        fromHql = page.getHql().substring(fromIndex);

        /**
         * �?查查询语句中有无排序字段
         */
        if (page.getOrderby() != null && page.getOrderby().length() > 0) {
            String tmpHql = page.getHql();
            if (page.getHql().indexOf("order by ") == -1) {
                tmpHql += " order by " + page.getOrderby();
            } else {
                tmpHql += ", " + page.getOrderby();
            }
            page.setHql(tmpHql);
        }

        if (page.getRecordSum() == 0) {// 计算总数
            String countHql = "select count(*) " + fromHql;
            Query countQuery = session.createQuery(countHql);
            // Query countQuery = session.createQuery(countHql);
            Object totalRow = countQuery.list().iterator().next();
            page.setRecordSum(totalRow == null ? Integer.parseInt("0")
                    : Integer.parseInt(totalRow.toString()));
        }

        account(page);
        if (page.getRecordSum() > 0) {// 查询记录信息
            Query query = session.createQuery(page.getHql());
            query.setFirstResult((page.getCurrePage() - 1) * page.getPageSize());
            query.setMaxResults(page.getPageSize());
            List resultList = query.list();
            page.setResultList(resultList);
            ServletActionContext.getRequest().setAttribute("pi", page);
        }
    }

    /**
     * 计算分页数据
     */
    private static void account(PageInfo pi) {
        /**
         * 计算分页导航信息
         */
        // 计算总页�?

        pi.setPageSum((pi.getRecordSum() % pi.getPageSize() == 0) ? pi
                .getRecordSum() / pi.getPageSize() : pi.getRecordSum()
                / pi.getPageSize() + 1);
        if (pi.getPageSum() == 0)
            pi.setPageSum(1);
        // 计算当前页码�?
        if (pi.getCurrePage() < 1)
            pi.setCurrePage(1);
        else if (pi.getCurrePage() > pi.getPageSum())
            pi.setCurrePage(pi.getPageSum());
        // 计算当前页的记录数量
        pi.setCurrePageRecord(pi.getRecordSum()
                - (pi.getPageSize() * (pi.getCurrePage() - 1)));
        if (pi.getCurrePageRecord() > pi.getPageSize())
            pi.setCurrePageRecord(pi.getPageSize());
        if (pi.getCurrePageRecord() < 0)
            pi.setCurrePageRecord(0);
        pi.setFirstRecord(pi.getPageSize() * (pi.getCurrePage() - 1));
        // 计算是否有下�?�?,上一�?
        pi.setHasNext((pi.getCurrePage() < pi.getPageSum()) ? true : false);
        pi.setHasPrevious((pi.getCurrePage() > 1) ? true : false);
        if (pi.getRecordSum() == 0) {// 如果没有记录
            pi.setResultList(new ArrayList());
        }
    }

    /**
     * 计算jsp用户总数
     */
    public int getCountNumBySql(String sql, Session session,
                                final Map<Serializable, Serializable> map) {
        Query countQuery = session.createSQLQuery(sql.toString());
        int countNum = 0;
        for (Serializable key : map.keySet()) {
            countQuery.setParameter((Integer) key, map.get(key));
        }
        if (countQuery.list() != null && countQuery.list().size() > 0) {
            countNum = countQuery.list().size();
        }
        return countNum;
    }

    public Class<T> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.entityClass;
    }

    public void flushSession() {
        this.getSession().flush();
    }

    public void clearSession() {
        this.getSession().clear();
    }

    public void delete(PK id) {
        delete(get(id));
    }

    public void delete(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getHibernateTemplate().delete(entity);
        logger.debug("delete entity: " + entity);
    }

    /**
     * 获取实体类对应的表名
     *
     * @return
     */
    public String getTableName() {
        String result = null;
        Annotation[] an = entityClass.getAnnotations();
        for (int i = 0; i < an.length; i++) {
            if (an[i] instanceof Table) {
                Table t = (Table) an[i];
                result = t.name();
            }
        }
        return result;
    }

    public long getSequence(String seqName) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ").append(seqName).append(".nextval from dual ");
        return queryUniqueNumberBySql(sql.toString());
    }

    /**
     * 获取某实体类对应的序列，序列应该加入某个方法的注解中
     *
     * @param t
     * @return
     */
    public Long getSeqValue() {
        String seqName = null;
        Annotation[] an = entityClass.getAnnotations();
        for (int i = 0; i < an.length; i++) {
            if (an[i] instanceof SequenceGenerator) {
                SequenceGenerator seq = (SequenceGenerator) an[i];
                seqName = seq.sequenceName();
                break;
            }
        }
        if (seqName == null) {
            an = entityClass.getSuperclass().getAnnotations();
            for (int i = 0; i < an.length; i++) {
                if (an[i] instanceof SequenceGenerator) {
                    SequenceGenerator seq = (SequenceGenerator) an[i];
                    seqName = seq.sequenceName();
                    break;
                }
            }
        }
        if (seqName == null) {
            Method[] m = entityClass.getMethods();
            for (Method m1 : m) {
                Annotation[] man = m1.getAnnotations();
                for (int i = 0; i < man.length; i++) {
                    if (man[i] instanceof SequenceGenerator) {
                        SequenceGenerator seq = (SequenceGenerator) man[i];
                        seqName = seq.sequenceName();
                        break;
                    }
                }
                if (seqName != null) {
                    break;
                }
            }
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" select " + seqName + ".nextval  from dual ");
        return queryUniqueNumberBySql(sql.toString());
    }

    public String getEntityName() {
        if (entityClass != null) {
            return entityClass.getName();
        }
        return null;
    }

    public boolean exists(PK id) {
        T entity = (T) getHibernateTemplate().get(getEntityClass(), id);
        return entity != null;
    }

    public T get(PK id) {
        Assert.notNull(id, "id不能为空");
        return (T) getHibernateTemplate().get(getEntityClass(), id);
    }

    public <T2> T2 get(Class T2, PK id) {
        Assert.notNull(id, "id不能为空");
        return (T2) getHibernateTemplate().get(T2, id);
    }

    public T get(Map<String, Object> values) {
        String fullName = entityClass.getName();
        int lastIndex = fullName.lastIndexOf(".");
        String name = fullName.substring(lastIndex + 1);
        StringBuffer sql = new StringBuffer("from " + name);
        setNamedParams(sql, values);
        return queryUniqueByHql(sql.toString(), values);
    }

    public List<T> getList(Map<String, Object> values) {
        String fullName = entityClass.getName();
        int lastIndex = fullName.lastIndexOf(".");
        String name = fullName.substring(lastIndex + 1);
        StringBuffer sql = new StringBuffer("from " + name);
        setNamedParams(sql, values);
        return queryListByHql(sql.toString(), values);
    }

    public List<T> getAll() {
        return getHibernateTemplate().loadAll(getEntityClass());
    }

    public Integer save(T entity) {
        Assert.notNull(entity, "entity不能为空");
        // getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
        // getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
        logger.debug("save entity: " + entity);
        Serializable s = getHibernateTemplate().save(entity);
        return (Integer) s;
    }

    public void saveOrUpdateObj(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getHibernateTemplate().saveOrUpdate(entity);
        logger.debug("saveOrUpdate entity: " + entity);
    }

    public void merge(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getHibernateTemplate().merge(entity);
        logger.debug("merge entity: " + entity);
    }

    public void update(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getHibernateTemplate().update(entity);
        logger.debug("update entity: " + entity);
    }

    public int executeByHql(final String queryString,
                            final Map<String, ?> values) {
        /*
         * return (Integer) getHibernateTemplate().execute( new
         * HibernateCallback() { public Object doInHibernate(Session session)
         * throws HibernateException, SQLException { Query query =
         * session.createQuery(queryString); if (values != null && values.size()
         * > 0) { query.setProperties(values); } return query.executeUpdate(); }
         * });
         */
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new UpdateExecutor(), queryDecorator);
        return (Integer) result;
    }

    public Object HibernateExecute(final Boolean isSql,
                                   final String queryString, final Executor executor,
                                   final QueryDecorator queryDecorator) {
        return getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = isSql ? session.createSQLQuery(queryString)
                        : session.createQuery(queryString);
                if (queryDecorator != null) {
                    queryDecorator.setQuery(query);
                    query = queryDecorator.execute();
                }
                return executor.execute(query);
            }
        });
    }

    public int executeByHql(final String queryString, final Object... values) {
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new UpdateExecutor(), queryDecorator);
        return (Integer) result;
    }

    public int executeBySql(final String queryString,
                            final Map<String, ?> values) {
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(true, queryString,
                new UpdateExecutor(), queryDecorator);
        return (Integer) result;
    }

    public int executeBySql(final String queryString, final Object... values) {
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(true, queryString,
                new UpdateExecutor(), queryDecorator);
        return (Integer) result;
    }

    public T queryUniqueByHql(final String queryString, final Object... values) {
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new UniqueResultExecutor(), queryDecorator);
        return (T) result;
    }

    public T queryUniqueByHql(final String queryString,
                              final Map<String, ?> values) {
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new UniqueResultExecutor(), queryDecorator);
        return (T) result;

    }

    public T queryUniqueBySql(final boolean transformer,
                              final String queryString, final Map<String, ?> values) {
        QueryDecorator entityClassTransformerQueryDecorator = null;
        if (transformer) {
            entityClassTransformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                    null, entityClass);
        }
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                entityClassTransformerQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new UniqueResultExecutor(), queryDecorator);
        return (T) result;
    }

    public <T2> List<T2> queryUniqueBySql(final String queryString,
                                          final Class<T2> dto, final Map<String, ?> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(queryString)
                        .setResultTransformer(
                                new OracleHibernateMappingTransformer(dto));
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }
                List<T2> list = query.list();
                return list;
            }
        });
    }

    public <T2> List<T2> queryUniqueBySql(final String queryString,
                                          final Class<T2> dto, final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(queryString)
                        .setResultTransformer(
                                new OracleHibernateMappingTransformer(dto));
                if (values != null && values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }
                List<T2> list = query.list();
                return list;
            }
        });
    }

    public T queryUniqueBySql(final boolean transformer,
                              final String queryString, final Object... values) {
        /*
         * return (T) getHibernateTemplate().execute(new HibernateCallback() {
         * public Object doInHibernate(Session session) throws
         * HibernateException, SQLException {
         *
         * Query query; if (transformer) { //
         * query.setResultTransformer(Transformers //
         * .aliasToBean(getEntityClass())); query =
         * session.createSQLQuery(queryString).addEntity(entityClass); } else {
         * query = session.createSQLQuery(queryString); }
         *
         * if (values != null) { for (int i = 0; i < values.length; i++) {
         * query.setParameter(i, values[i]); } } return query.uniqueResult(); }
         * });
         */
        QueryDecorator entityClassTransformerQueryDecorator = null;
        if (transformer) {
            entityClassTransformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                    null, entityClass);
        }
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(
                entityClassTransformerQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new UniqueResultExecutor(), queryDecorator);
        return (T) result;
    }

    public T queryUniqueBySql(String queryString, Map<String, ?> values) {
        return queryUniqueBySql(true, queryString, values);
    }

    public T queryUniqueBySql(String queryString, Object... values) {
        return queryUniqueBySql(true, queryString, values);
    }

    public Object queryObjectBySql(final String queryString,
                                   final Map<String, ?> values) {
        Object object = queryUniqueBySql(false, queryString, values);
        return object;
    }

    public Object queryObjectBySql(final String queryString,
                                   final Object... values) {
        /*
         * Number count = (Number) getHibernateTemplate().execute( new
         * HibernateCallback() { public Object doInHibernate(Session session)
         * throws HibernateException, SQLException { Query query =
         * session.createQuery(queryString); if (values != null) {
         * query.setProperties(values); } return (Number) query.uniqueResult();
         * } }); return count == null ? 0 : count.longValue();
         */
        Object object = queryUniqueBySql(false, queryString, values);
        return object;
    }

    public Long queryUniqueNumberByHql(final String queryString,
                                       final Map<String, ?> values) {
        /*
         * Number count = (Number) getHibernateTemplate().execute( new
         * HibernateCallback() { public Object doInHibernate(Session session)
         * throws HibernateException, SQLException { Query query =
         * session.createQuery(queryString); if (values != null) {
         * query.setProperties(values); } return (Number) query.uniqueResult();
         * } }); return count == null ? 0 : count.longValue();
         */
        Object object = queryUniqueByHql(queryString, values);
        return object == null ? 0 : ((Number) object).longValue();
    }

    public Long queryUniqueNumberByHql(final String queryString,
                                       final Object... values) {
        Object object = queryUniqueByHql(queryString, values);
        return object == null ? 0 : ((Number) object).longValue();
    }

    public Long queryUniqueNumberBySql(final String queryString,
                                       final Map<String, ?> values) {
        Object object = queryUniqueBySql(false, queryString, values);
        return object == null ? 0 : ((Number) object).longValue();
    }

    public BigDecimal queryUniqueBigDecimalBySql(final String queryString,
                                                 final Map<String, ?> values) {
        Object object = queryUniqueBySql(false, queryString, values);
        return object == null ? new BigDecimal("0.00") : new BigDecimal(
                object.toString());
    }

    public Long queryUniqueNumberBySql(final String queryString,
                                       final Object... values) {
        Object object = queryUniqueBySql(false, queryString, values);
        return object == null ? 0 : ((Number) object).longValue();
    }

    public BigDecimal queryUniqueBigDecimalBySql(final String queryString,
                                                 final Object... values) {
        Object object = queryUniqueBySql(false, queryString, values);
        return object == null ? new BigDecimal("0.00") : new BigDecimal(
                object.toString());
    }

    public List<T> queryListByHql(final String queryString,
                                  final Map<String, ?> values) {
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T>) result;
    }

    public List<T> queryListByHql(final String queryString,
                                  final Object... values) {
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(null,
                values);
        Object result = HibernateExecute(false, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T>) result;
    }

    public List<T> queryListBySql(final boolean transformer,
                                  final String queryString, final Map<String, ?> values) {
        QueryDecorator entityClassTransformerQueryDecorator = null;
        if (transformer) {
            entityClassTransformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                    null, entityClass);
        }
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                entityClassTransformerQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T>) result;
    }

    public List<T> queryListBySql(final boolean transformer,
                                  final String queryString, final Object... values) {
        return queryListBySql(transformer, queryString, null, values);
    }

    public List<T> queryListBySql(final boolean transformer,
                                  final String queryString, final Class transFormerClass,
                                  final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query;
                if (transformer) {
                    if (transFormerClass == null) {
                        query = session.createSQLQuery(queryString).addEntity(
                                entityClass);
                    } else {
                        query = session.createSQLQuery(queryString)
                                .setResultTransformer(
                                        new OracleHibernateMappingTransformer(
                                                transFormerClass));
                    }
                } else {
                    query = session.createSQLQuery(queryString);
                }

                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }
                // System.out.println(queryString);
                List list = query.list();
                if (list == null) {
                    list = new LinkedList<T>();
                }
                // System.out.println(list.size());
                return list;
            }
        });
    }

    /**
     * 使用SQl查询结果List
     *
     * @param transformer
     * @param queryString
     * @param values
     * @return 如果transformer为true, 返回List<List>. 否则返回List<Object>
     */
    public List queryObjectListBySql(final boolean transformer,
                                     final String queryString, final Object... values) {
        QueryDecorator transformerQueryDecorator = null;
        if (transformer) {
            transformerQueryDecorator = new SimpleListTransformerQueryDecorator(
                    null);
        }
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(
                transformerQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        List list = (List) result;
        return (list != null && list.size() > 0 && list.get(0) != null) ? list
                : null;
    }

    public List queryObjectListBySql(final String queryString,
                                     final Map<String, ?> map) {

        QueryDecorator queryDecorator = new MapParameterQueryDecorator(null,
                map);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        List list = (List) result;
        return (list != null && list.size() > 0 && list.get(0) != null) ? list
                : null;
    }

    public List<BigDecimal[]> queryListBigDecimalBySql(
            final String queryString, final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(queryString);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }
                List<Object[]> list = query.list();
                List<BigDecimal[]> result = new LinkedList<BigDecimal[]>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Object[] obj = list.get(i);
                        BigDecimal[] rs = new BigDecimal[obj.length];
                        for (int j = 0; j < obj.length; j++) {
                            rs[j] = obj[j] == null ? new BigDecimal("0.00")
                                    : new BigDecimal(obj[j].toString());
                        }
                        result.add(rs);
                    }
                }
                return result;
            }
        });
    }

    public List<BigDecimal[]> queryListBigDecimalBySql(
            final String queryString, final int currentPage,
            final int pageSize, final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }

                if (currentPage > 0 && pageSize > 0) {
                    query.setFirstResult((currentPage - 1) * pageSize);
                    query.setMaxResults(pageSize);
                } else if (pageSize > 0) {
                    query.setMaxResults(pageSize);
                }

                List<Object[]> list = query.list();

                List<BigDecimal[]> result = new LinkedList<BigDecimal[]>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Object[] obj = list.get(i);
                        BigDecimal[] rs = new BigDecimal[obj.length];
                        for (int j = 0; j < obj.length; j++) {
                            rs[j] = obj[j] == null ? new BigDecimal("0.00")
                                    : new BigDecimal(obj[j].toString());
                        }
                        result.add(rs);
                    }
                }

                return result;
            }
        });
    }

    public List<BigDecimal[]> queryListBigDecimalBySql(
            final String queryString, final Map<String, Object> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString);
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }

                List<Object[]> list = query.list();

                List<BigDecimal[]> result = new LinkedList<BigDecimal[]>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Object[] obj = list.get(i);
                        BigDecimal[] rs = new BigDecimal[obj.length];
                        for (int j = 0; j < obj.length; j++) {
                            rs[j] = obj[j] == null ? new BigDecimal("0.00")
                                    : new BigDecimal(obj[j].toString());
                        }
                        result.add(rs);
                    }
                }

                return result;
            }
        });
    }

    public List<BigDecimal[]> queryListBigDecimalBySql(
            final String queryString, final int currentPage,
            final int pageSize, final Map<String, Object> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString);
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }

                if (currentPage > 0 && pageSize > 0) {
                    query.setFirstResult((currentPage - 1) * pageSize);
                    query.setMaxResults(pageSize);
                } else if (pageSize > 0) {
                    query.setMaxResults(pageSize);
                }

                List<Object[]> list = query.list();
                List<BigDecimal[]> result = new LinkedList<BigDecimal[]>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Object[] obj = list.get(i);
                        BigDecimal[] rs = new BigDecimal[obj.length];
                        for (int j = 0; j < obj.length; j++) {
                            rs[j] = obj[j] == null ? new BigDecimal("0.00")
                                    : new BigDecimal(obj[j].toString());
                        }
                        result.add(rs);
                    }
                }
                return result;
            }
        });
    }

    public List<Object[]> queryListObjectBySql(final String queryString,
                                               final int currentPage, final int pageSize,
                                               final Map<String, Object> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString);
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }

                if (currentPage > 0 && pageSize > 0) {
                    query.setFirstResult((currentPage - 1) * pageSize);
                    query.setMaxResults(pageSize);
                } else if (pageSize > 0) {
                    query.setMaxResults(pageSize);
                }

                List<Object[]> list = query.list();

                return list;
            }
        });
    }

    public List<Object[]> queryListObjectBySql(final String queryString,
                                               final Map<String, Object> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString);
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }

                List<Object[]> list = query.list();

                return list;
            }
        });
    }

    /**
     * 使用SQl查询实体类T的结果List，调用queryListBySql(boolean transformer, String
     * queryString, Map<String, ?> values)方法实现
     *
     * @param queryString
     * @param values
     * @return
     */
    public List<T> queryListBySql(String queryString, Map<String, ?> values) {
        return queryListBySql(true, queryString, values);
    }

    /**
     * 使用SQl查询实体类T的结果List，调用queryListBySql(boolean transformer, String
     * queryString, Class transFormerClass, Object... values)方法实现
     *
     * @param queryString
     * @param values
     * @return
     */
    public List<T> queryListBySql(String queryString, Object... values) {
        return queryListBySql(true, queryString, null, values);
    }

    public List queryListBySql(final String queryString, final Class c,
                               final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                Query query = session.createSQLQuery(queryString).addEntity(c);

                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }

                return query.list();
            }
        });
    }

    public <T2> List<T2> queryListBySqlWithTranCalss(final String queryString,
                                                     final Class<T2> tranClass, final Object... values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query;
                // query =
                // session.createSQLQuery(queryString).setResultTransformer(Transformers.aliasToBean(tranClass));
                query = session.createSQLQuery(queryString)
                        .setResultTransformer(
                                new OracleHibernateMappingTransformer<T2>(
                                        tranClass));
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        query.setParameter(i, values[i]);
                    }
                }
                return query.list();
            }
        });
    }

    public <T2> List<T2> queryListBySqlWithTranCalss(final String queryString,
                                                     final Class<T2> tranClass, final Map<String, ?> values) {
        QueryDecorator transformerQueryDecorator = new OracleMappingTransformerQueryDecorator(
                null, tranClass);
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                transformerQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List) result;

    }

    /**
     * 使用SQl查询Object的结果List，调用queryObjectListBySql( boolean transformer, String
     * queryString, Object... values)方法实现
     *
     * @param queryString
     * @param values
     * @return
     */
    public List queryObjectListBySql(String queryString, Object... values) {
        return queryObjectListBySql(false, queryString, values);
    }

    /**
     * 使用HQL查询分页的结果List,调用queryPageListByHql(String queryString,int currentPage,
     * int pageSize,Map<String, ?> values) 方法实现
     *
     * @param queryString
     * @param page
     * @param values
     * @return
     */
    public List<T> queryPageListByHql(final String queryString, Page<T> page,
                                      final Map<String, ?> values) {
        return queryPageListByHql(queryString,
                page == null ? 0 : page.getPageNo(),
                page == null ? 0 : page.getPageSize(), values);
    }

    /**
     * 使用HQL查询分页的结果List,调用queryPageListByHql(String queryString,int currentPage,
     * int pageSize, Object... values) 方法实现
     *
     * @param queryString
     * @param page
     * @param values
     * @return
     */
    public List<T> queryPageListByHql(final String queryString, Page<T> page,
                                      final Object... values) {
        return queryPageListByHql(queryString,
                page == null ? 0 : page.getPageNo(),
                page == null ? 0 : page.getPageSize(), values);
    }

    /**
     * 使用HQL查询分页的结果List
     *
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public List<T> queryPageListByHql(final String queryString,
                                      final int currentPage, final int pageSize,
                                      final Map<String, ?> values) {
        /*
         * return getHibernateTemplate().executeFind(new HibernateCallback() {
         * public Object doInHibernate(Session session) throws
         * HibernateException, SQLException { Query query =
         * session.createQuery(queryString); if (values != null && values.size()
         * > 0) { query.setProperties(values); } if (currentPage > 0 && pageSize
         * > 0) { query.setFirstResult((currentPage - 1) * pageSize);
         * query.setMaxResults(pageSize); } else if (pageSize > 0) {
         * query.setMaxResults(pageSize); } List list = query.list(); return
         * list; } });
         */
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                null, currentPage, pageSize);
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(false, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List) result;
    }

    /**
     * 使用HQL查询分页的结果List
     *
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public List<T> queryPageListByHql(final String queryString,
                                      final int currentPage, final int pageSize, final Object... values) {
        /*
         * return getHibernateTemplate().executeFind(new HibernateCallback() {
         * public Object doInHibernate(Session session) throws
         * HibernateException, SQLException { Query query =
         * session.createQuery(queryString); if (values != null) { for (int i =
         * 0; i < values.length; i++) { query.setParameter(i, values[i]); } } if
         * (currentPage > 0 && pageSize > 0) { query.setFirstResult((currentPage
         * - 1) * pageSize); query.setMaxResults(pageSize); } else if (pageSize
         * > 0) { query.setMaxResults(pageSize); } List list = query.list();
         * return list; } });
         */
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                null, currentPage, pageSize);
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(false, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List) result;
    }

    /**
     * 使用SQL查询分页的结果List
     *
     * @param transformer
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public <T2> List<T2> queryPageListBySql(final boolean transformer,
                                            final String queryString, final Class<T2> dto,
                                            final int currentPage, final int pageSize, final Object... values) {
        QueryDecorator entityClassTransformerQueryDecorator = null;
        if (transformer) {
            if (dto == null) {
                entityClassTransformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                        null, entityClass);
            } else {
                entityClassTransformerQueryDecorator = new OracleMappingTransformerQueryDecorator(
                        null, dto);
            }
        }
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                entityClassTransformerQueryDecorator, currentPage, pageSize);
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T2>) result;
    }

    public <T2> List<T2> queryPageListBySql(final boolean transformer,
                                            final String queryString, final Class<T2> dto,
                                            final int currentPage, final int pageSize,
                                            final Map<String, ?> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query;
                if (transformer) {
                    if (dto == null) {
                        query = session.createSQLQuery(queryString).addEntity(
                                entityClass);
                    } else {
                        query = session.createSQLQuery(queryString)
                                .setResultTransformer(
                                        new OracleHibernateMappingTransformer(
                                                dto));
                    }
                } else {
                    query = session.createSQLQuery(queryString);
                }
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }
                if (currentPage > 0 && pageSize > 0) {
                    query.setFirstResult((currentPage - 1) * pageSize);
                    query.setMaxResults(pageSize);
                } else if (pageSize > 0) {
                    query.setMaxResults(pageSize);
                }
                List list = query.list();

                return list;
            }
        });
    }

    public <T2> List<T2> queryListBySql(final boolean transformer,
                                        final String queryString, final Class<T2> dto,
                                        final Map<String, ?> values) {
        QueryDecorator transformerQueryDecorator = null;
        if (transformer) {
            if (dto == null) {
                transformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                        null, entityClass);
            } else {
                transformerQueryDecorator = new OracleMappingTransformerQueryDecorator(
                        null, dto);
            }
        }
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                transformerQueryDecorator, 0, 0);
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);

        return (List<T2>) result;
    }

    /**
     * 使用SQL查询分页的结果List
     *
     * @param transformer
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public List<T> queryPageListBySql(final boolean transformer,
                                      final String queryString, final int currentPage,
                                      final int pageSize, final Object... values) {
        QueryDecorator transformerQueryDecorator = null;
        if (transformer) {
            transformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                    null, entityClass);
        }
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                transformerQueryDecorator, currentPage, pageSize);
        QueryDecorator queryDecorator = new ArrayParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T>) result;
    }

    /**
     * 使用SQL查询分页的结果List,调用queryPageListBySql(final boolean transformer, final
     * String queryString, final int currentPage, final int pageSize,
     * Map<String, ?> values)方法实现
     *
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public List<T> queryPageListBySql(String queryString, int currentPage,
                                      int pageSize, Map<String, ?> values) {
        return queryPageListBySql(true, queryString, currentPage, pageSize,
                values);
    }

    public List<T> queryPageListBySql(boolean transformer, String queryString,
                                      int currentPage, int pageSize, Map<String, ?> values) {
        QueryDecorator transformerQueryDecorator = null;
        if (transformer) {
            transformerQueryDecorator = new EntityClassTransformerQueryDecorator(
                    null, entityClass);
        }
        QueryDecorator paginationQueryDecorator = new PaginationQueryDecorator(
                transformerQueryDecorator, currentPage, pageSize);
        QueryDecorator queryDecorator = new MapParameterQueryDecorator(
                paginationQueryDecorator, values);
        Object result = HibernateExecute(true, queryString,
                new ListResultExecutor(), queryDecorator);
        return (List<T>) result;
    }

    /**
     * 使用SQL查询分页的结果List,调用queryPageListBySql(final boolean transformer, final
     * String queryString, final int currentPage, final int pageSize, final
     * Object... values)方法实现
     *
     * @param queryString
     * @param currentPage
     * @param pageSize
     * @param values
     * @return
     */
    public List<T> queryPageListBySql(String queryString, int currentPage,
                                      int pageSize, Object... values) {
        return queryPageListBySql(true, queryString, currentPage, pageSize,
                values);
    }

    /**
     * 使用SQL查询分页的结果List,调用queryPageListBySql(String queryString, int
     * currentPage, int pageSize, Map<String, ?> values)方法实现
     *
     * @param queryString
     * @param page
     * @param values
     * @return
     */
    public List<T> queryPageListBySql(String queryString, Page<T> page,
                                      Map<String, ?> values) {
        return queryPageListBySql(queryString,
                page == null ? 0 : page.getPageNo(),
                page == null ? 0 : page.getPageSize(), values);
    }

    /**
     * 使用SQL查询分页的结果List,调用queryPageListBySql(String queryString, int
     * currentPage, int pageSize, Object... values)方法实现
     *
     * @param queryString
     * @param page
     * @param values
     * @return
     */
    public List<T> queryPageListBySql(String queryString, Page<T> page,
                                      Object... values) {
        return queryPageListBySql(queryString,
                page == null ? 0 : page.getPageNo(),
                page == null ? 0 : page.getPageSize(), values);
    }

    /**
     * 通过sql语句查询两个字段,一般用于类似下拉列表情况
     *
     * @param sql
     * @param values
     * @return
     */
    public List queryResultToMap(final String sql, final Map<String, ?> values) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                if (values != null && values.size() > 0) {
                    query.setProperties(values);
                }
                List list = query.list();
                return list;
            }
        });
    }

    /**
     * 通过sql语句查询两个字段,一般用于类似下拉列表情况
     *
     * @param sql
     * @param values
     * @return
     */
    public Map<String, String> queryResultToMapByString(final String sql,
                                                        final Object... values) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        List<Object[]> list = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(sql);
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                query.setParameter(i, values[i]);
                            }
                        }

                        List list = query.list();
                        return list;
                    }
                });
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                result.put((list.get(i)[0].toString()),
                        String.valueOf(list.get(i)[1]));
            }
        }
        return result;
    }

    /**
     * 通过sql语句查询两个字段,一般用于类似下拉列表情况
     *
     * @param sql
     * @param values
     * @return
     */
    public Map<Long, String> queryResultToMap(final String sql,
                                              final Object... values) {
        Map<Long, String> result = new LinkedHashMap<Long, String>();
        List<Object[]> list = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(sql);
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                query.setParameter(i, values[i]);
                            }
                        }

                        List list = query.list();
                        return list;
                    }
                });
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                result.put(Long.valueOf(list.get(i)[0].toString()),
                        String.valueOf(list.get(i)[1]));
            }
        }
        return result;
    }

    public Map<Long, Long> queryResultToMapByLong(final String sql,
                                                  final Object... values) {
        Map<Long, Long> result = new LinkedHashMap<Long, Long>();
        List<Object> list = getHibernateTemplate().executeFind(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(sql);
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                query.setParameter(i, values[i]);
                            }
                        }

                        List list = query.list();
                        return list;
                    }
                });
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                result.put(Long.valueOf(list.get(i).toString()),
                        Long.valueOf(list.get(i).toString()));
            }
        }
        return result;
    }

    /**
     * 设置命名参数，拼凑sql or hql语句
     *
     * @param sql
     * @param values
     */
    public void setNamedParams(StringBuffer sql, Map<String, ?> values) {
        if (values != null && values.size() > 0) {
            int paramsCnt = 0;
            sql.append(" where ");
            for (Map.Entry<String, ?> m : values.entrySet()) {
                if (paramsCnt > 0) {
                    sql.append(" and ");
                }
                PropertyFilter filterProperty = new PropertyFilter(m.getKey());
                sql.append(filterProperty.getFieldName()).append(" ")
                        .append(filterProperty.getFilterName()).append(" :")
                        .append(m.getKey());
                paramsCnt++;
            }
        }
    }

    public void updateBySql(String sql, List<Object> params)
            throws HibernateException {
        final List<Object> param = params;
        final String mSql = sql;
        try {
            this.getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Query query = session.createSQLQuery(mSql);
                    if (param != null && param.size() > 0) {
                        for (int i = 0; i < param.size(); i++) {
                            query.setParameter(i, param.get(i));
                        }
                    }
                    return query.executeUpdate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPageCountRow(String sql) {
        String newStr = "";
        if (sql.indexOf(" group by ") < 0) {
            if (sql.indexOf("from_replace") > 0) {
                newStr = "select count(*) "
                        + " from "
                        + sql.substring(sql.indexOf(" from_replace ")
                        + " from_replace ".length());
            } else {
                newStr = "select count(*) "
                        + sql.substring(sql.lastIndexOf(" from "));
            }
        } else {
            newStr = "select count(*) from (" + sql + ") su";
        }
        final String exe_sql = newStr;
        int countRow = (Integer) this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Integer doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(exe_sql);
                        return Integer.valueOf(query.uniqueResult().toString());
                    }
                });
        return countRow;
    }

    public int execBatchSql(String strSql, List<List<Object>> list) {
        int R = 1;
        Connection conn = connectMySql();
        try {
            // 关闭事务自动提交
            conn.setAutoCommit(false);
            PreparedStatement pStatement = conn.prepareStatement(strSql);
            for (int y = 0; y < list.size(); y++) {
                List<Object> item = list.get(y);
                for (int i = 0; i < item.size(); i++) {
                    pStatement.setObject(i + 1, item.get(i));
                }
                // 把一个SQL命令加入命令列表
                pStatement.addBatch();
            }
            // 执行批量更新
            pStatement.executeBatch();
            // 语句执行完毕，提交本事务
            conn.commit();
            // R = pStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            R = -1;
        } finally {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                R = -1;
            }
        }
        return R;
    }

    public Connection connectMySql() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");// 指定连接类型
            conn = DriverManager.getConnection(
                    new ConfigProperty().getConfigValue("jdbcUrl"),
                    new ConfigProperty().getConfigValue("user"),
                    new ConfigProperty().getConfigValue("password"));// 获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public PageUtil queryListSQLPageByField(String sql, int currentPage, int pageSize) {
        int countRow = 0;
        final String exe_sql = " select count(*) from (" + sql + ") as su ";
        countRow = (Integer) this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Integer doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(exe_sql);
                        return Integer.valueOf(query.uniqueResult().toString());
                    }
                });
        final String newsql = sql;
        final PageUtil page = new PageUtil(pageSize, currentPage, countRow);
        List<Object> arr = (List<Object>) this.getHibernateTemplate().execute(new HibernateCallback() {
            public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(newsql);
                query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
                query.setMaxResults(page.getPageSize());
                return query.list();
            }
        });
        page.setList(arr);
        return page;
    }

    public PageUtil queryListSQLPageByField(String sql, String sqlCount, int currentPage, int pageSize) {
        int countRow = 0;
        if (org.apache.commons.lang3.StringUtils.isEmpty(sqlCount)) {
            sqlCount = " select count(*) from (" + sql + ") as su ";
        }
        final String exe_sql = sqlCount;

        countRow = (Integer) this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Integer doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(exe_sql);
                        return Integer.valueOf(query.uniqueResult().toString());
                    }
                });
        final String newsql = sql;
        final PageUtil page = new PageUtil(pageSize, currentPage, countRow);
        List<Object> arr = (List<Object>) this.getHibernateTemplate().execute(new HibernateCallback() {
            public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(newsql);
                query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
                query.setMaxResults(page.getPageSize());
                return query.list();
            }
        });
        page.setList(arr);
        return page;
    }

    public PageUtil queryListMapSQLPageByField(String sql, String sqlCount, int currentPage, int pageSize, Object... values) {
        int countRow = 0;
        final String exe_sql = " select count(*) from (" + sql + ") as su ";
        if (!StringUtils.isEmpty(sqlCount)) {

        }
        countRow = (Integer) this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Integer doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        Query query = session.createSQLQuery(exe_sql);
                        return Integer.valueOf(query.uniqueResult().toString());
                    }
                });

        final PageUtil page = new PageUtil(pageSize, currentPage, countRow);

        Query query = get_Session().createSQLQuery(sql);
        query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        query.setResultTransformer(new ResultTransformer() {
            // alues=值 columns=列名
            public Object transformTuple(Object[] values, String[] columns) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columns.length; i++) {
                    map.put(columns[i], values[i] == null ? "" : values[i]);
                }
                return map;
            }

            public List transformList(List columns) {
                return columns;
            }
        });
        page.setResultListMap(query.list());
        return page;
    }

    public Session get_Session() {

        return getSessionObj();
    }

    public int maxId(String sql) {

        int id = -1;
        Session session = getSessionObj();
        Connection con = session.connection();
        PreparedStatement pst = null;
        ResultSet results = null;
        try {
            pst = con.prepareStatement(sql);
            results = pst.executeQuery();
            while (results.next()) {
                id = results.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ;
        return id;
    }

    public ResultSet selecAllBySQL(String sql) {
        Session session = getSessionObj();
        Connection con = session.connection();
        PreparedStatement pst = null;
        ResultSet results = null;
        try {
            pst = con.prepareStatement(sql);
            results = pst.executeQuery();
            // ;
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBySQL(String sql) {
        try {
            Session session = getSessionObj();
            Connection con = session.connection();
            PreparedStatement pst = null;
            pst = con.prepareStatement(sql);
            pst.execute();
            ;
        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int Insert(String Sql) {
        return 0;
    }

    /**
     * 批量新增
     */
	/*public PageUtil saveOrUpdateAll(List<String> sqlList) {
		//getHibernateTemplate().saveOrUpdateAll(list);
		//new HibernateCallback(){}
		//bulkUpdate
		//this.getHibernateTemplate().saveOrUpdateAll(sqlList);
		//this.getHibernateTemplate().bulkUpdate("", value)
		return null;
	}*/

}

@SuppressWarnings("rawtypes")
class HibernateCallbackUtil {

    public static Query createSqlQueryAndSetResultTransformer(
            final boolean isTransformer, final String queryString,
            Session session, Class entityClass, ResultTransformer transformer) {
        Query query;
        if (isTransformer) {
            if (transformer != null) {
                query = session.createSQLQuery(queryString);
                query.setResultTransformer(transformer);
            } else {
                query = session.createSQLQuery(queryString).addEntity(
                        entityClass);
            }
        } else {
            query = session.createSQLQuery(queryString);
        }
        return query;
    }

}

interface Executor {
    Object execute(Query query);
}

class UniqueResultExecutor implements Executor {
    public Object execute(Query query) {
        return query.uniqueResult();
    }
}

class ListResultExecutor implements Executor {
    public Object execute(Query query) {
        return query.list();
    }
}

class UpdateExecutor implements Executor {
    public Object execute(Query query) {
        return query.executeUpdate();
    }
}

interface QueryDecorator {
    Query execute();

    Query getQuery();

    void setQuery(Query query);
}

abstract class AbstractQueryDecorator implements QueryDecorator {
    protected Query query = null;

    protected QueryDecorator decoratee;

    public AbstractQueryDecorator(QueryDecorator queryDecoratee) {
        decoratee = queryDecoratee;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public Query execute() {
        toExecute();
        if (decoratee != null) {
            decoratee.setQuery(query);
            return decoratee.execute();
        } else {
            return query;
        }
    }

    abstract void toExecute();

}

class ArrayParameterQueryDecorator extends AbstractQueryDecorator {

    protected Object[] values;

    public ArrayParameterQueryDecorator(QueryDecorator queryDecoratee,
                                        final Object... values) {
        super(queryDecoratee);
        this.values = values;
    }

    @Override
    void toExecute() {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
    }
}

class MapParameterQueryDecorator extends AbstractQueryDecorator {

    protected Map<String, ?> map;

    public MapParameterQueryDecorator(QueryDecorator queryDecoratee,
                                      final Map<String, ?> values) {
        super(queryDecoratee);
        this.map = values;
    }

    @Override
    void toExecute() {
        if (map != null && map.size() > 0) {
            query.setProperties(map);
        }
    }
}

@SuppressWarnings({"rawtypes"})
class EntityClassTransformerQueryDecorator extends AbstractQueryDecorator {

    protected Class entityClass;

    public EntityClassTransformerQueryDecorator(QueryDecorator queryDecoratee,
                                                Class entityClass) {
        super(queryDecoratee);
        this.entityClass = entityClass;
    }

    @Override
    void toExecute() {
        ((SQLQuery) query).addEntity(entityClass);
    }
}

@SuppressWarnings({"unchecked", "rawtypes"})
class OracleMappingTransformerQueryDecorator extends AbstractQueryDecorator {
    protected Class transFormerClass;

    public OracleMappingTransformerQueryDecorator(
            QueryDecorator queryDecoratee, Class transFormerClass) {
        super(queryDecoratee);
        this.transFormerClass = transFormerClass;
    }

    @Override
    void toExecute() {
        ((SQLQuery) query)
                .setResultTransformer(new OracleHibernateMappingTransformer(
                        transFormerClass));
    }
}

class SimpleListTransformerQueryDecorator extends AbstractQueryDecorator {
    public SimpleListTransformerQueryDecorator(QueryDecorator queryDecoratee) {
        super(queryDecoratee);
    }

    @Override
    void toExecute() {
        ((SQLQuery) query).setResultTransformer(Transformers.TO_LIST);
    }
}

class PaginationQueryDecorator extends AbstractQueryDecorator {

    private int currentPage;

    private int pageSize;

    public PaginationQueryDecorator(QueryDecorator queryDecoratee,
                                    int currentPage, int pageSize) {
        super(queryDecoratee);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    @Override
    void toExecute() {
        if (currentPage > 0 && pageSize > 0) {
            query.setFirstResult((currentPage - 1) * pageSize);
            query.setMaxResults(pageSize);
        } else if (pageSize > 0) {
            query.setMaxResults(pageSize);
        }
    }

}
