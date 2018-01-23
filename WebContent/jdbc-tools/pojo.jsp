<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*, java.sql.*, com.ajaxjs.jdbc.DocumentMaker, com.ajaxjs.framework.mock.MockDataSource"%>
<%
	DocumentMaker maker = new DocumentMaker();
	maker.conn = MockDataSource.getTestMySqlConnection("jdbc:mysql://localhost:3306/ng_zmy", "root", "root");

	List<String> tables = maker.getAllTableName();
	Map<String, String> tablesComment = maker.getCommentByTableName(tables);
	Map<String, List<Map<String, String>>> infos = maker.getColumnCommentByTableName(tables);
%>
public class BaseModel implements Serializable { }
