package com.ajaxjs.jxc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.cms.service.TreeLikeService;
import com.ajaxjs.util.ioc.ComponentMgr;

public class TestTreeLikeService extends BaseTest {
	@Test
	public void test() {
		TreeLikeService service = ComponentMgr.get(TreeLikeService.class);
		List<Catalog> list = service.getDirectChildren(74);
		assertEquals(1, list.size());

		list = TreeLikeService.dao.getAllChildren(74);
		assertEquals(9, list.size());
	}

	@Test
	public void create() {
		TreeLikeService service = ComponentMgr.get(TreeLikeService.class);

		Catalog bean = new Catalog();
		bean.setName("fooo");
		bean.setPid(-1);
//		assertNotNull(service.create(bean));
		
		bean.setName("barr");
		bean.setPid(98);
		assertNotNull(service.create(bean));
	}
}
