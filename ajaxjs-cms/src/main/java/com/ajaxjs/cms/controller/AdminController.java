package com.ajaxjs.cms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

@Path("/admin")
public class AdminController implements IController, Constant {
	@GET
	public String admin() {
		return BaseController.cms("admin");
	}

	@TableName(value = "general_log")
	public static interface GlobalLogDao extends IBaseDao<Map<String, Object>> {
	}

	public static GlobalLogDao dao = new Repository().bind(GlobalLogDao.class);

	@GET
	@Path("GlobalLog")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView model) {
		model.put("uiName", "操作日志");
		model.put(PageResult, dao.findPagedList(start, limit));
		return BaseController.cms("global-log");
	}

	@GET
	@Path("/Logger")
	public String show(ModelAndView mv) {
		return BaseController.cms("logger");
	}
	
	@GET
	@Path("/DataBaseConnection")
	public String show(ModelAndView mv, MvcRequest request) {
		mv.put("list", get(request.mappath("/META-INF/context.xml")));
		return BaseController.cms("database-connection");
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static List<Map<String, String>> get(String file) {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			NodeList resource = document.getElementsByTagName("Resource");

			List<Map<String, String>> list = new ArrayList<>();
			for (int i = 0; i < resource.getLength(); i++) {
				Node r = resource.item(i);

				NamedNodeMap attrs = r.getAttributes();
				Map<String, String> map = new HashMap<>();

				for (int j = 0; j < attrs.getLength(); j++) {
					Node attr = attrs.item(j);

					if (CommonUtil.regMatch("name|username|password|driverClassName|url", attr.getNodeName()) != null) {
						map.put(attr.getNodeName(), attr.getNodeValue());
					}
				}

				list.add(map);
			}

			return list;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
