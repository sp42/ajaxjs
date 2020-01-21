package com.ajaxjs.workflow;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Process;

public class TestProcess {
	@Test
	public void testRead() {
		System.out.println(engine.process().getProcesss(null));
		System.out.println(engine.process().getProcesss(new Page<Process>(), 
				new QueryFilter().setName("subprocess1")));
		System.out.println(engine.process().getProcessByVersion("subprocess1", 0));
		System.out.println(engine.process().getProcessByName("subprocess1"));
	}
}
