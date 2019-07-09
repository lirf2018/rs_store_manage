package com.yufan.core.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yufan.common.action.ImageAction;
import com.yufan.pojo.TbMainMenu;
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
import com.yufan.pojo.TbFunctions;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 菜单管理
 * @作者 lirongfan
 * @时间 2016年4月22日 下午4:40:43
 */
@Scope("prototype")
@Service("menuAction")
@Namespace("/menu")
@ParentPackage("common")
public class MenuAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;

    private TbFunctions function = new TbFunctions();
    private List<Map<String, Object>> listMenu = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listLeve = new ArrayList<Map<String, Object>>();

    private String menuType;//主菜单标识parentMenu 子菜单标识 subMenu
    private String menuId;//菜单标识
    private String handdleType;//操作类型   create update delete
    private Integer currentPage;//当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbMainMenu mainMenu;

    private String photoPath;
    private File file;

    //增加或者修改子菜单所附带的父级菜单
    private String pmenuid;

    /**
     * 查询主菜单或者子菜单
     */
    @Action(value = "loadParentMenusOrSubMenus")
    public void loadParentMenusOrSubMenus() {
        try {
            initData();
            menuType = request.getParameter("menuType");
            if (null != request.getParameter("currentPage")) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }

            if ("parentMenu".equals(menuType)) {//查询主菜单
                if (null == currentPage || 0 == currentPage) {
                    page.setCurrentPage(1);
                    page.setPageSize(1000);
                } else {
                    page.setCurrentPage(1);
                    page.setPageSize(1000);
                }

            } else if ("subMenu".equals(menuType)) {//查询子菜单
                menuId = request.getParameter("menuId");
                String searchName = request.getParameter("searchName");
                String functionCode = request.getParameter("functionCode");
                String state = request.getParameter("state");
//				function.setFunctionId(Integer.parseInt(menuId));
                String pmenuid = request.getParameter("pmenuid");
                if (null != pmenuid) {
                    function.setFunctionParentid(Integer.parseInt(pmenuid));
                }
                function.setFunctionParentid(Integer.parseInt(menuId));
                function.setFunctionName(searchName);
                function.setFunctionCode(functionCode);
                if (null != state && !"".equals(state)) {
                    function.setStatus(Integer.parseInt(state));
                }
                if (null == currentPage || 0 == currentPage)
                    page.setCurrentPage(1);
                else {
                    page.setCurrentPage(currentPage);
                }
            }

            page = findInfoDao.loadParentMenusOrSubMenus(function, page, menuType);
            response.getWriter().write(write_Json_result("ok", page));
            System.out.println(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到增加或者修改主菜单
     *
     * @return
     */
    @Action(value = "toaddorupdateparentMenupage", results = {
            @Result(name = "success", location = "/jsp/sys/addoredit-parentmenu.jsp")})
    public String toaddorupdateparentMenupage() {
        initData();
        handdleType = request.getParameter("handdleType");
        if ("update".equals(handdleType)) {//更新
            String menuId = request.getParameter("menuId");
            function = findInfoDao.loadFunctionById(Integer.parseInt(menuId));
        }
        return "success";
    }

    /**
     * 跳转到增加或者修改子菜单
     *
     * @return
     */
    @Action(value = "toaddorupdatesubMenupage", results = {
            @Result(name = "success", location = "/jsp/sys/addoredit-submenu.jsp")})
    public String toaddorupdatesubMenupage() {
        initData();
        handdleType = request.getParameter("handdleType");
        System.out.println("===============================" + handdleType);
        function.setFunctionParentid(0);
        if ("update".equals(handdleType) && !"0".equals(request.getParameter("menuId"))) {//更新
            String menuId = request.getParameter("menuId");
            function = findInfoDao.loadFunctionById(Integer.parseInt(menuId));
        }
        listMenu = findInfoDao.listParentMenusMap(1);
        return "success";
    }

    /**
     * 增加或者修改菜单
     */
    @Action(value = "addorupdateMenu")
    public void addorupdateMenu() {
        initData();
        try {
            int id = 0;
            if (null != function.getFunctionId()) {
                id = function.getFunctionId();
            }
            // 检测名称
            if (findInfoDao.checkName(id, function.getFunctionName(), menuType)) {
                response.getWriter().write(write_Json_result("false", "名称已存在"));
                return;
            }
            // 检测编码
            if (findInfoDao.checkcode(id, function.getFunctionCode(), menuType)) {
                response.getWriter().write(write_Json_result("false", "编码已存在"));
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if ("parentMenu".equals(menuType)) {// 增加或者修改主菜单
                //自动将排序-1
                saveInfoDao.updateSortMenuByTableName("tb_functions", function.getFunctionParentid(), function.getSort());
                if (null != function.getFunctionId()) {// 更新
                    TbFunctions menu = findInfoDao.loadFunctionById(function.getFunctionId());
                    function.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                    function.setLastalterman(user.getLoginName());
                    function.setFunctionParentid(0);
                    function.setFunctionType(0);
                    function.setCreateman(menu.getCreateman());
                    function.setCreatetime(menu.getCreatetime());
                    saveInfoDao.addorupdateMenu(function);
                    response.getWriter().write(write_Json_result("ok", "更新成功"));
                    return;
                } else {// 增加
                    function.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    function.setCreateman(user.getLoginName());
                    function.setFunctionParentid(0);
                    function.setFunctionType(0);
                    saveInfoDao.addorupdateMenu(function);
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                    return;
                }
            } else {// 增加或者修改子菜单
                //自动将排序-1
                saveInfoDao.updateSortMenuByTableName("tb_functions", function.getFunctionParentid(), function.getSort());
                if (null != function.getFunctionId()) {// 更新
                    TbFunctions menu = findInfoDao.loadFunctionById(function.getFunctionId());
                    function.setLastaltertime(new Timestamp(System.currentTimeMillis()));
                    function.setLastalterman(user.getLoginName());
                    function.setFunctionType(1);
                    function.setCreateman(menu.getCreateman());
                    function.setCreatetime(menu.getCreatetime());
                    saveInfoDao.addorupdateMenu(function);
                    response.getWriter().write(write_Json_result("ok", "更新成功"));
                    return;
                } else {// 增加
                    function.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    function.setCreateman(user.getLoginName());
                    function.setFunctionType(1);
                    saveInfoDao.addorupdateMenu(function);
                    response.getWriter().write(write_Json_result("ok", "增加成功"));
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到菜单列表
     *
     * @return
     */
    @Action(value = "toMenuListPage", results = {
            @Result(name = "success", location = "/jsp/sys/list_menu.jsp")})
    public String toMenuListPage() {
        return "success";
    }

    /**
     * 删除或者启用菜单
     */
    @Action(value = "deleteOrUpdateMenu")
    public void deleteOrUpdateMenu() {
        try {
            initData();
            String status = request.getParameter("status");
            String function_type = request.getParameter("function_type");
            function.setStatus(Integer.parseInt(status));
            function.setFunctionId(Integer.parseInt(menuId));
            if (null != function_type && !"".equals(function_type) && !"null".equals(function_type)) {
                function.setFunctionType(Integer.parseInt(function_type));
            }
            boolean flag = saveInfoDao.deleteOrUpdateMenu(function);
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------华丽分割线-------main页菜单------------------------------------

    /**
     */
    @Action(value = "loadMainMenuDataList")
    public void loadMainMenuDataList() {
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
            String status = request.getParameter("status");
            page = findInfoDao.loadMainNemuPageList(page, status);

            //查询一级分类列表
            List<Map<String, Object>> listLeve1 = findInfoDao.listClassfyMap(null);
            Map<Integer, String> mapLeve = new HashMap<Integer, String>();
            for (int i = 0; i < listLeve1.size(); i++) {
                int leveId = Integer.parseInt(String.valueOf(listLeve1.get(i).get("level_id")));
                mapLeve.put(leveId, String.valueOf(listLeve1.get(i).get("level_name")));
            }
            //查询类目列表
            List<Map<String, Object>> listCatogroy = findInfoDao.listClassfyCatogryMap(null, null);
            Map<Integer, String> mapCatogroy = new HashMap<Integer, String>();
            for (int i = 0; i < listCatogroy.size(); i++) {
                int catogroyId = Integer.parseInt(String.valueOf(listCatogroy.get(i).get("category_id")));
                mapCatogroy.put(catogroyId, String.valueOf(listCatogroy.get(i).get("category_name")));
            }
            //处理结果
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < page.getList().size(); i++) {
                String[] value = new String[11];
                Object[] obj = (Object[]) page.getList().get(i);
                value[0] = String.valueOf(obj[0]);
                value[1] = String.valueOf(obj[1]);
                value[2] = String.valueOf(obj[2]);
                value[3] = String.valueOf(obj[3]);
                String leve1Ids = String.valueOf(obj[4]);
                value[4] = leve1Ids;
                String leName = "";
                String l[] = leve1Ids.split(",");
                for (int j = 0; j < l.length; j++) {
                    String v = l[j];
                    if (StringUtils.isNotEmpty(v) && !"null".equals(v)) {
                        if (j == l.length - 1) {
                            leName = leName + v + "-" + mapLeve.get(Integer.parseInt(v));
                        } else {
                            leName = leName + v + "-" + mapLeve.get(Integer.parseInt(v)) + "<br/>";
                        }
                    }
                }
                value[8] = leName;
                String categoryIds = String.valueOf(obj[5]);
                value[5] = categoryIds;
                String caName = "";
                String c[] = categoryIds.split(",");
                for (int j = 0; j < c.length; j++) {
                    String v = c[j];
                    if (StringUtils.isNotEmpty(v) && !"null".equals(v)) {
                        if (j == c.length - 1) {
                            caName = caName + v + "-" + mapCatogroy.get(Integer.parseInt(v));
                        } else {
                            caName = caName + v + "-" + mapCatogroy.get(Integer.parseInt(v)) + "<br/>";
                        }
                    }
                }
                value[9] = caName;
                String createtime = String.valueOf(obj[6]);
                value[6] = createtime;
                String menuImg = String.valueOf(obj[7]);
                value[7] = menuImg;
                value[10] = String.valueOf(obj[8]);
                list.add(value);
            }
            page.setList(list);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一级分类列表
     */
    @Action(value = "listLeve")
    public void listLeve() {
        initData();
        try {
            List<Map<String, Object>> listLeve1 = findInfoDao.listClassfyMap(null);
            response.getWriter().write(write_Json_result("ok", listLeve1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询类目列表
     */
    @Action(value = "listCatogory")
    public void listCatogory() {
        initData();
        try {
            Integer leveId = null;
            if (request.getParameter("leveId") != null && !"0".equals(request.getParameter("leveId"))) {
                leveId = Integer.parseInt(request.getParameter("leveId"));
            }
            String categroyName = request.getParameter("categroyName");

            String categorys = request.getParameter("categorys");//已选择的类目
            String[] categorysArr = categorys.split(",");
            Map<String, String> categoryMap = new HashMap<String, String>();
            for (int i = 0; i < categorysArr.length; i++) {
                if (StringUtils.isNotEmpty(categorysArr[i])) {
                    categoryMap.put(categorysArr[i], categorysArr[i]);
                }
            }

            List<Map<String, Object>> catogryList_ = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> catogryList = findInfoDao.listClassfyCatogroyByClassfyId(leveId, categroyName);
            for (int i = 0; i < catogryList.size(); i++) {
                Map<String, Object> map = catogryList.get(i);
                String categoryId = String.valueOf(map.get("category_id"));
                if (categoryMap.get(categoryId) == null) {
                    catogryList_.add(map);
                }
            }
            response.getWriter().write(write_Json_result("ok", catogryList_));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加菜单页面
     *
     * @return
     */
    @Action(value = "toAddMainMenuPage", results = {
            @Result(name = "success", location = "/jsp/sys/addoredit-mainmenu.jsp")})
    public String toAddMainMenuPage() {
        initData();
        photoPath = RsConstants.phone_url + "null.jpg";
        listLeve = findInfoDao.listClassfyMap(null);
        if (listLeve.size() > 0) {
            request.setAttribute("listLeveSize", 1);
        } else {
            request.setAttribute("listLeveSize", 0);
        }

        //查询菜单模板
        List<Map<String, Object>> listParam = findInfoDao.listParamMap("sys_code");
        for (int i = 0; i < listParam.size(); i++) {
            String paramKey = listParam.get(i).get("param_key").toString();
            if ("main_menu".equals(paramKey)) {
                request.setAttribute("menuUrl", listParam.get(i).get("param_value"));
            }
        }


        String leveIds = "";
        if ("update".equals(handdleType)) {
            int id = Integer.parseInt(request.getParameter("id"));
            mainMenu = findInfoDao.loadTbMainMenu(id);
            if (StringUtils.isNotEmpty(mainMenu.getMenuImg())) {
                photoPath = RsConstants.phone_url + mainMenu.getMenuImg();
            }
            leveIds = mainMenu.getLeve1Ids();
            if (StringUtils.isNotEmpty(leveIds)) {
                String[] leveIdsArray = leveIds.split(",");
                Map<String, String> hasLeveIds = new HashMap<>();
                for (int i = 0; i < leveIdsArray.length; i++) {
                    hasLeveIds.put(leveIdsArray[i], leveIdsArray[i]);
                }
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < listLeve.size(); i++) {
                    Map<String, Object> map = listLeve.get(i);
                    String leveId = String.valueOf(map.get("level_id"));
                    map.put("selectLeveid", 0);
                    if (hasLeveIds.get(leveId) != null) {
                        map.put("selectLeveid", 1);
                    }
                    list.add(map);
                }
                request.setAttribute("listLeve_", list);
            }
            if (StringUtils.isNotEmpty(mainMenu.getCategoryIds())) {
                List<Map<String, Object>> listTbCategoryByIds = findInfoDao.listTbCategoryByIds(mainMenu.getCategoryIds());
                request.setAttribute("listCategory", listTbCategoryByIds);
            }

        }

        return "success";
    }

    /**
     * 增加主页菜单
     */
    @Action(value = "addMainMenu")
    public void addMainMenu() {
        try {
            initData();
            boolean flag = false;
            if (file != null) {
                String imgpath = new ImageAction().uploadFileToLocalhost(file);
                if (null != imgpath && !"".equals(imgpath)) {
                    mainMenu.setMenuImg(imgpath);
                }
            }
            if ("update".equals(handdleType)) {
                TbMainMenu mainMenu_ = findInfoDao.loadTbMainMenu(mainMenu.getId());
                if (file == null) {
                    mainMenu.setMenuImg(mainMenu_.getMenuImg());
                }
            }
            //类目
            String categorys = mainMenu.getCategoryIds();
            if (StringUtils.isNotEmpty(categorys)) {
                categorys = categorys.trim();
                if (categorys.endsWith(",")) {
                    categorys = categorys.substring(0, categorys.length() - 1);
                }
                mainMenu.setCategoryIds(categorys);
                mainMenu.setMenuUrl(mainMenu.getMenuUrl() + "&categoryIds=" + categorys);
            }
            //一级分类
            String leveIds = mainMenu.getLeve1Ids();
            if (StringUtils.isNotEmpty(leveIds)) {
                leveIds = leveIds.trim();
                if (leveIds.endsWith(",")) {
                    leveIds = leveIds.substring(0, leveIds.length() - 1);
                }
                mainMenu.setLeve1Ids(leveIds);
                mainMenu.setMenuUrl(mainMenu.getMenuUrl() + "&leve1Ids=" + leveIds);
            }
            mainMenu.setStatus(1);
            mainMenu.setCreatetime(new Timestamp(System.currentTimeMillis()));
            flag = saveInfoDao.saveOrUpdateObj(mainMenu);
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "操作成功"));
            } else {
                response.getWriter().write(write_Json_result("false", "操作失败"));
            }

        } catch (Exception e) {
            LOG.info("---->" + e);
        }
    }

    /**
     * 修改主页菜单排序
     */
    @Action(value = "updateMainMenuSort")
    public void updateMainMenuSort() {
        try {
            initData();
            String[] ids = request.getParameterValues("ids");
            String[] menuSort = request.getParameterValues("menuSort");
            for (int i = 0; i < ids.length; i++) {
                if (StringUtils.isNotEmpty(ids[i])) {
                    saveInfoDao.updateMainMenuSort(Integer.parseInt(ids[i]), Integer.parseInt(menuSort[i]));
                }
            }
            response.getWriter().write(write_Json_result("ok", "操作成功"));
        } catch (Exception e) {
            LOG.info("---->" + e);
        }
    }

    @Action(value = "deleteMainMenu")
    public void deleteMainMenu() {
        initData();
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            boolean flag = saveInfoDao.updateMainMenuStatus(id, status);
            if (flag && status == 0) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else if (flag && status == 1) {
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

    public TbFunctions getFunction() {
        return function;
    }

    public void setFunction(TbFunctions function) {
        this.function = function;
    }

    public List<Map<String, Object>> getListMenu() {
        return listMenu;
    }


    public void setListMenu(List<Map<String, Object>> listMenu) {
        this.listMenu = listMenu;
    }


    public String getMenuType() {
        return menuType;
    }


    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }


    public String getMenuId() {
        return menuId;
    }


    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }


    public String getHanddleType() {
        return handdleType;
    }


    public void setHanddleType(String handdleType) {
        this.handdleType = handdleType;
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


    public String getPmenuid() {
        return pmenuid;
    }


    public void setPmenuid(String pmenuid) {
        this.pmenuid = pmenuid;
    }

    public TbMainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(TbMainMenu mainMenu) {
        this.mainMenu = mainMenu;
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

    public List<Map<String, Object>> getListLeve() {
        return listLeve;
    }

    public void setListLeve(List<Map<String, Object>> listLeve) {
        this.listLeve = listLeve;
    }

}
