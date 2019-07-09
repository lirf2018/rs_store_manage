package com.yufan.common.dao.impl;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

/**
 * 针对oracle的sql查询做对象转换工具类
 */
public class OracleHibernateMappingTransformer<T> implements
        ResultTransformer {

    private static final long serialVersionUID = -5199190581393587893L;
    private final Class<T> resultClass;
    private Setter[] setters;
    private PropertyAccessor propertyAccessor;

    public OracleHibernateMappingTransformer(Class<T> resultClass) {
        if (resultClass == null)
            throw new IllegalArgumentException("resultClass cannot be null");
        this.resultClass = resultClass;
        propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[]{
                PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
                PropertyAccessorFactory.getPropertyAccessor("field")});
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        try {
            if (setters == null) {
                setters = new Setter[aliases.length];
                for (int i = 0; i < aliases.length; i++) {
                    String alias = convertColumnToProperty(aliases[i]);
                    if (alias != null) {
                        try {
                            setters[i] = propertyAccessor.getSetter(
                                    resultClass, alias);
                        } catch (PropertyNotFoundException e) {
                            continue;
                        }
                    }
                }
            }
            result = resultClass.newInstance();
        } catch (InstantiationException e) {
            throw new HibernateException("Could not instantiate resultclass: "
                    + resultClass.getName());
        } catch (IllegalAccessException e) {
            throw new HibernateException("Could not instantiate resultclass: "
                    + resultClass.getName());
        } catch (Exception e) {
            System.out.println("");
            throw new RuntimeException(e);
        }
        return result;
    }


    public String convertColumnToProperty(String columnName) {
        columnName = columnName.toLowerCase();
        StringBuffer buff = new StringBuffer(columnName.length());
        StringTokenizer st = new StringTokenizer(columnName, "_");
        while (st.hasMoreTokens()) {
            buff.append(StringUtils.capitalize(st.nextToken()));
        }
        buff.setCharAt(0, Character.toLowerCase(buff.charAt(0)));
        return buff.toString();
    }


    @SuppressWarnings("unchecked")
    public List transformList(List collection) {
        return (List<T>) collection;
    }

    public static void main(String args[]) {
        int n1 = 1;
        long l1 = 1;
        l1 = n1;
        double d1 = n1;
        OracleHibernateMappingTransformer<String> transfer = new OracleHibernateMappingTransformer<String>(
                String.class);
        System.out.println(transfer.convertColumnToProperty("XXX_YYY_ZZZ"));
    }
}
