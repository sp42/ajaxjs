package com.ajaxjs.framework.tag;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.data_service.model.DataServiceConstant.CRUD;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.util.logger.LogHelper;

public class TagPlugin implements IPlugin {
	private static final LogHelper LOGGER = LogHelper.getLog(TagPlugin.class);

	@Override
	public boolean onRequest(CRUD type, ServiceContext ctx) {
		return true;
	}

	@Override
	public boolean before(CRUD type, ServiceContext ctx) {
		return true;
	}

	@Override
	public void after(CRUD type, ServiceContext ctx, Object result) {
		if (type != CRUD.LIST)
			return;

		// 标签跨表查询详情
		@SuppressWarnings("unchecked")
		PageResult<Map<String, Object>> page = (PageResult<Map<String, Object>>) result;

		boolean isTagServiceEnable = true, tagQuery = isTagServiceEnable && !page.isZero() && page.get(0).get("tags") != null; // 是否有数据

		if (tagQuery) {
			// 先获取所有 tag 信息
			Map<Integer, String> tags = new HashMap<>();

			try (Connection tagConn = ctx.getDatasource().getConnection()) {
				JdbcConnection.setConnection(tagConn);
				List<TagInfo> list = TagService.DAO.findList(null); // 找到所有标签

				if (list == null || list.size() == 0) {

				} else {
					list.forEach(item -> tags.put(item.getTagIndex(), item.getName()));
				}
			} catch (Throwable e) {
				LOGGER.warning(e);
			}

			LOGGER.info(tags + "");
			// 得到实体的各个 tagIndex
			page.forEach(item -> {
				Object _tags = item.get("tags");

				if (_tags != null && _tags instanceof Number) {
					long tagsTotal = (_tags instanceof Long) ? (long) _tags : ((Integer) _tags).longValue();
					int[] trueStatus = TagService.getTrueStatus(tagsTotal);

					item.put("tagsIndexs", trueStatus);
					List<String> tagNames = new ArrayList<>();

					for (int tagIndex : trueStatus) {
						String name = tags.get(tagIndex);
						if (name != null)
							tagNames.add(name);
					}

					item.put("tagsNames", String.join("，", tagNames));
				}
			});
		}

	}

}
