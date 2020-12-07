package com.ajaxjs.shop.service;

import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.web.mvc.MvcRequest;

@Component
public class OrderItemService extends BaseService<OrderItem> {
	@TableName(value = "shop_order_item", beanClass = OrderItem.class)
	public static interface OrderItemDao extends IBaseDao<OrderItem> {
		@Select("SELECT e.*, o.orderNo, o.tradeStatus, o.payStatus, o.orderPrice, o.totalPrice, o.payDate, g.name AS goodsName FROM ${tableName} e "
				+ "INNER JOIN shop_order_info o ON e.orderId = o.id " + "INNER JOIN shop_goods g ON e.goodsId = g.id" + WHERE_REMARK_ORDER)
		@Override
		public PageResult<OrderItem> findPagedList(int start, int limit, Function<String, String> doSql);

		@Select("SELECT e.*, o.*, g.name AS goodsName, f.name AS formatName FROM ${tableName} e " + "INNER JOIN shop_order_info o ON e.orderId = o.id "
				+ "INNER JOIN shop_goods g ON e.goodsId = g.id " + "INNER JOIN shop_goods_format f ON e.goodsFormatId = f.id WHERE e.id = ?")
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
	 * 时间范围的查询
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String betweenCreateDate(String sql) {
		return betweenCreateDate("o.createDate", MvcRequest.getHttpServletRequest()).apply(sql);
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
		Function<String, String> sqlHander = byAny().andThen(OrderItemService::betweenCreateDate);

		if (sellerId != 0 && RoleService.check(p, RightConstant.SHOP_SELLER))
			sqlHander = sqlHander.andThen(by("e.sellerId", sellerId));

		return dao.findPagedList(start, limit, sqlHander);
	}
}