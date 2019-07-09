package com.yufan.core.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yufan.util.ConfigProperty;
import com.yufan.util.RefreshCacheUtil;
import com.yufan.util.RequestMethod;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbParam;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 参数管理
 * @作者 lirongfan
 * @时间 2016年4月26日 上午9:55:06
 */
@Scope("prototype")
@Service("paramAction")
@Namespace("/param")
@ParentPackage("common")
public class ParamAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;// 标识
    private String handleType;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbParam param_ = new TbParam();

    private List<Map<String, Object>> listParamType;

    // 查询条件
    private String param_name;
    private String state;
    private String param_code;
    private Integer isConfirm;

    /**
     * 加载参数列表
     */
    @Action(value = "loadParamList")
    public void loadParamList() {
        try {
            initData();
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request
                        .getParameter("currentPage"));
            }
            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }

            param_.setParamName(param_name);
            param_.setParamCode(param_code);

            if (null != state && !"null".equals(state) && !"".equals(state)) {
                param_.setStatus(Integer.parseInt(state));
            }
            param_.setIsMakeSure(isConfirm);

            page = findInfoDao.loadParamList(page, param_);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询参数类型
     */
    @Action(value = "selectParamType")
    public void selectParamType() {
        initData();
        try {
            listParamType = findInfoDao.listParamGroupMap();
            response.getWriter().write(write_Json_result("ok", listParamType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改页面
     *
     * @return
     */
    @Action(value = "toAddOrUpdateParamPage",
            results = {@Result(name = "success", location = "/jsp/sys/addorupdate-param.jsp")})
    public String toAddOrUpdateParamPage() {
        initData();
        try {
            if ("update".equals(handleType)) {
                param_ = findInfoDao.loadTbParamInfo(Integer.parseInt(id));
                param_.setIsMakeSure(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改参数
     */
    @Action(value = "addOrUpdateParam")
    public void addOrUpdateParam() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(handleType)) {// 增加
                param_.setCreatetime(new Timestamp(System.currentTimeMillis()));
                param_.setCreateman(user.getLoginName());
                param_.setIsMakeSure(0);
                flag = saveInfoDao.addOrUpdateParam(param_);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(
                            write_Json_result("false", "增加失败"));
                }
            } else {// 修改
                TbParam param = new TbParam();
                param = findInfoDao.loadTbParamInfo(param_.getParamId());
                param_.setCreateman(param.getCreateman());
                param_.setCreatetime(param.getCreatetime());
                param_.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                param_.setLastalterman(user.getLoginName());
                param_.setIsMakeSure(0);
                flag = saveInfoDao.addOrUpdateParam(param_);
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParam();
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败(" + reusltMsg + ")"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除参数
     */
    @Action(value = "delParamState")
    public void delParamState() {
        initData();
        try {
            boolean flag = saveInfoDao.delParamState(id, state);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshParam();
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                if ("0".equals(state)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功(" + reusltMsg + ")"));
                }
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败(" + reusltMsg + ")"));
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

    /**
     * 查询
     */
    @Action(value = "listParamMap")
    public void listParamMap() {
        initData();
        try {
            String param_code = request.getParameter("param_code");
            List<Map<String, Object>> listParam = findInfoDao.listParamMap(param_code);
            response.getWriter().write(write_Json_result("ok", listParam));
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
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

    public TbParam getParam_() {
        return param_;
    }

    public void setParam_(TbParam param_) {
        this.param_ = param_;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Map<String, Object>> getListParamType() {
        return listParamType;
    }

    public void setListParamType(List<Map<String, Object>> listParamType) {
        this.listParamType = listParamType;
    }

    public String getParam_code() {
        return param_code;
    }

    public void setParam_code(String param_code) {
        this.param_code = param_code;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }
}
