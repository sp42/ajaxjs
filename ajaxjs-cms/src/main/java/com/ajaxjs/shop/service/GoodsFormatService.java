package com.ajaxjs.shop.service;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.GoodsFormat;

/**
 * 商品规格
 * 
 * @author Frank Cheung
 *
 */
@Bean
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
		return dao.findList(setWhere("goodsId = " + goodsId));
	}
}