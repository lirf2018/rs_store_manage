package com.yufan.taobao.action;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.yufan.common.action.LotFilterAction;
import com.yufan.taobao.OperationModel;
import com.yufan.taobao.bean.ModelBean;
import com.yufan.taobao.dao.FindInfoDao;
import com.yufan.taobao.dao.SaveInfoDao;
import com.yufan.util.ConfigProperty;
import com.yufan.util.ImageUtil;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 taobao页面管理
 * @作者 lirongfan
 * @时间 2015年11月30日 下午2:34:26
 */
@Scope("prototype")
@Controller("modelAction")
@Namespace("/taobao")
@ParentPackage("common")
public class ModelAction extends LotFilterAction {

    private FindInfoDao findInfoDao_taobao;
    private SaveInfoDao saveInfoDao_taobao;

    private String id;// 标识
    private String state;//
    private String handleType;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private ModelBean bean = new ModelBean();

    private String photoPath;
    private File file;

    private String power = "0";//查看权限 0没有权限1有权限
    private String fromPage;

    /**
     * 查询条件
     */
    private String name;

    private String toModelPage;

    private String picCount;


    /**
     * 加载商品模板列表
     */
    @Action(value = "loadTaobaoList")
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

            bean.setPageName(name);
            if (null != picCount && !"".equals(picCount) && !"null".equals(picCount)) {
                bean.setImgNum(Integer.parseInt(picCount));
            }

            page = findInfoDao_taobao.loadTaobaoModelList(bean, page);
            if (null == id || "".equals(id) || "null".equals(id)) {
                handdate();
            }
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action(value = "getTaobaoPower")
    public void getTaobaoPower() {
        List<Map<String, Object>> list = findInfoDao_taobao.getTaoBaoPower();
        if (null != list && list.size() > 0) {
            power = String.valueOf(list.get(0).get("param_value"));
        }
    }

    /**
     * 数据图片处理
     */
    public void handdate() {
        initData();
        String fwPath = new ConfigProperty().getConfigValue("fw_path");
        photoPath = fwPath + "image/nophone.jpg";//默认图片访问地址
        List<Object> obj_list = new ArrayList<Object>();

        String path = request.getSession().getServletContext().getRealPath("/");//项目路径
        String img_path = path + "\\temporaryImgs\\taobaoimgs\\";//图片保存地址
        //删除临时图片
        ImageUtil.getInstance().delAllFile(img_path);
        for (int i = 0; i < page.getList().size(); i++) {
            Object[] obj = (Object[]) page.getList().get(i);
            List<Object> slist = new ArrayList<Object>();
            for (int j = 0; j < obj.length; j++) {
                if (j == 4) {//图片位置
                    if (null == obj[j]) {
                        slist.add(photoPath);
                    } else {
                        String img_nameString = String.valueOf(UUID.randomUUID()) + ".jpg";//图片名称
                        if (returnImgName((byte[]) obj[j], img_path, img_nameString, img_path)) {//生成图片
                            String imgpath = fwPath + "temporaryImgs/taobaoimgs/" + img_nameString;//图片查看地址
                            slist.add(imgpath);
                        }
                    }
                } else {
                    slist.add(obj[j]);
                }
            }
            obj_list.add(slist);
        }
        page.setList(obj_list);

    }

