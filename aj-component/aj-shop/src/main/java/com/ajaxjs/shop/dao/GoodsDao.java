package com.ajaxjs.shop.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.entity.service.TreeLikeService;
import com.ajaxjs.shop.model.Goods;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;

@TableName(value = "shop_goods", beanClass = Goods.class)
public interface GoodsDao extends IBaseDao<Goods> {
	@Select(value = "SELECT e.*, gc.name AS catalogName, "
			+ "(SELECT GROUP_CONCAT(id, '|', name, '|', price, '|', uid, '|', coverPrice) FROM shop_goods_format f WHERE e.id = f.goodsId ) AS formats "
			+ "FROM ${tableName} e " + TreeLikeService.TreeLikeoDao.LEFT_JOIN_CATALOG
			+ DESCENDING_ID, countSql = "SELECT COUNT(id) AS count FROM ${tableName}")
	PageResult<Map<String, Object>> findGoods_Format(int start, int limit);

	@Select("SELECT o.buyerId, o.createDate, u.username, u.avatar FROM shop_order_item o "
			+ "INNER JOIN user u ON u.id = o.buyerId INNER JOIN ${tableName} e ON e.id = o.goodsId "
			+ "INNER JOIN shop_group g ON g.goodsId = e.id WHERE e.id = ?")
	List<Map<String, Object>> findWhoBoughtGoods(long goodsId);
}