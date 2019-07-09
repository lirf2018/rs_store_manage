package com.yufan.other.help;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectBase {

    private volatile static CollectBase collectBase;

    public static CollectBase getInstence() {
        if (null == collectBase) {
            synchronized (CollectBase.class) {
                if (null == collectBase) {
                    collectBase = new CollectBase();
                }
            }
        }
        return collectBase;
    }


    public String collect(String content, String regex) {
        String str = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            String value = m.group(2);
            str = value;
//			System.out.println(value);
        }
        if (null != str) {
            str.trim();
        }
        return str;
    }

    public String collectIndex(String content, String regex, int index) {
        String str = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            String value = m.group(index);
            str = value;
//			System.out.println(value);
        }
        if (null != str) {
            str.trim();
        }
        return str;
    }

    public List<Object> collectList(String content, String regex) {
        List<Object> list = new ArrayList<Object>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String value = m.group(2);
//			System.out.println(value);
            if (null != value) {
                value.trim();
            }
            list.add(value);
        }
        return list;
    }

    public List<Object> collectListIndex(String content, String regex, int index) {
        List<Object> list = new ArrayList<Object>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String value = m.group(index);
//			System.out.println(value);
            if (null != value) {
                value = value.trim();
            }
            list.add(value);
        }
        return list;
    }

    public List<String> collectList1(String content, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            int a = m.groupCount();
            for (int i = 2; i < m.groupCount() + 1; i++) {
//			System.out.println(m.group());
                String value = m.group(i);
//			System.out.println(m.group(i));
                if (null != value) {
                    value.trim();
                }
                list.add(value);
            }
        }
        return list;
    }

    public String exeConnection(String destUrl) {
        return exeConnection(destUrl, "utf-8");
    }

    public String exeConnection(String destUrl, String charsetName) {
        StringBuffer exeResult = new StringBuffer("");
        String tempResult = "";
        try {
            URL serverUrl = new URL(destUrl);
            HttpURLConnection conn = (HttpURLConnection) serverUrl
                    .openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setUseCaches(false); // Post 请求不能使用缓存

            conn.setRequestProperty("Content-type", "text/html"); // 可序列化设置
            // application/x-java-serialized-object
            conn.setConnectTimeout(60 * 1000); // 设置连接超时时间 30秒钟
            conn.setReadTimeout(60 * 1000); // 设置读超时时间 30秒钟
            conn.connect();

            // 获取接口调用的返回结果

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), charsetName));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    exeResult.append(line);
                }
            }
            tempResult = exeResult.toString();

            conn.disconnect(); // 关闭链接
        } catch (Exception e) {
            // e.printStackTrace();
            return "";
        }

        return tempResult;
    }

    /**
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static int objToInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static double objToDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * post 请求
     *
     * @param httpUrl
     * @param httpJson
     * @return
     */
    public static String httpPost(String httpUrl, String httpJson) {

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestProperty("content-type", "text/xml");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("contentType", "UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "UTF-8"));
            out.write(httpJson);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else {
                    result.append(line);
                }
            }

            return result.toString();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String postForm(String url, String param) {
        String result = "";
        try {
            URL httpurl = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) httpurl
                    .openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    httpConn.getOutputStream(), "utf-8"));
            out.write(param);
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("没有结果！" + e);
        }
        return result;
    }
}
