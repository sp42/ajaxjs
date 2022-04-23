package com.ajaxjs.framework.tag;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.logger.LogHelper;

public class TagService extends BaseService<TagService> {
	private static final LogHelper LOGGER = LogHelper.getLog(TagService.class);

	@TableName(value = "sys_tag", beanClass = TagInfo.class)
	public static interface TagDao extends IBaseDao<TagInfo> {
		/**
		 * 返回空闲 tag 所在的 id，然后插入到这里
		 *
		 * @return
		 */
		@Select("SELECT id FROM ${tableName} WHERE stat = -1 ORDER BY tagIndex ASC LIMIT 1")
		Long getMaxEmptyId();

		@Select(" SELECT count(id) FROM ${tableName} WHERE stat != -1")
		Integer getMaxTotal();
	}

	public static TagDao DAO = new Repository().bind(TagDao.class);

	public void create(TagInfo tag) {
		Long emptyTagId = DAO.getMaxEmptyId();

		if (emptyTagId == null) { // 插入到最后即可
			// 确定 tagIndex
			int tagIndex = DAO.getMaxTotal();
			tagIndex++;
			tag.setTagIndex(tagIndex);

			DAO.create(tag);
		} else { // 已有位置，tagIndex 不变也不能变，变了就麻烦
			DAO.update(tag);
		}
	}

	/**
	 * 删除 or 修改 tag 之后，实体携带的 tag 信息怎么办？ 应该要同步一下，不过问题来了，理论上所有有 tags 字段的实体都一起改。
	 * 怎么知道哪些实体要改？
	 */
	public void resetEnitiyTag(int tagIndex) {
		String[] tables = null; // 影响的哪些表
		String sql = "SELECT id, tags FROM ${tableName} WHERE ((tags >> ?) & 1)"; // ? = ${tagIndex}
		Connection conn = null;

		// 找到包含 tag 的那些实体
		for (String table : tables) {
			List<Map<String, Object>> r = JdbcHelper.queryAsMapList(conn, sql.replace("${tableName}", table), tagIndex);

			for (Map<String, Object> map : r) {
				long id = (long) map.get("id");
				long oldTags = (long) map.get("tags");
				long newTags = oldTags - (1 << tagIndex);// 将第 tagIndex 位设置为 0

				String update = "UPDATE ${tableName} SET tags = ? WHERE id = ?";
				// 批量更新
				if (JdbcHelper.update(conn, update.replace("${tableName}", table), newTags, id) > 0) {

				} else
					LOGGER.warning("更新 tags 失败， id: {0}", id);
			}
		}
	}

	/**
	 * 
	 * @param total 总值
	 * @param index 移位
	 * @return 值是否等于 1
	 */
	public static boolean check(long total, int index) {
		total = total >>> index;
		return (total & 1) == 1;
	}

	/**
	 * 遍历二进制中的每一位, 每遇到一个为 1 的位置就将该位置放入数组
	 *
	 * @param total 总值
	 * @return
	 */
	public static int[] getTrueStatus(long total) {
		List<Integer> list = new ArrayList<>();

		for (int i = 0; i < 64; i++) {
			if (check(total, i))
				list.add(i);
		}

		return list.stream().mapToInt(Integer::valueOf).toArray();
	}
}
