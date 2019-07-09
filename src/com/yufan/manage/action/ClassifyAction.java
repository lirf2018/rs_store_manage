package com.yufan.manage.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yufan.util.ImageUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.ImageAction;
import com.yufan.common.action.LotFilterAction;
import com.yufan.core.dao.FindInfoDao;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbCatogryLevel1;
import com.yufan.pojo.TbClassyfyCatogryRel;
import com.yufan.pojo.TbItemprops;
import com.yufan.pojo.TbPropsValue;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;
import com.yufan.vo.CatogryLeve1Condition;
import com.yufan.vo.ClassfyCondition;

/**
 * 功能名称: 分类表
 * 开发人: lirf
 * 开发时间: 2016下午1:49:33
 * 其它说明：
 */
@Scope("prototype")
@Service("classifyAction")
@Namespace("/manage")
@ParentPackage("common")
public class ClassifyAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    //分类
    private String category_id;
    private TbCategory category = new TbCategory();
    private List<Map<String, Object>> list_category;
    //分类属性表
    private String itemprops_id;
    private TbItemprops itemprops = new TbItemprops();
    private List<Map<String, Object>> list_itemprops;
    //分类属性值表
    private String propsValue_id;
    private TbPropsValue propsValue = new TbPropsValue();
    private List<Map<String, Object>> list_propsValue;
    //一级分类
    private List<Map<String, Object>> listClassfyCatogry;
    private TbCatogryLevel1 caleve1 = new TbCatogryLevel1();
    private String leveId;
    private String leveName;
    //为action中跳转过来
    private String click = "1";
    //点击对象
    private String clickObj = "category";//itemprops,propsValue

    private String status;//状态

    private String photoPath;

    private String haddle = "update";//add增加  update

    private File file;

    private String from_page;

    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    //查询条件
    private String itemprops_name;
    private String itemprops_code;
    private String value_name;

    /**
     * 查询类目列表
     */
    @Action(value = "loadlistCategory")
    public void loadlistCategory() {
        try {
            initData();
            if (null == leveId || "".equals(leveId.trim())) {
                leveId = "0";
            }
            Integer searchStatus = null;
            if (null != status && !"".equals(status)) {
                searchStatus = Integer.parseInt(status);
            }
            list_category = findInfoDao.listTbCategoryMap(searchStatus, Integer.parseInt(leveId));
            response.getWriter().write(write_Json_result("msg", list_category));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到类目管理列表(点击具体类目时跳转的页面)
     */
    @Action(value = "toLoadlistCategoryPage",
            results = {@Result(name = "success", location = "/jsp/manage/list_category.jsp")})
    public String toLoadlistCategoryPage() {
        setPages();
        //查询类目列表
//		List<Map<String, Object>> list_category_ = new ArrayList<Map<String, Object>>();
        if (null == leveId || "".equals(leveId.trim())) {
            leveId = "0";
        }
        list_category = findInfoDao.listTbCategoryMap(category.getStatus(), Integer.parseInt(leveId));
//		list_category = new ArrayList<Map<String,Object>>(); 
//		for (int i = 0; i < list_category_.size(); i++) {
//			Map<String, Object> map = list_category_.get(i);
//			map.put("index", i);
//			list_category.add(map);
//		}

        //查询类目属性列表
        itemprops.setCategoryId(Integer.parseInt(category_id));
        list_itemprops = findInfoDao.listTbItempropsMap(itemprops);

        category = findInfoDao.loadTbCategoryInfo(Integer.parseInt(category_id));
        photoPath = RsConstants.phone_url + "null.jpg";
        if (null != category.getCategoryImg() && !"".equals(category.getCategoryImg())) {
            photoPath = RsConstants.phone_url + category.getCategoryImg();
        }

        return "success";
    }

    /**
     * 删除或者启用类目
     */
    @Action(value = "delorupdatecategory")
    public void delorupdatecategory() {
        initData();
        try {
            boolean flag = saveInfoDao.delorupdatecategory(category_id, status);
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(status)) {
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
     * 删除或者启用类目属性
     */
    @Action(value = "delorupdateitemprops")
    public void delorupdateitemprops() {
        initData();
        try {
            boolean flag = saveInfoDao.delorupdateitemprops(itemprops_id, status);
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(status)) {
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
     * 跳转到新增类目页面(点击新增类目时跳转的页面)
     */
    @Action(value = "toAddOrUpdatecategoryPage",
            results = {@Result(name = "success", location = "/jsp/manage/list_category.jsp")})
    public String toAddOrUpdatecategoryPage() {
        // 查询类目列表
        List<Map<String, Object>> list_category_ = new ArrayList<Map<String, Object>>();
        if (null == leveId || "".equals(leveId.trim())) {
            leveId = "0";
        }
        list_category_ = findInfoDao.listTbCategoryMap(category.getStatus(), Integer.parseInt(leveId));
        list_category = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list_category_.size(); i++) {
            Map<String, Object> map = list_category_.get(i);
            map.put("index", i);
            list_category.add(map);
        }
        photoPath = RsConstants.phone_url + "null.jpg";
        // 查询类目属性列表
        if ("add".equals(haddle)) {
            if (!"undefined".equals(category_id)) {
                itemprops.setCategoryId(Integer.parseInt(category_id));
                list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
            } else {
                itemprops.setCategoryId(0);
                list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
                category.setCategoryId(0);
                category_id = "0";
            }
        } else {
            itemprops.setCategoryId(Integer.parseInt(category_id));
            list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
        }
        category.setShort_(0);
        return "success";
    }

    /**
     * 新增或者修改类目
     */
    @Action(value = "addorupdatecategory")
    public void addorupdatecategory() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            category.setParentId(0);
            category.setIsParent(1);
            if (null == category.getShort_()) {
                category.setShort_(0);
            }
            if (null != category.getCategoryCode()) {
                category.setCategoryCode(category.getCategoryCode().trim());
            }
            boolean flag = false;
            if ("add".equals(haddle)) {
                //查询类目编码是否存在
                flag = findInfoDao.checkClassifyCodeIsExsit(category.getCategoryCode(), null);
                if (flag) {
                    response.getWriter().write(write_Json_result("false", "类目编码已存在"));
                    return;
                }
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        category.setCategoryImg(imgpath);
                    }
                }
                category.setCategoryId(null);
                category.setCreateman(user.getLoginName());
                category.setCreatetime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(category);
                String leveIdRel = request.getParameter("leveId_rel");
                if (null != leveIdRel && !"".equals(leveIdRel.trim()) && !"null".equals(leveIdRel.trim())) {
                    //（一个类目只能对应一个分类）如果分类不为空,则默认增加分类类目关系saveClassfyCatogryrel
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(category.getCategoryId() + "", leveIdRel);
                    saveClassfyCatogryrel(map, Integer.parseInt(leveIdRel));
                }
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(
                            write_Json_result("false", "增加失败"));
                }
            } else {
                //查询类目编码是否存在
                flag = findInfoDao.checkClassifyCodeIsExsit(category.getCategoryCode(), category.getCategoryId());
                if (flag) {
                    response.getWriter().write(write_Json_result("false", "类目编码已存在"));
                    return;
                }
                TbCategory category_ = findInfoDao.loadTbCategoryInfo(category.getCategoryId());
                category.setCreateman(category_.getCreateman());
                category.setCreatetime(category_.getCreatetime());
                category.setLastalterman(user.getLoginName());
                category.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        category.setCategoryImg(imgpath);
                        ImageUtil.getInstance().removePathImg(category_.getCategoryImg(), request);
                    }
                } else {
                    category.setCategoryImg(category_.getCategoryImg());
                }
                flag = saveInfoDao.saveOrUpdateObj(category);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到新增类目属性页面(点击新增类目属性)
     *
     * @return
     */
    @Action(value = "toitempropsPage",
            results = {@Result(name = "success", location = "/jsp/manage/list_category.jsp")})
    public String toitempropsPage() {
        //查询类目列表
        List<Map<String, Object>> list_category_ = new ArrayList<Map<String, Object>>();
        if (null == leveId || "".equals(leveId.trim())) {
            leveId = "0";
        }
        list_category_ = findInfoDao.listTbCategoryMap(category.getStatus(), Integer.parseInt(leveId));
        list_category = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list_category_.size(); i++) {
            Map<String, Object> map = list_category_.get(i);
            map.put("index", i);
            list_category.add(map);
        }
        //查询类目属性列表
        itemprops.setCategoryId(Integer.parseInt(category_id));
        list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
        itemprops.setSort(0);

        category = findInfoDao.loadTbCategoryInfo(Integer.parseInt(category_id));

        return "success";
    }

    /**
     * 新增或者修改类目属性
     */
    @Action(value = "addorupdateitemprops")
    public void addorupdateitemprops() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (null != itemprops.getPropCode()) {
                itemprops.setPropCode(itemprops.getPropCode().trim());
            }

            //查询属性编码是否存在
            if (findInfoDao.checkPropCodeIsExsit(itemprops.getPropCode(), itemprops.getPropId())) {
                response.getWriter().write(write_Json_result("false", "属性编码已存在"));
                return;
            }

            if (null == itemprops.getSort()) {
                itemprops.setSort(0);
            }
            JSONObject obj = new JSONObject();
            if ("add".equals(haddle)) {
                itemprops.setCreateman(user.getLoginName());
                itemprops.setCreatetime(new Timestamp(System.currentTimeMillis()));
                int itempropsId = saveInfoDao.saveOrUpdateItemprops(itemprops);
                if (itempropsId > 0) {
                    obj.put("categoryId", itemprops.getCategoryId());
                    obj.put("itempropsId", itempropsId);
                    response.getWriter().write(write_Json_result("ok", obj));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {
                String value_id[] = request.getParameterValues("value_id");
                String value_names[] = request.getParameterValues("value_name");
                String oute_ids[] = request.getParameterValues("oute_id");
                String propsValue_values[] = request.getParameterValues("propsValue_value");
                String prop_sort[] = request.getParameterValues("value_sort");

                boolean flag = false;
                if (value_names != null) {
                    for (int i = 0; i < value_names.length; i++) {
                        if ("".equals(value_names[i])) {
                            flag = true;
                            break;
                        }
                    }
                    for (int i = 0; i < propsValue_values.length; i++) {
                        if ("".equals(propsValue_values[i])) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        response.getWriter().write(write_Json_result("false", "值名称和属性值不能为空!"));
                        return;
                    }

                    flag = false;
                }

                TbItemprops itemprops_ = findInfoDao.loadTbItempropsInfo(itemprops.getPropId());
                itemprops.setCreateman(itemprops_.getCreateman());
                itemprops.setCreatetime(itemprops_.getCreatetime());
                itemprops.setLastalternan(user.getLoginName());
                itemprops.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(itemprops);
                if (flag) {
                    obj.put("categoryId", itemprops.getCategoryId());
                    obj.put("itempropsId", itemprops.getPropId());
                    //保存类目属性值
                    if ("fromcategory".equals(from_page)) {//来自页面
                        savePropsValue(value_id, value_names, oute_ids, propsValue_values, prop_sort);
                    }
                    response.getWriter().write(write_Json_result("ok", obj));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存属性值
     */
    public void savePropsValue(String value_id[], String value_names[], String oute_ids[], String propsValue_values[], String prop_sort[]) {
        try {
            //删除属性值status=0 //删除移除的属性值
            String valueIds = "";
            for (int i = 0; i < value_id.length; i++) {
                if (i == value_id.length - 1) {
                    if (!"0".equals(value_id[i])) {
                        valueIds = valueIds + value_id[i];
                    }
                } else {
                    if (!"0".equals(value_id[i])) {
                        valueIds = valueIds + value_id[i] + ",";
                    }
                }
            }
            saveInfoDao.delPropsValueByValueIdsStatus(valueIds, itemprops.getPropId());
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (null == propsValue_values || propsValue_values.length == 0) {
                return;
            }
            //保存当前的属性值
            for (int i = 0; i < propsValue_values.length; i++) {
                propsValue = new TbPropsValue();
                if ("0".equals(value_id[i])) {
                    propsValue.setValueId(null);
                } else {
                    propsValue.setValueId(Integer.parseInt(value_id[i]));
                }
                propsValue.setPropId(itemprops.getPropId());
                propsValue.setValueName(value_names[i]);
                propsValue.setCategoryId(Integer.parseInt(category_id));
                propsValue.setOuteId(oute_ids[i]);
                propsValue.setValue(propsValue_values[i]);
                propsValue.setShort_(Integer.parseInt(prop_sort[i]));
                propsValue.setCreateman(user.getLoginName());
                propsValue.setCreatetime(new Timestamp(System.currentTimeMillis() + i));
                propsValue.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                propsValue.setLastalterman(user.getLoginName());
                propsValue.setStatus(1);
                saveInfoDao.saveOrUpdateObj(propsValue);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击类目属性跳转的页面
     *
     * @return
     */
    @Action(value = "clickitempropspage",
            results = {@Result(name = "success", location = "/jsp/manage/list_category.jsp")})
    public String clickitempropspage() {
        setPages();
        // 查询类目列表
        List<Map<String, Object>> list_category_ = new ArrayList<Map<String, Object>>();
        if (null == leveId || "".equals(leveId.trim())) {
            leveId = "0";
        }
        list_category_ = findInfoDao.listTbCategoryMap(category.getStatus(), Integer.parseInt(leveId));
        list_category = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list_category_.size(); i++) {
            Map<String, Object> map = list_category_.get(i);
            map.put("index", i);
            list_category.add(map);
        }
        // 查询类目属性列表
        itemprops.setCategoryId(Integer.parseInt(category_id));
        list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
        itemprops = findInfoDao.loadTbItempropsInfo(Integer.parseInt(itemprops_id));
        category = findInfoDao.loadTbCategoryInfo(Integer.parseInt(category_id));

        //加载类目属性值列表
        propsValue = new TbPropsValue();
        propsValue.setPropId(Integer.parseInt(itemprops_id));
        list_propsValue = findInfoDao.listTbPropsValueMap(propsValue);

        return "success";
    }

    //================================================================================

    /**
     * 查询类目属性列表
     */
    @Action(value = "loadItempropsListPage")
    public void loadItempropsListPage() {

        try {
            setPages();
            initData();

            ClassfyCondition con = new ClassfyCondition();

            if (null != category_id && !"".equals(category_id) && !"0".equals(category_id)) {
                con.setCatogryId(Integer.parseInt(category_id));
            }
            if (null != status && !"".equals(status)) {
                con.setStatus(Integer.parseInt(status));
            }
            con.setPropName(itemprops_name);
            con.setPropCode(itemprops_code);

            if (leveId != null && !"0".equals(leveId) && !"".equals(leveId)) {
                con.setLeveId(Integer.parseInt(leveId));
            }

            page = findInfoDao.loadItempropsListPage(page, con);
            response.getWriter().write(write_Json_result("msg", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到类目属性增加或者修改页面
     *
     * @return
     */
    @Action(value = "toAddOrUpdateItempropsPage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-itemprops.jsp")})
    public String toAddOrUpdateItempropsPage() {
        initData();


        //查询一级分类
        listClassfyCatogry = findInfoDao.listClassfyMap(null);

//		category.setStatus(1);
//		List<Map<String, Object>> list_category_ = new ArrayList<Map<String, Object>>();
//		if(null==leveId||"".equals(leveId.trim())){
//			leveId="0";
//		}
//		list_category = findInfoDao.listTbCategoryMap(category.getStatus(),Integer.parseInt(leveId));
//		list_category = new ArrayList<Map<String,Object>>(); 
//		for (int i = 0; i < list_category_.size(); i++) {
//			Map<String, Object> map = list_category_.get(i);
//			map.put("index", i);
//			list_category.add(map);
//		}
        if ("update".equals(haddle)) {
            itemprops = findInfoDao.loadTbItempropsInfo(Integer.parseInt(itemprops_id));

            //查询一级分类
            List<Map<String, Object>> listMap = findInfoDao.listClassfyMap(itemprops.getCategoryId());
            if (null != listMap && listMap.size() > 0) {
                leveId = String.valueOf(listMap.get(0).get("level_id"));
                //查询类目
                list_category = findInfoDao.listTbCategoryMap(1, Integer.parseInt(leveId));
            }

        } else {
            itemprops.setSort(0);
        }

        return "success";
    }


    //================================================================================

    /**
     * 查询类目属性值列表
     */
    @Action(value = "loadPropsValueListPage")
    public void loadPropsValueListPage() {
        setPages();
        try {
            initData();

            ClassfyCondition con = new ClassfyCondition();

            if (null != itemprops_id && !"".equals(itemprops_id) && !"0".equals(itemprops_id)) {
                con.setPropId(Integer.parseInt(itemprops_id));
            }

            if ("0".equals(status) || "1".equals(status)) {
                con.setStatus(Integer.parseInt(status));
            }
            if (null != value_name && !"".equals(value_name)) {
                con.setValueName(value_name);
            }

            if (null != category_id && !"0".equals(category_id) && !"".equals(category_id)) {
                con.setCatogryId(Integer.parseInt(category_id));
            }
            if (null != leveId && !"0".equals(leveId) && !"".equals(leveId)) {
                con.setLeveId(Integer.parseInt(leveId));
            }


            page = findInfoDao.loadPropsValueListPage(page, con);
            response.getWriter().write(write_Json_result("msg", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除类目属性值
     *
     * @return
     */
    @Action(value = "delStatusPropsValue")
    public void delStatusPropsValue() {
        initData();
        try {
            boolean flag = saveInfoDao.delStatusPropsValue(propsValue_id, status);
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(status)) {
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
     * 跳转到增加或者修改或者属性值页面
     */
    @Action(value = "toAddorUpdatePropsValuePage",
            results = {@Result(name = "success", location = "/jsp/manage/addorupdate-propsvalue.jsp")})
    public String toAddorUpdatePropsValuePage() {
//		itemprops.setStatus(1);
//		list_itemprops  = findInfoDao.listTbItempropsMap(itemprops);
        //查询一级分类
        listClassfyCatogry = findInfoDao.listClassfyMap(null);
//		//查询类目列表
//		list_category = findInfoDao.listTbCategoryMap(1, null);

        photoPath = RsConstants.phone_url + "null.jpg";

        if ("update".equals(haddle)) {
            propsValue = findInfoDao.loadTbPropsValueInfo(Integer.parseInt(propsValue_id));
            if (null != propsValue.getValueImg() && !"".equals(propsValue.getValueImg())) {
                photoPath = RsConstants.phone_url + propsValue.getValueImg();
            }

            //查询一级分类
            List<Map<String, Object>> listMap = findInfoDao.listClassfyMap(propsValue.getCategoryId());
            if (null != listMap && listMap.size() > 0) {
                leveId = String.valueOf(listMap.get(0).get("level_id"));
                //查询类目
                list_category = findInfoDao.listTbCategoryMap(1, Integer.parseInt(leveId));
                //判断当前类目是否存在
                boolean flag = false;
                for (int i = 0; i < list_category.size(); i++) {
                    category_id = String.valueOf(list_category.get(i).get("category_id"));
                    if (category_id.equals(propsValue.getCategoryId() + "")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {//当前类目存在--
                    //查询类目属性
                    itemprops = new TbItemprops();
                    itemprops.setCategoryId(propsValue.getCategoryId());
                    itemprops.setStatus(1);
                    list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
                }

            }

        }
        return "success";
    }


    /**
     * 增加或者修改或者属性值页面
     */
    @Action(value = "addorUpdatePropsValue")
    public void addorUpdatePropsValue() {

        try {
            initData();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            boolean flag = false;
            if ("update".equals(haddle)) {
                TbPropsValue pv = findInfoDao.loadTbPropsValueInfo(propsValue.getValueId());
                propsValue.setCreateman(pv.getCreateman());
                propsValue.setCreatetime(pv.getCreatetime());
//				itemprops = findInfoDao.loadTbItempropsInfo(propsValue.getPropId());
//				propsValue.setCategoryId(itemprops.getCategoryId());
                if (null == propsValue.getShort_() || "".equals(propsValue.getShort_())) {
                    propsValue.setShort_(0);
                }

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        propsValue.setValueImg(imgpath);
                    }
                } else {
                    propsValue.setValueImg(pv.getValueImg());
                }

                flag = saveInfoDao.saveOrUpdateObj(propsValue);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "修改成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "修改失败"));
                }
            } else {

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        propsValue.setValueImg(imgpath);
                    }
                }

                if (null == propsValue.getShort_() || "".equals(propsValue.getShort_())) {
                    propsValue.setShort_(0);
                }
//				itemprops = findInfoDao.loadTbItempropsInfo(propsValue.getPropId());
//				propsValue.setCategoryId(itemprops.getCategoryId());
                propsValue.setCreatetime(new Date());
                propsValue.setCreateman(user.getLoginName());
                propsValue.setCreateman(user.getLoginName());
                flag = saveInfoDao.saveOrUpdateObj(propsValue);
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询类目属性列表
     */
    @Action(value = "loadlistItemprops")
    public void loadlistItemprops() {
        try {
            initData();
            if (null != itemprops) {
                if (null != status && !"".equals(status)) {
                    itemprops.setStatus(1);
                }
            }
            if (null != category_id && !"".equals(category_id)) {
                itemprops.setCategoryId(Integer.parseInt(category_id));
            }

            list_itemprops = findInfoDao.listTbItempropsMap(itemprops);
            response.getWriter().write(write_Json_result("msg", list_itemprops));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //----------------------------------------------------一级分类表----------------------------------

    /**
     * 加载一级分类表目录
     */
    @Action(value = "loadCatogryLeve1ListPage")
    public void loadCatogryLeve1ListPage() {
        setPages();
        try {
            initData();
            CatogryLeve1Condition caleve1_ = new CatogryLeve1Condition();
            if (null != leveId && !"".equals(leveId) && !"0".equals(leveId)) {
                caleve1_.setLevelId(Integer.parseInt(leveId));
            }
            if ("0".equals(status) || "1".equals(status)) {
                caleve1_.setStatus(Integer.parseInt(status));
            }
            caleve1_.setLevelName(leveName);
            caleve1_.setLevelCode(request.getParameter("levelCode"));
            caleve1_.setCategoryCode(request.getParameter("categoryCode"));
            if (null != category_id && !"".equals(category_id)) {
                caleve1_.setCategoryId(Integer.parseInt(category_id));
            }

            page = findInfoDao.loadCatogryLeve1ListPage(page, caleve1_);
            response.getWriter().write(write_Json_result("msg", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载一级分类表目录
     */
    @Action(value = "loadCatogryLeve1ListPage2")
    public void loadCatogryLeve1ListPage2() {
        setPages();
        try {
            initData();
            CatogryLeve1Condition caleve1_ = new CatogryLeve1Condition();
            if (null != leveId && !"".equals(leveId) && !"0".equals(leveId)) {
                caleve1_.setLevelId(Integer.parseInt(leveId));
            }
            caleve1_.setLevelName(leveName);
            if ("0".equals(status) || "1".equals(status)) {
                caleve1_.setStatus(Integer.parseInt(status));
            }

            caleve1_.setLevelCode(request.getParameter("levelCode"));
            caleve1_.setCategoryCode(request.getParameter("categoryCode"));
            if (null != category_id && !"".equals(category_id)) {
                caleve1_.setCategoryId(Integer.parseInt(category_id));
            }

            page = findInfoDao.loadCatogryLeve1ListPage2(page, caleve1_);
            response.getWriter().write(write_Json_result("msg", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载一级分类对应的类目
     */
    @Action(value = "loadClassfyCatogryByClassfyId")
    public void loadClassfyCatogryByClassfyId() {
        initData();
        try {
            Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
            List<Map<String, Object>> listCategroy = findInfoDao.listClassfyCatogroyByClassfyId(categoryId, null);
            response.getWriter().write(write_Json_result("msg", listCategroy));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到增加或者修改一级目录页面
     *
     * @return
     */
    @Action(value = "toAddorUpdateCatogryLeve1Page", results = {
            @Result(name = "success", location = "/jsp/manage/addorupdate-catogryleve1.jsp")})
    public String toAddorUpdateCatogryLeve1Page() {
        initData();
        try {
            //查询类目列表 //查询分类类目关系
            list_category = findInfoDao.listClassfyCatogryMap(null, null);

            photoPath = RsConstants.phone_url + "null.jpg";
            if ("update".equals(haddle)) {
                caleve1 = findInfoDao.loadCatogryLeve1Info(Integer.parseInt(leveId));
                if (caleve1.getLevelImg() != null && !"".equals(caleve1.getLevelImg())) {
                    photoPath = RsConstants.phone_url + caleve1.getLevelImg();
                }
            } else {
                caleve1.setLevelSort(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 增加或者修改一级目录
     */
    @Action(value = "addOrUpdateCatogryLeve1")
    public void addOrUpdateCatogryLeve1() {
        initData();
        try {
            boolean flag = false;
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");

            //检测分类编码是否已存在
            boolean checkCode = findInfoDao.checkCatogryLeveCodeIsExist(caleve1.getLevelId() + "", caleve1.getLevelCode());
            if (checkCode) {
                response.getWriter().write(write_Json_result("false", "编码已存在"));
                return;
            }

            //map 用于保存关系 key为类目标识 value为分类标识
            Map<String, String> map = new HashMap<String, String>();
            String[] categoryIds = request.getParameterValues("categoryIds");

            if ("add".equals(haddle)) {//新增
                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        caleve1.setLevelImg(imgpath);
                    }
                }
                caleve1.setCreateman(user.getLoginName());
                caleve1.setCreatetime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(caleve1);
                if (categoryIds != null && categoryIds.length > 0) {
                    for (int i = 0; i < categoryIds.length; i++) {
                        map.put(categoryIds[i], caleve1.getLevelId() + "");
                    }
                }
                saveClassfyCatogryrel(map, caleve1.getLevelId());
                if (flag) {
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                } else {
                    response.getWriter().write(write_Json_result("false", "增加失败"));
                }
            } else {//更新
                //是否保留已当前已存在的且类目为正常状态下的分类类目关系
                String ifLeaveRel = request.getParameter("ifLeaveRel");
                if ("1".equals(ifLeaveRel)) {//保留
                    //先查询当前已存在的且类目为正常状态下的分类类目关系
                    List<Map<String, Object>> list = findInfoDao.listClassfyCatogryMap(null, caleve1.getLevelId());
                    for (int i = 0; i < list.size(); i++) {
                        String categoryId = list.get(i).get("category_id").toString();
                        map.put(categoryId, caleve1.getLevelId() + "");
                    }
                }
                //清空数据（再把查询当前已存在的且类目为正常状态下的分类类目关系重新增加）
                saveInfoDao.deleteClassfyCatogryRel(caleve1.getLevelId());
                if (categoryIds != null && categoryIds.length > 0) {
                    for (int i = 0; i < categoryIds.length; i++) {
                        map.put(categoryIds[i], caleve1.getLevelId() + "");
                    }
                }

                TbCatogryLevel1 level1 = findInfoDao.loadCatogryLeve1Info(caleve1.getLevelId());
                caleve1.setCreateman(level1.getCreateman());
                caleve1.setCreatetime(level1.getCreatetime());

                if (file != null) {
                    String imgpath = new ImageAction().uploadFileToLocalhost(file);
                    if (null != imgpath && !"".equals(imgpath)) {
                        caleve1.setLevelImg(imgpath);
                        ImageUtil.getInstance().removePathImg(level1.getLevelImg(), request);
                    }
                } else {
                    caleve1.setLevelImg(level1.getLevelImg());
                }
                caleve1.setLastalterman(user.getLoginName());
                caleve1.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                flag = saveInfoDao.saveOrUpdateObj(caleve1);
                saveClassfyCatogryrel(map, caleve1.getLevelId());
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
     * 保存分类类目关系
     *
     * @param maps
     * @param leveId
     */
    public void saveClassfyCatogryrel(Map<String, String> maps, Integer leveId) {
        if (null != maps) {
            for (Map.Entry<String, String> map : maps.entrySet()) {
                String key = map.getKey();
                TbClassyfyCatogryRel rel = new TbClassyfyCatogryRel();
                rel.setLevelId(leveId);
                rel.setCategoryId(Integer.parseInt(key));
                saveInfoDao.saveOrUpdateObj(rel);
            }
        }
    }

    /**
     * 修改状态
     */
    @Action(value = "updateCatogryLeve1Status")
    public void updateCatogryLeve1Status() {
        initData();
        try {
            boolean flag = saveInfoDao.updateCatogryLeve1Status(leveId, status);
            if (flag) {
                if ("0".equals(status)) {
                    response.getWriter().write(write_Json_result("ok", "删除成功"));
                } else {
                    response.getWriter().write(write_Json_result("ok", "启用成功"));
                }
            } else {
                if ("0".equals(status)) {
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
     * 查询分类类目关系列表
     */
    @Action(value = "listClassfyCatogryMap")
    public void listClassfyCatogryMap() {
        try {
            initData();
            String keyWord = request.getParameter("keyWord");
            list_category = findInfoDao.listClassfyCatogryMap(keyWord, null);
            response.getWriter().write(write_Json_result("msg", list_category));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询分类列表
     */
    @Action(value = "listClassfyMap")
    public void listClassfyMap() {
        try {
            initData();
            list_category = findInfoDao.listClassfyMap(null);
            response.getWriter().write(write_Json_result("msg", list_category));
        } catch (IOException e) {
            e.printStackTrace();
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

    public TbCategory getCategory() {
        return category;
    }

    public void setCategory(TbCategory category) {
        this.category = category;
    }


    public String getFrom_page() {
        return from_page;
    }

    public void setFrom_page(String from_page) {
        this.from_page = from_page;
    }

    public List<Map<String, Object>> getList_category() {
        return list_category;
    }


    public void setList_category(List<Map<String, Object>> list_category) {
        this.list_category = list_category;
    }


    public TbItemprops getItemprops() {
        return itemprops;
    }


    public void setItemprops(TbItemprops itemprops) {
        this.itemprops = itemprops;
    }


    public List<Map<String, Object>> getList_itemprops() {
        return list_itemprops;
    }


    public void setList_itemprops(List<Map<String, Object>> list_itemprops) {
        this.list_itemprops = list_itemprops;
    }


    public TbPropsValue getPropsValue() {
        return propsValue;
    }


    public void setPropsValue(TbPropsValue propsValue) {
        this.propsValue = propsValue;
    }


    public List<Map<String, Object>> getList_propsValue() {
        return list_propsValue;
    }


    public void setList_propsValue(List<Map<String, Object>> list_propsValue) {
        this.list_propsValue = list_propsValue;
    }


    public String getClick() {
        return click;
    }


    public void setClick(String click) {
        this.click = click;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getItemprops_id() {
        return itemprops_id;
    }

    public void setItemprops_id(String itemprops_id) {
        this.itemprops_id = itemprops_id;
    }

    public String getClickObj() {
        return clickObj;
    }

    public String getLeveName() {
        return leveName;
    }

    public void setLeveName(String leveName) {
        this.leveName = leveName;
    }

    public void setClickObj(String clickObj) {
        this.clickObj = clickObj;
    }

    public String getHaddle() {
        return haddle;
    }

    public void setHaddle(String haddle) {
        this.haddle = haddle;
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

    public String getItemprops_name() {
        return itemprops_name;
    }

    public void setItemprops_name(String itemprops_name) {
        this.itemprops_name = itemprops_name;
    }

    public String getItemprops_code() {
        return itemprops_code;
    }

    public void setItemprops_code(String itemprops_code) {
        this.itemprops_code = itemprops_code;
    }

    public String getPropsValue_id() {
        return propsValue_id;
    }

    public void setPropsValue_id(String propsValue_id) {
        this.propsValue_id = propsValue_id;
    }

    public String getValue_name() {
        return value_name;
    }

    public void setValue_name(String value_name) {
        this.value_name = value_name;
    }

    public TbCatogryLevel1 getCaleve1() {
        return caleve1;
    }

    public void setCaleve1(TbCatogryLevel1 caleve1) {
        this.caleve1 = caleve1;
    }

    public String getLeveId() {
        return leveId;
    }

    public List<Map<String, Object>> getListClassfyCatogry() {
        return listClassfyCatogry;
    }


    public void setListClassfyCatogry(List<Map<String, Object>> listClassfyCatogry) {
        this.listClassfyCatogry = listClassfyCatogry;
    }


    public void setLeveId(String leveId) {
        this.leveId = leveId;
    }


}
