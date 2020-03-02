package com.ajaxjs.cms.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Consumer;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.logger.LogHelper;

@ServerEndpoint("/tomcat_log")
@Path("/admin/tomcat-log")
public class TomcatLogController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(TomcatLogController.class);

	private LogFileTailer tailer;

	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session) {
		tailer = new LogFileTailer("C:\\temp\\bar.txt", 1000, true);
		tailer.setTailing(true);
		tailer.addListener(log -> {
			try {
				session.getBasicRemote().sendText(log + "<br />");
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		});
		tailer.start();
	}

	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		tailer.setTailing(false);
	}

	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}

	/**
	 * 实现Linux Tail 功能跟踪日志，可持续监控某日志信息。 Linux
	 * tail命令用途是依照要求将指定的文件的最后部分输出到标准设备，通常是终端，通俗讲来，就是把某个档案文件的最后几行显示到终端上，假设该档案有更新，tail会自己主动刷新，确保你看到最新的档案内容。
	 * 
	 * 参考：https://blog.51cto.com/6140717/1052845
	 * https://www.cnblogs.com/snowater/p/7603611.html
	 * https://blog.csdn.net/xxgwo/article/details/51198113
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	public static class LogFileTailer extends Thread {
		private long sampleInterval = 2000;

		private File logfile;

		private boolean startAtBeginning;

		private Consumer<String> callback;

		/**
		 * 监视开关，true = 打开监视
		 */
		private boolean tailing;

		/**
		 * 
		 * @param file             要监视的文本文件
		 * @param sampleInterval   读取时间间隔
		 * @param startAtBeginning 是否显示文件头？还是说只显示后面变化的部分
		 */
		public LogFileTailer(String file, long sampleInterval, boolean startAtBeginning) {
			logfile = new File(file);
			this.sampleInterval = sampleInterval;
			this.startAtBeginning = startAtBeginning;
		}

		/**
		 * 设置回调事件
		 * 
		 * @param callback 回调事件
		 */
		public void addListener(Consumer<String> callback) {
			this.callback = callback;
		}

		/**
		 * 监视开关，true = 打开监视
		 * 
		 * @param tailing true = 打开监视
		 */
		public void setTailing(boolean tailing) {
			this.tailing = tailing;
		}

		@Override
		public void run() {
			long filePointer = startAtBeginning ? 0 : logfile.length();

			try {
				RandomAccessFile file = new RandomAccessFile(logfile, "r");
				while (tailing) {
					long fileLength = logfile.length();

					if (fileLength < filePointer) {
						file = new RandomAccessFile(logfile, "r");
						filePointer = 0;
					}

					if (fileLength > filePointer) {
						file.seek(filePointer);
						String line = file.readLine();

						while (line != null) {
							line = new String(line.getBytes("ISO-8859-1"), "utf-8");

							if (callback != null)
								callback.accept(line);
							line = file.readLine();
						}

						filePointer = file.getFilePointer();
					}

					sleep(sampleInterval);
				}

				file.close();
			} catch (IOException | InterruptedException e) {

			}
		}

		public static void main(String[] args) throws IOException {
			LogFileTailer tailer = new LogFileTailer("C:\\temp\\bar.txt", 1000, true);
			tailer.setTailing(true);
			tailer.addListener(System.out::println);
			tailer.start();
		}
	}
}
