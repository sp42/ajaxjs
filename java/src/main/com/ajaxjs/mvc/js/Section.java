/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.mvc.js;

import java.sql.Connection;

import com.ajaxjs.app.App;
//import com.ajaxjs.app.Route;
//import com.ajaxjs.javascript.IEngine;
//import com.ajaxjs.mvc.RequestHelper;
//import com.ajaxjs.mvc.ResponseHelper;
//import com.ajaxjs.mvc.db.DBAccessImpl;
//import com.ajaxjs.mvc.db.QueryRunner;
//import com.ajaxjs.mvc.db.result.PagedList;
//import com.ajaxjs.mvc.db.result.PureList;
//import com.ajaxjs.mvc.db.result.Record;
//import com.ajaxjs.mvc.db.result.Result;
//import com.ajaxjs.mvc.entity.ISection;
import com.ajaxjs.util.StringUtil;

public class Section {

 

//	public void action(RequestHelper request, ResponseHelper response) {
//		Route route = new Route(request);
//		String tableName = "blog"; // uri 指定的就是表名
//		
//		Integer start = (int)request.getWithTypeCast("start"), limit = (int)request.getWithTypeCast("limit");
//		
//		if(route.isMatch("section_detail")){
//			getSectionDetail(request.get("id")).saveInfoResult(request);
////			
////			sql.addWhere(String.format("uid = '%s'", _request.get("id")));
//		}else if(route.isMatch("subsection_list")){
//				
//		}else if(route.isMatch("content_list")){
////			getContentList(tableName, request.get("id"), start, limit).saveListResult(request);
//		}
//		
////		afterRequest(request, response);
//		
//	}
//	private static final String joinSql = 
//			"(%1 LEFT JOIN section ON section.entryId = %1.uid) " +
//					"LEFT JOIN 	sectionDetail ON (sectionDetail.uid = section.sectionId)";
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 */
//	public Result<Record> getSectionDetail(String id){
////		DBAccess dao = BaseController.DaoFactory();
////		Result<Record> result = dao.queryOne(new QueryRunner().from("sectionDetail").where("uid = '" + id + "'").toString());
//		return null;
////		return result;
//	}
//	
//	/**
//	 * 
//	 * @param tableName
//	 * @param sectionId
//	 * @param start
//	 * @param limit
//	 * @return
//	 */
//	public Result<PagedList> getContentList(Connection conn, String tableName, String sectionId, Integer start, Integer limit){
//		QueryRunner qr = new QueryRunner();
//		qr.setMainTable(tableName);
//		qr.select(tableName + ".*", "section.isHidden", "sectionDetail.name AS seciotnName", "sectionDetail.uid  AS seciotnId", "section.isHot");
//		qr.from(joinSql.replaceAll("%1", tableName));
//		qr.where(getSubSections(sectionId));
//		
//		qr.setOrderBy(" ORDER BY createDate DESC");
//		
//		DBAccessImpl dao = new DBAccessImpl(conn);
//		Result<PagedList> result = dao.queryList(qr, start, limit);
//		return result;
//	}
//	
//	private final static String getSubSections_js = 
//		"JSON_Tree.util.getAllChildrenIdByNode_as_idArr(bf.AppStru.data, '%s').join(',')";
//	
//	private IEngine js = App.jsRuntime;
//	
//	/**
//	 * 
//	 * @param parentSectionId  父栏目 id
//	 * @return
//	 */
//	private String getSubSections(String parentSectionId){
//		String sections = js.eval(String.format(getSubSections_js, parentSectionId), String.class);
//		System.out.println(String.format(getSubSections_js, parentSectionId));
//		String[] arr = sections.split(",");
//		
//		String where = "section.sectionId = '" + StringUtil.stringJoin(arr, "' OR section.sectionId = '") + "'";
//		
//		return where;
//	}
//	
//	
//	/**
//	 * 根据父栏目 id 获取其下所有子栏目
//	 * 
//	 * @param parentSectionId
//	 *            父栏目 id
//	 * @return 子栏目列表
//	 */
//	public Result<PureList> getSubSectionList(Connection conn, String parentSectionId) {
//		// 生成 where 条件
//		String sections = js.eval(String.format(getSubSections_js, parentSectionId), String.class);
//		String[] arr = sections.split(",");
//		
//		String where = "sectionDetail.uid = '" + StringUtil.stringJoin(arr, "' OR sectionDetail.uid = '") + "'";
//
//		DBAccessImpl dao = new DBAccessImpl(conn);
//		Result<PureList> result = dao.queryList(new QueryRunner().from("sectionDetail").where(where));
//		return result;
//	}
//	
//	// detail 跟 info 不同，info 为单纯的信息， detail 还包括其他丰富的信息
//	
//	public void getDetail(String sectionId, String infoId){
//		
//	}
}
