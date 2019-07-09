package com.yufan.manage.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import com.yufan.pojo.TbRuleRel;
import com.yufan.common.bean.PageUtil;
import com.yufan.util.RsConstants;

/**
 * @功能名称 规则关联管理
 * @作者 lirongfan
 * @时间 2016年8月12日 下午4:46:24
 */
@Scope("prototype")
@Service("ruleRelateAction")
@Namespace("/manage")
@ParentPackage("common")
public class RuleRelateAction extends LotFilterAction {

    private FindInfoDao findInfoDao;
    private SaveInfoDao saveInfoDao;
    private static Logger LOG = Logger.getLogger(RuleRelateAction.class);

    private String id;// 标识
    private String op;// add or update
    private Integer currentPage;// 当前页数
    private PageUtil page = new PageUtil(RsConstants.PAGE_SIZE, 1, 0);

    private TbRuleRel rel = new TbRuleRel();

    private List<Map<String, Object>> listRule;
    private List<Map<String, Object>> listRel;

    /**
     * 查询条件
     */
    private String ruleName;
    private String shopName;
    private String partnersName;
    private String name;
    private Integer timeLimit;//规则时效

    @Action(value = "tolistrulerelpage", results = {
            @Result(name = "success", location = "/jsp/manage/list_rule_rel.jsp")})
    public String tolistrulerelpage() {
        initData();
        return "success";
    }

//	/**
//	 * 加载数据规则
//	 * 列表页面：1.如果登录人是超级管理员或者网站管理员则(卡券,商品,)列表显示全部关联(没有操作权限)
//	 * 2.反之则根据添加数据所在的店铺标识为主来显示
//	 */
//	@Action(value = "loadDataRuleRle")
//	public void loadDataRuleRle() {
//		try {
//			initData();
//			if (null != request.getParameter("currentPage")) {
//				currentPage = Integer.parseInt(request .getParameter("currentPage"));
//			}
//			if (null == currentPage || 0 == currentPage)
//				page.setCurrentPage(1);
//			else {
//				page.setCurrentPage(currentPage);
//			}
//			TbAdmin user = (TbAdmin) request.getSession() .getAttribute("user");
//			Integer shopId = user.getShopId();//店铺标识
//			Integer partnerId=0;//商家标识
//			if(shopId!=null&&shopId!=0){
//				//店铺管理员
//				//是否是外部店铺
//				TbShop shop = findInfoDao.loadTbShopInfo(shopId);
//				Integer isOutShop = shop.getIsOutShop();
//				if(isOutShop !=2){
//					//内部店铺,查询全部
//					shopId = 0;
//				}else{
//					//外部部店铺(第三方店铺)必须关联登录人所在的店铺
//					TbPartners partner = findInfoDao.loadTbPartnersByShopIdInfo(shopId);
//					if(null==partner){
//						partnerId = -1;
//						LOG.info("登录人所在的店铺即外部部店铺(第三方店铺)必须关联商家,处理方法,把登录人所属于的店铺与一个商家关联即可");
//					}else{
//						partnerId = partner.getId();
//					}
//				}
//			}else{
//				//超级管理员,查询全部
//				shopId = 0;
//			}
//			if (1==fromtype) {// 1商家
//				page = findInfoDao.loadDataPartnersRuleRle(page, ruleName, name, timeLimit,partnerId);
//			} else if (2==fromtype) {//2商品
//				page = findInfoDao.loadDataGoodsRuleRle(page, ruleName, name, timeLimit,shopId);
//			} else if(4==fromtype){// 4全部
//				page = findInfoDao.loadDataAllRuleRle(page, ruleName, name, typeId, timeLimit,shopId,partnerId,null);
//			}else{// 3卡券
//				page = findInfoDao.loadDataTicketRuleRle(page, ruleName, name, timeLimit,shopId);
//			}
//			response.getWriter().write(write_Json_result("ok", page));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

//	/**
//	 * 跳转页面
//	 */
//	@Action(value = "toAddRuleRelPage", results = {
//			@Result(name = "addpageshop", location = "/jsp/manage/addorupdate-partnersrulerel.jsp"),
//			@Result(name = "addpagegoods", location = "/jsp/manage/addorupdate-goodsrulerel.jsp"),
//			@Result(name = "addpageticket", location = "/jsp/manage/addorupdate-ticketrulerel.jsp") })
//	public String toAddRuleRelPage() {
//		initData();
//		if (fromtype==1) {// 1商家 2商品3卡券
//			return "addpageshop";
//		} else if (fromtype==2) {// 1商家 2商品3卡券
//			return "addpagegoods";
//		} else {
//			return "addpageticket";
//		}
//	}

//	/**
//	 * 加载规则列表
//	 */
//	@Action(value = "listRule")
//	public void listRule(){
//		initData();
//		try {
//			TbRule rule = new TbRule();
//			rule.setStatus(1);
//			if(null!=ruleName&&!"".equals(ruleName)){
//				rule.setRuleName(ruleName);
//			}
//			//规则类型 0:商家规则1商品规则2卡券规则',
//			if (fromtype==1) {// 1商家
//				rule.setRuleType(0);
//			} else if (fromtype==2) {// 2商品
//				rule.setRuleType(1);
//			} else {//3卡券
//				rule.setRuleType(2);
//			}
//			TbAdmin user = (TbAdmin) request.getSession() .getAttribute("user");
//			Integer shopId = user.getShopId();
//			Integer partnerId  = null;//商家标识
//			if (null != shopId && shopId != 0) {
//				//店铺账号
//				//判断是否是第三方店铺
//				TbShop shop = findInfoDao.loadTbShopInfo(shopId);
//				Integer isOutShop = shop.getIsOutShop();
//				if(isOutShop ==2){
//					//外部部店铺(第三方店铺)必须关联登录人所在的店铺
//					TbPartners partner = findInfoDao.loadTbPartnersByShopIdInfo(shopId);
//					if(null==partner){
//						partnerId = -1;
//						LOG.info("登录人所在的店铺即外部部店铺(第三方店铺)必须关联商家,处理方法,把登录人所属于的店铺与一个商家关联即可");
//					}else{
//						partnerId = partner.getId();
//					}
//
//				}
//			}
//			rule.setRuleBelong(partnerId);
//
//			listRule = findInfoDao.listRuleMap(rule);
//			response.getWriter().write(write_Json_result("ok", listRule));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 加载关联商家/卡券/商品列表(页面)
//	 */
//	@Action(value = "listRelation")
//	public void listRelation(){
//		initData();
//		try {
//			TbAdmin user = (TbAdmin) request.getSession() .getAttribute("user");
//			if (fromtype==1) {// 1商家
//				TbPartners partners= new TbPartners();
//				partners.setStatus(1);
//				if(null!=name&&!"".equals(name)){
//					partners.setPartnersName(name);
//				}
//				//如果当前登录用户是网站管理员,则显示全部商家,否则判断登录人员所在店铺是内部店铺还是外部店铺
//				Integer shopId = user.getShopId();
//
//				if (shopId != null && shopId != 0) {//店铺管理员
//					//判断登录人员所在店铺是内部店铺还是外部店铺
//					TbShop shop = findInfoDao.loadTbShopInfo(shopId);
//					if(2==shop.getIsOutShop()){//外部店铺
//						partners.setShopId(user.getShopId());
//					}
//				}
//				listRelName = findInfoDao.listPartnersMap(partners);
//
//			} else if (fromtype==2) {// 2商品
//				TbGoods goods = new TbGoods();
//				goods.setStatus(1);
//				if(null!=name&&!"".equals(name)){
//					goods.setGoodsName(name);
//				}
//				goods.setShopId(user.getShopId());
//				listRelName = findInfoDao.listGoodsMap(goods);
//			} else {//3卡券
//				TbTicket ticket = new TbTicket();
//				ticket.setStatus(1);
//				if(null!=name&&!"".equals(name)){
//					ticket.setTikcetName(name);
//				}
//				ticket.setShopId(user.getShopId());
//				listRelName = findInfoDao.listTicketsMap(ticket);
//			}
//			response.getWriter().write(write_Json_result("ok", listRelName));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 增加规则关联
//	 */
//	@Action(value = "addRuleRel")
//	public void addRuleRel(){
//		initData();
//		try {
//			boolean flag = true;
//			TbAdmin user = (TbAdmin) request.getSession() .getAttribute("user");
//			if(null==user||user.getShopId()==null||user.getShopId()==0){
//				response.getWriter().write(write_Json_result("false", "只有店铺成员才能添加规则关联"));
//				return;
//			}
//			//查询规则是否是公共规则
//			TbRule rule = findInfoDao.loadTbRuleByIdInfo(rel.getRuleId());
//			if(rule.getRuleBelong()==1){
//				//0共用,1.商家标识(非公共)
//				if(rel.getRelIdType()==2){
//					//2卡券规则
//					TbTicket ticket = findInfoDao.loadTicketInfo(rel.getRelId());
//					if (ticket.getPartnersId() != rule.getRuleBelong()) {
//						flag = false;
//					}
//				}else if(rel.getRelIdType()==1){
//					//1商品规则
//					TbGoods goods = findInfoDao.loadTbGoodsInfo(rel.getRelId());
//					if(goods.getPartnersId()!=rule.getRuleBelong()){
//						flag = false;
//					}
//				}else {
//					//0:商家规则
//					if(rel.getRelId()!=rule.getRuleBelong()){
//						flag = false;
//					}
//				}
//			}
//			if(!flag){
//				response.getWriter().write(write_Json_result("false", "非公共规则"));
//				return;
//			}
//
//			//检查对应卡券/商品/商家 查询是否已经增加规则
//			/**
//			 * 一个卡券只能对应一个规则
//			 */
//			if(2==rel.getRelIdType()){//卡券
//				flag = findInfoDao.checkTicketRelIsExsit(rel.getRelId());
//				if(flag){
//					response.getWriter().write(write_Json_result("false", "增加失败,规则关联已存在,一个卡券只能对应一个规则"));
//					return;
//				}
//			}else{
//				flag = findInfoDao.checkRelIsExsit(rel.getRuleId(), rel.getRelId(), rel.getRelIdType());
//				if(flag){
//					response.getWriter().write(write_Json_result("false", "增加失败,规则关联已存在,请先删除对应规则关联"));
//					return;
//				}
//			}
//
//			//时间处理
//			String startTime = request.getParameter("startTime");
//			String endTime = request.getParameter("endTime");
//			String format = "yyyy-MM-dd HH:mm:ss";
//			if(null!=startTime&&!"".equals(startTime.trim())){
//				rel.setStartTime(new Timestamp(DatetimeUtil.formatStringToData(format, startTime).getTime()));
//			}
//			if(null!=endTime&&!"".equals(endTime.trim())){
//				rel.setEndTime(new Timestamp(DatetimeUtil.formatStringToData(format, endTime).getTime()));
//			}
//			rel.setCreateman(user.getLoginName());
//			rel.setCreatetime(new Timestamp(System.currentTimeMillis()));
//
//			rel.setIsMakeSure(0);
//			flag = saveInfoDao.saveObject(rel);
//			if (flag) {
//				response.getWriter().write(write_Json_result("ok", "增加成功"));
//			} else {
//				response.getWriter().write(write_Json_result("false", "增加失败"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * 删除规则关联
     */
    @Action(value = "delRuleRel")
    public void delRuleRel() {
        initData();
        try {
            String id = request.getParameter("id");
            boolean flag = saveInfoDao.delRuleRel(Integer.parseInt(id));
            if (flag) {
                response.getWriter().write(write_Json_result("ok", "删除成功"));
            } else {
                response.getWriter().write(write_Json_result("ok", "删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //================================================================新方法分开==============================================================

    /**
     * 查询卡券规则列表（新方法）
     */
    @Action(value = "loadTicletRulePage")
    public void loadTicletRulePage() {
        initData();
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
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer shopId = user.getShopId();//店铺标识
            page = findInfoDao.loadTicletRulePage(page, ruleName, name, timeLimit, shopId, shopName, partnersName);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询商品规则列表（新方法）
     */
    @Action(value = "loadGoodsRulePage")
    public void loadGoodsRulePage() {
        initData();
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
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer shopId = user.getShopId();//店铺标识
            page = findInfoDao.loadGoodsRulePage(page, ruleName, name, timeLimit, shopId, shopName, partnersName);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询商家规则列表（新方法）
     */
    @Action(value = "loadPartnersRulePage")
    public void loadPartnersRulePage() {
        initData();
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
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer shopId = user.getShopId();//登录人员店铺标识
            page = findInfoDao.loadPartnersRulePage(page, ruleName, partnersName, timeLimit, shopId);
            response.getWriter().write(write_Json_result("ok", page));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转页面------------ 卡券
     */
    @Action(value = "toAddTicketRuleRel", results = {@Result(name = "success", location = "/jsp/manage/addorupdate-ticketrulerel.jsp")})
    public String toAddTicketRuleRel() {
        initData();
        return "success";
    }

    /**
     * 增加卡券规则页面---卡券列表
     */
    @Action(value = "getAddTicketRulePageListTicket")
    public void getAddTicketRulePageListTicket() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String ticketName = request.getParameter("ticketName");
            listRel = findInfoDao.listTicketRulerel(user.getShopId(), ticketName);
            response.getWriter().write(write_Json_result("ok", listRel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加卡券规则页面---规则列表
     */
    @Action(value = "getAddTicketRulePageListRule")
    public void getAddTicketRulePageListRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer ticketId = null;
            String ticketIdStr = request.getParameter("ticketId");
            if (null != ticketIdStr && !"".equals(ticketIdStr) && !"0".equals(ticketIdStr)) {
                ticketId = Integer.parseInt(ticketIdStr);
            }
            listRule = findInfoDao.listTicketRuelRel(ticketId, ruleName, user.getShopId());
            response.getWriter().write(write_Json_result("ok", listRule));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存卡券规则
     */
    @Action(value = "saveTicketRule")
    public void saveTicketRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String ticketId = request.getParameter("relId");
            rel.setRelId(Integer.parseInt(ticketId));
            rel.setRelIdType(2);
            rel.setCreateman(user.getLoginName());
            rel.setCreatetime(new Date());
            rel.setIsMakeSure(0);
            String rules[] = request.getParameterValues("checkSub");
            if (null != rules && rules.length > 0) {
                for (int i = 0; i < rules.length; i++) {
                    String ruleId = rules[i];
                    rel.setRuleId(Integer.parseInt(ruleId));
                    rel.setId(null);
                    saveInfoDao.saveObject(rel);
                }
            }
            response.getWriter().write(write_Json_result("ok", "增加成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转页面------------ 商品
     */
    @Action(value = "toAddGoodsRuleRel", results = {@Result(name = "success", location = "/jsp/manage/addorupdate-goodsrulerel.jsp")})
    public String toAddGoodsRuleRel() {
        initData();
        return "success";
    }

    /**
     * 增加商品规则页面---商品列表
     */
    @Action(value = "getAddGoodsRulePageListGoods")
    public void getAddGoodsRulePageListGoods() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String goodsName = request.getParameter("goodsName");
            listRel = findInfoDao.listGoodsRulerel(user.getShopId(), goodsName);
            response.getWriter().write(write_Json_result("ok", listRel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加商品规则页面---规则列表
     */
    @Action(value = "getAddGoodsRulePageListRule")
    public void getAddGoodsRulePageListRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer goodsId = null;
            String goodsIdStr = request.getParameter("goodsId");
            if (null != goodsIdStr && !"".equals(goodsIdStr) && !"0".equals(goodsIdStr)) {
                goodsId = Integer.parseInt(goodsIdStr);
            }
            listRule = findInfoDao.listGoodsRuelRel(goodsId, ruleName, user.getShopId());
            response.getWriter().write(write_Json_result("ok", listRule));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存商品规则
     */
    @Action(value = "saveGoodsRule")
    public void saveGoodsRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String goodsId = request.getParameter("relId");
            rel.setRelId(Integer.parseInt(goodsId));
            rel.setRelIdType(1);
            rel.setCreateman(user.getLoginName());
            rel.setCreatetime(new Date());
            rel.setIsMakeSure(0);
            String rules[] = request.getParameterValues("checkSub");
            if (null != rules && rules.length > 0) {
                for (int i = 0; i < rules.length; i++) {
                    String ruleId = rules[i];
                    rel.setRuleId(Integer.parseInt(ruleId));
                    rel.setId(null);
                    saveInfoDao.saveObject(rel);
                }
            }
            response.getWriter().write(write_Json_result("ok", "增加成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转页面------------ 商家
     */
    @Action(value = "toAddPartnersRuleRel", results = {@Result(name = "success", location = "/jsp/manage/addorupdate-partnersrulerel.jsp")})
    public String toAddPartnersRuleRel() {
        initData();
        return "success";
    }

    /**
     * 增加商家规则页面---商家列表
     */
    @Action(value = "getAddPartnersRulePageListPartners")
    public void getAddPartnersRulePageListPartners() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String partnersName = request.getParameter("partnersName");
            listRel = findInfoDao.listPartnersRulerel(user.getShopId(), partnersName);
            response.getWriter().write(write_Json_result("ok", listRel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加商家规则页面---规则列表
     */
    @Action(value = "getAddPartnersRulePageListRule")
    public void getAddPartnersRulePageListRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            Integer partnersId = null;
            String partnersIdStr = request.getParameter("partnersId");
            if (null != partnersIdStr && !"".equals(partnersIdStr) && !"0".equals(partnersIdStr)) {
                partnersId = Integer.parseInt(partnersIdStr);
            }
            listRule = findInfoDao.listPartnreesRuelRel(partnersId, ruleName, user.getShopId());
            response.getWriter().write(write_Json_result("ok", listRule));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存商品规则
     */
    @Action(value = "savePartnersRule")
    public void savePartnersRule() {
        initData();
        try {
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String partnersId = request.getParameter("relId");
            rel.setRelId(Integer.parseInt(partnersId));
            rel.setRelIdType(0);
            rel.setCreateman(user.getLoginName());
            rel.setCreatetime(new Date());
            rel.setIsMakeSure(0);
            String rules[] = request.getParameterValues("checkSub");
            if (null != rules && rules.length > 0) {
                for (int i = 0; i < rules.length; i++) {
                    String ruleId = rules[i];
                    rel.setRuleId(Integer.parseInt(ruleId));
                    rel.setId(null);
                    saveInfoDao.saveObject(rel);
                }
            }
            response.getWriter().write(write_Json_result("ok", "增加成功"));
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

    public String getPartnersName() {
        return partnersName;
    }

    public void setPartnersName(String partnersName) {
        this.partnersName = partnersName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public TbRuleRel getRel() {
        return rel;
    }

    public void setRel(TbRuleRel rel) {
        this.rel = rel;
    }


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Map<String, Object>> getListRule() {
        return listRule;
    }

    public void setListRule(List<Map<String, Object>> listRule) {
        this.listRule = listRule;
    }


    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public List<Map<String, Object>> getListRel() {
        return listRel;
    }

    public void setListRel(List<Map<String, Object>> listRel) {
        this.listRel = listRel;
    }

}
