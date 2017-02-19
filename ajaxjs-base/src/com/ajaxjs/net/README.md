#简单封装 HTTP 请求 
简单封装 Java 类库里面的 HttpURLConnection 来完成日常的 HTTP 请求，如 GET、HEAD、POST 等的请求。和文件操作一样，其内部使用了链式风格的调用方式。

GET/HEAD 请求
-----------------------
GET 请求用法参见下面的测试用例，包括普通 GET 请求、获取 302 重定向调转地址、获取资源文件体积大小、是否 404以及下载二进制文件等功能。

	System.out.println(Client.GET("https://www.baidu.com/"));
	
	// 获取资源文件体积大小
	long size = Client.getFileSize("http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png");
	assertEquals(size, 4102L);
	// 获取 302 重定向跳转地址
	System.out.println(Client.get302redirect("https://baidu.com"));
	// 封装 head 请求检测是否 404
	assertTrue(!Client.is404("http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png"));
	// B 站强制 Gzip 返回，無論请求是否带有 GZIP 字段
	System.out.println(Client.GET_Gzip("http://www.bilibili.com/video/av5178498/"));

POST 请求
-----------------------

	String url = "http://localhost:8080/pachong/post.jsp";
	String result = Client.POST(url, new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("foo", "bar");
		}
	});
	System.out.println("Feedback:" + result);
	
通过 telnet 发送电子邮件
-----------------------	
	Mail mail = new Mail();
	mail.setMailServer("smtp.163.com");
	mail.setAccount("pacoweb");
	mail.setPassword("xxxxxx");
	mail.setFrom("tom@163.com");
	mail.setTo("jack@qq.com");
	mail.setSubject("hihi你好");
	mail.setHTML_body(true);
	mail.setContent("你好哦<a href=\"http://qq.com\">fdsfds</a>");
	
	try(Sender sender = new Sender(mail)){
		boolean isOk = sender.sendMail();
		System.out.println(isOk);
		assertTrue(isOk);
	};