package com.ajaxjs.cms.user.role;

public interface RightConstant {
	public static final int READ = 1;
	public static final int CREATE = 2;
	public static final int UPDATE = 3;
	public static final int DELETE = 4;
	
	@Note("允许浏览前台")
	public static final int FRONTEND_ALLOW_ENTNER = 4;
	
	@Note("进入后台")
	public static final int ADMIN_SYSTEM_ALLOW_ENTNER = 5;
	
	@Note("API 接口")
	public static final int API_ALLOW_ACCESS = 6;
	
	@Note("文章模块")
	public static final int ARTICLE = 7;
	
	@Note(value = "上线文章", allowCRUD = true)
	public static final int ARTICLE_ONLINE = 8;

	@Note(value = "已下线文章", allowCRUD = true)
	public static final int ARTICLE_OFFLINE = 9;

	@Note("留言反馈模块")
	public static final int FEEDBACK = 10;
}
