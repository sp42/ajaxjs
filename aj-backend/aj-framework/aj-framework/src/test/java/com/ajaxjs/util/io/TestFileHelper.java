package com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

public class TestFileHelper {
    String dir = Resources.getResourcesFromClass(TestFileHelper.class, "");
    String fullPath = dir + File.separator + "bar.txt";

    @Test
    public void testCreateRead() {
        // create and update
        FileHelper.saveText(fullPath, "hihi");

        // read
        String result = FileHelper.openAsText(fullPath);

        assertTrue(result.startsWith("hihi"));

        FileHelper.saveText(fullPath, "hihi2");
        assertTrue(FileHelper.openAsText(fullPath).startsWith("hihi2"));

        // delete
        FileHelper.delete(fullPath);
    }

    @Test
    public void testGetFileName() {
        assertEquals("bar.java", FileHelper.getFileName("c:\\foo\\bar.java"));
        assertEquals("bar.java", FileHelper.getFileName("c:/foo/bar.java"));
    }

    @Test
    public void testGetFileSuffix() {
        assertEquals("java", FileHelper.getFileSuffix("c:/foo/bar.java"));
    }

    @Test
    public void testGetDirNameByDate() {
        assertTrue(FileHelper.getDirNameByDate().startsWith("\\" + Calendar.getInstance().get(Calendar.YEAR)));
    }

    @Test
    public void testCreateFile() {
        try {
            assertNotNull(FileHelper.createFile(fullPath, true));
            FileHelper.delete(fullPath);
        } catch (IOException e) {
        }
    }

}
