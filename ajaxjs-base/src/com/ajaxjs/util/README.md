#AJAXJS base-util
文件增删改
-----------
采用链式风格调用；采用 Java 7 autoclose 自动关闭流。

	import com.ajaxjs.util.io.FileUtil;
	
	public class TestFileUtil {
		String dir = TestFileUtil.class.getResource("/").getPath();
		String fullpath = dir + File.separator + "bar.txt";
	
		@Test
		public void testCreateRead() {
			// create and update
			new FileUtil().setFilePath(fullpath).setOverwrite(true).setContent("hihi").save().close();
			// read
			String result = new FileUtil().setFilePath(fullpath).read().byteStream2stringStream().close().getContent();
			
			System.out.println(result);
			assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
			
			// delete
			new FileUtil().setFilePath(fullpath).delete();
		}
	}

日志服务
-------------
AJAXJS 针对 JDK 自带的 logger 稍加扩展，使用例子如下：

 
	private static final LogHelper log = LogHelper.getLog(TestLogHelper.class);
	log.warning("fooo");
	log.info("bar");

对于 WARNING 级别信息会以磁盘文件方式保存在 /META-INF/logger/log.txt 下。
如果你习惯其他 log，建议使用 slf4j 或 apache common logging，可参考我写的教程[《使用 slf4j + Java.util.logger》](http://blog.csdn.net/zhangxin09/article/details/50611373)。