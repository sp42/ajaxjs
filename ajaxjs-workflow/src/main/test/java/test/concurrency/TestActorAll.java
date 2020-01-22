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
package test.concurrency;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.TestSnakerBase;
import org.snaker.engine.entity.Order;

import com.ajaxjs.workflow.WorkflowUtils;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestActorAll extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(WorkflowUtils
						.getStreamFromClasspath("test/concurrency/actorall/process.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1", "2"});
		Order order = engine.startInstanceById(processId, "2", args);
		System.out.println(order);
		//Assert.assertEquals(2, tasks.size());
		//execute(tasks, args);
	}
}