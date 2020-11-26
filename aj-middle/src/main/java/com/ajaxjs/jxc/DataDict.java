package com.ajaxjs.jxc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.IController;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 数据字典
 * 
 * @author sp42 frank@ajaxjs.com
 *
 *
 *         收货信息： 姓名：黎志坚 邮寄地址：广东省 广州市 越秀区白云路45号之四602 邮政编码：510200 移动电话：13533476582
 * 
 */
public class DataDict implements IController {
	@TableName(value = "", beanClass = Map.class)
	public interface DataDictDao extends IBaseDao<Map<String, Object>> {
		@Select("SELECT * FROM ${tableName}")
		public List<Map<String, Object>> get(Function<String, String> handler);

		@Insert("INSERT INTO `${tableName}` (`key`, `value`) VALUES (?, ?)")
		public Long create(String key, String value);
	}

	public static DataDictDao dao = new Repository().bind(DataDictDao.class);

	public static Long create(String tableName, String key, String value) {
		Repository re = new Repository();
		re.setTableName(tableName); // 动态指定表名，所以只能牺牲性能，没有 static DAO，而是每次都创建
		DataDictDao dao = re.bind(DataDictDao.class);

		Map<String, Object> map = new HashMap<>();
		map.put("key", key);
		map.put("value", value);

		return dao.create(map);
	}

	public static List<Map<String, Object>> getList(String tableName) {
		return dao.get(sql -> sql.replace("${tableName}", tableName));
	}

	public static Map<String, String> getMap(String tableName) {
		List<Map<String, Object>> list = getList(tableName);

		Map<String, String> map = new HashMap<>();
		for (Map<String, Object> _map : list) {
			map.put(_map.get("key").toString(), _map.get("value").toString());
		}

		System.out.println(map);

		return map;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String getJSON(@NotNull @QueryParam("tableName") String tableName) {
		return BaseController.toJson(getList(tableName));
	}

	@GET
	@Path("page")
	public String page(ModelAndView mv) {
		mv.put("DataDicts", ReflectUtil.getConstantsInt(Constant.DataDict.class));

		return "/DataDict.jsp";
	}
}
