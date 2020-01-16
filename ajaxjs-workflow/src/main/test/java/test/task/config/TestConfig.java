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
package test.task.config;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.TestSnakerBase;
import org.snaker.engine.entity.Order;
import org.snaker.engine.helper.StreamHelper;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestConfig extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/config/process.snaker"));
	}
	
	@Test
	public void test() {
		Order order = engine.startInstanceByName("config", 0, "2", null);
		System.out.println("order=" + order);
	}
}
