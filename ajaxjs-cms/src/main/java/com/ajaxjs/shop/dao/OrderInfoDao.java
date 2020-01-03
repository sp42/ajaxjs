package com.ajaxjs.shop.dao;

import java.util.List;

import com.ajaxjs.cms.app.attachment.Attachment_pictureDao;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;

@TableName(value = "shop_order_info", beanClass = OrderInfo.class)
public interface OrderInfoDao extends IBaseDao<OrderInfo> {
//	@Select("SELECT oInfo.*, (SELECT FROM shop_order_item INNER JOIN shop_goods WHERE orderId = oInfo.id) AS goodsNames FROM ${tableName} oInfo WHERE buyerId = ?")
//	@Select("SELECT oInfo.* FROM ${tableName} oInfo WHERE buyerId = ?")
//	List<OrderInfo> getOrderListByUserId(long userId);
//	
//	@Select("SELECT oInfo.* FROM ${tableName} oInfo WHERE orderNo = ?")
//	OrderInfo getOrderByOrderNo(String orderNo);
	
	public static final String list = "SELECT o.*, entry.name, o.goodsNumber, o.goodsPrice, o.goodsAmount, f.name AS formatName, (" 
+ Attachment_pictureDao.LINK_COVER + ") AS cover FROM shop_order_item o INNER JOIN shop_goods entry ON entry.id = o.goodsId INNER JOIN shop_goods_format f ON f.id = o.goodsFormatId ";
	
	@Select(list + " WHERE o.orderId = ?")
	List<OrderItem> findOrderItemListByOrderId(long orderId);
	
	
	@Select("SELECT id FROM shop_order_item WHERE orderId = ?")
	List<OrderItem> findOrderItemListByOrderId_Simple(long orderId);

	@Select("SELECT oauthId FROM user_oauth o WHERE o.userId = ?")
	String findUserOpenId(long userId);
}