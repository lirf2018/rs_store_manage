package com.yufan.manage.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yufan.pojo.TbPartners;
import com.yufan.util.ImageUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.yufan.common.action.ImageAction;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbBannel;
import com.yufan.util.DatetimeUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.BannelCondition;

/**
 * 功能名称: bannel 管理
 * 开发人: lirf
 * 开发时间: 2016下午8:59:09
 * 其它说明：
 */
@Scope("prototype")
@Controller("bannelAction")
@Namespace("/manage")
@ParentPackage("common")
public class BannelAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;//标识
    private String op;//add or update
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbBannel bannel = new TbBannel();


    private String photoPath;
    private File file;

    //条件
    private String status;

    private List<Map<String, Object>> list_channel = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listPartners = new ArrayList<Map<String, Object>>();

    /**
     */
    @Action(value = "loadbannelDataList")
    public void loadDataList() {
        try {
            initData();
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }
            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }

            BannelCondition bannel_ = new BannelCondition();
            String bannelName = request.getParameter("bannelName");
            if (null != bannelName && !"".equals(bannelName)) {
                bannel_.setBannelName(bannelName);
            }
            String partnersName = request.getParameter("partnersName");
            if (null != partnersName && !"".equals(partnersName)) {
                bannel_.setPartnersName(partnersName);
            }
            String channelId = request.getParameter("channel_id");
            if (null != channelId && !"".equals(channelId)) {
                bannel_.setChannelId(Integer.parseInt(channelId));
            }
            String objId = request.getParameter("objId");
            if (null != objId && !"".equals(objId) && !"0".equals(objId)) {
                bannel_.setObj(Integer.parseInt(objId));
            }
            if (null != status && !"".equals(status)) {
                bannel_.setStatus(Integer.parseInt(status));
            }

            page = findInfoDao.loadTbBannelPage(page, bannel_);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改活动专题页面
     *
     * @return
     */
    @Action(value = "toAddorUpdatebannelPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-bannel.jsp")})
    public String toAddorUpdatebannelPage() {
        initData();
        try {
            //查询渠道列表
            list_channel = findInfoDao.listChannelMap("1");
            //查询商家列表
            TbPartners partners = new TbPartners();
            partners.setStatus(1);
            listPartners = findInfoDao.listPartnersMap(partners);

            photoPath = RsConstants.phone_url + "null.jpg";
            if ("update".equals(op)) {
                bannel = findInfoDao.loadTbBannelInfo(Integer.parseInt(id));
                if (bannel.getBannelImg() != null && !"".equals(bannel.getBannelImg())) {
                    photoPath = RsConstants.phone_url + bannel.getBannelImg();
                }
            } else {
                bannel.setWeight(0);
                bannel.setStartTime(new Date());
                bannel.setEndTime(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改活动专题
     */
    @Action(value = "addOrupdatebannel")
    public void addOrUpdate() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
//			bannel.setStartTime(DatetimeUtil.formatStringToData("yyyy-MM-dd HH:mm:ss", bannel.getDateS().split(" ")[0]+" 00:00:00"));
//			bannel.setEndTime(DatetimeUtil.formatStringToData("yyyy-MM-dd HH:mm:ss", bannel.getDateE().split(" ")[0]+" 23:59:59"));

            String beginTime = request.getParameter("beginTime");
            String format = "yyyy-MM-dd HH:mm:ss";
            if (null != beginTime && !"".equals(beginTime) && !"null".equals(beginTime)) {
                bannel.setStartTime(new Timestamp(DatetimeUtil.convertStrToDate(beginTime, format).getTime()));
            }
            String endTime = request.getParameter("endTime");
            if (null != endTime && !"".equals(endTime) && !"null".equals(endTime)) {
                bannel.setEndTime(new Timestamp(DatetimeUtil.convertStrToDate(endTime, format).getTime()));
            }


            if ("add".equals(op)) {//新增
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        bannel.setBannelImg(imgpath);
                        ;
                    }
                }

                bannel.setCreateman(user.getLoginName());
                bannel.setCreatetime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(bannel);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {
                TbBannel bannel_ = findInfoDao.loadTbBannelInfo(bannel.getBannelId());

                bannel.setCreateman(bannel_.getCreateman());
                bannel.setCreatetime(bannel_.getCreatetime());

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        ImageUtil.getInstance().removePathImg(bannel_.getBannelImg(), request);
                        bannel.setBannelImg(imgpath);
                    }
                } else {
                    bannel.setBannelImg(bannel_.getBannelImg());
                }

                bannel.setLastalterman(user.getLoginName());
                bannel.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(bannel);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败"));
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除活动专题
     */
    @Action(value = "delStatusBannel")
    public void deleteStatus() {
        initData();
        try {
            boolean flag = saveInfoDao.upadteTbBannelStatus(id, status);
            if (flag && "0".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else if (flag && "1".equals(status)) {
                response.getWriter().write(write_Json_result("ok", "启用成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置页码
     *
     * @return
     */
    public void setPages() {
        if (null != currentPage && !"".equals(currentPage)) {
            page.setCurrentPage(currentPage);
        }
    }

    public FindInfoDao getFindInfoDao() {
        return findInfoDao;
    }

    public void setFindInfoDao(FindInfoDao findInfoDao) {
        this.findInfoDao = findInfoDao;
    }

    public SaveInfoDao getSaveInfoDao() {
        return saveInfoDao;
    }

    public void setSaveInfoDao(SaveInfoDao saveInfoDao) {
        this.saveInfoDao = saveInfoDao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public PageUtil getPage() {
        return page;
    }

    public void setPage(PageUtil page) {
        this.page = page;
    }

    public TbBannel getBannel() {
        return bannel;
    }

    public void setBannel(TbBannel bannel) {
        this.bannel = bannel;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, Object>> getList_channel() {
        return list_channel;
    }

    public void setList_channel(List<Map<String, Object>> list_channel) {
        this.list_channel = list_channel;
    }

    public List<Map<String, Object>> getListPartners() {
        return listPartners;
    }

    public void setListPartners(List<Map<String, Object>> listPartners) {
        this.listPartners = listPartners;
    }
}
