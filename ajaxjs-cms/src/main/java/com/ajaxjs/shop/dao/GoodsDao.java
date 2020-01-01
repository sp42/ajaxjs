package com.ajaxjs.shop.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.Goods;

@TableName(value = "shop_goods", beanClass = Goods.class)
public interface GoodsDao extends IBaseDao<Goods> {
	@Select(value = "SELECT entry.*, gc.name AS catalogName, " + selectCover + " AS cover, "
			+ "(SELECT GROUP_CONCAT(id, '|', name, '|', price, '|', uid, '|', coverPrice) FROM shop_goods_format f WHERE entry.id = f.goodsId ) AS formats "
			+ "FROM shop_goods entry " + catelog_simple_join
			+ " ORDER BY entry.id DESC", 
			countSql = "SELECT COUNT(id) AS count FROM ${tableName}")
	PageResult<Map<String, Object>> findGoods_Format(int start, int limit);

	@Select("SELECT o.buyerId, o.createDate, u.username, u.avatar FROM shop_order_item o "
			+ "INNER JOIN user u ON u.id = o.buyerId " + "INNER JOIN shop_goods entry ON entry.id = o.goodsId "
			+ "INNER JOIN shop_group g ON g.goodsId = entry.id " + "WHERE g.id = ?")
	List<Map<String, Object>> findWhoBoughtGoods(long goodsId);
}