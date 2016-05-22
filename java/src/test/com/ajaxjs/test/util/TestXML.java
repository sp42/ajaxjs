package com.ajaxjs.test.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.util.XML_Wrapper;

public class TestXML {
	XML_Wrapper root;
	String saveTo;
	
	@Before
	public void setUp() {
		root = XML_Wrapper.getRoot("d:/temp/test.xml");
		saveTo = "d:/temp/test2.xml";
	}

	@Test
	public void 解析xml() {
		List<XML_Wrapper> pList = root.getChildrenElement("property");

		for (XML_Wrapper d : pList) {
			System.out.println(
					d.child("name").text() + ", " + d.child("value").text() + ", " + d.child("description").text());
			assertNotNull(d);
		}
	}

	@Test
	public void 修改xml() {
		List<XML_Wrapper> pList = root.getChildrenElement("property");

		XML_Wrapper d = pList.get(0);
		XML_Wrapper nameDom = d.child("name");
		nameDom.setAttribute("a", "2");
		nameDom.setAttribute("b", "3");
		nameDom.text("测试呵呵");

		assertNotNull(nameDom);
		d.child("description").text("添加描述……");

		root.write(saveTo);
	}

	@Test
	public void 新建xml() {
		XML_Wrapper root = XML_Wrapper.newDom("configuration");

		XML_Wrapper propertyDom = root.addElement("property");
		propertyDom.addElement("name", "名称");
		propertyDom.addElement("value", "值");
		XML_Wrapper dDom = propertyDom.addElement("description", "描述");
		dDom.setAttribute("a", "2");

		root.write(saveTo);
	}
}