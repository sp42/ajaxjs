/**
 * 
 */
package com.ajaxjs.javatools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * 实现大文件切割原型
 * 
 * @author http://coach.iteye.com/blog/773148
 * 
 */
public class FileSplit {

	public static void main(String[] args) throws IOException {
		int count = 12;
		FileSplit spliter = new FileSplit("C:/tmp/HFDP.zip", count);
		spliter.doSplit();

		FileComb combiner = new FileComb("rs.zip", count);
		combiner.doComb();
	}

	private File file;
	private int count = 1;
	private int splitSize = 0;
	private String exportPath = "c:/tmp/split.";

	public FileSplit(String file, int count) {
		this.file = new File(file);
		this.count = count;
		this.splitSize = (int) Math.ceil(((double) this.file.length() / (double) count));
	}

	public void doSplit() throws IOException {
		int buffersize = 1024;
		ByteBuffer buffer = ByteBuffer.allocate(buffersize);

		FileInputStream fis = new FileInputStream(this.file);
		FileOutputStream fos = new FileOutputStream(exportPath + (this.count < 10 ? ("0" + this.count) : this.count));
		FileChannel wch = fos.getChannel();
		FileChannel rch = fis.getChannel();
		
		try {
			int readSize = 0;
			while (rch.read(buffer) != -1) {
				readSize += buffersize;
				buffer.flip();
				if (readSize >= splitSize) {
					wch.close();
					fos.close();
					this.count--;
					fos = new FileOutputStream(exportPath + (this.count < 10 ? ("0" + this.count) : this.count));

					wch = fos.getChannel();
					readSize = 0;
				}
				wch.write(buffer);
				buffer.clear();

			}
			if (buffer.position() != 0) {// there's something write out
				buffer.flip();
				wch.write(buffer);
				buffer.clear();
			}

		} finally {
			wch.close();
			fos.close();
			rch.close();
			fis.close();

			buffer.clear();
			buffer = null;
		}

	}
}
