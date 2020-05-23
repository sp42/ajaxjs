package com.ajaxjs.app.service;

import java.util.List;
import java.util.Objects;

import com.ajaxjs.app.DataDictController;
import com.ajaxjs.app.catalog.Catalog;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
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
	
	static ScanTable fn = (sqls, entityTypeId, entityIds, caseSql) -> {
		switch (entityTypeId) {
		case 1:
			sqls.add(String.format(SELECT, 1, caseSql, "entity_article", entityIds));
			break;
		case 2:
			sqls.add(String.format(SELECT, 2, caseSql, "shop_goods", entityIds));
			break;
		case DataDictController.DataDictService.ENTRY_TOPIC:
			sqls.add(String.format(SELECT, entityTypeId, caseSql, "shop_topic", entityIds));
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
	
	@Override
	ScanTable getScanTable() {
		return fn;
	}

	@Resource("CatalogService")
	private CatalogService catalogService;

	/**
	 * SectionInfo 是一棵树。为简约起见，SectionInfo 没有独立弄成一张表放进去，而是放进 catalog 表。一般在后台里调用该方法
	 * 
	 * @return 所有栏目对象，一般用于树状结构的 UI 显示用
	 */
	public List<Catalog> findSectionCatalog() {
		int sectionCatalog_Id = ConfigService.getValueAsInt("data.sectionCatalog_Id");
		Objects.requireNonNull(sectionCatalog_Id, "配置中没有 sectionCatalog_Id 参数");

		List<Catalog> c = catalogService.findAllListByParentId(sectionCatalog_Id, false);
		Catalog parent = new Catalog();
		parent.setId(Long.parseLong(sectionCatalog_Id + ""));
		parent.setName("栏目");
		parent.setPid(-1);
		c.add(parent);

		c.sort((c1, c2) -> {
			return c1.getPid().compareTo(c2.getPid());
		});

		return c;
	}
}
