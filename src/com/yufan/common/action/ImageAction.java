package com.yufan.common.action;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbImgUploadNameRecord;
import com.yufan.util.DatetimeUtil;
import com.yufan.util.ImageUtil;
import com.yufan.util.RsConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.Timestamp;

/**
 * 功能名称: 图片上传类
 * 开发人: lirf
 * 开发时间: 2016下午3:00:10
 * 其它说明：
 */
@Scope("prototype")
@Service("imageAction")
@Namespace("/image")
@ParentPackage("common")
public class ImageAction extends LotFilterAction {

    private File file;
    private File upfile;
    private String from;//图片来源 用于控制不同图片大小和尺寸

    @Autowired
    private SaveInfoDao saveInfoDao;

    /**
     * 图片上传
     * form:
     * 商品主图：goodsMainPic
     */
    @Action(value = "uploadFile")
    public void uploadFile() {
        LOG.info("-->上传图片-->来源：" + from);
        try {
            initData();
            String imgpath = "";
            JSONObject obj = new JSONObject();
            obj.put("imgpath", imgpath);
            if (null != file) {
                //对商品主图大小限制
                if ("goodsMainPic".equals(from)) {
                    BufferedImage image = ImageIO.read(file);
                    int width = image.getWidth();
                    int height = image.getHeight();
                    LOG.info("----图片大小=" + width + "px*" + height + "px");
                    if (width != 150 || height != 150) {
                        response.getWriter().write(write_Json_result("false", "主图长*宽=150px*150px"));
                        return;
                    }
                }

                imgpath = uploadFileToLocalhost(file);
            }
            if (null != imgpath && !"".equals(imgpath)) {
                String imgpath_ = RsConstants.phone_url + imgpath;
                obj.put("imgpath", imgpath_);
                obj.put("img", imgpath);//不包括http
                response.getWriter().write(write_Json_result("ok", obj));
            } else {
                response.getWriter().write(write_Json_result("false", obj));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 百度编辑器上传图片
     * {
     * "state": "SUCCESS",
     * "url": "upload/demo.jpg",
     * "title": "demo.jpg",
     * "original": "demo.jpg"
     */
    @Action(value = "baiduUploadFile")
    public void baiduUploadFile() {
        System.out.println("-->baiduUploadFile上传图片");
        try {
            initData();
            PrintWriter writer = null;
            writer = response.getWriter();
            String imgpath = "";
            JSONObject obj = new JSONObject();
            obj.put("state", "false");
            obj.put("url", "false");
            obj.put("title", "false");
            obj.put("original", "false");
            if (null != upfile) {
                imgpath = uploadFileToLocalhost(upfile);
            }
            if (null != imgpath && !"".equals(imgpath)) {
                String savePath = RsConstants.phone_url + imgpath;
                String strs[] = imgpath.split("/");
                String name = strs[strs.length - 1];
                obj.put("state", "SUCCESS");
                State storageState = new BaseState(true);
                storageState.putInfo("size", upfile.length());
                storageState.putInfo("title", name);
                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", ".jpg");
                storageState.putInfo("original", name);
                /**
                 * {
                 "state": "SUCCESS",
                 "title": "02a预定960x480.jpg",
                 "original": "02a预定960x480.jpg",
                 "type": ".jpg",
                 "url": "http://192.168.2.99:8080/M00/00/B6/wKgCY1nMr8-ADd8RAADB3oIay94899.jpg",
                 "size": "49630"
                 }
                 */
//				String out = storageState.toString();
                String out = "{\"state\": \"SUCCESS\",\"title\": \"02a\\u9884\\u5b9a960x480.jpg\",\"original\": \"02a\\u9884\\u5b9a960x480.jpg\",\"type\": \".jpg\",\"url\": \"http://192.168.2.99:8080/M00/00/B6/wKgCY1nMu0uAdzeuAADB3oIay94743.jpg\",\"size\": \"49630\"}";
                writer.write(out);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将文件存到本地
     */
    public String uploadFileToLocalhost(File file) {
        try {
            initData();
            //E:\tomcat\apache-tomcat-8.0.15\webapps\rs_store_manage
            String path = request.getSession().getServletContext().getRealPath("/");
            String p[] = path.split("webapps");

            // 文件按 年/月 目录保存
            String date = DatetimeUtil.getNow("yyyy-MM-dd");
            String dates[] = date.split("-");
            //图片根文件夹
            String root = p[0] + "webapps/image/";
            String img = dates[0] + "/" + dates[1] + "/";
            String filename = DatetimeUtil.getNow("yyyyMMdd") + System.currentTimeMillis() + ".jpg";
            String filePath = img + filename;
            System.out.println("-------->" + root + img);
            System.out.println("----filePath---->" + filePath);
            //生成文件
            ImageUtil.getInstance().getFile(ImageUtil.getInstance().getBytes(file), root + img, filename);

            //保存上传图片路径
            saveImgRecord(filePath, file.length());
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 保存图片上传路径
     *
     * @param imgPathName
     */
    private void saveImgRecord(String imgPathName, long imgSize) {
        try {
            if (StringUtils.isEmpty(imgPathName)) {
                LOG.info("--------->图片上传路径为空");
                return;
            }
            TbImgUploadNameRecord imgUploadNameRecord = new TbImgUploadNameRecord();
            imgUploadNameRecord.setCreateTime(new Timestamp(new Date().getTime()));
            imgUploadNameRecord.setImgPath(imgPathName);
            imgUploadNameRecord.setImgStatus(0);
            imgUploadNameRecord.setImgSize(imgSize);
            saveInfoDao.saveObject(imgUploadNameRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }


    public void setFile(File file) {
        this.file = file;
    }

    public File getUpfile() {
        return upfile;
    }

    public void setUpfile(File upfile) {
        this.upfile = upfile;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
