<%@page pageEncoding="utf-8" import="java.io.*, java.net.*, javax.imageio.ImageIO, java.awt.Image, java.awt.image.*"%>
<%
	// JSP 实用程序之简易图片服务器 
	// http://blog.csdn.net/zhangxin09/article/details/51523489
	// sp42 2016-5-30
	String url = request.getParameter("url");
	if(url == null){
		out.println("缺少 url 参数！");
		return;
	}
	try{
		getImg(url, request, response);
		//清除输出流，防止释放时被捕获异常
		out.clear();
		out = pageContext.pushBody(); 
	} catch(Exception e) {
		out.println("Error:" + e);
	}
	
%>
<%!
	/**
	 * 主要的函数
	 */
	public static void getImg(String url, HttpServletRequest request,  HttpServletResponse response) throws IOException {
		System.out.println("请求地址：" + url);
		URL urlObj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
	
		int imgSize = getSize(conn, urlObj.getHost());
		if (imgSize < (1024 * 100)) {
			response.sendRedirect(url);// 发送重定向
			return;
		} else {
	//		System.out.println("BigImg");
			
			String imgType = getImgType(url);
			
			response.setContentType(getContentType(imgType));
			try (
				InputStream is = new URL(url).openStream();
				ServletOutputStream op = response.getOutputStream();
			){
				String height = request.getParameter("h"), width = request.getParameter("w");
				
				if (height != null && width != null) {
					BufferedImage bufImg = setResize(ImageIO.read(is), Integer.parseInt(height), Integer.parseInt(width));
					ImageIO.write(bufImg, imgType, op);
				} else if (height != null) {
					Image img = ImageIO.read(is);// 将输入流转换为图片对象
					int[] size = resize(img, Integer.parseInt(height), 0);
					
					BufferedImage bufImg = setResize(img, size[0], size[1]);
					ImageIO.write(bufImg, imgType, op);
				} else if (width != null) {
					Image img = ImageIO.read(is);// 将输入流转换为图片对象
					int[] size = resize(img, 0, Integer.parseInt(width));
					
					BufferedImage bufImg = setResize(img, size[0], size[1]);
					ImageIO.write(bufImg, imgType, op);
				} else {
					// 直接写浏览器
					byte[] buf = new byte[1024];
					int len = is.read(buf, 0, 1024);
					while (len != -1) {
						op.write(buf, 0, len);
						len = is.read(buf, 0, 1024);
					}
				} 
			}  
		}
	}
	
	/**
	 * 返回 Content type
	 * @param imgType
	 * @return
	 */
	private static String getContentType(String imgType) {
		switch (imgType) {
		case "jpg":
			return "image/jpeg";
		case "gif":
			return "image/gif";
		case "png":
			return "image/png";
		default:
			return null;
		}
	}
	
	/**
	 * 获取 url 最后的 .jpg/.png/.gif
	 * @param url
	 * @return
	 */
	private static String getImgType(String url) {
		String[] arr = url.split("/");
		arr = arr[arr.length - 1].split("\\.");
		String t = arr[1];
		
		return t.replace('.', ' ').trim().toLowerCase();
	}
	
	/**
	 * 缩放比例
	 * 
	 * @param img
	 *            图片对象
	 * @param height
	 *            高
	 * @param width
	 *            宽
	 * @return
	 */
	public static int[] resize(Image img, int height, int width) {
		int oHeight = img.getHeight(null), oWidth = img.getWidth(null);
		double ratio = (new Integer(oHeight)).doubleValue() / (new Integer(oWidth)).doubleValue();
		
		if(width != 0) {
			height =  (int) (ratio * width); 
		}else {
			width = (int) ( height / ratio);
		} 
		
		return new int[]{height, width};
	} 
	
	
	/**
	 * 完成设置图片大小
	 * 
	 * @param img
	 *            图片对象
	 * @param height
	 *            高
	 * @param width
	 *            宽
	 * @return 缓冲的图片对象
	 */
	public static BufferedImage setResize(Image img, int height, int width) {
		BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufImg.getGraphics().drawImage(img, 0, 0, width, height, null);
		
		return bufImg;
	}
	
	/**
	 * 
	 * @param conn
	 * @param referer
	 * @return
	 */
	public static int getSize(HttpURLConnection conn, String referer) {
		try {
			conn.setRequestMethod("HEAD");
			conn.setInstanceFollowRedirects(false); // 必须设置false，否则会自动redirect到Location的地址
			conn.addRequestProperty("Accept-Charset", "UTF-8;");
			conn.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
			conn.addRequestProperty("Referer", referer);
			conn.setConnectTimeout(5000);// 设置超时
			conn.setReadTimeout(30000);
	
			InputStream body = null;
	
			try {
				body = conn.getInputStream();
			} catch (UnknownHostException e) {
				throw new RuntimeException("未知地址！" + conn.getURL().getHost() + conn.getURL().getPath());
			} catch (FileNotFoundException e) {
				throw new RuntimeException("404 地址！" + conn.getURL().getHost() + conn.getURL().getPath());
			} catch (SocketTimeoutException e) {
				throw new RuntimeException("请求地址超时！" + conn.getURL().getHost() + conn.getURL().getPath());
			}
	
			body.close();
			// if (conn.getResponseCode() > 400) // 如果返回的结果是400以上，那么就说明出问题了
			// LOGGER.warning("Err when got responseCode :" +
			// conn.getResponseCode() + getUrlStr());
	
			// conn.connect();也就是打开流
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		String sizeStr = conn.getHeaderField("content-length");
	
		return Integer.parseInt(sizeStr);
	}
%>
