package com.ajaxjs.app.service;

import java.util.List;

import com.ajaxjs.app.DataDictController;
import com.ajaxjs.app.catalog.Catalog;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.shop.ShopConstant;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class SectionService extends SectionListService {
	{
		setUiName("栏目");
		setShortName("secion");
	}

	public static final String SELECT_GROUPS = "SELECT g.id AS entryId, entry.name, %s AS entryTypeId, %s FROM shop.shop_goods entry INNER JOIN shop_group g ON g.goodsId = entry.id WHERE g.id in (%s)\n";

	private static ScanTable fn = (sqls, entityTypeId, entityIds, caseSql) -> {
		switch (entityTypeId) {
		case DataDictController.DataDictService.ENTRY_ARTICLE:
			sqls.add(String.format(SELECT, entityTypeId, caseSql, "entity_article", entityIds));
			break;
		case DataDictController.DataDictService.ENTRY_TOPIC:
			sqls.add(String.format(SELECT, entityTypeId, caseSql, "entity_topic", entityIds));
			break;
		case DataDictController.DataDictService.ENTRY_ADS:
			sqls.add(String.format(SELECT, entityTypeId, caseSql, "entity_ads", entityIds));
			break;
		case ShopConstant.ENTRY_GOODS:
			sqls.add(String.format(SELECT, entityTypeId, caseSql, "shop_goods", entityIds));
			break;
		case ShopConstant.ENTRY_GROUP:
			sqls.add(String.format(SELECT_GROUPS, entityTypeId,
					caseSql.replaceAll("WHEN entry.id", "WHEN g.id") /* 修正一下，别名有所不同 */, entityIds));
			break;
		}
	};

	/**
	 * Find all bookmarks, For admin use
	 * 
	 * @return
	 */
	public List<SectionList> findAds() {
		return findSectionListBySectionId(ConfigService.getValueAsInt("data.section.ads_Catalog_Id"));
	}

	public List<SectionList> findSectionListBySectionId(int sectionId) {
		return findListBySectionId(sectionId, fn);
	}

	/**
	 * 可分页的
	 * 
	 * @param start
	 * @param limit
	 * @param sectionId
	 * @return
	 */
	public PageResult<SectionList> findSectionListBySectionId(int start, int limit, int sectionId) {
		return findListBySectionId(start, limit, sectionId, fn);
	}

	private CatalogService catalogService = new CatalogService();

	/**
	 * SectionInfo 是一棵树。为简约起见，SectionInfo 没有独立弄成一张表放进去，而是放进 catalog 表。一般在后台里调用该方法
	 * 
	 * @return
	 */
	public List<Catalog> findSectionCatalog() {
		List<Catalog> c = catalogService
				.findAllListByParentId(ConfigService.getValueAsInt("data.section.masterCatalogId"), false);
		Catalog parent = new Catalog();
		parent.setId(Long.parseLong(ConfigService.getValueAsInt("data.section.masterCatalogId") + ""));
		parent.setName("栏目");
		parent.setPid(-1);
		c.add(parent);

		c.sort((c1, c2) -> {
			return c1.getPid().compareTo(c2.getPid());
		});

		return c;
	}
}
