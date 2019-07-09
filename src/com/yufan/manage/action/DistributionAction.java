package com.yufan.manage.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbRegion;
import com.yufan.util.ConfigProperty;
import com.yufan.util.RefreshCacheUtil;
import com.yufan.util.RequestMethod;
import org.apache.commons.lang3.StringUtils;
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
import com.yufan.pojo.TbDistributionAddr;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * 功能名称: 配送地址管理
 * 开发人: lirf
 * 开发时间: 2016下午10:52:17
 * 其它说明：
 */
@Scope("prototype")
@Service("distributionAction")
@Namespace("/manage")
@ParentPackage("common")
public class DistributionAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private String id;// 标识
    private String op;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbDistributionAddr distribution = new TbDistributionAddr();

    // 查询条件
    private String addrPrefix;//地址前缀
    private String detailAddr;//详细地址
    private String responsible;//负责人/电话
    private String status;
    private String addrType;

    private TbRegion region;

    /**
     * 加载配送地址列表
     */
    @Action(value = "loadDistributionAddrList")
    public void loadDistributionAddrList() {
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
            if (null != status && !"null".equals(status) && !"".equals(status)) {
                distribution.setStatus(Integer.parseInt(status));
            }
            if (null != addrType && !"null".equals(addrType) && !"".equals(addrType)) {
                distribution.setAddrType(Integer.parseInt(addrType));
            }
            distribution.setAddrPrefix(addrPrefix);
            distribution.setDetailAddr(detailAddr);
            distribution.setResponsibleMan(responsible);

            page = findInfoDao.loadTBDistributionPage(page, distribution);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加或者修改页面
     *
     * @return
     */
    @Action(value = "toauDistributionAddrPage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-sendaddr.jsp")})
    public String toauDistributionAddrPage() {
        initData();
        try {
            if ("update".equals(op)) {
                distribution = findInfoDao.loadTbDistributionAddrInfo(Integer.parseInt(id));
                request.setAttribute("addrType_" + distribution.getAddrType(), "checked");
            } else {
                distribution.setAddrShort(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改配送地址
     */
    @Action(value = "addOrUpdateDistributionAddr")
    public void addOrUpdateDistributionAddr() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("add".equals(op)) {// 增加
                distribution.setCreatetime(new Timestamp(System.currentTimeMillis()));
                distribution.setCreateman(user.getLoginName());

                String addrTypes[] = request.getParameterValues("addrTypes");

                for (int i = 0; i < addrTypes.length; i++) {
                    distribution.setAddrType(Integer.parseInt(addrTypes[i]));
                    flag = saveInfoDao.saveOrUpdateObj(distribution);
                    distribution.setId(null);
                }
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshPlatform(0);//更新全部
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败(" + reusltMsg + ")"));
                }
            } else {// 修改
                TbDistributionAddr distribution_ = findInfoDao.loadTbDistributionAddrInfo(distribution.getId());
                distribution.setCreateman(distribution_.getCreateman());
                distribution.setCreatetime(distribution_.getCreatetime());
                distribution.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                distribution.setLastalterman(user.getLoginName());
                flag = saveInfoDao.saveOrUpdateObj(distribution);
                //更新参数缓存
                JSONObject reuslt = RefreshCacheUtil.getInstence().refreshPlatform(distribution.getId());
                String reusltMsg = reuslt.getString("msg");
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败(" + reusltMsg + ")"));
                }
                //更新用户对应收货地址为无效状态
                saveInfoDao.updateUserAddrStatus(2, String.valueOf(distribution.getId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除配送地址
     */
    @Action(value = "delDistributionAddrstatus")
    public void delDistributionAddrstatus() {
        initData();
        try {
            boolean flag = saveInfoDao.updateTbDistributionAddrStatus(status, id);
            //更新参数缓存
            JSONObject reuslt = RefreshCacheUtil.getInstence().refreshPlatform(Integer.parseInt(id));
            String reusltMsg = reuslt.getString("msg");
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功(" + reusltMsg + ")"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功(" + reusltMsg + ")"));
                }
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败(" + reusltMsg + ")"));
            }
            saveInfoDao.updateUserAddrStatus(2, id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //*****************************************华丽分割*********************全国自选地址************************

    /**
     * 查看详情全国地址管理
     */
    @Action(value = "loadAddrManagePage")
    public void loadAddrManagePage() {
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
            TbRegion region = new TbRegion();
            if (null != status && !"null".equals(status) && !"".equals(status)) {
                region.setStatus(Integer.parseInt(status));
            }
            String regionCode = request.getParameter("regionCode");
            if (StringUtils.isNotEmpty(regionCode)) {
                //补充位数达到12位
                regionCode = regionCode.trim();
                if (regionCode.length() == 1) {
                    regionCode = regionCode + "00000000000";
                } else if (regionCode.length() == 2) {
                    regionCode = regionCode + "0000000000";
                } else if (regionCode.length() == 3) {
                    regionCode = regionCode + "000000000";
                } else if (regionCode.length() == 4) {
                    regionCode = regionCode + "00000000";
                } else if (regionCode.length() == 5) {
                    regionCode = regionCode + "0000000";
                } else if (regionCode.length() == 6) {
                    regionCode = regionCode + "000000";
                } else if (regionCode.length() == 7) {
                    regionCode = regionCode + "00000";
                } else if (regionCode.length() == 8) {
                    regionCode = regionCode + "0000";
                } else if (regionCode.length() == 9) {
                    regionCode = regionCode + "000";
                } else if (regionCode.length() == 10) {
                    regionCode = regionCode + "00";
                } else if (regionCode.length() == 11) {
                    regionCode = regionCode + "0";
                }
                region.setRegionCode(regionCode);
            }
            String regionType = request.getParameter("regionType");
            if (!"-1".equals(regionType)) {
                region.setRegionType(Integer.parseInt(regionType));
            }
            String level = request.getParameter("level");
            if (!"-1".equals(level)) {
                region.setRegionLevel(Integer.parseInt(level));
            }
            String regionName = request.getParameter("regionName");
            if (StringUtils.isNotEmpty(regionName)) {
                region.setRegionName(regionName);
            }
            String regionCodeP = request.getParameter("regionCodeP");
            if (StringUtils.isNotEmpty(regionCodeP)) {
                if (StringUtils.isNotEmpty(regionCodeP)) {
                    //补充位数达到12位
                    regionCodeP = regionCodeP.trim();
                    if (regionCodeP.length() == 1) {
                        regionCodeP = regionCodeP + "00000000000";
                    } else if (regionCodeP.length() == 2) {
                        regionCodeP = regionCodeP + "0000000000";
                    } else if (regionCodeP.length() == 3) {
                        regionCodeP = regionCodeP + "000000000";
                    } else if (regionCodeP.length() == 4) {
                        regionCodeP = regionCodeP + "00000000";
                    } else if (regionCodeP.length() == 5) {
                        regionCodeP = regionCodeP + "0000000";
                    } else if (regionCodeP.length() == 6) {
                        regionCodeP = regionCodeP + "000000";
                    } else if (regionCodeP.length() == 7) {
                        regionCodeP = regionCodeP + "00000";
                    } else if (regionCodeP.length() == 8) {
                        regionCodeP = regionCodeP + "0000";
                    } else if (regionCodeP.length() == 9) {
                        regionCodeP = regionCodeP + "000";
                    } else if (regionCodeP.length() == 10) {
                        regionCodeP = regionCodeP + "00";
                    } else if (regionCodeP.length() == 11) {
                        regionCodeP = regionCodeP + "0";
                    }
                }
                region.setParentId(regionCodeP);
                TbRegion region1 = findInfoDao.loadTbRegion(regionCodeP);
                if (null != region1) {
                    page.setExtendsString(region1.getRegionName());
                }
            }
            page = findInfoDao.loadAddrManagePage(page, region);
            if (StringUtils.isNotEmpty(regionCodeP)) {
                region = findInfoDao.loadTbRegion(regionCodeP);
                if (null != region) {
                    page.setExtendsString(region.getRegionName());
                }
            }
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到全国地址管理
     *
     * @return
     */
    @Action(value = "toUpdateRegionPage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-region.jsp")})
    public String toUpdateRegionPage() {

        if (op.equals("update")) {
            region = findInfoDao.loadTbRegion(Integer.parseInt(id));
        }
        return "success";
    }


    /**
     * 保存全国地址
     */
    @Action(value = "saveRegionStatus")
    public void saveRegionStatus() {
        initData();
        try {
            PrintWriter writer = response.getWriter();
            JSONObject out = new JSONObject();
            out.put("flag", "false");

            //查询代码是否存在
            boolean flag = findInfoDao.checkRegionCode(region.getRegionId(), region.getRegionCode().trim());
            if (flag) {
                out.put("msg", "行政区划代码已存在");
                writer.print(out);
                writer.flush();
                writer.close();
                return;
            }
            region.setRegionCode(region.getRegionCode().trim());
            region.setRegionName(region.getRegionName().trim());
            region.setCreatetime(new Timestamp(new Date().getTime()));
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (op.equals("update")) {
                TbRegion region_ = findInfoDao.loadTbRegion(region.getRegionId());
                region.setStatus(region_.getStatus());
                region.setCreateman(region_.getCreateman());
                user.setLastalterman(user.getLoginName());
                region.setLastaltertime(new Timestamp(new Date().getTime()));
                saveInfoDao.saveOrUpdateObj(region);
                saveInfoDao.updateUserAddrStatus(1, region_.getRegionCode());
                saveInfoDao.updateUserAddrStatus(1, region.getRegionCode());
            } else {
                region.setStatus(1);
                region.setCreateman(user.getLoginName());
                saveInfoDao.saveObject(region);

            }
            out.put("flag", "ok");
            out.put("msg", "操作成功");
            writer.print(out);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 逻辑修改状态
     */
    @Action(value = "updateRegionStatus")
    public void updateRegionStatus() {
        initData();
        try {
            PrintWriter writer = response.getWriter();
            TbRegion region = findInfoDao.loadTbRegion(Integer.parseInt(id));
            saveInfoDao.updateTbRegionStatus(region.getRegionId(), Integer.parseInt(status));
            writer.print(1);
            writer.flush();
            writer.close();
            saveInfoDao.updateUserAddrStatus(1, region.getRegionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有地区
     */
    @Action(value = "loadTbRegionListMap")
    public void loadTbRegionListMap() {
        initData();
        try {
            String regionCode = request.getParameter("regionCode");
            String regionName = request.getParameter("regionName");
            List<Map<String, Object>> listTbRegion = findInfoDao.loadTbRegionListMap(regionCode, regionName);
            response.getWriter().write(write_Json_result("ok", listTbRegion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查看详情全国地址详细
     */
    @Action(value = "loadAddrDetail")
    public void loadAddrDetail() {
        try {
            initData();
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }
            page.setPageSize(10);
            if (null == currentPage || 0 == currentPage)
                page.setCurrentPage(1);
            else {
                page.setCurrentPage(currentPage);
            }
            TbRegion region = new TbRegion();
            if (null != status && !"null".equals(status) && !"".equals(status)) {
                region.setStatus(Integer.parseInt(status));
            }
            String regionCode = request.getParameter("regionCode");
            if (StringUtils.isNotEmpty(regionCode)) {
                region.setRegionCode(regionCode);
            }
            String regionName = request.getParameter("regionName");
            if (StringUtils.isNotEmpty(regionName)) {
                region.setRegionName(regionName);
            }
            String regionType = request.getParameter("regionType");
            if (!"-1".equals(regionType)) {
                region.setRegionType(Integer.parseInt(regionType));
            }
            page = findInfoDao.loadAddrDetailPage(page, region);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询地址子地址分类
     */
    @Action(value = "loadAddrListSub")
    public void loadAddrSub() {
        initData();
        try {
            JSONObject out = new JSONObject();
            PrintWriter writer = response.getWriter();
            String regionCode = request.getParameter("regionCode");
            String regionName = request.getParameter("regionName");
            TbRegion region = findInfoDao.loadTbRegion(regionCode);
            List<Map<String, Object>> list = findInfoDao.loadAddrListMap(regionCode, regionName);
            out.put("flag", "ok");
            out.put("list", list);
            if (null != region) {
                out.put("regionName", region.getRegionName());
            }
            writer.print(out);
            writer.flush();
            writer.close();
        } catch (Exception e) {
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

    public TbDistributionAddr getDistribution() {
        return distribution;
    }

    public void setDistribution(TbDistributionAddr distribution) {
        this.distribution = distribution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAddrPrefix() {
        return addrPrefix;
    }

    public void setAddrPrefix(String addrPrefix) {
        this.addrPrefix = addrPrefix;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public TbRegion getRegion() {
        return region;
    }

    public void setRegion(TbRegion region) {
        this.region = region;
    }
}
