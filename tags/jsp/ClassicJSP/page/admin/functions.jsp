<%@page pageEncoding="UTF-8" import="sun.misc.BASE64Decoder, java.io.*"%>
<%!
	// 检查 HTTP Basic 认证

	/**
	 * 是否空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 是否不合法的数组
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isBadArray(String[] arr) {
		return arr == null || arr.length != 2;
	}

	/**
	 * 
	 * @param authorization
	 *            认证后每次HTTP请求都会附带上 Authorization 头信息
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return true = 认证成功/ false = 需要认证
	 */
	public static boolean checkAuth(String authorization, String username, String password) {
		if (isEmptyString(authorization))
			return false;

		String[] basicArray = authorization.split("\\s+");
		if (isBadArray(basicArray))
			return false;

		String idpass = null;
		try {
			byte[] buf = new BASE64Decoder().decodeBuffer(basicArray[1]);
			idpass = new String(buf, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (isEmptyString(idpass))
			return false;

		String[] idpassArray = idpass.split(":");
		if (isBadArray(idpassArray))
			return false;

		return username.equalsIgnoreCase(idpassArray[0]) && password.equalsIgnoreCase(idpassArray[1]);
	}

	/**
	 * 可编辑标识开始
	 */
	private final static String startToken = "<!-- Editable AREA|START -->";

	/**
	 * 可编辑标识结束
	 */
	private final static String endToken = "<!-- Editable AREA|END -->";

	/**
	 * 根据 页面中可编辑区域之标识，取出来。
	 * 
	 * @param fullFilePath
	 *            完整的 jsp 文件路径
	 * @return 可编辑内容
	 * @throws IOException
	 */
	public static String read_jsp_fileContent(String fullFilePath) throws IOException {
		String jsp_fileContent = readFile(fullFilePath);

		int start = jsp_fileContent.indexOf(startToken), end = jsp_fileContent.indexOf(endToken);

		try {
			jsp_fileContent = jsp_fileContent.substring(start + startToken.length(), end);
		} catch (StringIndexOutOfBoundsException e) {
			jsp_fileContent = null;

			String msg = "页面文件" + fullFilePath + "中没有标记可编辑区域之标识。请参考：" + startToken + "/" + endToken;
			throw new IOException(msg);
		}

		return jsp_fileContent;
	}

	/**
	 * 请求附带文件参数，将其转换真实的磁盘文件路径
	 * 
	 * @param rawFullFilePath
	 *            URL 提交过来的磁盘文件路径，可能未包含文件名或加了很多 url 参数
	 * @return 完整的磁盘文件路径
	 */
	static String getFullPathByRequestUrl(String rawFullFilePath) {
		if (rawFullFilePath.indexOf(".jsp") == -1)
			rawFullFilePath += "/index.jsp"; // 加上 扩展名

		if (rawFullFilePath.indexOf("?") != -1) // 去掉 url 参数
			rawFullFilePath = rawFullFilePath.replaceAll("\\?.*$", "");

		return rawFullFilePath;
	}

	/**
	 * 保存要修改的页面
	 * 
	 * @param rawFullFilePath
	 *            真实的磁盘文件路径
	 * @param newContent
	 *            新提交的内容
	 * @throws IOException
	 */
	public static void save_jsp_fileContent(String rawFullFilePath, String newContent) throws IOException {
		String fullFilePath = getFullPathByRequestUrl(rawFullFilePath); // 真实的磁盘文件路径
		String jsp_fileContent = readFile(fullFilePath), toDel_fileContent = read_jsp_fileContent(fullFilePath);// 读取旧内容
//System.out.println(jsp_fileContent);
//System.out.println(toDel_fileContent);
		if (toDel_fileContent != null) {
			jsp_fileContent = jsp_fileContent.replace(toDel_fileContent, newContent);
			save2file(fullFilePath, jsp_fileContent); // 保存新内容
		} else {
			throw new IOException("页面文件中没有标记可编辑区域之标识。请参考： startToken/endTpoken");
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists())
			throw new FileNotFoundException(filename + " 不存在！");

		try (FileInputStream is = new FileInputStream(file);) {
			String line = null;
			StringBuilder result = new StringBuilder();

			try (InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
					BufferedReader reader = new BufferedReader(isReader);) {
				while ((line = reader.readLine()) != null) {
					result.append(line);
					result.append('\n');
				}
			} catch (IOException e) {
				System.err.println(e);
			}

			return result.toString();
		} catch (IOException e) {
			System.err.println("讀取文件流出錯！" + filename);
			throw e;
		}
	}

	/**
	 * 写文件不能用 FileWriter，原因是会中文乱码
	 * 
	 * @param filename
	 * @param content
	 * @throws IOException
	 */
	public static void save2file(String filename, String content) throws IOException {
		try (FileOutputStream out = new FileOutputStream(filename);
				// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
				OutputStreamWriter writer = new OutputStreamWriter(out, "UTF8");) {
			writer.write(content);
		} catch (IOException e) {
			System.err.println("写入文件" + filename + "失败");
			throw e;
		}
	}
	
	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public String Mappath(String relativePath) {
		String absoluteAddress = getServletContext().getRealPath(relativePath); // 绝对地址
		
		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}
%>