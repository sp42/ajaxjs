package com.util.java_compiler;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Bobby
 * @create: 2020-02-14 20:28
 * @description:
 **/
@RestController
@RequestMapping("/dynamicloader")
public class DynamicLoaderTestController {

	private static String javaSrc = "public class TestClass{" + "public void sayHello(String msg) {"
			+ "System.out.printf(\"Hello %s! This message from a Java String.%n\",msg);" + "}" + "public int add(int a,int b){" + "return a+b;" + "}" + "}";

	private static String javaSrcInterreptor = "import com.alibaba.fastjson.JSONObject;\n" + "import com.ocft.gateway.common.context.GatewayContext;\n"
			+ "import com.ocft.gateway.common.evaluator.JsonOperateEvalutor;\n" + "import com.ocft.gateway.common.exceptions.GatewayException;\n"
			+ "import com.ocft.gateway.entity.GatewayInterface;\n" + "import com.ocft.gateway.entity.InterfaceConfig;\n"
			+ "import com.ocft.gateway.entity.RequestAccessLimit;\n" + "import com.ocft.gateway.entity.RequestType;\n"
			+ "import com.ocft.gateway.interceptor.GatewayInterceptor;\n" + "import com.ocft.gateway.service.IInterfaceConfigService;\n"
			+ "import com.ocft.gateway.service.IRequestTypeService;\n" + "import com.ocft.gateway.utils.MathUtil;\n"
			+ "import com.ocft.gateway.utils.RedisUtil;\n" + "import com.ocft.gateway.utils.WebUtil;\n" + "import lombok.extern.slf4j.Slf4j;\n"
			+ "import org.apache.commons.lang3.StringUtils;\n" + "import org.slf4j.Logger;\n" + "import org.slf4j.LoggerFactory;\n"
			+ "import org.springframework.beans.factory.annotation.Autowired;\n" + "import org.springframework.stereotype.Component;\n"
			+ "import javax.servlet.http.HttpServletRequest;\n" + "import java.util.Date;\n" + "import java.util.List;" + "@Slf4j\n" + "@Component\n"
			+ "public class TestIntercept implements GatewayInterceptor {" + "    @Override\n" + "    public void doInterceptor(GatewayContext context) {"
			+ "       System.out.printf(\"Hello Bobby\");    " + "    }" + "}" + "";

	@RequestMapping("/test")
	public static void main(String[] args)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Map<String, byte[]> bytecode = DynamicLoader.compile("TestIntercept.java", javaSrcInterreptor);
		MemoryClassLoader classLoader = new MemoryClassLoader(bytecode);
		Class clazz = classLoader.loadClass("TestIntercept");
		Object object = clazz.newInstance();
//        Method doInterceptor = clazz.getMethod("doInterceptor", GatewayContext.class);
//        doInterceptor.invoke(object, new GatewayContext());
	}

	public void testCompile() {
		Map<String, byte[]> bytecode = DynamicLoader.compile("TestClass.java", javaSrc);
		for (Iterator<String> iterator = bytecode.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			byte[] code = bytecode.get(key);
			System.out.printf("Class: %s, Length: %d%n", key, code.length);
		}
	}
}