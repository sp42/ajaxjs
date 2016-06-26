/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;

public class AnnotationUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(AnnotationUtils.class);

	/**
	 * 
	 */
	public static Map<String, ActionAndView> controllers = new HashMap<>();
	
	
	public static void scan(Class<? extends IController> clz) {
		Controller controller = clz.getAnnotation(Controller.class);
		
		if(controller != null) {
			ActionAndView cInfo = new ActionAndView();
			cInfo.controller = Reflect.newInstance(clz);
			
			for (Method method : clz.getMethods()) {
				Path subPath = method.getAnnotation(Path.class);
			
				if (subPath != null) {
					String subPathValue = subPath.value();
					ActionAndView subPath_Info;

					// 有子路径
					if (cInfo.subPath.containsKey(subPathValue)) { // 已经有这个 subPath
						subPath_Info = cInfo.subPath.get(subPathValue);
						methodSend(method, subPath_Info);
					} else {
						subPath_Info = new ActionAndView();
						methodSend(method, subPath_Info);
						
						cInfo.subPath.put(subPathValue, subPath_Info);
					}
				} else {
					// 类本身……
					methodSend(method, cInfo);
				}
			}

			// 总路径
			Path path = clz.getAnnotation(Path.class);
			if (path != null) {
				controllers.put(path.value(), cInfo); // save the controller instance, path as key
				LOGGER.info(path.value() + " 登记路径成功！");
			} else {
				LOGGER.warning("No Path info!");
			}
		} else {
			LOGGER.warning("This is NOT a Controller!");
		}
	}


	private static void methodSend(Method method, ActionAndView cInfo) {
		if (method.getAnnotation(GET.class) != null) {
			cInfo.GET_method = method;
		} else if (method.getAnnotation(POST.class) != null) {
			cInfo.POST_method = method;
		} else if (method.getAnnotation(PUT.class) != null) {
			cInfo.PUT_method = method;
		} else if (method.getAnnotation(DELETE.class) != null) {
			cInfo.DELETE_method = method;
		}
		
	}

}
