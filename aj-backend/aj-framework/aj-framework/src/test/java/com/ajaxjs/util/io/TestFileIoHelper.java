package com.ajaxjs.util.io;

import static junit.framework.TestCase.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

//import static com.ajaxjs.util.io.FileIoHelper.*;

public class TestFileIoHelper {

	public void testFileConcat() {
		String content = FileIoHelper.openContent("C:\\project\\aj2\\aj-util\\src\\test\\java\\com\\ajaxjs\\util\\io\\test.txt");
		System.out.println(content);

		try {
//            File file = ResourceUtils.getFile("classpath:test.txt");
//            System.out.println(file);
			ClassPathResource resource = new ClassPathResource("classpath:test.txt");
//            InputStream inputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			content = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(content);
	}

	@Test
	public void testReadFile() {
		String content = FileIoHelper.readFile(Resources.getResourcesFromClass(getClass(), "test.txt"));
		assertNotNull(content);
		System.out.println(content);
	}
}
