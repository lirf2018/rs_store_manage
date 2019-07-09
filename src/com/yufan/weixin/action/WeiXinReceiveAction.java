package com.yufan.weixin.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.yufan.common.action.LotFilterAction;
import com.yufan.util.Sha1Util;
import com.yufan.util.XmlConverUtil;
import com.yufan.weixin.bean.InputMessage;
import com.yufan.weixin.bean.OutputMessage;
import com.yufan.weixin.bean.WeixinResp;

/**
 * 功能名称: 微信消息接收(不能被拦截不然配置会失败)
 * 开发人: lirf
 * 开发时间: 2016下午1:20:31
 * 其它说明：
 */
@Scope("prototype")
@Controller("WeiXinReceiveAction")
@Namespace("/weixin")
@ParentPackage("struts-default")
public class WeiXinReceiveAction extends LotFilterAction {
    private String Token = "weixin";
    //https://512164882.pagekite.me/rs_store_manage/weixin/receiveMsgFromWeixin

    //	@Action(value="receiveMsgFromWeixin",results={
//			 @Result(name="success",location="/jsp/core/addOrUpdateSendAddr.jsp")})
    @Action(value = "receiveMsgFromWeixin")
    public void receiveMsgFromWeixin() {
        initData();
        System.out.println("微信消息接收-----------------");
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        System.out.println("---->isGet=" + isGet);
        if (isGet) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            System.out.println(signature);
            System.out.println(timestamp);
            System.out.println(nonce);
            System.out.println(echostr);
            access(request, response);
        } else {
            // 进入POST聊天处理  
            System.out.println("enter post");
            // 接收消息并返回消息  
            try {
                String message = acceptMessage();
                System.out.println("接收消息=" + message);
                response.getWriter().write(message);
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 验证URL真实性
     *
     * @param request
     * @param response
     * @return String
     * @author morning
     * @date 2015年2月17日 上午10:53:07
     */
    private String access(HttpServletRequest request, HttpServletResponse response) {
        // 验证URL真实性  
        System.out.println("进入验证access");
        String signature = request.getParameter("signature");// 微信加密签名  
        String timestamp = request.getParameter("timestamp");// 时间戳  
        String nonce = request.getParameter("nonce");// 随机数  
        String echostr = request.getParameter("echostr");// 随机字符串  
        List<String> params = new ArrayList<String>();
        params.add(Token);
        params.add(timestamp);
        params.add(nonce);
        // 1. 将token、timestamp、nonce三个参数进行字典序排序  
        Collections.sort(params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密  
        String temp = Sha1Util.getSha1(params.get(0) + params.get(1) + params.get(2));
        if (temp.equals(signature)) {
            try {
                response.getWriter().write(echostr);
                System.out.println("成功返回 echostr：" + echostr);
                return echostr;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("失败 认证");
        return null;
    }

    private String acceptMessage() throws Exception {
        // 处理接收消息  
        ServletInputStream in = request.getInputStream();

        // 将流转换为字符串
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            xmlMsg.append(new String(b, 0, n, "UTF-8"));
        }
//    	System.out.println("xmlMsg------->"+xmlMsg);
        InputMessage inputMsg = XmlConverUtil.xmlToBean(xmlMsg.toString(), InputMessage.class);
        String servername = inputMsg.getToUserName();// 服务端  
        String custermname = inputMsg.getFromUserName();// 客户端  
        long createTime = inputMsg.getCreateTime();// 接收时间  
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间  

        // 取得消息类型  
        String msgKey = inputMsg.getEventKey();
        System.out.println("取得消息类型key  ----->" + msgKey);
        // 取得事件类型  
        String event = inputMsg.getEvent();
        System.out.println("取得事件类型  ----->" + event);
        // 根据消息类型获取对应的消息内容  
        System.out.println("开发者微信号：" + servername);
        System.out.println("发送方帐号(openId)：" + custermname);
        System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
        System.out.println("消息内容：" + inputMsg.getContent());
        System.out.println("消息Id：" + inputMsg.getMsgId());
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("msgKey", msgKey);//取得消息类型
        valuesMap.put("event", event);//取得事件类型
        valuesMap.put("servername", servername);//服务端
        valuesMap.put("custermname", custermname);//客户端 发送方帐号(openId)
        valuesMap.put(createTime + "", createTime);// 接收时间
        valuesMap.put("content", inputMsg.getContent());//消息内容
        valuesMap.put("msgId", inputMsg.getMsgId());//消息ID

        //接收类型
        int type = 0;
//		if("subscribe".equals(event)){//关注或者取消关注
//			type=1;
//		}

        String word = "";

        switch (type) {
            case 1:
                reduceSubscribe(valuesMap);
                break;

            default:
                word = returnWord(custermname, servername, returnTime);
                break;
        }
        return word;
    }

    /**
     * 处理关注
     */
    public void reduceSubscribe(Map<String, Object> valuesMap) {

    }

    /**
     * 通用返回
     *
     * @param custermname
     * @param servername
     * @param returnTime
     * @return
     */
    public String returnWord(String custermname, String servername, Long returnTime) {

        StringBuffer str = new StringBuffer();
        str.append("<xml>");
        str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");
        str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");
        str.append("<CreateTime>" + returnTime + "</CreateTime>");
        str.append("<MsgType><![CDATA[text]]></MsgType>");
        str.append("<Content><![CDATA[你查询有误？]]></Content>");
        str.append("</xml>");
        return str.toString();
    }


    /**
     * 组成微信请求参数
     *
     * @param outMsg
     * @return
     */
    public String xmlFormatWeixin(OutputMessage outMsg) {
        return null;
    }


//    private void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
//    	// 处理接收消息  
//    	ServletInputStream in = request.getInputStream();  
//    	// 将POST流转换为XStream对象  
//    	XStream xs = SerializeXmlUtil.createXstream();  
//    	xs.processAnnotations(InputMessage.class);  
//    	xs.processAnnotations(OutputMessage.class);  
//    	// 将指定节点下的xml节点数据映射为对象  
//    	xs.alias("xml", InputMessage.class);  
//    	// 将流转换为字符串  
//    	StringBuilder xmlMsg = new StringBuilder();  
//    	byte[] b = new byte[4096];  
//    	for (int n; (n = in.read(b)) != -1;) {  
//    		xmlMsg.append(new String(b, 0, n, "UTF-8"));  
//    	}  
//    	// 将xml内容转换为InputMessage对象  
//    	InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());  
//    	
//    	String servername = inputMsg.getToUserName();// 服务端  
//    	String custermname = inputMsg.getFromUserName();// 客户端  
//    	long createTime = inputMsg.getCreateTime();// 接收时间  
//    	Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间  
//    	
//    	// 取得消息类型  
//    	String msgType = inputMsg.getMsgType();  
//    	System.out.println("取得消息类型  ----->"+msgType);
//    	// 根据消息类型获取对应的消息内容  
//    	if (msgType.equals(MsgType.Text.toString())) {  
//    		// 文本消息  
//    		System.out.println("开发者微信号：" + inputMsg.getToUserName());  
//    		System.out.println("发送方帐号：" + inputMsg.getFromUserName());  
//    		System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));  
//    		System.out.println("消息内容：" + inputMsg.getContent());  
//    		System.out.println("消息Id：" + inputMsg.getMsgId());  
//    		
//    		StringBuffer str = new StringBuffer();  
//    		str.append("<xml>");  
//    		str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");  
//    		str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");  
//    		str.append("<CreateTime>" + returnTime + "</CreateTime>");  
//    		str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");  
//    		str.append("<Content><![CDATA[你说的是：" + inputMsg.getContent() + "，吗？]]></Content>");  
//    		str.append("</xml>");  
//    		System.out.println(str.toString());  
//    		response.getWriter().write(str.toString());  
//    	}  
////         获取并返回多图片消息  
//    	if (msgType.equals(MsgType.Image.toString())) {  
//    		System.out.println("获取多媒体信息");  
//    		System.out.println("多媒体文件id：" + inputMsg.getMediaId());  
//    		System.out.println("图片链接：" + inputMsg.getPicUrl());  
//    		System.out.println("消息id，64位整型：" + inputMsg.getMsgId());  
//    		
//    		OutputMessage outputMsg = new OutputMessage();  
//    		outputMsg.setFromUserName(servername);  
//    		outputMsg.setToUserName(custermname);  
//    		outputMsg.setCreateTime(returnTime);  
//    		outputMsg.setMsgType(msgType);  
//    		ImageMessage images = new ImageMessage();  
//    		images.setMediaId(inputMsg.getMediaId());  
//    		outputMsg.setImage(images);  
//    		System.out.println("xml转换：/n" + xs.toXML(outputMsg));  
//    		response.getWriter().write(xs.toXML(outputMsg));  
//    		
//    	}  
//    }


    /**
     * 关于网页授权的两种scope的区别说明
     * 1、以snsapi_base为scope发起的网页授权，是用来获取进入页面的用户的openid的，并且是静默授权并自动跳转到回调页的。用户感知的就是直接进入了回调页（往往是业务页面）
     *
     * 2、以snsapi_userinfo为scope发起的网页授权，是用来获取用户的基本信息的。但这种授权需要用户手动同意，并且由于用户同意过，所以无须关注，就可在授权后获取该用户的基本信息。
     */


}
