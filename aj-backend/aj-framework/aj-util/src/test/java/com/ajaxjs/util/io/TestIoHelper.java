package com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TestIoHelper {
	String dir = Resources.getResourcesFromClass(TestIoHelper.class, "");
	String fullpath = dir + File.separator + "bar.txt";
	
	@Test
	public void testFileConcat() {
		byte[] ab = new byte[] {97, 98}, c = new byte[] {99};
		byte[] abc = StreamHelper.concat(ab, c);
		
		assertEquals('a', abc[0]);
		assertEquals('b', abc[1]);
		assertEquals('c', abc[2]);
	}
	
	@Test
	public void testByteIndexOf() {
		byte[] abcd = new byte[] {97, 98, 99, 100}, bc = new byte[] {98, 99};
		
		assertEquals(1, StreamHelper.byteIndexOf(abcd, bc, 0));
	}
	
	@Test
	public void testFileAsByte() {
		File file = new File(fullpath);
		
		FileHelper.saveText(file, "ab");
		
		byte[] b = FileHelper.openAsByte(new File(fullpath));
		byte a = 97;
		assertEquals(b[0], a);
		file.delete();
	}
}
