package com.ajaxjs.util.logger;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

import com.ajaxjs.util.logger.FileHandler;

public class TestFileHandler {
	@Test
	public void test() throws SecurityException, IOException {
		FileHandler handler = new FileHandler("c:/temp", "hhh", ".log");
		assertNotNull(handler);
		LogRecord record = new LogRecord(Level.WARNING, "test");
		handler.flush();
		record.setMessage("fooiiiiiiiiiiiiiiiiiiii");
		handler.publish(record);
		handler.close();
	}
}
