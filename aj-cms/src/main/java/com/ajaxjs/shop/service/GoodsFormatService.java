package com.ajaxjs.shop.service;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.shop.model.GoodsFormat;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

/**
 * 商品规格
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class GoodsFormatService extends BaseService<GoodsFormat> {
	@TableName(value = "shop_goods_format", beanClass = GoodsFormat.class)
	public static interface GoodsFormatDao extends IBaseDao<GoodsFormat> {
	}

	public static GoodsFormatDao dao = new Repository().bind(GoodsFormatDao.class);

	{
		setUiName("商品规格型号");
		setShortName("goodsFormat");
		setDao(dao);
	}

	/**
	 * 获取商品的所有规格
	 * 
	 * @param goodsId 商品 id
	 * @return 商品的所有规格
	 */
	public List<GoodsFormat> findByGoodsId(long goodsId) {
		return dao.findList(by("goodsId", goodsId));
	}
}