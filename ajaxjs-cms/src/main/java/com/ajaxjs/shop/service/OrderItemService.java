package com.ajaxjs.shop.service;

import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
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
		@Select("SELECT e.*, o.orderNo, o.tradeStatus, o.payStatus, o.orderPrice, o.totalPrice, g.name AS goodsName FROM ${tableName} e "
				+ "INNER JOIN shop_order_info o ON e.orderId = o.id " + "INNER JOIN shop_goods g ON e.goodsId = g.id"
				+ WHERE_REMARK_ORDER)
		@Override
		public PageResult<OrderItem> findPagedList(int start, int limit, Function<String, String> doSql);

		@Select("SELECT e.*, o.*, g.name AS goodsName, f.name AS formatName FROM ${tableName} e "
				+ "INNER JOIN shop_order_info o ON e.orderId = o.id " + "INNER JOIN shop_goods g ON e.goodsId = g.id "
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

	/**
	 * 分页，有 sellerId 权限控制，以支持多用户
	 * 
	 * @param start
	 * @param limit
	 * @param p
	 * @param sellerId
	 * @return
	 */
	public PageResult<OrderItem> findPagedList(int start, int limit, long p, long sellerId) {
		return dao.findPagedList(start, limit,
				(sellerId != 0 && RoleService.check(p, RightConstant.SHOP_SELLER)) ? by("e.sellerId", sellerId) : null);
	}
}