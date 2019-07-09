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
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbAdmin;
import com.yufan.util.DatetimeUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.ActivityCondition;

/**
 * 功能名称: 活动专题管理
 * 开发人: lirf
 * 开发时间: 2016下午12:18:51
 * 其它说明：
 */
@Scope("prototype")
@Controller("activityAction")
@Namespace("/manage")
@ParentPackage("common")
public class ActivityAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;//标识
    private String op;//add or update
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbActivity activity = new TbActivity();


    private String photoPath;
    private File file;

    //条件
    private String status;
    private String channel_id;

    private List<Map<String, Object>> list_channel = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listPartners = new ArrayList<Map<String, Object>>();

    /**
     * 加载商品模板列表
     */
    @Action(value = "loadActivityDataList")
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

            ActivityCondition condition = new ActivityCondition();

            if (null != status && !"".equals(status) && !"null".equals(status)) {
                condition.setStatus(Integer.parseInt(status));
            }
            if (null != channel_id && !"".equals(channel_id) && !"0".equals(channel_id)) {
                condition.setChannelId(Integer.parseInt(channel_id));
            }
            String objId = request.getParameter("objId");
            if (null != objId && !"".equals(objId) && !"0".equals(objId)) {
                condition.setObj(Integer.parseInt(objId));
            }
            String partnersName = request.getParameter("partnersName");
            if (null != partnersName && !"".equals(partnersName) && !"null".equals(partnersName)) {
                condition.setPartnersName(partnersName);
            }
            String title = request.getParameter("title");
            if (null != title && !"".equals(title) && !"null".equals(title)) {
                condition.setActivityTitle(title);
            }
            page = findInfoDao.loadActivityPage(page, condition);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改活动专题页面
     *
     * @return
     */
    @Action(value = "toAddorUpdateActivityPage", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-activity.jsp")})
    public String toAddorUpdateActivityPage() {
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
                activity = findInfoDao.loadTbActivityInfo(Integer.parseInt(id));
                if (activity.getActivityImg() != null && !"".equals(activity.getActivityImg())) {
                    photoPath = RsConstants.phone_url + activity.getActivityImg();
                }
            } else {
                activity.setWeight(0);
                activity.setStartTime(new Date());
                activity.setEndTime(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改活动专题
     */
    @Action(value = "addOrupdateActivity")
    public void addOrUpdate() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
//			activity.setStartTime(DatetimeUtil.formatStringToData("yyyy-MM-dd HH:mm:ss", activity.getDateS().split(" ")[0]+" 00:00:00"));
//			activity.setEndTime(DatetimeUtil.formatStringToData("yyyy-MM-dd HH:mm:ss", activity.getDateE().split(" ")[0]+" 23:59:59"));

            String beginTime = request.getParameter("beginTime");
            String format = "yyyy-MM-dd HH:mm:ss";
            if (null != beginTime && !"".equals(beginTime) && !"null".equals(beginTime)) {
                activity.setStartTime(new Timestamp(DatetimeUtil.convertStrToDate(beginTime, format).getTime()));
            }
            String endTime = request.getParameter("endTime");
            if (null != endTime && !"".equals(endTime) && !"null".equals(endTime)) {
                activity.setEndTime(new Timestamp(DatetimeUtil.convertStrToDate(endTime, format).getTime()));
            }

            if ("add".equals(op)) {//新增
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        activity.setActivityImg(imgpath);
                    }
                }

                activity.setCreateman(user.getLoginName());
                activity.setCreatetime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(activity);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {
                TbActivity activity_ = findInfoDao.loadTbActivityInfo(activity.getActivityId());

                activity.setCreateman(activity_.getCreateman());
                activity.setCreatetime(activity_.getCreatetime());

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        ImageUtil.getInstance().removePathImg(activity_.getActivityImg(), request);
                        activity.setActivityImg(imgpath);
                    }
                } else {
                    activity.setActivityImg(activity_.getActivityImg());
                }

                activity.setLastalterman(user.getLoginName());
                activity.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(activity);
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
    @Action(value = "delStatusActivity")
    public void deleteStatus() {
        initData();
        try {
            boolean flag = saveInfoDao.upadteTbActivitStatus(id, status);
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

    public TbActivity getActivity() {
        return activity;
    }

    public void setActivity(TbActivity activity) {
        this.activity = activity;
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public List<Map<String, Object>> getList_channel() {
        return list_channel;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
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
