package com.ajaxjs.shop.dep;

import java.util.List;

import com.ajaxjs.cms.SectionList;
import com.ajaxjs.cms.SectionListService;
import com.ajaxjs.cms.app.catalog.Catalog;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.app.catalog.CatalogServiceImpl;
import com.ajaxjs.cms.controller.DataDictController;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.shop.ShopConstant;

/**
 * 
 * @author Frank Cheung
 *
 */
@Bean
public class SectionService extends SectionListService {
	{
		setUiName("栏目");
		setShortName("secion");
	}

	public static final String selectGroups = "SELECT g.id AS entryId, entry.name, %s AS entryTypeId, %s, (" + IBaseDao.selectCover
			+ ") AS cover FROM shop.shop_goods entry INNER JOIN shop_group g ON g.goodsId = entry.id WHERE g.id in (%s)\n";

	private ScanTable fn = (sqls, entryTypeId, entryIds, caseSql) -> {
		switch (entryTypeId) {
		case DataDictController.DataDictService.ENTRY_TOPIC:
			sqls.add(String.format(select, entryTypeId, caseSql, "entity_topic", entryIds));
			break;
		case DataDictController.DataDictService.ENTRY_ADS:
			sqls.add(String.format(select, entryTypeId, caseSql, "entity_ads", entryIds));
			break;
		case ShopConstant.ENTRY_GOODS:
			sqls.add(String.format(select, entryTypeId, caseSql, "shop_goods", entryIds));
			break;
		case ShopConstant.ENTRY_GROUP:
			sqls.add(String.format(selectGroups, entryTypeId, caseSql.replaceAll("WHEN entry.id", "WHEN g.id") /*修正一下，别名有所不同*/, entryIds));
			break;
		}
	};

	/**
	 * Find all bookmarks, For admin use
	 * 
	 * @return
	 */
	public List<SectionList> findAds() {
		return findSectionListBySectionId(ConfigService.getValueAsInt("data.section.ads_Catelog_Id"));
	}

	public List<SectionList> findSectionListBySectionId(int sectionId) {
		return findSectionListBySectionId(sectionId, fn);
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
		return super.findSectionListBySectionId(start, limit, sectionId, fn);
	}

	private CatalogService catalogService = new CatalogServiceImpl();

	/**
	 * SectionInfo 是一棵树。为简约起见，SectionInfo 没有独立弄成一张表放进去，而是放进 catalog 表。一般在后台里调用该方法
	 * 
	 * @return
	 */
	public List<Catalog> findSectionCatalog() {
		List<Catalog> c = catalogService.findAllListByParentId(ConfigService.getValueAsInt("data.section.sectionList_Catelog_Id"), false);
		Catalog parent = new Catalog();
		parent.setId(154L);
		parent.setName("栏目");
		parent.setPid(-1);
		c.add(parent);

		c.sort((c1, c2) -> {
			return c1.getPid().compareTo(c2.getPid());
		});

		return c;
	}
}
