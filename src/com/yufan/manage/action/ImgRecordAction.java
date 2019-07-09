package com.yufan.manage.action;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.*;
import com.yufan.util.ImageUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 上传图片管理
 */
@Scope("prototype")
@Service("imgRecordAction")
@Namespace("/manage")
@ParentPackage("common")
public class ImgRecordAction extends LotFilterAction {

    @Autowired
    private FindInfoDao findInfoDao;

    @Autowired
    private SaveInfoDao saveInfoDao;

    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);
    private Integer status;
    private String imgPathName;
    private String ids;

    /**
     * 加载配送地址列表
     */
    @Action(value = "loadImgRecordList")
    public void loadImgRecordList() {
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
            page = findInfoDao.loadImgRecordPage(page, status, imgPathName);
            response.getWriter().write(write_Json_result("ok", page));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新使用状态
     */
    @Action(value = "refushUseStatus")
    public void refushUseStatus() {
        //
        initData();
        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "操作失败");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String ids = request.getParameter("ids");
            String[] idsArr = ids.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                if (StringUtils.isNotEmpty(idsArr[i])) {
                    TbImgUploadNameRecord imgUploadNameRecord = findInfoDao.loadTbImgUploadNameRecord(Integer.parseInt(idsArr[i]));
                    if (null != imgUploadNameRecord) {
                        String tableName = checkImgDB(imgUploadNameRecord.getImgPath());
                        if ("".equals(tableName)) {
                            //说明图片地址已失效(逻辑删除,移动备份)
                            boolean flag = ImageUtil.getInstance().removePathImg(imgUploadNameRecord.getImgPath(), request);
                            if (flag) {
                                saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 2);
                            }
                        } else if (null != tableName) {
                            //说明图片地址正在使用
                            saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 1, tableName);
                        }
                    }
                }
            }
            out.put("flag", 1);
            out.put("msg", "操作成功");
            writer.print(out);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            out.put("msg", "操作异常");
            writer.print(out);
            writer.flush();
            writer.close();
            e.printStackTrace();
        }

    }

    /**
     * 移除图片
     */
    @Action(value = "removeImg")
    public void removeImg() {
        //
        initData();
        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "操作失败");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String ids = request.getParameter("ids");
            String typeRemoveImg = request.getParameter("typeRemoveImg");
            String type = request.getParameter("type");
            if ("1".equals(type)) {
                //指定
                TbImgUploadNameRecord imgUploadNameRecord = findInfoDao.loadTbImgUploadNameRecord(typeRemoveImg);
                if (null != imgUploadNameRecord) {
                    String tableName = checkImgDB(imgUploadNameRecord.getImgPath());
                    if ("".equals(tableName)) {
                        //说明图片地址已失效(逻辑删除,移动备份)
                        boolean flag = ImageUtil.getInstance().removePathImg(imgUploadNameRecord.getImgPath(), request);
                        if (flag) {
                            saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 2);
                        }
                    } else if (null != tableName) {
                        //说明图片地址正在使用
                        saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 1, tableName);
                    }
                }
            } else {
                String[] idsArr = ids.split(",");
                for (int i = 0; i < idsArr.length; i++) {
                    if (StringUtils.isNotEmpty(idsArr[i])) {
                        TbImgUploadNameRecord imgUploadNameRecord = findInfoDao.loadTbImgUploadNameRecord(Integer.parseInt(idsArr[i]));
                        if (null != imgUploadNameRecord) {
                            String tableName = checkImgDB(imgUploadNameRecord.getImgPath());
                            if ("".equals(tableName)) {
                                //说明图片地址已失效(逻辑删除,移动备份)
                                boolean flag = ImageUtil.getInstance().removePathImg(imgUploadNameRecord.getImgPath(), request);
                                if (flag) {
                                    saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 2);
                                }
                            } else if (null != tableName) {
                                //说明图片地址正在使用
                                saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 1, tableName);
                            }
                        }
                    }
                }
            }
            out.put("flag", 1);
            out.put("msg", "操作成功,如果图片正在使用,则无法移除");
            writer.print(out);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            out.put("msg", "操作异常");
            writer.print(out);
            writer.flush();
            writer.close();
            e.printStackTrace();
        }

    }

    /**
     * 删除图片
     */
    @Action(value = "delImg")
    public void delImg() {
        //
        initData();
        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "操作失败");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String ids = request.getParameter("ids");
            String typeDeleImg = request.getParameter("typeDeleImg");
            String type = request.getParameter("type");
            if ("1".equals(type)) {
                //指定
                TbImgUploadNameRecord imgUploadNameRecord = findInfoDao.loadTbImgUploadNameRecord(typeDeleImg);
                if (null != imgUploadNameRecord) {
                    String tableName = checkImgDB(imgUploadNameRecord.getImgPath());
                    if ("".equals(tableName)) {
                        //说明图片地址已失效(物理删除)
                        boolean flag = ImageUtil.getInstance().deletePathImg(imgUploadNameRecord.getImgPath(), request);
                        if (flag) {
                            saveInfoDao.delTbImgUploadNameRecord(imgUploadNameRecord.getId());
                        }
                    } else if (null != tableName) {
                        //说明图片地址正在使用
                        saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 1, tableName);
                    }
                }
            } else {
                String[] idsArr = ids.split(",");
                for (int i = 0; i < idsArr.length; i++) {
                    if (StringUtils.isNotEmpty(idsArr[i])) {
                        TbImgUploadNameRecord imgUploadNameRecord = findInfoDao.loadTbImgUploadNameRecord(Integer.parseInt(idsArr[i]));
                        if (null != imgUploadNameRecord) {
                            String tableName = checkImgDB(imgUploadNameRecord.getImgPath());
                            if ("".equals(tableName)) {
                                //说明图片地址已失效(物理删除)
                                boolean flag = ImageUtil.getInstance().deletePathImg(imgUploadNameRecord.getImgPath(), request);
                                if (flag) {
                                    saveInfoDao.delTbImgUploadNameRecord(imgUploadNameRecord.getId());
                                }
                            } else if (null != tableName) {
                                //说明图片地址正在使用
                                saveInfoDao.updateTbImgUploadNameRecord(imgUploadNameRecord.getId(), 1, tableName);
                            }
                        }

                    }
                }
            }
            out.put("flag", 1);
            out.put("msg", "操作成功,如果图片正在使用,则无法删除");
            writer.print(out);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            out.put("msg", "操作异常");
            writer.print(out);
            writer.flush();
            writer.close();
            e.printStackTrace();
        }

    }

    private String checkImgDB(String ImgPath) {
        try {
            String tableName = "";
            TbActivity activity = findInfoDao.loadTbActivityByImgPath(ImgPath);
            if (null != activity) {
                return "tb_activity";
            }

            TbBannel bannel = findInfoDao.loadTbBannelByImgPath(ImgPath);
            if (null != bannel) {
                return "tb_bannel";
            }
            TbCategory category = findInfoDao.loadTbCategoryByImgPath(ImgPath);
            if (null != category) {
                return "tb_category";
            }
            TbCatogryLevel1 catogryLevel1 = findInfoDao.loadTbCatogryLevel1ByImgPath(ImgPath);
            if (null != catogryLevel1) {
                return "tb_catogry_level1";
            }
            TbComment comment = findInfoDao.loadTbCommentByImgPath(ImgPath);
            if (null != comment) {
                return "tb_comment";
            }
            TbGoods goods = findInfoDao.loadTbGoodsImgPath(ImgPath);
            if (null != goods) {
                return "tb_goods";
            }
            TbGoodsSku goodsSku = findInfoDao.loadTbGoodsSkuImgPath(ImgPath);
            if (null != goodsSku) {
                return "tb_goods_sku";
            }
            TbImg img = findInfoDao.loadTbImgImgPath(ImgPath);
            if (null != img) {
                return "tb_img";
            }
            TbItemprops tbItemprops = findInfoDao.loadTbItempropsImgPath(ImgPath);
            if (null != tbItemprops) {
                return "tb_itemprops";
            }
            TbMainMenu mainMenu = findInfoDao.loadTbMainMenuImgPath(ImgPath);
            if (null != mainMenu) {
                return "tb_main_menu";
            }
            TbOrderCart orderCart = findInfoDao.loadTbOrderCartImgPath(ImgPath);
            if (null != orderCart) {
                return "tb_order_cart";
            }
            TbOrderDetail orderDetail = findInfoDao.loadTbOrderDetailImgPath(ImgPath);
            if (null != orderDetail) {
                return "tb_order_detail";
            }
            TbPartners partners = findInfoDao.loadTbPartnersImgPath(ImgPath);
            if (null != partners) {
                return "tb_partners";
            }
            TbShop shop = findInfoDao.loadTbShopImgPath(ImgPath);
            if (null != shop) {
                return "tb_shop";
            }
            TbTicket ticket = findInfoDao.loadTbTicketImgPath(ImgPath);
            if (null != ticket) {
                return "tb_ticket";
            }
            TbTicketDownQr tbTicketDownQr = findInfoDao.loadTbTicketDownQrImgPath(ImgPath);
            if (null != tbTicketDownQr) {
                return "tb_ticket_down_qr";
            }
            TbUserInfo userInfo = findInfoDao.loadTbUserInfoImgPath(ImgPath);
            if (null != userInfo) {
                return "tb_user_info";
            }
            return tableName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImgPathName() {
        return imgPathName;
    }

    public void setImgPathName(String imgPathName) {
        this.imgPathName = imgPathName;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public static void main(String[] args) {

    }
}
