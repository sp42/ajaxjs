<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.DataInputStream, java.io.FileOutputStream, java.io.File, java.net.URLEncoder"%>

<%!/*
<body>
   <form action="upload2.jsp" enctype="multipart/form-data" method="POST" >
      selectimage: <input type="file" name="myfile"/><br>
      <input type="submit" value="upload"/>
   </form>
</body>
*/
// 在字节数组里查找某个字节数组，找到返回>=0，未找到返回-1

	  
	private int byteIndexOf(byte[] data, byte[] search, int start) {
		int index = -1;
		int len = search.length;
		for (int i = start, j = 0; i < data.length; i++) {
			int temp = i;
			j = 0;
			while (data[temp] == search[j]) {
				// System.out.println((j+1)+",值："+data[temp]+","+search[j]);
				//计数
				j++;
				temp++;
				if (j == len) {
					index = i;
					return index;
				}
			}
		}
		return index;
	}

	public String upload() {
		//定义上传的最大文件字节数1M
		int MAX_SIZE = 1024000;
		DataInputStream in = null;
		FileOutputStream fileOut = null;

		String rootPath, remoteAddr = req.getRemoteAddr(), serverName = req.getServerName(),
				realPath = req.getRealPath("/");

		realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
		//设置保存文件的目录
		rootPath = realPath + "\\upload\\";
		//取得客户端上传的数据类型
		String contentType = req.getContentType();

		try {
			if (contentType.indexOf("multipart/form-data") >= 0) {
				in = new DataInputStream(req.getInputStream());
				int formDataLength = req.getContentLength();
				if (formDataLength > MAX_SIZE) {
					respWriter.println("0,文件大小超过系统限制！");
					respWriter.flush();
					return "文件大小超过系统限制！";
				}
				//保存上传的文件数据
				byte dateBytes[] = new byte[formDataLength];
				int byteRead = 0, totalRead = 0;
				while (totalRead < formDataLength) {
					byteRead = in.read(dateBytes, totalRead, formDataLength);
					totalRead += byteRead;
				}
				String data = new String(dateBytes, "UTF-8");
				//取得上传的文件名
				String saveFile = data.substring(data.indexOf("filename=\"") + 10);
				saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
				saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
				//取得数据分割字符串
				int lastIndex = contentType.lastIndexOf("="); //数据分割线开始位置boundary=---------------------------
				String boundary = contentType.substring(lastIndex + 1, contentType.length());//---------------------------257261863525035

				//计算开头数据头占用的长度
				int startPos;
				startPos = byteIndexOf(dateBytes, "filename=\"".getBytes(), 0);
				startPos = byteIndexOf(dateBytes, "\n".getBytes(), startPos) + 1; //遍历掉3个换行符到数据块
				startPos = byteIndexOf(dateBytes, "\n".getBytes(), startPos) + 1;
				startPos = byteIndexOf(dateBytes, "\n".getBytes(), startPos) + 1;

				//边界位置
				int endPos = byteIndexOf(dateBytes, boundary.getBytes(), (dateBytes.length - startPos)) - 4;

				//创建文件
				String fileName = rootPath + saveFile;
				File checkFile = new File(fileName);
				if (checkFile.exists()) {
					//printErr("文件已经存在！");
					//return;
				}
				File fileDir = new File(rootPath);
				if (!fileDir.exists())
					fileDir.mkdirs();
				//写入文件
				fileOut = new FileOutputStream(fileName);
				fileOut.write(dateBytes, startPos, endPos - startPos);
				fileOut.flush();

				return fileName;
			} else {
				return "未找到上传文件！";
			}
		} catch (Exception error) {
			return error.toString();
		} finally {
			try {
				if (in != null)
					in.close();
				if (fileOut != null)
					fileOut.close();
			} catch (Exception e) {
			}
		}
	}
	
	%>