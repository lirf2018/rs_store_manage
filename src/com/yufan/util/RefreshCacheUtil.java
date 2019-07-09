package com.yufan.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/14 15:45
 * 功能介绍:  更新缓存
 */
public class RefreshCacheUtil {
    private volatile static RefreshCacheUtil refreshCacheUtil;

    public static RefreshCacheUtil getInstence() {
        if (null == refreshCacheUtil) {
            synchronized (RefreshCacheUtil.class) {
                if (null == refreshCacheUtil) {
                    refreshCacheUtil = new RefreshCacheUtil();
                }
            }
        }
        return refreshCacheUtil;
    }

    /**
     * 更新商品
     *
     * @return
     */
    public JSONObject refreshGoods(int goodsId) {

        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "更新缓存失败");
        try {
            //调用接口
            JSONObject obj = new JSONObject();
            obj.put("goods_id", goodsId);

            ResultBean resultBean = CommonRequst.executeNew(obj, "refresh_goods");
            if (null != resultBean && resultBean.getRespCode().intValue() == 1) {
                out.put("flag", 1);
                out.put("msg", "更新缓存成功");
            } else {
                if (null == resultBean) {
                    out.put("flag", 4);
                    out.put("msg", "更新缓存失败");
                } else {
                    out.put("flag", resultBean.getRespCode().intValue());
                    out.put("msg", resultBean.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 更新参数
     *
     * @return
     */
    public JSONObject refreshParam() {

        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "更新缓存失败");
        try {
            //调用接口
            JSONObject obj = new JSONObject();

            ResultBean resultBean = CommonRequst.executeNew(obj, "refresh_param");
            if (null != resultBean && resultBean.getRespCode().intValue() == 1) {
                out.put("flag", 1);
                out.put("msg", "更新缓存成功");
            } else {
                if (null == resultBean) {
                    out.put("flag", 4);
                    out.put("msg", "更新缓存失败");
                } else {
                    out.put("flag", resultBean.getRespCode().intValue());
                    out.put("msg", resultBean.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 更新商家
     *
     * @return
     */
    public JSONObject refreshParters(int partersId) {

        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "更新缓存失败");
        try {
            //调用接口
            JSONObject obj = new JSONObject();
            obj.put("partners_id", partersId);

            ResultBean resultBean = CommonRequst.executeNew(obj, "refresh_partners");
            if (null != resultBean && resultBean.getRespCode().intValue() == 1) {
                out.put("flag", 1);
                out.put("msg", "更新缓存成功");
            } else {
                if (null == resultBean) {
                    out.put("flag", 4);
                    out.put("msg", "更新缓存失败");
                } else {
                    out.put("flag", resultBean.getRespCode().intValue());
                    out.put("msg", resultBean.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 更新平台
     *
     * @return
     */
    public JSONObject refreshPlatform(int addrId) {

        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "更新缓存失败");
        try {
            //调用接口
            JSONObject obj = new JSONObject();
            obj.put("addr_id", addrId);

            ResultBean resultBean = CommonRequst.executeNew(obj, "refresh_platform");
            if (null != resultBean && resultBean.getRespCode().intValue() == 1) {
                out.put("flag", 1);
                out.put("msg", "更新缓存成功");
            } else {
                if (null == resultBean) {
                    out.put("flag", 4);
                    out.put("msg", "更新缓存失败");
                } else {
                    out.put("flag", resultBean.getRespCode().intValue());
                    out.put("msg", resultBean.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 更新店铺
     *
     * @return
     */
    public JSONObject refreshShop(int shopId) {

        JSONObject out = new JSONObject();
        out.put("flag", 0);
        out.put("msg", "更新缓存失败");
        try {
            //调用接口
            JSONObject obj = new JSONObject();
            obj.put("shop_id", shopId);

            ResultBean resultBean = CommonRequst.executeNew(obj, "refresh_shop");
            if (null != resultBean && resultBean.getRespCode().intValue() == 1) {
                out.put("flag", 1);
                out.put("msg", "更新缓存成功");
            } else {
                if (null == resultBean) {
                    out.put("flag", 4);
                    out.put("msg", "更新缓存失败");
                } else {
                    out.put("flag", resultBean.getRespCode().intValue());
                    out.put("msg", resultBean.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
}