    /**
     * 生成本地图片
     *
     * @param imgs           图片二进制
     * @param img_path       图片保存地址
     * @param img_nameString 图片名称
     * @param path           项目路径
     * @return
     */
    public boolean returnImgName(byte[] imgs, String img_path, String img_nameString, String path) {
        try {
            path = path.replace("/", "\\");
            ImageUtil.getInstance().getFile(imgs, img_path, img_nameString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 逻辑删除
     */
    @Action(value = "delModel")
    public void delModel() {
        initData();
        try {
            boolean flag = saveInfoDao_taobao.delStatus(id, state);
            if (flag && "0".equals(state)) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else if (flag && "1".equals(state)) {
                response.getWriter().write(write_Json_result("ok", "启用成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增和修改
     */
    @Action(value = "saveOrUpdate")
    public void saveOrUpdate() {
        initData();
        try {
            boolean flag = false;
            bean.setState(1);
            if ("add".equals(handleType)) {//新增
                if (null != file) {
//					if(file.length()>307200){//300kb
//						response.getWriter().write(write_Json_result("false", "图片大小不能大于300kb"));
//						return;
//					}
                    Blob blob = new SerialBlob(ImageUtil.getInstance().getBytes(file));
                    bean.setImg(blob);
                }
                bean.setCreateTime(new Date());
                bean.setBContents(returnRepalceStr(bean.getBContents()));
                flag = saveInfoDao_taobao.saveOrUpdate(bean);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {
                ModelBean bean_ = findInfoDao_taobao.getModelBeanById(Integer.parseInt(id));

                if (null != file) {
//					if(file.length()>307200){//300kb
//						response.getWriter().write(write_Json_result("false", "图片大小不能大于300kb"));
//						return;
//					}
                    Blob blob = new SerialBlob(ImageUtil.getInstance().getBytes(file));
                    bean.setImg(blob);
                    bean_.setImg(blob);
                } else {
                    bean.setImg(bean_.getImg());
                }

                if ("sub".equals(fromPage)) {//来自配置页面
                    bean.setYuanMa(bean_.getYuanMa());
                    bean.setBContents(bean_.getBContents());
                    bean.setCreateTime(bean_.getCreateTime());
                    bean = OperationModel.handdle(bean);
                } else {

                    bean_.setPageName(bean.getPageName());
                    bean_.setImgNum(bean.getImgNum());
                    bean_.setSort(bean.getSort());
                    bean_.setToPage(bean.getToPage());
                    bean_.setBContents(bean.getBContents());
                    bean_.setRemark(bean.getRemark());
                    bean = bean_;
                }

                bean.setBContents(returnRepalceStr(bean.getBContents()));
                flag = saveInfoDao_taobao.saveOrUpdate(bean);
                if (flag) {
                    if ("sub".equals(fromPage)) {//来自配置页面
                        response.getWriter().write(write_Json_result("ok", "配置成功"));
                    } else {
                        response.getWriter().write(write_Json_result("ok", "修改成功"));
                    }
                } else {
                    if ("sub".equals(fromPage)) {
                        response.getWriter().write(write_Json_result("false", "配置失败"));
                    } else {
                        response.getWriter().write(write_Json_result("false", "修改失败"));
                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 特殊字符替换
     *
     * @return
     */
    public String returnRepalceStr(String str) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("state", "1");
        map.put("param_code", "html_code");
        List<Map<String, Object>> list = findInfoDao_taobao.getSysParam(map);
        if (null != list && list.size() > 0 && null != str && !"".equals(str)) {
            for (int i = 0; i < list.size(); i++) {
                String key = String.valueOf("&" + list.get(i).get("param_key") + ";");
                String value = String.valueOf(list.get(i).get("param_value"));
                str = str.replace(key, value);
            }
        }
        return str;
    }

    /**
     * 增加或者修改公共页面
     *
     * @return
     */
    @Action(value = "toAddOrUpdatePage", results = {
            @Result(name = "success", location = "/jsp/taobao/addModel.jsp")})
    public String toAddOrUpdatePage() {
        initData();
        try {
            String fwPath = new ConfigProperty().getConfigValue("fw_path");
            photoPath = fwPath + "image/nophone.jpg";//默认图片访问地址
            if ("add".equals(handleType)) {

            } else {
                String path = request.getSession().getServletContext().getRealPath("/");
                String img_nameString = String.valueOf(UUID.randomUUID()) + ".jpg";//图片名称
                String img_path = path + "\\temporaryImg\\";//图片保存地址
                ImageUtil.getInstance().delAllFile(img_path);

                bean = findInfoDao_taobao.getModelBeanById(Integer.parseInt(id));

                if (null != bean.getImg() && !"".equals(bean.getImg())) {
                    path = path.replace("/", "\\");
                    ImageUtil.getInstance().getFile(bean.getImg().getBinaryStream(), img_path, img_nameString);
                    photoPath = fwPath + "temporaryImg/" + img_nameString;//图片查看地址
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /*************************************************************/
    /**
     * 跳转个模板配置页面
     *
     * @return
     */
//	@Action(value="toModel001Page",results={
//			 @Result(name="success",location="/jsp/taobao/sub/model001.jsp")})
    @Action(value = "toModelSubPage")
    public String toModel001Page() {
        initData();
        try {
            String fwPath = new ConfigProperty().getConfigValue("fw_path");
            photoPath = fwPath + "image/nophone.jpg";//默认图片访问地址

            String path = request.getSession().getServletContext().getRealPath("/");
            String img_nameString = String.valueOf(UUID.randomUUID()) + ".jpg";//图片名称
            String img_path = path + "\\temporaryImg\\";//图片保存地址
            ImageUtil.getInstance().delAllFile(img_path);

            bean = findInfoDao_taobao.getModelBeanById(Integer.parseInt(id));

            if (null != bean.getImg() && !"".equals(bean.getImg())) {
                path = path.replace("/", "\\");
                ImageUtil.getInstance().getFile(bean.getImg().getBinaryStream(), img_path, img_nameString);
                photoPath = fwPath + "temporaryImg/" + img_nameString;//图片查看地址
            }
            String page_url = "../jsp/taobao/sub/" + toModelPage + ".jsp";
            request.getRequestDispatcher(page_url).forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*************************************************************/


    public FindInfoDao getFindInfoDao_taobao() {
        return findInfoDao_taobao;
    }

    public void setFindInfoDao_taobao(FindInfoDao findInfoDao_taobao) {
        this.findInfoDao_taobao = findInfoDao_taobao;
    }

    public SaveInfoDao getSaveInfoDao_taobao() {
        return saveInfoDao_taobao;
    }

    public void setSaveInfoDao_taobao(SaveInfoDao saveInfoDao_taobao) {
        this.saveInfoDao_taobao = saveInfoDao_taobao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public ModelBean getBean() {
        return bean;
    }

    public void setBean(ModelBean bean) {
        this.bean = bean;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPicCount() {
        return picCount;
    }

    public void setPicCount(String picCount) {
        this.picCount = picCount;
    }

    public File getFile() {
        return file;
    }

    public String getToModelPage() {
        return toModelPage;
    }

    public void setToModelPage(String toModelPage) {
        this.toModelPage = toModelPage;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();//源码
        list1.add(");<div style='height:400px;' class='QdotCode'>");
        list1.add(");	<div class='footer-more-trigger most-footer Qdotpst0trYV4' style='border:none 0;padding:0;background-color:transparent;width:1920px;height:400px;border:none;padding:0;top:auto;left:auto;'>");
        list1.add(");		<div class='footer-more-trigger most-footer Qdotpst0trYV4' style='border:none 0;padding:0;background-color:transparent;width:1920px;height:400px;left:-485px;top:0px;border:none;padding:0;overflow:hidden;'>");
        list1.add(");			<div class='Qdotlayer0trYV4' style='height:400px;width:1920px;overflow:hidden;'>");
        list1.add(");				<div class='Qdotwrap0trYV4 J_TWidget' data-widget-type='Carousel' data-widget-config='{&quot;effect&quot;: &quot;scrollx&quot;,&quot;easing&quot;: &quot;easeBoth&quot;,&quot;circular&quot;:true,&quot;interval&quot;:&quot;3&quot;,&quot;duration&quot;:0.5,&quot;autoplay&quot;:true,&quot;contentCls&quot;: &quot;Qdotcontent0trYV4&quot;,&quot;prevBtnCls&quot;:&quot;Qdotprev0trYV4&quot;,&quot;nextBtnCls&quot;:&quot;Qdotnext0trYV4&quot;}' style='height:400px;width:1920px;overflow:hidden;position:relative;'>");
        list1.add(");					<div class='most-footer footer-more-trigger' style='width:1920px;height:60px;border:none 0;padding:0;background-color:transparent;z-index:80;left:auto;top:430px;'>");
        list1.add(");						&nbsp;");
        list1.add(");					</div>");
        list1.add(");					<ul class='Qdotcontent0trYV4 clearfix' style='top:0px;z-index:9;width:999999px;'>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&amp;id=521909586291&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i1/200258047/TB2IN75eVXXXXcpXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&amp;id=521908173127&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i2/200258047/TB25oQ0eVXXXXXWXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&amp;id=521889516123&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i2/200258047/TB2IU.9eVXXXXbBXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&amp;id=521889292267&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i1/200258047/TB2ij.WeVXXXXaOXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&amp;id=521887514224&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i4/200258047/TB2OEE3eVXXXXXbXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list1.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.oQ9opz&amp;id=521870491225&amp;scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i3/200258047/TB2.P78eVXXXXbCXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list1.add(");						</li>");
        list1.add(");					</ul>");
        list1.add(");					<div class='most-footer footer-more-trigger' style='width:950px;height:60px;z-index:90;top:220px;overflow:hidden;clear:both;border:none 0;background:none;left:485px;'>");
        list1.add(");						 <span class='Qdotprev0trYV4 Qdotpst0trYV4' style='width:auto;border:none 0;padding:0;background-color:transparent;float:left;cursor:pointer;'> <img data-ks-lazyload='//gdp.alicdn.com/imgextra/i4/200258047/TB2Nq7leVXXXXXdXXXXXXXXXXXX_!!200258047.png' src='//gdp.alicdn.com/imgextra/i4/200258047/TB2Nq7leVXXXXXdXXXXXXXXXXXX_!!200258047.png' width='60' height='60' /></span> <span class='Qdotnext0trYV4 Qdotpst0trYV4' style='width:auto;border:none 0;padding:0;background-color:transparent;float:right;cursor:pointer;'> <img data-ks-lazyload='//gdp.alicdn.com/imgextra/i3/200258047/TB2VKj.eVXXXXb3XXXXXXXXXXXX_!!200258047.png' src='//gdp.alicdn.com/imgextra/i3/200258047/TB2VKj.eVXXXXb3XXXXXXXXXXXX_!!200258047.png' width='60' height='60' /></span>");
        list1.add(");					</div>");
        list1.add(");				</div>");
        list1.add(");			</div>");
        list1.add(");		</div>");
        list1.add(");	</div>");
        list1.add(");</div>");

        List<String> list2 = new ArrayList<String>();
        list2.add(");<div style='height:400px;' class='QdotCode'>");
        list2.add(");	<div class='footer-more-trigger most-footer Qdotpst0trYV4' style='border:none 0;padding:0;background-color:transparent;width:1920px;height:400px;border:none;padding:0;top:auto;left:auto;'>");
        list2.add(");		<div class='footer-more-trigger most-footer Qdotpst0trYV4' style='border:none 0;padding:0;background-color:transparent;width:1920px;height:400px;left:-485px;top:0px;border:none;padding:0;overflow:hidden;'>");
        list2.add(");			<div class='Qdotlayer0trYV4' style='height:400px;width:1920px;overflow:hidden;'>");
        list2.add(");				<div class='Qdotwrap0trYV4 J_TWidget' data-widget-type='Carousel' data-widget-config='{'effect': 'scrollx','easing': 'easeBoth','circular':true,'interval':'3','duration':0.5,'autoplay':true,'contentCls': 'Qdotcontent0trYV4','prevBtnCls':'Qdotprev0trYV4','nextBtnCls':'Qdotnext0trYV4'}' style='height:400px;width:1920px;overflow:hidden;position:relative;'>");
        list2.add(");					<div class='most-footer footer-more-trigger' style='width:1920px;height:60px;border:none 0;padding:0;background-color:transparent;z-index:80;left:auto;top:430px;'>");
        list2.add(");						&nbsp;");
        list2.add(");					</div>");
        list2.add(");					<ul class='Qdotcontent0trYV4 clearfix' style='top:0px;z-index:9;width:999999px;'>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&id=521909586291&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i1/200258047/TB2IN75eVXXXXcpXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&id=521908173127&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i2/200258047/TB25oQ0eVXXXXXWXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&id=521889516123&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i2/200258047/TB2IU.9eVXXXXbBXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&id=521889292267&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i1/200258047/TB2ij.WeVXXXXaOXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.JM0GgO&id=521887514224&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i4/200258047/TB2OEE3eVXXXXXbXpXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");						<li style='list-style-type:none;margin:0px;padding:0px;width:1920px;height:400px;float:left;'>");
        list2.add(");							 <a target='_blank' href='//item.taobao.com/item.htm?spm=0.0.0.0.oQ9opz&id=521870491225&scene=taobao_shop' style='padding:0px;margin:0px;'> <img border='0' src='//gdp.alicdn.com/imgextra/i3/200258047/TB2.P78eVXXXXbCXXXXXXXXXXXX_!!200258047.jpg' height='400' width='1920' /></a>");
        list2.add(");						</li>");
        list2.add(");					</ul>");
        list2.add(");					<div class='most-footer footer-more-trigger' style='width:950px;height:60px;z-index:90;top:220px;overflow:hidden;clear:both;border:none 0;background:none;left:485px;'>");
        list2.add(");						 <span class='Qdotprev0trYV4 Qdotpst0trYV4' style='width:auto;border:none 0;padding:0;background-color:transparent;float:left;cursor:pointer;'> <img data-ks-lazyload='//gdp.alicdn.com/imgextra/i4/200258047/TB2Nq7leVXXXXXdXXXXXXXXXXXX_!!200258047.png' src='//gdp.alicdn.com/imgextra/i4/200258047/TB2Nq7leVXXXXXdXXXXXXXXXXXX_!!200258047.png' width='60' height='60' /></span> <span class='Qdotnext0trYV4 Qdotpst0trYV4' style='width:auto;border:none 0;padding:0;background-color:transparent;float:right;cursor:pointer;'> <img data-ks-lazyload='//gdp.alicdn.com/imgextra/i3/200258047/TB2VKj.eVXXXXb3XXXXXXXXXXXX_!!200258047.png' src='//gdp.alicdn.com/imgextra/i3/200258047/TB2VKj.eVXXXXb3XXXXXXXXXXXX_!!200258047.png' width='60' height='60' /></span>");
        list2.add(");					</div>");
        list2.add(");				</div>");
        list2.add(");			</div>");
        list2.add(");		</div>");
        list2.add(");	</div>");
        list2.add(");</div>");


        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).equals(list2.get(i))) {
//						System.out.println("------------>ok");
            } else {
                System.out.println(i + "------------>" + list1.get(i));
                System.out.println(i + "------------>" + list2.get(i));
                break;
            }
        }

    }
}
