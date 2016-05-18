package com.ajaxjs.test.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.util.XML;


public class TestXML {
	@Before
	public void setUp() {
	}
	
	@Test
	public void 解析xml(){
		XML root = XML.getRoot("d:/test.xml");
		List<XML> pList = root.elements("property");

		for(XML d: pList){
			System.out.println(d.elementText("name") + ", " + d.elementText("value") + ", " + d.elementText("description"));
			assertNotNull(d);
		}
	}
	@Test
	public void 修改xml(){
		XML root = XML.getRoot("d:/test.xml");

		List<XML> pList = root.elements("property");

		XML d = pList.get(0);
		XML nameDom = d.element("name");
		nameDom.setAttribute("a", "2");
		nameDom.setAttribute("b", "3");
		nameDom.updateElementText("测试呵呵");
		
		assertNotNull(nameDom);
		d.updateElementText("description", "按当地");

		root.write("d:/test2.xml");
	}
	
	@Test
	public void 新建xml(){
		XML root = XML.newDom("configuration");

		XML propertyDom = root.addElement("property");
		propertyDom.addElement("name ", "名称");
		propertyDom.addElement("value", "值");
		XML dDom = propertyDom.addElement("description", "描述");
		dDom.setAttribute("a", "2");

		root.write("d:/test2.xml");
	}
}