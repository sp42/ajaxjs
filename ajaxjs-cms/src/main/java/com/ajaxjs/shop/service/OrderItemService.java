package com.ajaxjs.shop.service;

import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService; 

@Bean
public class OrderItemService extends BaseService<OrderItem> {
	@TableName(value = "shop_order_item", beanClass = OrderItem.class)
	public static interface OrderItemDao extends IBaseDao<OrderItem> {
		@Select("SELECT i.*, o.orderNo, o.tradeStatus, o.payStatus, o.orderPrice, o.totalPrice, "
				+ "entry.name AS goodsName, " + selectCover + " AS cover  FROM ${tableName} i INNER JOIN shop_order_info o ON i.orderId = o.id INNER JOIN shop_goods entry ON i.goodsId = entry.id WHERE 1=1 ORDER BY i.id DESC")
		public PageResult<OrderItem> findOrderItemDetailList(int start, int limit, Function<String, String> doSql);
		
		@Select("SELECT e.*, o.*, g.name AS goodsName, f.name AS formatName FROM ${tableName} e "
				+ "INNER JOIN shop_order_info o ON e.orderId = o.id "
				+ "INNER JOIN shop_goods g ON e.goodsId = g.id "
				+ "INNER JOIN shop_goods_format f ON e.goodsFormatId = f.id WHERE e.id = ?")
		@Override
		public OrderItem findById(Long id);
	}

	public static OrderItemDao dao = new Repository().bind(OrderItemDao.class);

	{
		setUiName("订单明细");
		setShortName("orderItem");
		setDao(dao);
	}
	
	public PageResult<OrderItem> findOrderItemDetailList(int start, int limit, long p, long sellerId, Map<String, String[]> map) {
		return dao.findOrderItemDetailList(start, limit, oldSql -> {
			// 权限校验
			if (RoleService.check(p, RightConstant.SHOP_SELLER) && sellerId != 0) {
				oldSql = oldSql.replaceAll("1=1", "1 = 1 AND i.sellerId = " + sellerId);
			}
			
			if (map != null && map.size() > 0) {
				oldSql = new QueryParams(map).addWhereToSql(oldSql);
			}
			
			return oldSql;
		});
	}
}