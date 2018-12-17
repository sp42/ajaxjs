package com.ajaxjs.cms.dao;

import java.util.List;

import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.cms.model.Ads;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface AdsDao extends IDao<Ads, Long>, Catelogable<Ads> {
	final static String tableName = "entity_ads";

	final static String list = "SELECT e.*, c.name AS catelogName, (SELECT path FROM attachment_picture p1 WHERE e.uid = p1.owner AND p1.catelog = 2 ORDER BY p1.id DESC LIMIT 0, 1) AS cover, "
			+ " (SELECT path FROM attachment_picture p2 WHERE e.uid = p2.owner AND p2.catelog = 1 ORDER BY p2.id DESC LIMIT 0, 1) AS img FROM " + tableName + " e LEFT JOIN " + Catelogable.getByCatelogId
			+ " ON e.catelog = c.id WHERE e.catelog = c.id";

	@Select("SELECT e.*,  (SELECT path FROM attachment_picture p1 WHERE e.uid = p1.owner AND p1.catelog = 2 ORDER BY p1.id DESC LIMIT 0, 1) AS cover, "
			+ " (SELECT path FROM attachment_picture p2 WHERE e.uid = p2.owner AND p2.catelog = 1 ORDER BY p2.id DESC LIMIT 0, 1) AS img " + " FROM " + tableName + " e WHERE id = ?")
	@Override
	public Ads findById(Long id);

	@Select(value = list)
	@Override
	public PageResult<Ads> findPagedListByCatelogId(int catelogId, int start, int limit);

	@Select(value = list)
	@Override
	public List<Ads> findListByCatelogId(int catelogId);

	@Insert(tableName = tableName)
	@Override
	public Long create(Ads entry);

	@Update(tableName = tableName)
	@Override
	public int update(Ads entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Ads entry);
}