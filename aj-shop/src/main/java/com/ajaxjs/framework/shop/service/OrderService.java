package com.ajaxjs.framework.shop.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.data_service.api.ApiController;
import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.framework.shop.model.Goods;
import com.ajaxjs.net.http.HttpEnum;
import com.ajaxjs.wechat.Constant;
import com.ajaxjs.wechat.applet.model.LoginSession;

/**
 * 订单处理
 * 
 * @author Frank Cheung
 *
 */
public abstract class OrderService implements Constant, HttpEnum {
	/**
	 * 谁买了些什么？
	 * 
	 * @param session 相当于用户 id
	 * @param goodsId 商品 id
	 * @return 预支付的参数
	 */
	abstract public Map<String, Object> preOrder(LoginSession session, long goodsId);

//	/**
//	 * 支付平台得到交易 id 后，更新到 Order 表
//	 * 
//	 * @param orderId       订单 id
//	 * @param transactionId 交易 id
//	 */
//	abstract public void updateOrderTransactionId(Long orderId, String transactionId);

	@Autowired
	private ApiController c;

	final static GoodsDao goodsDao = new Caller("cms", "cms_article").bind(GoodsDao.class, Goods.class);

	public Goods findGoodsById(long goodsId) {
		c.initCache();
		return goodsDao.findById(goodsId);
	}
}
