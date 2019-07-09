package com.yufan.common.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.yufan.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.logicalcobwebs.proxool.admin.Admin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@SuppressWarnings({"rawtypes"})
public class LotFilterAction<T, pk> extends ActionSupport implements Preparable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(LotFilterAction.class);

    private String msg;
    //通过jsonplug 返回给页面
    protected Integer opeType;//操作类型  1为添加，2为修改，3为删除
    protected String incorrectMsg;

    private String jsonStr;
    private HttpSession session;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServletContext servletContext;
    protected Map<String, Object> data;
    protected List<Object[]> listObje = new ArrayList<Object[]>();
//	protected Page<T> page = new Page<T>(15);


    @Override
    public String execute() throws Exception {
        return super.execute();
    }

    public void initData() {
//		if (request == null || response == null || servletContext == null) {
        servletContext = ServletActionContext.getServletContext();
        request = ServletActionContext.getRequest();
        response = ServletActionContext.getResponse();
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
//		}

    }

    public String write_Json_result(String flag, Object msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flag", flag);
        map.put("msg", msg);
        String str = JsonUtils.objectToJson(map);
        return str;
    }

    public Admin getCurrentUser() {
        ActionContext ac = ActionContext.getContext();
        Map session = ac.getSession();
        Admin user = (Admin) session.get("user");
        return user;
    }


    public PrintWriter getWriter() {
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            log.error("IO异常");
            e.printStackTrace();
            return null;
        }
        return writer;
    }

    public static String httpPost(String httpUrl, String httpJson) {
        StringBuffer result = new StringBuffer();
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
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 生成10位随机数
     *
     * @return
     */
    public static String getRandom() {
        int num = (int) Math.round(Math.random() * 25 + 97);
        char temp = (char) num;
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            val += String.valueOf(random.nextInt(7));
        }
        return temp + val;
    }


    /*
     * 流量充值
     */
    public static String httpConnSend(String urlStr, String content, String requestMethod) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");//application/json
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.connect();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "utf-8"));
            out.write(content);
            out.flush();
            out.close();
            StringBuffer result = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
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
            return null;
        }

    }


    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public HttpSession getSession() {
        this.session = this.getRequest().getSession();
        return this.session;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public Integer getOpeType() {
        return opeType;
    }

    public void setOpeType(Integer opeType) {
        this.opeType = opeType;
    }

    public String getIncorrectMsg() {
        return incorrectMsg;
    }

    public void setIncorrectMsg(String incorrectMsg) {
        this.incorrectMsg = incorrectMsg;
    }

//	public Page getPage() {
//		return page;
//	}
//
//	public void setPage(Page page) {
//		this.page = page;
//	}
//
//	public int getPageNumber() {
//		return pageNumber;
//	}
//
//	public void setPageNumber(int pageNumber) {
//		this.pageNumber = pageNumber;
//	}
//
//	public int getPageSize() {
//		return pageSize;
//	}
//
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<Object[]> getListObje() {
        return listObje;
    }

    public void setListObje(List<Object[]> listObje) {
        this.listObje = listObje;
    }

    @Override
    public void prepare() throws Exception {
        // TODO Auto-generated method stub

    }
//	public Page<T> getPage() {
//		return page;
//	}
//
//
//	public void setPage(Page<T> page) {
//		this.page = page;
//	}
//	


}
