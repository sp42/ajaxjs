package com.ajaxjs.shop.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.controller.DataDictController;
import com.ajaxjs.cms.controller.TopicController;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.dao.GoodsDao;
import com.ajaxjs.shop.dep.GroupService;
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

	static {
		DataDictController.DataDictService.Entry_IdName.put(ShopConstant.ENTRY_GOODS, new GoodsService().getUiName());
		DataDictController.DataDictService.Entry_IdName.put(ShopConstant.ENTRY_GROUP, new GroupService().getUiName());
		DataDictController.DataDictService.Entry_IdName.put(DataDictController.DataDictService.ENTRY_TOPIC,
				TopicController.service.getUiName());
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
		// 查询单个记录，带有类别的。如果找不到则返回 null
		Goods goods = dao.find(by("id", id));
		Map<String, Object> map = new HashMap<>();

		map.put("info", goods);
		map.put("formats", goodsFormatService.findByGoodsId(goods.getId()));
		map.put("pics", pictureService.findAttachmentPictureByOwner(goods.getUid())); // banner images
//		map.put("userHasCollect", userId == 0L ? false : ShopBookmarkService.userHasCollect(userId, id, ShopConstant.ENTRY_GOODS));
//		map.put("cartGoodsCount", userId == 0L ? 0 : CartService.dao.getCartListCountByUserId(userId));

		return map;
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.productCatalog_Id");
	}

	public PageResult<Goods> findPagedListByCatalogId(int catalogId, int start, int limit, int status, int sellerId) {
		Function<String, String> sqlHander = CatalogService.setCatalog(catalogId, getDomainCatalogId())
				.andThen(setStatus(status)).andThen(BaseService::searchQuery).andThen(BaseService::betweenCreateDate);

		if (sellerId != 0)
			sqlHander.andThen(by("sellerId", sellerId));

		return dao.findPagedList(start, limit, sqlHander);
	}
}