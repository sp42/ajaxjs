package com.ajaxjs;

import static org.junit.Assert.assertNotNull;

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
	public void test() {
		List<DataDict> list = dataDictService.getDataDict(3L);
		assertNotNull(list.get(0));
	}
}
