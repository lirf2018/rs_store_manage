package com.yufan.core.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbChannel;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * 功能名称: 渠道管理
 * 开发人: lirf
 * 开发时间: 2016下午12:41:58
 * 其它说明：
 */
@Scope("prototype")
@Service("channelAction")
@Namespace("/channel")
@ParentPackage("common")
public class ChannelAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private TbChannel channel = new TbChannel();
    private String handleType;//add or update
    private String channelId;//渠道标识

    private List<Map<String, Object>> list_channel = new ArrayList<Map<String, Object>>();

    /**
     * 查询条件
     */
    private String searchName;
    private String state;

    /**
     * 加载渠道列表
     *
     * @return
     */
    @Action(value = "loadlistChannel")
    public void loadlistChannel() {
        System.out.println("=======rolelist===========");
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
            channel.setChannelName(searchName);
            if (null != state && !"".equals(state)) {
                channel.setStatus(Integer.parseInt(state));
            }
            page = findInfoDao.listChannel(page, channel);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 增加或者修改渠道
     */
    @Action(value = "addOrUpdateChannel")
    public void addOrUpdateChannel() {
        initData();
        try {

            if (findInfoDao.checkChannelName(channel.getChannelId() + "", channel.getChannelName())) {
                response.getWriter().write(write_Json_result("false", "渠道名称已存在!"));
                return;
            }
            if (findInfoDao.checkChannelCode(channel.getChannelId() + "", channel.getChannelCode())) {
                response.getWriter().write(write_Json_result("false", "渠道编码已存在!"));
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {//增加渠道
                channel.setStatus(1);
                channel.setCreatetime(new Timestamp(System.currentTimeMillis()));
                channel.setCreateman(user.getLoginName());
                boolean flag = saveInfoDao.addOrUpdateChannel(channel);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
                return;
            } else {//修改
                TbChannel channel_ = findInfoDao.loadChannelInfo(channel.getChannelId());
                channel.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                channel.setLastalterman(user.getLoginName());
                channel.setCreateman(channel_.getCreateman());
                channel.setCreatetime(channel_.getCreatetime());
                channel.setStatus(channel_.getStatus());
                boolean flag = saveInfoDao.addOrUpdateChannel(channel);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除或者启用渠道(逻辑删除)
     */
    @Action(value = "delOrUpdateChannel")
    public void delOrUpdateChannel() {
        initData();
        try {
            String state = request.getParameter("status");
            String channelid = request.getParameter("channelid");
            boolean flag = saveInfoDao.deleteOrupdateChannel(channelid, state);
            if (flag) {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除失败"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用失败"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询渠道列表
     */
    @Action(value = "listChannelMap")
    public void listChannelMap() {
        initData();
        try {
            list_channel = findInfoDao.listChannelMap(null);
            response.getWriter().write(write_Json_result("ok", list_channel));
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

    public TbChannel getChannel() {
        return channel;
    }

    public void setChannel(TbChannel channel) {
        this.channel = channel;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Map<String, Object>> getList_channel() {
        return list_channel;
    }

    public void setList_channel(List<Map<String, Object>> list_channel) {
        this.list_channel = list_channel;
    }
}
