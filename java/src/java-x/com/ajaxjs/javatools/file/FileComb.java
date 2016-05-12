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
 * @author Administrator
 * 
 */
public class FileComb {
	private int count;
	private String inPath = "c:/tmp/split.";

	private String exportPath = "c:/tmp/";

	public FileComb(String fileName, int count) {
		this.count = count;
		this.exportPath += fileName;
	}

	public void doComb() throws IOException {

		FileOutputStream fos = new FileOutputStream(exportPath);
		FileChannel wch = fos.getChannel();

		try {
			for (int i = this.count; i >= 0; i--) {
				String suff = null;
				if (i < 10) {
					suff = inPath + "0" + i;
				} else {
					suff = inPath + i;
				}
				rw(new File(suff), wch);
			}
		} finally {
			wch.close();
			fos.close();
		}

	}

	public void rw(File in, FileChannel wch) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		FileChannel rch = fis.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);

		while (rch.read(buffer) != -1) {
			buffer.flip();
			wch.write(buffer);
			buffer.clear();
		}
		if (buffer.position() != 0) {
			wch.write(buffer);
		}

		rch.close();
		fis.close();
	}
}
