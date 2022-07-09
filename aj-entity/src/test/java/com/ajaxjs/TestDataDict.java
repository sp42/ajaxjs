package com.ajaxjs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.entity.datadict.DataDict;
import com.ajaxjs.entity.datadict.DataDictServiceImpl;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDataDict {
	@Autowired
	DataDictServiceImpl dataDictService;

	@Test
	public void testGet() {
		List<DataDict> list;
		list = dataDictService.getDataDict(3L);
		assertNotNull(list.get(0));

		list = dataDictService.getDataDictChildren(2L);
		assertNotNull(list.get(0));

		Integer depth = dataDictService.getDepthById(10L);
		assertNotNull(depth);
	}

	@Test
	public void testWrite() {
		DataDict d = new DataDict();
		d.setName("test");
		d.setParentId(4L);
		DataDict createDataDict = dataDictService.createDataDict(d);
		assertNotNull(createDataDict);

		d = new DataDict();
		d.setId(createDataDict.getId());
		d.setName("test222");
		assertTrue(dataDictService.updateDataDict(d));

		assertTrue(dataDictService.deleteDataDict(createDataDict.getId(), false));
	}

//	@Test
	public void testDelChildren() {
		dataDictService.deleteDataDict(5658L, true);
	}
}
