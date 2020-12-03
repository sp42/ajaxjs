package com.ajaxjs.shop.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.common.AttachmentService;
import com.ajaxjs.cms.common.TreeLikeService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.shop.dao.GoodsDao;
import com.ajaxjs.shop.model.Goods;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;

/**
 * 商品
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class GoodsService extends BaseService<Goods> {
	public static GoodsDao dao = new Repository().bind(GoodsDao.class);

	{
		setUiName("商品");
		setShortName("goods");
		setDao(dao);
	}

	@Resource("GoodsFormatService")
	private GoodsFormatService goodsFormatService;

	@Resource
	private AttachmentService pictureService;

	@Resource("CartService")
	private CartService shopCartService;

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
//		map.put("pics", pictureService.findAttachmentPictureByOwner(goods.getUid())); // banner images
//		map.put("userHasCollect", userId == 0L ? false : ShopBookmarkService.userHasCollect(userId, id, ShopConstant.ENTRY_GOODS));
//		map.put("cartGoodsCount", userId == 0L ? 0 : CartService.dao.getCartListCountByUserId(userId));

		return map;
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.productCatalog_Id");
	}

	static Function<String, String> noContent = sql -> sql.replace("*", "id, name, cover, coverPrice, stat, titlePrice, createDate, catalogId, brand, sellerId");
	
	public PageResult<Goods> findPagedListByCatalogId(int catalogId, int start, int limit, int status, int sellerId) {
		Function<String, String> sqlHander = TreeLikeService.setCatalog(catalogId, getDomainCatalogId())
				.andThen(setStatus(status)).andThen(BaseService::searchQuery).andThen(BaseService::betweenCreateDate).andThen(noContent);
				
//				.andThen(BaseService::betweenCreateDate);

		if (sellerId != 0)
			sqlHander.andThen(by("sellerId", sellerId));

		return dao.findPagedList(start, limit, sqlHander);
	}
}