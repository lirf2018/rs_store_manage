package com.yufan.core.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yufan.common.dao.CommonDAO;
import com.yufan.core.dao.SaveInfoDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbChannel;
import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbGoodsAttribute;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbItemprops;
import com.yufan.pojo.TbParam;
import com.yufan.pojo.TbRole;
import com.yufan.pojo.TbRoleFunction;
import com.yufan.pojo.TbTicket;
import com.yufan.pojo.TbTicketAttribute;
import com.yufan.pojo.TbUserRole;
import com.yufan.pojo.TbWeixinAccessToken;
import com.yufan.pojo.TbWeixinMenu;

/**
 * @功能名称 数据操作增加修改删除 dao 接口实现类
 * @作者 lirongfan
 * @时间 2015年10月29日 下午2:40:49
 */
@Scope("prototype")
@Service("saveInfoDao")
public class SaveInfoDaoImpl implements SaveInfoDao {
    @Resource(name = "commonDao")
    private CommonDAO commonDao;

    /**
     * 保存微信菜单
     *
     * @param weixin
     * @return
     */
    @Override
    public int saveWeixinMenu(TbWeixinMenu weixin) {
        commonDao.saveOrUpdate(weixin);
        return weixin.getMenuId();
    }

    /**
     * 逻辑删除微信子菜单
     *
     * @param status
     * @param menu_id
     * @param appId_secret
     * @return
     */
    @Override
    public boolean delWeixinMenu(String status, String menu_id, String appId_secret) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_weixin_menu set  status=" + status + " where 1=1 ");
        if (null != menu_id && !"".equals(menu_id) && !"null".equals(menu_id)) {
            sql.append(" and menu_id=" + menu_id + "  ");
        }
        if (null != appId_secret && !"".equals(appId_secret) && !"null".equals(appId_secret)) {
            sql.append(" and key_code='" + appId_secret + "'  ");
        }
        commonDao.executeBySql(sql.toString());
        if (null != menu_id && !"".equals(menu_id) && !"null".equals(menu_id)) {
            delWeixinMenuSub(status, menu_id);
        }
        return true;
    }

    public boolean delWeixinMenuSub(String status, String menu_id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_weixin_menu set  status=" + status + " where menu_parent=" + menu_id + " ");
        commonDao.executeBySql(sql.toString());
        return true;
    }

    /**
     * 保存accessToken
     *
     * @param accessToken
     */
    public void saveWeixinAccessToken(TbWeixinAccessToken accessToken) {
        //删除之前的token
        commonDao.executeBySql(" update tb_weixin_access_token set status=0 where  combine_code=? ", accessToken.getCombineCode());
        commonDao.saveOrUpdate(accessToken);
    }

    /**
     * 更新登录时间
     */
    @Override
    public void updateLoinTime(int id) {
        String sql = " update tb_admin set lastlogintime=now() where admin_id=? ";
        commonDao.executeBySql(sql, id);
    }

    /**
     * 保存或者更新菜单
     *
     * @param function
     * @return
     */
    @Override
    public boolean addorupdateMenu(TbFunctions function) {
        try {
            commonDao.saveOrUpdate(function);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启用或者删除菜单(逻辑删除)
     *
     * @param function
     * @return
     */
    @Override
    public boolean deleteOrUpdateMenu(TbFunctions function) {
        try {
            String sql = " update tb_functions set `status`=? where function_id=?  ";
            commonDao.executeBySql(sql, function.getStatus(), function.getFunctionId());
            //删除下面的子菜单
//			if(function.getFunctionType()==0&&function.getStatus()==0){
//				delMenuSubs(function.getFunctionId());
//			}
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //删除子菜单
    protected void delMenuSubs(int paretId) {
        String sql = " update tb_functions set `status`=0 where function_parentid=?  ";
        commonDao.executeBySql(sql, paretId);
    }

    /**
     * 增加或者修改角色
     *
     * @param role
     */
    @Override
    public boolean addOrUpdateRole(TbRole role) {
        try {
            commonDao.saveOrUpdate(role);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 逻辑删除角色
     *
     * @param role_id
     * @param status
     */
    @Override
    public boolean delOrUpdateRole(String role_id, String status) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" update tb_role set status=?  where role_id=? ");
            commonDao.executeBySql(sql.toString(), status, role_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 增加或者修改参数
     *
     * @param param
     * @return
     */
    @Override
    public boolean addOrUpdateParam(TbParam param) {
        try {
            commonDao.saveOrUpdate(param);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 逻辑删除参数
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public boolean delParamState(String id, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_param set status=?,is_make_sure=0 where param_id=? ");
        commonDao.executeBySql(sql.toString(), status, id);
        return true;
    }

    /**
     * 删除相应角色的功能权限
     *
     * @param role_id
     */
    @Override
    public boolean delRoleFunction(String role_id) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" DELETE from tb_role_function where role_id=" + role_id + " ");
            commonDao.executeBySql(sql.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 增加或者更新角色功能
     *
     * @param roleFunction
     * @return
     */
    @Override
    public boolean addRoleFunction(TbRoleFunction roleFunction) {
        commonDao.saveOrUpdate(roleFunction);
        return true;
    }

    /**
     * 增加管理员
     *
     * @param admin
     * @return
     */
    @Override
    public boolean addAdmin(TbAdmin admin) {
        commonDao.saveOrUpdate(admin);
        return true;
    }

    /**
     * 删除或启用用户
     *
     * @param admin_id
     * @param status
     * @return
     */
    @Override
    public boolean deleteOrUseAdmin(String admin_id, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_admin set status=?,is_make_sure=0 where admin_id=? ");
        commonDao.executeBySql(sql.toString(), status, admin_id);
        return true;
    }

    /**
     * 增加或者更新用户角色
     */
    @Override
    public boolean addOrUpdateAdminRole(TbUserRole admin_role) {
        try {
            //先删除原来用户角色
            deleteAdminRoleByAdminId(admin_role.getAdminId());
            commonDao.saveOrUpdate(admin_role);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询用户是否已经存在对应角色
     *
     * @return
     */
    private boolean deleteAdminRoleByAdminId(int admin_id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" DELETE from tb_user_role where admin_id=? ");
        commonDao.executeBySql(sql.toString(), admin_id);
        //修改用户为验证状态
        String sqlUpdate = "update tb_admin set is_make_sure=0 where admin_id=?";
        commonDao.executeBySql(sqlUpdate, admin_id);
        return true;
    }

    /**
     * 删除用户角色(物理删除)
     */
    @Override
    public boolean deleteAdminRole(String admin_role_id) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" DELETE from  tb_user_role where id=? ");
            commonDao.executeBySql(sql.toString(), admin_role_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 增加或者修改渠道
     *
     * @param channel
     * @return
     */
    @Override
    public boolean addOrUpdateChannel(TbChannel channel) {
        commonDao.saveOrUpdate(channel);
        return true;
    }

    /**
     * 删除或者启用渠道(逻辑删除)
     *
     * @param channelid
     * @param status
     * @return
     */
    @Override
    public boolean deleteOrupdateChannel(String channelid, String status) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" update tb_channel set status=? where channel_id=? ");
            commonDao.executeBySql(sql.toString(), status, channelid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除或者启用分类
     *
     * @param category_id
     * @param status
     */
    @Override
    public boolean delorupdatecategory(String category_id, String status) {
        try {
            String sql = " update tb_category set `status`=? where category_id=? ";
            commonDao.executeBySql(sql, status, category_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除或者启用分类属性
     *
     * @param itemprops_id
     * @param status
     */
    @Override
    public boolean delorupdateitemprops(String itemprops_id, String status) {
        try {
            String sql = " update tb_itemprops set `status`=? where prop_id=? ";
            commonDao.executeBySql(sql, status, itemprops_id);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存或者更新分类
     *
     * @param obj
     * @return
     */
    @Override
    public boolean saveOrUpdateObj(Object obj) {
        commonDao.saveOrUpdate(obj);
        return true;
    }

    @Override
    public int saveOrUpdateItemprops(TbItemprops itemprops) {
        try {
            commonDao.saveOrUpdate(itemprops);
            return itemprops.getPropId();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除分类属性为prop_ids的集合的属性值
     *
     * @param prop_ids
     * @return
     */
    @Override
    public boolean delPropsValue(String prop_ids) {
        try {
            String sql = " update  tb_props_value set `status`=0 where  prop_id in (?) ";
            commonDao.executeBySql(sql, prop_ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 逻辑删除同一个属性下的属性值
     *
     * @param value_ids
     * @param propId
     * @return
     */
    @Override
    public boolean delPropsValueByValueIdsStatus(String value_ids, Integer propId) {
        try {
            if (null == value_ids || "".equals(value_ids)) {
                return true;
            }
            String sql = " update  tb_props_value set `status`=0 where value_id not in (" + value_ids + ") and prop_id=? ";
            commonDao.executeBySql(sql, propId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除属性值
     *
     * @param propsValue_id
     * @param status
     * @return
     */
    @Override
    public boolean delStatusPropsValue(String propsValue_id, String status) {
        try {
            String sql = " update  tb_props_value set `status`=? where  value_id =? ";
            commonDao.executeBySql(sql, status, propsValue_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除店铺
     *
     * @param shop_id
     * @param status
     * @return
     */
    @Override
    public boolean delStatusShop(String shop_id, String status) {
        try {
            String sql = " update  tb_shop set `status`=? where  shop_id =? ";
            commonDao.executeBySql(sql, status, shop_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除店铺与商家关联
     *
     * @param shopId
     * @return
     */
    public boolean delShopPartnersRel(Integer shopId) {
        try {
            String sql = " DELETE from tb_shop_partners_rel where shop_id=? ";
            commonDao.executeBySql(sql, shopId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改活动专题状态
     *
     * @param activity_id
     * @param status
     * @return
     */
    @Override
    public boolean upadteTbActivitStatus(String activity_id, String status) {
        try {
            String sql = " update  tb_activity set `status`=? where  activity_id =? ";
            commonDao.executeBySql(sql, status, activity_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改bannel状态
     *
     * @param bannel_id
     * @param status
     * @return
     */
    public boolean upadteTbBannelStatus(String bannel_id, String status) {
        try {
            String sql = " update  tb_bannel set `status`=? where  bannel_id =? ";
            commonDao.executeBySql(sql, status, bannel_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改配送地址状态
     *
     * @param status
     * @param id
     * @return
     */
    public boolean updateTbDistributionAddrStatus(String status, String id) {
        try {
            String sql = " update  tb_distribution_addr set `status`=? where  id =? ";
            commonDao.executeBySql(sql, status, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改全国地址状态
     *
     * @param status
     * @param id
     * @return
     */
    public boolean updateTbRegionStatus(int id, int status) {
        try {
            String sql = " update  tb_region set `status`=? where  region_id =? ";
            commonDao.executeBySql(sql, status, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 删除用户地址
     */
    public void updateUserAddrStatus(int addrType, String addrIds) {
        String sql = " update tb_user_addr set status=0 where area_ids=? and addr_type=? ";
        commonDao.executeBySql(sql, addrIds, addrType);
    }

    /**
     * 删除用户地址
     */
    public void updateUserAddrStatus1(int addrType, String addrIds) {
        String sql = " update tb_user_addr set status=0 where area_ids like '%" + addrIds + "%' and addr_type=? ";
        commonDao.executeBySql(sql, addrType);
    }


    /**
     * 修改规则状态
     *
     * @param status
     * @param rule_id
     * @return
     */
    public boolean updateTbRuleStatus(String status, String rule_id) {
        try {
            String sql = " update  tb_rule set `status`=?,is_make_sure=0 where  rule_id =? ";
            commonDao.executeBySql(sql, status, rule_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 保存商品
     *
     * @param goods
     * @return
     */
    public boolean saveGoods(TbGoods goods) {
        commonDao.saveOrUpdate(goods);
        return true;
    }

    /**
     * 保存商品属性
     *
     * @param attributeList
     * @param goods
     * @return
     */
    public boolean saveGoodsAttrbute(List<TbGoodsAttribute> attributeList, TbGoods goods) {
        if (null != goods) {
            for (int i = 0; i < attributeList.size(); i++) {
                TbGoodsAttribute attribute = attributeList.get(i);
                attribute.setGoodsId(goods.getGoodsId());
                commonDao.saveOrUpdate(attributeList.get(i));
            }
        } else {
            for (int i = 0; i < attributeList.size(); i++) {
                commonDao.saveOrUpdate(attributeList.get(i));
            }
        }
        return true;
    }

    /**
     * 保存商品sku
     *
     * @param skuList
     * @param goods
     * @return
     */
    public boolean saveGoodsSku(List<TbGoodsSku> skuList, TbGoods goods) {
        if (null != goods) {
            for (int i = 0; i < skuList.size(); i++) {
                TbGoodsSku sku = skuList.get(i);
                sku.setGoodsId(goods.getGoodsId());
                commonDao.saveOrUpdate(skuList.get(i));
            }
        } else {
            for (int i = 0; i < skuList.size(); i++) {
                commonDao.saveOrUpdate(skuList.get(i));
            }
        }
        return true;
    }

    /**
     * 保存商品图片
     *
     * @param imgList
     * @param relateId
     * @return
     */
    public boolean saveImg(List<TbImg> imgList, Integer relateId) {
        if (null != relateId) {
            for (int i = 0; i < imgList.size(); i++) {
                TbImg tbImg = imgList.get(i);
                tbImg.setRelateId(relateId);
                commonDao.saveOrUpdate(imgList.get(i));
            }
        } else {
            for (int i = 0; i < imgList.size(); i++) {
                commonDao.saveOrUpdate(imgList.get(i));
            }
        }
        return true;
    }

    /**
     * 更新商品状态
     *
     * @param goods
     * @return
     */
    public boolean updateGoodsStatus(TbGoods goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_goods set status=" + goods.getStatus() + ",is_putaway=1 where goods_id=" + goods.getGoodsId() + " ");
        commonDao.executeBySql(sql.toString());
        return true;
    }

    /**
     * 更新上架状态
     *
     * @param goods
     * @return
     */
    public boolean updateGoodsIsPutaway(TbGoods goods) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_goods set is_putaway=" + goods.getIsPutaway() + " where goods_id=" + goods.getGoodsId() + " ");
        commonDao.executeBySql(sql.toString());
        return true;
    }

    /**
     * 删除商品SKU
     *
     * @param goodsId
     * @return
     */
    public boolean deleteGoodsSkuByGoodsId(Integer goodsId, Integer skuId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_goods_sku where goods_id=" + goodsId + " ");
        if (null != skuId && 0 != skuId) {
            sql.append(" and sku_id=" + skuId + " ");
        }
        commonDao.executeBySql(sql.toString());
        return true;
    }

    /**
     * 删除商品属性
     *
     * @param goodsId
     * @return
     */
    public boolean deleteGoodsAttributeByGoodsId(Integer goodsId, Integer valueId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_goods_attribute where goods_id=" + goodsId + " ");
        if (null != valueId && 0 != valueId) {
            sql.append(" and value_id=" + valueId + " ");
            commonDao.executeBySql(sql.toString());
        } else {
            commonDao.executeBySql(sql.toString());
        }
        return true;
    }

    /**
     * 删除商品图片
     *
     * @param relateId
     * @param imgClassyfi 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public boolean deleteImgBuRelateId(Integer relateId, Integer imgClassyfi) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_img where relate_id=? and img_classyfi=? ");
        commonDao.executeBySql(sql.toString(), relateId, imgClassyfi);
        return true;
    }

    /**
     * 修改卡券状态
     *
     * @param ticket
     * @return
     */
    public boolean updateTicketStatus(TbTicket ticket) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_ticket set status=?,is_putaway=1 where tikcet_id=? ");
        commonDao.executeBySql(sql.toString(), ticket.getStatus(), ticket.getTikcetId());
        return true;
    }

    /**
     * 修改上架状态
     *
     * @param ticket
     * @return
     */
    public boolean updateTicketPutaway(TbTicket ticket) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_ticket set is_putaway=? where tikcet_id=? ");
        commonDao.executeBySql(sql.toString(), ticket.getIsPutaway(), ticket.getTikcetId());
        return true;
    }

    /**
     * 删除卡券属性
     *
     * @param ticketId
     * @return
     */
    public boolean deleteTicketAttributeByTicketId(Integer ticketId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_ticket_attribute where ticket_id=? ");
        commonDao.executeBySql(sql.toString(), ticketId);
        return true;
    }

    /**
     * 保存卡券属性
     *
     * @param attributeList
     * @param ticket
     * @return
     */
    public boolean saveTicketAttrbute(List<TbTicketAttribute> attributeList, TbTicket ticket) {
        if (null != ticket) {
            for (int i = 0; i < attributeList.size(); i++) {
                TbTicketAttribute ticketAttribute = attributeList.get(i);
                ticketAttribute.setTicketId(ticket.getTikcetId());
                commonDao.saveOrUpdate(ticketAttribute);
            }
        } else {
            for (int i = 0; i < attributeList.size(); i++) {
                commonDao.saveOrUpdate(attributeList.get(i));
            }
        }

        return true;
    }

    /**
     * 删除规则关联
     *
     * @param id
     * @return
     */
    public boolean delRuleRel(int id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_rule_rel where id=? ");
        commonDao.executeBySql(sql.toString(), id);
        return true;
    }

    public boolean delRuleRuleId(int id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_rule_rel where rule_id=? ");
        commonDao.executeBySql(sql.toString(), id);
        return true;
    }

    /**
     * 保存数据
     *
     * @param obj
     * @return
     */
    public boolean saveObject(Object obj) {
        commonDao.saveOrUpdate(obj);
        return true;
    }

    /**
     * 修改商家状态
     *
     * @param id
     * @param status
     * @return
     */
    public boolean updatePartnersStatus(Integer id, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_partners set status=? where id=? ");
        commonDao.executeBySql(sql.toString(), status, id);
        return true;
    }

    /**
     * 删除兑换权限关联
     *
     * @param partnersId
     * @return
     */
    public boolean delExchangeId(Integer relId, Integer ticketId, Integer partnersId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tb_exchange_jurisdiction where 1=1  ");
        if (null != relId) {
            sql.append(" and id=" + relId + " ");
        }
        if (null != ticketId) {
            sql.append(" and tikcet_id=" + ticketId + "  ");
        }
        if (null != partnersId) {
            sql.append(" and partners_id=" + partnersId + " ");
        }

        commonDao.executeBySql(sql.toString());
        return true;
    }

    /**
     * 修改一级菜单状态
     *
     * @param leveId
     * @param status
     * @return
     */
    public boolean updateCatogryLeve1Status(String leveId, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_catogry_level1 set status=? where level_id=? ");
        commonDao.executeBySql(sql.toString(), status, leveId);
        return true;
    }

    /**
     * 删除分类类目关系
     *
     * @param leveId
     */
    public void deleteClassfyCatogryRel(Integer leveId) {
        String sql = " delete from tb_classyfy_catogry_rel where level_id=? ";
        commonDao.executeBySql(sql.toString(), leveId);
    }

    @Override
    public void updateSortByTableName(String tableName, Integer sort) {
        String sql = " update tb_functions set sort=sort-1 WHERE sort<=" + sort + " and sort !=0 ";
        commonDao.executeBySql(sql);
    }

    @Override
    public void updateSortMenuByTableName(String tableName, Integer parentId, Integer sort) {
        String sql = sql = " update tb_functions set sort=sort-1 WHERE sort<=" + sort + " and sort !=0 and function_parentid=" + parentId + " ";
        commonDao.executeBySql(sql);
    }

    /**
     * 用户确认
     *
     * @param ids
     */
    public void updateUserConfirm(String ids) {
        String sql = " update tb_admin set is_make_sure=1 where admin_id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 更新确认参数
     *
     * @param ids
     */
    public void updateParamConfirm(String ids) {
        String sql = " update tb_param set is_make_sure=1 where param_id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 更新上架卡券参数
     *
     * @param ids
     */
    public void updateTicketConfirm(String ids) {
        String sql = " update tb_ticket set is_putaway=2 where tikcet_id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 更新上架商品参数
     *
     * @param ids
     */
    public void updateGoodsConfirm(String ids) {
        String sql = " update tb_goods set is_putaway=2 where goods_id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 规则确认
     *
     * @param ids
     */
    public void updateRuleConfirm(String ids) {
        String sql = " update tb_rule set is_make_sure=1 where rule_id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 规则关联确认
     *
     * @param ids
     */
    public void updateRuleRelConfirm(String ids) {
        String sql = " update tb_rule_rel set is_make_sure=1 where id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 兑换确认
     *
     * @param ids
     */
    public void updateExchangeConfirm(String ids) {
        String sql = " update tb_exchange_jurisdiction set is_make_sure=1 where id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }

    /**
     * 商品排序确认
     *
     * @param strs
     */
    public void updateGoodssortConfirm(String strs) {
        String strArr[] = strs.split(";");
        for (int i = 0; i < strArr.length; i++) {
            String str[] = strArr[i].split("-");
            String goodsId = str[0];
            String sort = str[1];
            String sql = " update tb_goods set weight=? where goods_id=? ";
            commonDao.executeBySql(sql, sort, goodsId);
        }
    }

    /**
     * 卡券排序确认
     *
     * @param strs
     */
    public void updateTicketsortConfirm(String strs) {
        String strArr[] = strs.split(";");
        for (int i = 0; i < strArr.length; i++) {
            String str[] = strArr[i].split("-");
            String ticketId = str[0];
            String sort = str[1];
            String sql = " update tb_ticket set weight=? where tikcet_id=? ";
            commonDao.executeBySql(sql, sort, ticketId);
        }
    }

    /**
     * 抢购商品排序
     *
     * @param sortStr
     */
    public void updatetimeGoodsSortConfirm(String sortStr) {
        String strArr[] = sortStr.split(";");
        for (int i = 0; i < strArr.length; i++) {
            String str[] = strArr[i].split("-");
            String ticketId = str[0];
            String sort = str[1];
            String sql = " update tb_time_goods set weight=? where id=? ";
            commonDao.executeBySql(sql, sort, ticketId);
        }
    }

    /**
     * 抢购商品确认
     *
     * @param ids
     */
    public void updatetimeGoodsConfirm(String ids) {
        String sql = " update tb_time_goods set is_make_sure=1 where id in (" + ids + ") ";
        commonDao.executeBySql(sql);
    }


    /**
     * 修改抢购商品状态为0和 修改商品抢购状态为0
     *
     * @param goodsId
     */
    public void updateTimeGoodsStatus(Integer goodsId, String loginName) {
        // 修改抢购商品状态为0和 修改商品抢购状态为0
        String sqltimeGoods = " update tb_time_goods set status=0,lastaltertime=now(),lastalterman='" + loginName + "' where goods_id=" + goodsId + " ";
        commonDao.executeBySql(sqltimeGoods);
        String sqlGoods = " update tb_goods set is_time_goods=0 where goods_id=" + goodsId + " ";
        commonDao.executeBySql(sqlGoods);
    }

    /**
     * 更新商品为抢购商品
     *
     * @param goodsId
     */
    public void updateGoodsTimeGoods(Integer goodsId, Integer isTimeGoods) {
        String sql = " update tb_goods set is_time_goods=" + isTimeGoods + " where goods_id=" + goodsId + " ";
        commonDao.executeBySql(sql);
    }

    /**
     * 修改购物车商品
     *
     * @param goodsId
     */
    public void updateOrderCart(int goodsId, int status) {
        String sql = " update tb_order_cart set status=" + status + " where goods_id=" + goodsId + " ";
        commonDao.executeBySql(sql);
    }

    public void updateOrderCart(String goodsIds, int status) {
        String sql = " update tb_order_cart set status=" + status + " where goods_id in(" + goodsIds + ") ";
        commonDao.executeBySql(sql);
    }

    public boolean updateMainMenuStatus(int id, int status) {
        String sql = " update tb_main_menu set `status`=" + status + " where id=" + id + " ";
        commonDao.executeBySql(sql);
        return true;
    }

    public boolean updateMainMenuSort(int id, int menuSort) {
        String sql = " update tb_main_menu set menu_sort=" + menuSort + " where id=" + id + " ";
        commonDao.executeBySql(sql);
        return true;
    }

    public void updateTbImgUploadNameRecord(int id, int status, String tableName) {
        String sql = " update tb_img_upload_name_record set img_status=?,table_name=?,update_time=now() where id=? ";
        commonDao.executeBySql(sql, status, tableName, id);
    }

    public void updateTbImgUploadNameRecord(int id, String tableName) {
        String sql = " update tb_img_upload_name_record set table_name=?,update_time=now() where id=? ";
        commonDao.executeBySql(sql, tableName, id);
    }

    public void updateTbImgUploadNameRecord(int id, int status) {
        String sql = " update tb_img_upload_name_record set img_status=?,update_time=now() where id=? ";
        commonDao.executeBySql(sql, status, id);
    }

    public void delTbImgUploadNameRecord(int id) {
        String sql = " delete from tb_img_upload_name_record where id=? ";
        commonDao.executeBySql(sql, id);
    }

    public boolean updateWapUserStatus(int userId, int status) {
        String sql = " update tb_user_info set user_state=?  where user_id=? ";
        commonDao.executeBySql(sql, status, userId);
        return true;
    }


}
