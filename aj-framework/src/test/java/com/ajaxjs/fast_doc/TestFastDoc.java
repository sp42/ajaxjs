package com.ajaxjs.fast_doc;

import java.util.ArrayList;

import org.junit.Test;

import com.ajaxjs.fast_doc.doclet.JavaDocParser;

public class TestFastDoc {
	@Test
	public void testDoclet() {
		Params params = new Params();
		params.sources = new ArrayList<>();
		params.sources.add("D:\\code\\oba\\src\\main\\java\\com\\toway\\oba\\model\\ObaDTO.java");
//		params.sources.add("D:\\code\\oba\\src\\main\\java\\com\\toway\\oba\\model\\Task.java");
//		params.sources.add("D:\\code\\oba\\src\\main\\java\\com\\toway\\oba\\service\\IDoService.java");
//		params.sources.add("D:\\code\\oba\\src\\main\\java\\com\\toway\\oba\\service\\IKoEvaService.java");

		params.sourcePath = "d:\\code\\oba\\src\\main\\java\\;D:\\code\\websites2\\uav-common\\src\\main\\java;d:\\code\\aj\\aj-framework\\src\\main\\java;d:\\code\\aj\\aj-util\\src\\main\\java";
		params.classPath = Util
				.getClzPath("D:\\sp42\\profile\\dev\\Eclipse\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\droneswarm\\WEB-INF\\lib");
		JavaDocParser.init(params);
	}
}
