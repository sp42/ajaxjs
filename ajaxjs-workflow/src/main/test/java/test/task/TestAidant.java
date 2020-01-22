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
package test.task;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.TestSnakerBase;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;

import com.ajaxjs.workflow.WorkflowUtils;

/**
* @author yuqs
* @since 1.0
*/
public class TestAidant extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(WorkflowUtils.getStreamFromClasspath("test/task/aidant/process.snaker"));
	}
	
	@Test
	public void test() {
		Order order = engine.startInstanceByName("aidant", 0);
		System.out.println("order=" + order);
		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			//engine.executeTask(task.getId());
			engine.task().createNewTask(task.getId(), 1, "test");
		}
	}
}