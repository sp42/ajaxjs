package com.ajaxjs.shop.dao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.catalog.CatalogDao;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.Goods;

@TableName(value = "shop_goods", beanClass = Goods.class)
public interface GoodsDao extends IBaseDao<Goods> {
	@Select(value = "SELECT e.*, gc.name AS catalogName, "
			+ "(SELECT GROUP_CONCAT(id, '|', name, '|', price, '|', uid, '|', coverPrice) FROM shop_goods_format f WHERE e.id = f.goodsId ) AS formats "
			+ "FROM ${tableName} e " + CatalogDao.LEFT_JOIN_CATALOG + DESCENDING_ID, 
			countSql = "SELECT COUNT(id) AS count FROM ${tableName}")
	PageResult<Map<String, Object>> findGoods_Format(int start, int limit);

	@Select("SELECT o.buyerId, o.createDate, u.username, u.avatar FROM shop_order_item o "
			+ "INNER JOIN user u ON u.id = o.buyerId INNER JOIN ${tableName} e ON e.id = o.goodsId "
			+ "INNER JOIN shop_group g ON g.goodsId = e.id WHERE e.id = ?")
	List<Map<String, Object>> findWhoBoughtGoods(long goodsId);
	
	/**
	 * 查询单个记录，带有类别的。如果找不到则返回 null
	 * 
	 * @param id         记录 id
	 * @param sqlHandler 查找的条件
	 * @return 单个记录
	 */
	@Select(CatalogDao.FIND_BY_ID)
	public Goods findById_catalog(Long id, Function<String, String> sqlHandler);
}