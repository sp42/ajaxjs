/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.mvc.filter;

import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.simpleApp.CommonController;

/**
 * 数据库连接、关闭连接
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DataBaseFilter implements FilterAction {
	@Override
	public boolean before(MvcRequest request, MvcOutput response, IController controller) {
		CommonController.initDb();
		return true;
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, IController controller, boolean isSkip) {
		JdbcConnection.closeDb();
	}
}
