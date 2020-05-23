 package com.ajaxjs.app.service;

import java.util.List;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.shop.ShopConstant;

/**
 * 收藏
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class ShopBookmarkService extends SectionListService {
	{
		setUiName("用户收藏");
		setShortName("bookmark");
	}

	private static ScanTable fn = (sqls, entryTypeId, entryIds, caseSql) -> {
		switch (entryTypeId) {
		case ShopConstant.ENTRY_GOODS:
			sqls.add(String.format(SELECT, entryTypeId, caseSql, "shop_goods", entryIds));
			break;
		case ShopConstant.ENTRY_GROUP:
			sqls.add(String.format(SectionService.SELECT_GROUPS, entryTypeId, caseSql.replaceAll("WHEN entry.id", "WHEN g.id") /* 修正一下，别名有所不同 */, entryIds));
			break;
		}
	};
	
	@Override
	ScanTable getScanTable() {
		return fn;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Long checkBeforeCreate(SectionList bean, long userId) throws ServiceException {
		if (dao.checkIfExist(bean.getEntryId(), bean.getEntryTypeId(), userId) != null) {
			throw new ServiceException("你已收藏过啦～");
		} else
			return create(bean);
	}

	/**
	 * Find bookmarks by user id
	 * 
	 * @param userId
	 * @return
	 */
	public List<SectionList> findBookmarks(Long userId) {
		return union(getListByUserId(userId));
	}

	/**
	 * Find all bookmarks, For admin use
	 * 
	 * @return
	 */
	public List<SectionList> findBookmarks() {
		return union(getListByCatalogId(ConfigService.getValueAsInt("data.section.useBookmark_Catalog_Id")));
	}
	
	/**
	 * 可分页的
	 * 
	 * @param start
	 * @param limit
	 * @param sectionId
	 * @return
	 */
	public PageResult<SectionList> findSectionListBySectionId(int start, int limit) {
		return super.findListBySectionId(start, limit, ConfigService.getValueAsInt("data.section.useBookmark_Catelog_Id"));
	}

	@Override
	public Long create(SectionList bean) {
		bean.setCatalogId(ConfigService.getValueAsInt("data.section.useBookmark_Catelog_Id"));
		return super.create(bean);
	}

	/**
	 * 根据用户 id查找他所有的收藏商品
	 * 
	 * @param userId
	 * @return
	 */
	public List<SectionList> getListByUserId(long userId) {
		return dao.findList(sql -> sql + " WHERE userId = " + userId);
	}

	/**
	 * 用户是否已经收藏
	 * 
	 * @param userId
	 * @param entryId
	 * @param entryTypeId
	 * @return true = 未收藏
	 */
	public static boolean userHasCollect(long userId, long entryId, int entryTypeId) {
		return dao.checkIfExist(entryId, entryTypeId, userId) != null;
	}

	/**
	 * 切换收藏状态
	 * 
	 * @param userId
	 * @param entryId
	 * @param entryTypeId
	 * @return
	 */
	public String toggle(long userId, long entryId, int entryTypeId) {
		Long existId = dao.checkIfExist(entryId, entryTypeId, userId);
		SectionList bean = new SectionList();

		if (existId == null) {
			bean.setEntryId(entryId);
			bean.setEntryTypeId(entryTypeId);
			bean.setUserId(userId);

			if (create(bean) != null) {
				return BaseController.jsonOk_Extension("收藏成功", "\"added\":true");
			} else {
				return BaseController.jsonNoOk("创建收藏失败");
			}
		} else {
			// 删除=取消收藏
			bean.setId(existId);
			
			if (delete(bean)) {
				return BaseController.jsonOk_Extension("取消收藏成功", "\"added\":false");
			} else {
				return BaseController.jsonNoOk("删除收藏失败");
			}
		}
	}

//
//	@TableName(value = "shop_goods_bookmark", beanClass = GoodsBookmark.class)
//	public static interface GoodsBookmarkDao extends IBaseDao<GoodsBookmark> {
//		
//		@Override
//		public List<GoodsBookmark> findList();
//		
//		@Select("SELECT id, name, coverPrice, subTitle, createDate, catelogId, " + selectCover +" AS cover FROM shop_goods entry WHERE uid in (SELECT goodsUid FROM ${tableName} WHERE userId = ?)")
//		public List<Goods> getGoodsListByUserId(Long userId);
//		
//		@Select("SELECT * FROM ${tableName} WHERE userId = ? AND goodsUid = (SELECT uid FROM shop_goods WHERE shop_goods.id = ?)")
//		public GoodsBookmark findBookmarkByUserAndGoodsUid(long userId, long goodsId);
//	}
//	
//	public static GoodsBookmarkDao dao = new Repository().bind(GoodsBookmarkDao.class);

//	public GoodsBookmark findBookmarkByUserAndGoodsUid(long userId, long goodsId) {
//		return dao.findBookmarkByUserAndGoodsUid(userId, goodsId);
//	}
}