package com.ajaxjs.shop.dao;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;

@TableName(value = "shop_cart", beanClass = Cart.class)
public interface CartDao extends IBaseDao<Cart> {
	public static final String sql = "SELECT e.*, g.id AS goodsId, g.name AS goodsName, g.cover, f.name AS goodsFormat, f.price FROM ${tableName} e "
			+ "INNER JOIN shop_goods_format f ON e.goodsFormatId = f.id "
			+ "INNER JOIN shop_goods g ON f.goodsId = g.id" + WHERE_REMARK_ORDER;

	@Select(sql)
	@Override
	public PageResult<Cart> findPagedList(int start, int limit, Function<String, String> sqlHandler);

	@Select(sql)
	@Override
	public List<Cart> findList(Function<String, String> sqlHandler);

	/**
	 * 获取购物车总数
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT COUNT(*) AS count FROM ${tableName} WHERE userId = ?")
	public int getCartListCountByUserId(long userId);

	@Update("UPDATE ${tableName} SET goodsNumber = ? WHERE id = ? AND userId = ?")
	public int updateGoodsNumber(int goodsNumber, long id, long userId);

	/**
	 * 删除购物车
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	@Delete("DELETE FROM ${tableName} WHERE id = ? AND userId = ?")
	public boolean deleteMyCart(long id, long userId);
}
