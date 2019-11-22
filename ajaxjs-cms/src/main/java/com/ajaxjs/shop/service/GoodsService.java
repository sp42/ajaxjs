package com.ajaxjs.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.cms.controller.TopicController;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.Goods;

/**
 * 商品
 * 
 * @author Frank Cheung
 *
 */
@Bean
public class GoodsService extends BaseService<Goods> {
	public static GoodsDao dao = new Repository().bind(GoodsDao.class);

	{
		setUiName("商品");
		setShortName("goods");
		setDao(dao);
	}

	@Resource("GoodsFormatService")
	private GoodsFormatService goodsFormatService;

	@Resource("Attachment_pictureService")
	private Attachment_pictureService pictureService;

	@Resource("CartService")
	private CartService shopCartService;

	@TableName(value = "shop_goods", beanClass = Goods.class)
	public static interface GoodsDao extends IBaseDao<Goods> {
		@Select(value = "SELECT entry.*, gc.name AS catelogName, " + selectCover + " AS cover, "
				+ "(SELECT GROUP_CONCAT(id, '|', name, '|', price, '|', uid, '|', coverPrice) FROM shop_goods_format f WHERE entry.id = f.goodsId ) AS formats " + "FROM shop_goods entry " + catelog_simple_join
				+ " ORDER BY entry.id DESC", 
				countSql = "SELECT COUNT(id) AS count FROM ${tableName}")
		PageResult<Map<String, Object>> findGoods_Format(int start, int limit);
		
		
		@Select("SELECT o.buyerId, o.createDate, u.username, u.avatar FROM shop_order_item o "+
			"INNER JOIN user u ON u.id = o.buyerId "+
			"INNER JOIN shop_goods entry ON entry.id = o.goodsId "+
			"INNER JOIN shop_group g ON g.goodsId = entry.id " +
			"WHERE g.id = ?")
		List<Map<String, Object>> findWhoBoughtGoods(long goodsId);
	}

	/**
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public PageResult<Map<String, Object>> findGoods_Format(int start, int limit) {
		return dao.findGoods_Format(start, limit);
	}

	/**
	 * Get goods' detail.
	 * 
	 * @param id
	 * @param userId the id that user has login, if not 0L
	 * @return Map as a ViewObject
	 */
	public Map<String, Object> getGoodsDetail(long id, long userId) {
		Goods goods = dao.findById_catelog_avatar(id);
		Map<String, Object> map = new HashMap<>();

		map.put("info", goods);
		map.put("formats", goodsFormatService.findByGoodsId(goods.getId()));
		map.put("pics", pictureService.findAttachmentPictureByOwner(goods.getUid())); // banner images
		map.put("userHasCollect", userId == 0L ? false : ShopBookmarkService.userHasCollect(userId, id, ShopConstant.ENTRY_GOODS));
		map.put("cartGoodsCount", userId == 0L ? 0 : CartService.dao.getCartListCountByUserId(userId));

		return map;
	}

	static {
		DataDictService.Entry_IdName.put(ShopConstant.ENTRY_GOODS, new GoodsService().getUiName());
		DataDictService.Entry_IdName.put(ShopConstant.ENTRY_GROUP, new GroupService().getUiName());
		DataDictService.Entry_IdName.put(DataDictService.ENTRY_TOPIC, TopicController.service.getUiName());
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.goodsCatalog_Id");
	}

	@Override
	public PageResult<Goods> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit, null);
	}
 
	public PageResult<Goods> findPagedListByCatalogId(int catelogId, int start, int limit) {
		return dao.findPagedListByCatelogId_Cover(catelogId == 0 ? getDomainCatalogId() : catelogId, start, limit, QueryParams.initSqlHandler(QueryParams.init()));
	}
}