<%@page pageEncoding="UTF-8" import="java.util.*, java.util.regex.Pattern, java.net.InetAddress"%>
<%
	/**
	 * 浏览器 UA 检测
	 * @author frank
	 *
	 */
	class UA {
		/**
		 * 送入一个浏览器 UA 标识字符串
		 * 
		 * @param ua
		 *            UA 标识字符串
		 */
		public UA(String ua) {
			this.ua = ua;
		}
	
		/**
		 * 送入一个请求
		 * 
		 * @param request
		 *            请求对象
		 */
		public UA(HttpServletRequest request) {
			this.ua = getUA_String(request);
		}
		
		private String ua;
	
		/**
		 * 获取请求的 UA
		 * 
		 * @return 浏览器 UA 标识
		 */
		private String getUA_String(HttpServletRequest request) {
			String ua = request.getHeader("User-Agent");
			return ua != null ? ua.toLowerCase() : null; // 强制转换为小写字母
		}
	
		/**
		 * 是否 IE 浏览器
		 * 
		 * @return true 表示为 IE 浏览器
		 */
		public boolean isIE() {
			return ua.contains("msie");
		}
	
		/**
		 * IE 8 或其以下的皆为旧版 IE
		 * 
		 * @return true 表示为旧 IE 浏览器
		 */
		public boolean is_old_IE() {
			return isIE() && (ua.contains("msie 5.5") || ua.contains("msie 6.0")
						   || ua.contains("msie 7.0") || ua.contains("msie 8.0"));
		}
		
		/**
		 * 是否为 Firefox 浏览器
		 * 
		 * @return true 表示为 Firefox 浏览器
		 */
		public boolean isFireFox() {
			return ua.contains("firefox");
		}
	
		/**
		 * 是否为 Chrome 浏览器
		 * 
		 * @return true 表示为 Chrome 浏览器
		 */
		public boolean isChrome() {
			return ua.contains("chrome");
		}
		
		/**
		 * 是否为安卓
		 * 
		 * @return true 表示为 Android 浏览器
		 */
		public boolean isAndroid() {
			return ua.contains("android");
		}
	
		/**
		 * 是否为安卓 5.x
		 * 
		 * @return true 表示为 Android 5.x 浏览器
		 */
		public boolean isAndroid_5() {
			boolean is5 = Pattern.compile("Android\\s5", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is5;
		}
		
		/**
		 * 是否为安卓 4.x
		 * 
		 * @return true 表示为 Android 4.x 浏览器
		 */
		public boolean isAndroid_4() {
			boolean is4 = Pattern.compile("Android\\s4", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is4;
		}
		
		/**
		 * 是否为安卓 2.x
		 * 
		 * @return true 表示为 Android 2.x 浏览器
		 */
		public boolean isAndroid_2() {
			boolean is2 = Pattern.compile("Android\\s2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is2;
		}
		
		/**
		 * 是否为安卓 2.2.x
		 * 
		 * @return true 表示为 Android 2.2.x 浏览器
		 */
		public boolean isAndroid_2_2() {
			boolean is2_2 = Pattern.compile("Android\\s2\\.2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is2_2;
		}
		
		/**
		 * 是否为 iPhone
		 * 
		 * @return true 表示为 iPhone Safari 浏览器
		 */
		public boolean isIPhone() {
			return ua.contains("iphone");
		}
		
		/**
		 * 是否为 iPad
		 * 
		 * @return true 表示为 iPad Safari 浏览器
		 */
		public boolean isIPad() {
			return ua.contains("ipad");
		}
		
		/**
		 * 是否为 iOS 系统
		 * 
		 * @return true 表示为 iOS 系统
		 */
		public boolean isIOS() {
			return isIPad() || isIPhone();
		}
		
		/**
		 * 是否为 WinPhone 手机
		 * 
		 * @return true 表示为 WinPhone 手机
		 */
		public boolean isWinPhone() {
			return ua.contains("windows phone");
		}
		
		/**
		 * 是否为手机
		 * 
		 * @return true 表示为手机
		 */
		public boolean isPhone() {
			return isAndroid() || isIOS() || isWinPhone();
		}
	}

%><%!

/**
 * 提供样式的 url 地址的支持。由 ConfigListener 启动
 * @author frank
 *
 */
	/**
	 * ip 缓存
	 */
	private static String localIp = null;

	/**
	 * 获取本机 IP 地址
	 * 用于局域网内的测试机器
	 * @return 本机 IP
	 */
	public static String getLocalIp() {
		if (localIp == null) { // 第一次访问
			for (String ip : getAllLocalHostIP()) {
				if (ip.startsWith("192.168.")) { // 以 192.168.x.x 开头的都是局域网内的 IP
					localIp = ip;
					break;
				}
			}

			if (localIp == null)
				localIp = "localhost";// 还是 null，那就本机的……没开网卡？
		}

		return localIp;
	}
	

	/**
	 * 获得本地所有的 IP 地址
	 * 
	 * @return 本机所有 IP
	 */
	public static String[] getAllLocalHostIP() {
		InetAddress[] addrs = null;

		try {
			String hostName = InetAddress.getLocalHost().getHostName();// 获得主机名
			// 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。
			addrs = InetAddress.getAllByName(hostName);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}

		String[] ips = null;
		if (addrs !=null ) {
			ips = new String[addrs.length];
			int i = 0;
			for (InetAddress addr : addrs)
				ips[i++] = addr.getHostAddress();
		}

		return ips;
	}
	
	// 默认 8080 端口
	private final static String picPath = "http://%s:8080/%s/asset/";


	/**
	 * 也是 join，不过输入的参数不是数组而是 hash。
	 * 
	 * @param map
	 *            输入的 map
	 * @return 连续的字符串
	 */
	public static String join(Map<String, String> map) {
		String[] pairs = new String[map.size()];
		String div = "&";

		int i = 0;
		for (String key : map.keySet())
			pairs[i++] = key + "=" + map.get(key);

		/*
		 * 另外一种算法 // for (String key : pair.keySet()) cookieStr += key + "=" +
		 * pair.get(key) + ";"; // cookieStr = cookieStr.substring(0,
		 * cookieStr.length() - 1); // 删掉最后一个分号 // 另外一种算法 int i = 0; for(String
		 * key : hash.keySet()){ ... if(++i != size)buff.append(","); } //
		 * 另外一种算法，删除最后一个 , if (buff.length() > 1)buff =
		 * buff.deleteCharAt(buff.length() - 1); // 另外一种算法，删除最后一个 , ... if(i !=
		 * arr.length - 1)str += ",";
		 */
		return stringJoin(pairs, div);
	}
	
	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串数组
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(String[] arr, String join) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < arr.length; i++) {
			if (i == (arr.length - 1))
				sb.append(arr[i]);
			else
				sb.append(arr[i]).append(join);
		}

		return new String(sb);
	}
	
	/**
	 * 获取磁盘真實地址。
	 * 
	 * @param cxt
	 *            Web 上下文
	 * @param lessPath
	 *            LESS 预编译文件路径
	 * @return LESS 预编译文件完整的磁盘路径
	 */
	private static String Mappath(ServletContext cxt, String lessPath) {
		String absoluteAddress = cxt.getRealPath(lessPath); // 绝对地址

		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}
	
	/**
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 路径
	 * @return CSS 地址
	 */
	public static String getCssUrl(HttpServletRequest request, String lessPath, boolean isDebug) {
		String css = null;
		
		if (isDebug) {// 设置参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("lessFile", Mappath(request, lessPath));
			params.put("ns", Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称
			params.put("picPath", getBasePath(request) + "/asset");// 返回 CSS 背景图所用的图片
			params.put("MainDomain", "");
			params.put("isdebug", "true");
			
			css = "http://" + getLocalIp() + ":83/lessService/?" + join(params);
		} else {
			css = request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css").trim();
		}
		return css;
	}

	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String Mappath(HttpServletRequest request, String relativePath) {
		String absoluteAddress = request.getServletContext().getRealPath(relativePath); // 绝对地址

		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}

	/**
	 * 协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * 
	 * @return 网站名称
	 */
	public static String getBasePath(HttpServletRequest request) {
		String prefix = request.getScheme() + "://" + request.getServerName();

		int port = request.getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + request.getContextPath();
	}

%>