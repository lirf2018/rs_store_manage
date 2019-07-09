package com.yufan.weixintest.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSONObject;
import com.yufan.weixintest.weixin.bean.msg.RequestMessage;


/**
 * 功能名称: java下XML与JSON互相转换的Utils类
 * 开发人: lirf
 * 开发时间: 2016下午4:03:10
 * 其它说明：
 */
public class XmlConverJsonUtil {

    /**
     * 定义一个map缓存class的JAXBContext对象,优化装换效率
     */
    private static ConcurrentHashMap<Class<?>, JAXBContext> map = new ConcurrentHashMap<Class<?>, JAXBContext>();


    /**
     * map to xml xml <node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node>
     *
     * @param map
     * @return
     */
    public static String maptoXml(Map map) {
        Document document = DocumentHelper.createDocument();
        Element nodeElement = document.addElement("node");
        for (Object obj : map.keySet()) {
            Element keyElement = nodeElement.addElement("key");
            keyElement.addAttribute("label", String.valueOf(obj));
            keyElement.setText(String.valueOf(map.get(obj)));
        }
        return doc2String(document);
    }

    /**
     * list to xml xml <nodes><node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node><node><key
     * label="key1">value1</key><key
     * label="key2">value2</key>......</node></nodes>
     *
     * @param list
     * @return
     */
    public static String listtoXml(List list) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element nodesElement = document.addElement("nodes");
        int i = 0;
        for (Object o : list) {
            Element nodeElement = nodesElement.addElement("node");
            if (o instanceof Map) {
                for (Object obj : ((Map) o).keySet()) {
                    Element keyElement = nodeElement.addElement("key");
                    keyElement.addAttribute("label", String.valueOf(obj));
                    keyElement.setText(String.valueOf(((Map) o).get(obj)));
                }
            } else {
                Element keyElement = nodeElement.addElement("key");
                keyElement.addAttribute("label", String.valueOf(i));
                keyElement.setText(String.valueOf(o));
            }
            i++;
        }
        return doc2String(document);
    }

    /**
     * json to xml {"node":{"key":{"@label":"key1","#text":"value1"}}} conver
     * <o><node class="object"><key class="object"
     * label="key1">value1</key></node></o>
     *
     * @param json
     * @return
     */
    public static String jsontoXml(String json) {
        try {
            XMLSerializer serializer = new XMLSerializer();
            JSON jsonObject = JSONSerializer.toJSON(json);
            return serializer.write(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to map xml <node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node>
     *
     * @param xml
     * @return
     */
    public static Map xmltoMap(String xml) {
        try {
            Map map = new HashMap();
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext(); ) {
                Element elm = (Element) it.next();
                map.put(elm.attributeValue("label"), elm.getText());
                elm = null;
            }
            node = null;
            nodeElement = null;
            document = null;
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to list xml <nodes><node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node><node><key
     * label="key1">value1</key><key
     * label="key2">value2</key>......</node></nodes>
     *
     * @param xml
     * @return
     */
    public static List xmltoList(String xml) {
        try {
            List<Map> list = new ArrayList<Map>();
            Document document = DocumentHelper.parseText(xml);
            Element nodesElement = document.getRootElement();
            List nodes = nodesElement.elements();
            for (Iterator its = nodes.iterator(); its.hasNext(); ) {
                Element nodeElement = (Element) its.next();
                Map map = xmltoMap(nodeElement.asXML());
                list.add(map);
                map = null;
            }
            nodes = null;
            nodesElement = null;
            document = null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to json <node><key label="key1">value1</key></node> 转化为
     * {"key":{"@label":"key1","#text":"value1"}}
     *
     * @param xml
     * @return
     */
    public static String xmltoJson(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(xml).toString();
    }

    /**
     * @param document
     * @return
     */
    public static String doc2String(Document document) {
        String s = "";
        try {
            // 使用输出流来进行转化
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用UTF-8编码
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            s = out.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }


    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return inputStream// 从request中取得输入流
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(InputStream inputStream) throws Exception {

        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;


        return map;
    }


    /**
     * 把对象转换为xml格式字符串
     *
     * @param obj
     * @return
     */
    public static String toXML(Object obj) {
        try {
            JAXBContext context = getJAXBContext(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把xml字符串转换为对象
     *
     * @param xml
     * @param valueType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xml, Class<T> valueType) {
        try {
            JAXBContext context = getJAXBContext(valueType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JAXBContext getJAXBContext(Class<?> valueType) {
        try {
            JAXBContext context = null;
            if (map.containsKey(valueType)) {
                context = map.get(valueType);
            } else {
                context = JAXBContext.newInstance(valueType);
                map.put(valueType, context);
            }
            return context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        RequestMessage inputMsg = new RequestMessage();

        String xml = "<xml><ToUserName><![CDATA[gh_8c0b65823d6a]]></ToUserName>"
                + "<FromUserName><![CDATA[o8L87wJE1T75IidJJ5CNXue8-ctI]]></FromUserName>"
                + "<CreateTime>1456041270</CreateTime>"
                + "<MsgType><![CDATA[event]]></MsgType>"
                + "<Event><![CDATA[CLICK]]></Event>"
                + "<EventKey><![CDATA[1456039366502]]></EventKey>" + "</xml>";

        inputMsg = xmlToBean(xml, RequestMessage.class);
        System.out.println(JSONObject.toJSONString(inputMsg));


//        Map<String, Object> map=new HashMap<String, Object>();
//        map.put("Content", "sasdfdsfds");
//
//        OutputMessage out=new OutputMessage();
//        out.setCreateTime(String.valueOf(System.currentTimeMillis()));
//        System.out.println(toXML(out));

//    	String str="{\"Content\":\"dsgsdgsdg\"}";
//    	inputMsg=JSONObject.toJavaObject(JSONObject.parseObject(xmltoJson(xml)), InputMessage.class);
//    	System.out.println(JSONObject.toJSONString(inputMsg));
    }
}
