/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.workflow.old;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.workflow.WorkflowEngine;

/**
 * 测试并发
 * 
 */
public class TestConcurrent extends BaseTest {
	public static void main(String[] a) {
//		Process process = ComponentMgr.get(WorkflowEngine.class).process().findByName("simple");
//
//		for (int i = 0; i < 50; i++) {
//			new Thread(new StartProcess(engine, process.getId())).start();
//		}
	}
}

class StartProcess implements Runnable {
	private WorkflowEngine engine;
	private Long processId;

	public StartProcess(WorkflowEngine engine, Long processId) {
		this.engine = engine;
		this.processId = processId;
	}

	@Override
	public void run() {
		Map<String, Object> args = new HashMap<>();
		args.put("task1.operator", new String[] { "1" });

		try {
			engine.startInstanceById(processId, 2L, args);// simple流程
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}