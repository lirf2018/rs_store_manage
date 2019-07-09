package com.yufan.taobao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;

import com.yufan.taobao.bean.ModelBean;
import com.yufan.taobao.dao.FindInfoDao;

/**
 * @功能名称 用于处理数据和查询参数
 * @作者 lirongfan
 * @时间 2015年12月1日 下午4:50:41
 */
public class OperationModel {

    @Autowired
    private FindInfoDao findInfoDao_taobao;

    /**
     * 处理替换模板
     *
     * @param bean
     * @return
     */
    public static ModelBean handdle(ModelBean bean) {
        try {
            bean.setAContents(bean.getBContents());
            Class<?> beanclass = bean.getClass();
            Field[] fields = beanclass.getDeclaredFields();
            for (Field field : fields) {
                String classPro = field.getName();//得到bean属性
                if (classPro.length() > 6 && "value".equals(classPro.substring(0, 5))) {
                    Method m = (Method) bean.getClass().getDeclaredMethod("get" + getMethodName(classPro));
                    String val = (String) m.invoke(bean);// 调用getter方法获取属性值
                    if (null != val && !"".equals(String.valueOf(val)) && !"null".equals(String.valueOf(val))) {
                        System.out.println(val);
                        bean.setAContents(repalceStr(bean.getAContents(), classPro, val));
                    } else {
                        bean.setAContents(repalceStr(bean.getAContents(), classPro, ""));
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 替换模板代码
     *
     * @param str          模板代码
     * @param replaceName  替换名
     * @param replaceValue 替换值
     * @return 替换后代码
     */
    @SuppressWarnings("unused")
    private static String repalceStr(String str, String replaceName, String replaceValue) {
        return str.replaceAll(replaceName, replaceValue);
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }


    public static void main(String[] args) throws NoSuchMethodException, SecurityException, Exception {
        ModelBean bean = new ModelBean();
        bean.setValue01("abcd");
        Class<?> beanclass = bean.getClass();
        Field[] fields = beanclass.getDeclaredFields();
        for (Field field : fields) {
            String classPro = field.getName();
            if (classPro.length() > 6 && "value".equals(classPro.substring(0, 5))) {
                Method m = (Method) bean.getClass().getDeclaredMethod("get" + getMethodName(classPro));
                String val = (String) m.invoke(bean);// 调用getter方法获取属性值
                if (null != val && !"".equals(String.valueOf(val)) && !"null".equals(String.valueOf(val))) {
                    System.out.println(val);
                }
            }
        }
    }


    /**
     * html特殊字符替换
     *
     * @param str
     * @return
     */
    public static String repalceChar(String str) {


        return str;
    }
}
