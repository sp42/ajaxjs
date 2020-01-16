package org.snaker.engine.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.snaker.engine.SnakerException;

/**
 * 流数据帮助类
 * 
 * @author yuqs
 * @since 1.0
 */
public class StreamHelper {
	public static final int DEFAULT_CHUNK_SIZE = 1024;

	public static final int BUFFERSIZE = 4096;

	public static InputStream getStreamFromClasspath(String resourceName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resourceName);

		if (stream == null) {
			stream = StreamHelper.class.getClassLoader().getResourceAsStream(resourceName);
		}

		if (stream == null) {
			throw new SnakerException("resource " + resourceName + " does not exist");
		}

		return stream;
	}

	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transfer(in, out);
		return out.toByteArray();
	}

	public static long transfer(InputStream in, OutputStream out) throws IOException {
		long total = 0;
		byte[] buffer = new byte[BUFFERSIZE];
		for (int count; (count = in.read(buffer)) != -1;) {
			out.write(buffer, 0, count);
			total += count;
		}
		return total;
	}

	/**
	 * input->output字节流copy
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		return copy(inputStream, outputStream, DEFAULT_CHUNK_SIZE);
	}

	/**
	 * input->output字节流copy
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @param bufferSize
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n;
		while (-1 != (n = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, n);
			count += n;
		}
		return count;

	}

	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, DEFAULT_CHUNK_SIZE);
	}

	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		char[] buffer = new char[bufferSize];
		long count = 0;
		int n;
		while (-1 != (n = reader.read(buffer))) {
			writer.write(buffer, 0, n);
			count += n;
		}

		return count;

	}
}
